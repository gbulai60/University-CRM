package com.university.ui.views.addcourse;

import com.university.ui.data.entity.Course;
import com.university.ui.data.entity.Person;
import com.university.ui.data.service.CourseService;
import com.university.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
@PageTitle("Add course")
@Route(value = "addCourse", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class AddCourseView extends Div {

    private TextField name = new TextField("Course name");
    private TextField numCredit = new TextField("Number of credits");
    private TextField numStudent = new TextField("Number of students");
    private TextField numHours = new TextField("Number of hours");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Course> binder = new Binder<>(Course.class);
    private CourseService courseService;


    public AddCourseView(CourseService courseService) {
        this.courseService=courseService;
        addClassName("addCourse-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        binder.bindInstanceFields(this);
        clearForm();
        if(ComponentUtil.getData(UI.getCurrent(), Course.class)!=null)
            binder.setBean(ComponentUtil.getData(UI.getCurrent(), Course.class));

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
           // personService.update(binder.getBean());
            courseService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            clearForm();
        });
    }

    private void clearForm() {
        binder.setBean(new Course());
    }

    private Component createTitle() {
        return new H3("Course information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(name, numCredit, numHours, numStudent);
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
