package com.university.ui.views.liststudents;

import com.university.ui.data.entity.Group;
import com.university.ui.data.entity.Student;
import com.university.ui.data.entity.Person;
import com.university.ui.data.service.PersonService;
import com.university.ui.data.service.StudentService;
import com.university.ui.views.MainLayout;
import com.university.ui.views.addperson.AddPersonView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@PageTitle("List students")
@Route(value = "list_students", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class ListstudentsView extends Div {
     private Grid<Student> grid;
     private Filters filters;
     private StudentService studentService;
     private PersonService personService;


    public ListstudentsView(StudentService studentService,PersonService personService) {
        this.personService=personService;
        this.studentService=studentService;
        List<Group> groups= studentService.getAllGroups();
        setSizeFull();
        addClassNames("listpersons-view");
        filters = new Filters(() -> refreshGrid(),groups);
        VerticalLayout layout = new VerticalLayout(filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private Component createGrid() {
        grid = new Grid<>(Student.class, false);
        grid.addColumn(this::getStudentFirstName).setHeader("First name").setAutoWidth(true);
        grid.addColumn(this::getStudentLastName).setHeader("Last name").setAutoWidth(true);
        grid.addColumn(this::getGroupName).setHeader("Group").setAutoWidth(true);

        grid.setItems(  query -> studentService.list(PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)), Optional.ofNullable(filters.group.getValue()))
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
                dialog.addCancelListener(event ->    Notification.show( "Canceled student details update."));

                dialog.setRejectable(true);
                dialog.setRejectText("Discard");
                dialog.addRejectListener(event -> {
                    Notification.show( "Discard person details update.");
                });

                dialog.setConfirmText("Update");
                dialog.addConfirmListener(event -> {

                    ComponentUtil.setData(UI.getCurrent(), Person.class, personService.getPerson(item.getId()));
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
                dialog.addCancelListener(event ->    Notification.show( "Canceled student details delete."));

                dialog.setRejectable(true);
                dialog.setRejectText("Discard");
                dialog.addRejectListener(event -> {
                    Notification.show( "Discard student details delete.");
                });

                dialog.setConfirmText("Delete");
                dialog.addConfirmListener(event -> {
                    studentService.delete(item.getId());
                    Notification.show( "Student with idnp "+item.getId() + " details delete.");
                    refreshGrid();
                });
                dialog.open();

            });
            return delete;
        }).setHeader("Delete");

        return grid;
    }

    private String getStudentFirstName(Student studentObject) {
        if (studentObject != null && personService.getPerson(studentObject.getId())!=null) {
            return personService.getPerson(studentObject.getId()).getFirstName();
        }
        return "-";
    }
    private String getStudentLastName(Student studentObject) {
        if (studentObject != null && personService.getPerson(studentObject.getId())!=null) {
            return personService.getPerson(studentObject.getId()).getLastName();
        }
        return "-";
    }

    private String getGroupName(Student studentObject) {
        if (studentObject != null && studentObject.getGroup() != null) {
            return studentObject.getGroup().getName();
        }
        return "-";
    }

    public static class Filters extends Div  {


        private final ComboBox<Group> group = new ComboBox<Group>("Select group");

        private List<Group> groups;



        public Filters(Runnable onSearch,List<Group> groups) {
            this.groups=groups;
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            group.setPlaceholder("Group name");
            group.setItems(groups);
            group.setItemLabelGenerator(Group::getName);




            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                group.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> {
                    onSearch.run();});

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(group, actions);
        }


    }


    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
