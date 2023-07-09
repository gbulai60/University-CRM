package com.university.ui.views.listcourses;

import com.university.ui.data.Role;
import com.university.ui.data.entity.Course;
import com.university.ui.data.entity.StudentCourse;
import com.university.ui.data.service.CourseService;
import com.university.ui.data.service.StudentCourseService;
import com.university.ui.security.AuthenticatedUser;
import com.university.ui.views.MainLayout;
import com.university.ui.views.addcourse.AddCourseView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.Optional;


@PageTitle("List courses")
@Route(value = "list-courses", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class ListcoursesView extends Div {

    private Grid<Course> grid;
    private Filters filters;
    private final CourseService courseService;
    private final AuthenticatedUser authenticatedUser;
    private final StudentCourseService studentCourseService;


    public ListcoursesView(CourseService courseService, AuthenticatedUser authenticatedUser, StudentCourseService studentCourseService) {
        this.courseService = courseService;
        this.authenticatedUser = authenticatedUser;
        this.studentCourseService = studentCourseService;
        setSizeFull();
        addClassNames("listcourses-view");
        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private Component createGrid() {
        grid = new Grid<>(Course.class, false);
        grid.addColumn("name").setHeader("Name").setAutoWidth(true);
        grid.addColumn("numHours").setHeader("Number of hours ").setAutoWidth(true);
        grid.addColumn("numStudent").setHeader("Number of students").setAutoWidth(true);
        grid.addColumn("numCredit").setHeader("Number of credits").setAutoWidth(true);
        grid.setItems(query -> courseService.list(PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)), Optional.of(filters.name.getValue()),Optional.of(filters.numStudent.getValue()),Optional.of(filters.numCredit.getValue()),Optional.of(filters.numHours.getValue()))
                .stream());


        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        if(authenticatedUser.get().get().getRoles().contains(Role.ADMIN) || authenticatedUser.get().get().getRoles().contains(Role.TEACHER)) {
            grid.addComponentColumn(item -> {
                Button update = new Button("Update");
                update.addClickListener(e -> {
                    ConfirmDialog dialog = new ConfirmDialog();
                    dialog.setHeader("Confirm update dialog");
                    dialog.setText(
                            " Do you want to discard or update them?");

                    dialog.setCancelable(true);
                    dialog.addCancelListener(event -> Notification.show("Canceled course details update."));

                    dialog.setRejectable(true);
                    dialog.setRejectText("Discard");
                    dialog.addRejectListener(event -> {
                        Notification.show("Discard course details update.");
                    });

                    dialog.setConfirmText("Update");
                    dialog.addConfirmListener(event -> {

                        ComponentUtil.setData(UI.getCurrent(), Course.class, item);
                        UI.getCurrent().navigate(AddCourseView.class);
                        ComponentUtil.setData(UI.getCurrent(), Course.class, null);
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
                    dialog.addCancelListener(event -> Notification.show("Canceled person details delete."));

                    dialog.setRejectable(true);
                    dialog.setRejectText("Discard");
                    dialog.addRejectListener(event -> {
                        Notification.show("Discard course details delete.");
                    });

                    dialog.setConfirmText("Delete");
                    dialog.addConfirmListener(event -> {
                        courseService.delete(item.getId());
                        Notification.show(item.getName() + " details delete.");
                        refreshGrid();
                    });
                    dialog.open();

                });
                return delete;
            }).setHeader("Delete");
        }
        if (authenticatedUser.get().get().getRoles().contains(Role.STUDENT)){
            grid.addComponentColumn(item -> {
                Button delete = new Button("Add");
                delete.addClickListener(e -> {
                    ConfirmDialog dialog = new ConfirmDialog();
                    dialog.setHeader("Confirm  dialog");
                    dialog.setText(
                            " Do you want to discard or add them?");

                    dialog.setCancelable(true);
                    dialog.addCancelListener(event -> Notification.show("Canceled add course."));

                    dialog.setRejectable(true);
                    dialog.setRejectText("Discard");
                    dialog.addRejectListener(event -> {
                        Notification.show("Discard add course details.");
                    });

                    dialog.setConfirmText("Add");
                    dialog.addConfirmListener(event -> {
                        byte status = studentCourseService.update(new StudentCourse(authenticatedUser.get().get().getIdnp(), item.getId()));

                        if(status==1){
                        item.setNumStudent(item.getNumStudent()+1);
                        courseService.update(item);
                            Notification.show("Course has been added successfully ");}
                        else if (status==0)
                            Notification.show("Course already added");


                        refreshGrid();
                    });
                    dialog.open();

                });
                return delete;
            }).setHeader("Add");
        }

        return grid;
    }


    public static class Filters extends Div  {

        private final TextField name = new TextField("Name");
        private final TextField numHours = new TextField("Number of hours");
        private final TextField numStudent = new TextField("Number of students");
        private final TextField numCredit = new TextField("Number of credits");


        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            name.setPlaceholder("Name of course");

            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                name.clear();
                numCredit.clear();
                numHours.clear();
                numStudent.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(name, numCredit,numHours,numStudent, actions);
        }


    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
