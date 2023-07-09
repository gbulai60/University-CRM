package com.university.ui.views.addperson;

import com.university.ui.data.Role;
import com.university.ui.data.entity.*;
import com.university.ui.data.service.*;
import com.university.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@PageTitle("Save person")
@Route(value = "person-form", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class AddPersonView extends Div {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final TextField patronymic = new TextField("Patronymic");
    private final EmailField email = new EmailField("Email ");
    private final DatePicker dateOfBirth = new DatePicker("Date Of Birth ");
    private final PhoneNumberField phone = new PhoneNumberField("Phone");
    private final TextField idnp = new TextField("IDNP");
    private final TextField nationality= new TextField("Nationality");
    private final TextField seriesNo= new TextField("Series No");
    private final TextField address=new TextField("Address");
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final MultiSelectComboBox<Role> role = new MultiSelectComboBox<Role>("Select Role");
    private final ComboBox<Group> group = new ComboBox<Group>("Select group");
    private String id;
    private Student student;
    private Binder<Person> binder = new Binder<>(Person.class);




    public AddPersonView(StudentService studentService, PersonService personService) {
        List<Group> groups = studentService.getAllGroups();
        addClassName("addperson-view");
        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        group.setVisible(false);
        group.setItems(groups);
        binder.bindInstanceFields(this);
        group.setItemLabelGenerator(Group::getName);
        role.addValueChangeListener(event -> {
            if (event.getValue().contains(Role.STUDENT)) {
                group.setVisible(true);
            } else {
                group.setVisible(false);
            }
        });
        clearForm();
        if(ComponentUtil.getData(UI.getCurrent(), Person.class)!=null){
        binder.setBean(ComponentUtil.getData(UI.getCurrent(), Person.class));
        role.setValue(binder.getBean().getRoles());
        if(role.getValue().contains(Role.STUDENT)){

           group.setValue(studentService.getByIdnp(binder.getBean().getIdnp())!= null ? studentService.getByIdnp(binder.getBean().getIdnp()).getGroup() : null );
        }

        }


        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            User user;
            Optional<User> maybeUser= Optional.ofNullable(userService.findByIdnp(binder.getBean().getIdnp()));
            if(!maybeUser.isPresent()){
                user=new User();
                user.setName(firstName.getValue()+" "+lastName.getValue());
                user.setUsername(email.getValue());
                user.setIdnp(idnp.getValue());
                String password= userService.generateRandomPassword(15);
                user.setHashedPassword(passwordEncoder.encode(password));
                userService.sendEmail(email.getValue(),password);
            }
            else user=maybeUser.get();

            Set<Role> roles=role.getValue();
            user.setRoles(roles);
            binder.getBean().setRoles(roles);
            userService.update(user);
            personService.update(binder.getBean());
            if(role.getValue().contains(Role.STUDENT)) {
                id= idnp.getValue();
                student =new Student(id, group.getValue());
                studentService.update(student, "/students");
            }


            Notification.show(role.getValue() + " details stored.");
            clearForm();

        });

    }



    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        role.setItems(Role.values());
        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(firstName, lastName, dateOfBirth, phone, email,patronymic,idnp,nationality,seriesNo,address, role, group);
        return formLayout;
    }
    private void clearForm() {
        binder.setBean(new Person());
        role.clear();
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setAllowedCharPattern("[\\+\\d]");
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225","+373");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setAllowedCharPattern("\\d");
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
