package com.university.ui.views.grades;
import com.university.ui.data.entity.StudentCourse;
import com.university.ui.data.entity.User;
import com.university.ui.data.service.CourseService;
import com.university.ui.data.service.StudentCourseService;
import com.university.ui.data.service.UserService;
import com.university.ui.security.AuthenticatedUser;
import com.university.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@PageTitle("Grades")
@Route(value = "grades", layout = MainLayout.class)
@RolesAllowed("STUDENT")
@Uses(Icon.class)
public class GradesView extends Div {

    private Grid<StudentCourse> grid;

    private Filters filters;
    private final StudentCourseService studentCourseService;
    private final CourseService courseService;
    private final UserService userService;
    private final AuthenticatedUser authenticatedUser;

    public GradesView(StudentCourseService studentCourseService, CourseService courseService, UserService userService, AuthenticatedUser authenticatedUser) {
        this.studentCourseService = studentCourseService;
        this.courseService = courseService;
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;
        setSizeFull();
        addClassNames("grades-view");

        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public static class Filters extends Div {

        private final TextField courseName = new TextField("Course name");


        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            courseName.setPlaceholder("Course name");

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                courseName.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(courseName, actions);
        }




    }

    private Component createGrid() {
        Optional<User> maybeUser = authenticatedUser.get();
        String studentIdnp=null;
        if (maybeUser.isPresent() && !maybeUser.isEmpty())
            studentIdnp=maybeUser.get().getIdnp();
        grid = new Grid<>(StudentCourse.class, false);
        grid.addColumn(this::getCourseName).setHeader("Course name").setAutoWidth(true);
        grid.addColumn("average").setAutoWidth(true);
        String finalStudentIdnp = studentIdnp;
        grid.setItems(query -> studentCourseService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
               Optional.ofNullable(filters.courseName.getValue()).orElse(" "), finalStudentIdnp).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }
    private String getCourseName(StudentCourse studentCourse) {
        if (studentCourse != null && courseService.getCourse(studentCourse.getCourseId())!=null) {
            return courseService.getCourse(studentCourse.getCourseId()).getName();
        }
        return "-";
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
