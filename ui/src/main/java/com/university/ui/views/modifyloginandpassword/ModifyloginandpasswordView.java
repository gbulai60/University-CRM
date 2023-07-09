package com.university.ui.views.modifyloginandpassword;
import com.university.ui.data.entity.User;
import com.university.ui.data.service.UserService;
import com.university.ui.security.AuthenticatedUser;
import com.university.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@PageTitle("Modify login and password")
@Route(value = "person-form2", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class ModifyloginandpasswordView extends Div {

    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");
    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private UserService userService;
    private AuthenticatedUser authenticatedUser;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ModifyloginandpasswordView(UserService userService,AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        Optional<User> maybeUser = authenticatedUser.get();
        addClassName("modifyloginandpassword-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        if(maybeUser.isPresent()){
        User user = maybeUser.get();
        username.setValue(user.getUsername());

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Unsaved changes");
            dialog.setText(
                    "There are unsaved changes. Do you want to discard or save them?");

            dialog.setCancelable(true);
            dialog.addCancelListener(event ->    Notification.show( "Canceled details update."));

            dialog.setRejectable(true);
            dialog.setRejectText("Discard");
            dialog.addRejectListener(event -> {
                Notification.show( "Discard details update.");
            });

            dialog.setConfirmText("Save");
            dialog.addConfirmListener(event -> {
                user.setUsername(username.getValue());
                user.setHashedPassword(passwordEncoder.encode(password.getValue()));
                userService.update(user);
                Notification.show(user.getName() + " details update.");
            });

            if(password.getValue().equals(confirmPassword.getValue())){
                dialog.open();
                }
            else
            clearForm();
        });
        }

    }

    private void clearForm() {
        username.clear();
        password.clear();
        confirmPassword.clear();
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        password.setErrorMessage("Please enter a valid password");

        formLayout.add(username, password, confirmPassword);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }


}
