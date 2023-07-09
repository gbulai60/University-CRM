package com.university.ui.views.listpersons;

import com.university.ui.data.Role;
import com.university.ui.data.entity.Person;
import com.university.ui.data.service.PersonService;
import com.university.ui.data.service.StudentService;
import com.university.ui.views.MainLayout;
import com.university.ui.views.addperson.AddPersonView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@PageTitle("List persons")
@Route(value = "list_person", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class ListpersonsView extends Div {
     private Grid<Person> grid;
     private Filters filters;
     private PersonService personService;
     private StudentService studentService;

    public ListpersonsView(PersonService personService,StudentService studentService) {
        this.studentService=studentService;
        this.personService=personService;
        setSizeFull();
        addClassNames("listpersons-view");
        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private Component createGrid() {
        grid = new Grid<>(Person.class, false);
        grid.addColumn("firstName").setHeader("First Name").setAutoWidth(true);
        grid.addColumn("lastName").setHeader("Last Name").setAutoWidth(true);
        grid.addColumn("patronymic").setHeader("Patronymic").setAutoWidth(true);
        grid.addColumn("phone").setHeader("Phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setHeader("Date of Birth").setAutoWidth(true);
        grid.addColumn("email").setHeader("Email").setAutoWidth(true);
        grid.addColumn("nationality").setHeader("Nationality").setAutoWidth(true);
        grid.addColumn("seriesNo").setHeader("Series of buletin").setAutoWidth(true);
        grid.addColumn("address").setHeader("Address").setAutoWidth(true);
        grid.addColumn("idnp").setHeader("IDNP").setAutoWidth(true);
        grid.setItems(  query -> personService.list(PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)), Optional.of(filters.firstName.getValue()),
                        Optional.of(filters.lastName.getValue()),Optional.of(filters.startDate.getValue()!=null ? filters.startDate.getValue().toString() : ""),
                        Optional.of(filters.endDate.getValue()!=null ? filters.endDate.getValue().toString() : "" ),Optional.of(filters.role.getValue()))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        grid.addComponentColumn(item -> {
            Button update = new Button("Update");
            update.addClickListener(e -> {
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Confirm update dialog");
                dialog.setText(
                        " Do you want to discard or update them?");

                dialog.setCancelable(true);
                dialog.addCancelListener(event ->    Notification.show( "Canceled person details update."));

                dialog.setRejectable(true);
                dialog.setRejectText("Discard");
                dialog.addRejectListener(event -> {
                    Notification.show( "Discard person details update.");
                });

                dialog.setConfirmText("Update");
                dialog.addConfirmListener(event -> {

                    ComponentUtil.setData(UI.getCurrent(), Person.class, item);
                    UI.getCurrent().navigate(AddPersonView.class);
                    ComponentUtil.setData(UI.getCurrent(), Person.class, null);
                    refreshGrid();


                });
                dialog.open();

            });
            return update;
        }).setHeader("Update");
        grid.addComponentColumn(item -> {
            Button delete = new Button("Delete");
            delete.addClickListener(e -> {
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Confirm delete dialog");
                dialog.setText(
                        " Do you want to discard or delete them?");

                dialog.setCancelable(true);
                dialog.addCancelListener(event ->    Notification.show( "Canceled person details delete."));

                dialog.setRejectable(true);
                dialog.setRejectText("Discard");
                dialog.addRejectListener(event -> {
                    Notification.show( "Discard person details delete.");
                });

                dialog.setConfirmText("Delete");
                dialog.addConfirmListener(event -> {
                    personService.delete(item.getIdnp());
                    if(studentService.getByIdnp(item.getIdnp())!=null) studentService.delete(item.getIdnp());
                    Notification.show(item.getLastName()+ " "+item.getFirstName() + " details delete.");
                    refreshGrid();
                });
                dialog.open();

            });
            return delete;
        }).setHeader("Delete");

        return grid;
    }

    public static class Filters extends Div  {


        private final TextField firstName = new TextField("First name");
        private final TextField lastName = new TextField("Last name");
        private final MultiSelectComboBox<Role> role =  new MultiSelectComboBox<>("Role");
        private final DatePicker startDate = new DatePicker("Date of Birth");
        private final DatePicker endDate = new DatePicker();
        public Filters(Runnable onSearch) {


            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            firstName.setPlaceholder("First name");
            lastName.setPlaceholder("Last name");
            role.setItems(Role.STUDENT,Role.TEACHER,Role.EMPLOYEE);



            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                firstName.clear();
                endDate.clear();
                startDate.clear();
                lastName.clear();
                role.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> {
                    onSearch.run();});

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(firstName,lastName,createDateRangeFilter(),role, actions);
        }


        private Component createDateRangeFilter() {
            startDate.setPlaceholder("From");

            endDate.setPlaceholder("To");

            // For screen readers
            setAriaLabel(startDate, "From date");
            setAriaLabel(endDate, "To date");

            FlexLayout dateRangeComponent = new FlexLayout(startDate, new Text(" – "), endDate);
            dateRangeComponent.setAlignItems(FlexComponent.Alignment.BASELINE);
            dateRangeComponent.addClassName(LumoUtility.Gap.XSMALL);

            return dateRangeComponent;
        }
        private void setAriaLabel(DatePicker datePicker, String label) {
            datePicker.getElement().executeJs("const input = this.inputElement;" //
                    + "input.setAttribute('aria-label', $0);" //
                    + "input.removeAttribute('aria-labelledby');", label);
        }

    }


    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
