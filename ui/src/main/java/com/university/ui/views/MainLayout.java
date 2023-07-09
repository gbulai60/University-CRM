package com.university.ui.views;

import com.university.ui.data.entity.User;
import com.university.ui.security.AuthenticatedUser;
import com.university.ui.views.about.AboutView;
import com.university.ui.views.addperson.AddPersonView;
import com.university.ui.views.addcourse.AddCourseView;
import com.university.ui.views.grades.GradesView;
import com.university.ui.views.listcourses.ListcoursesView;
import com.university.ui.views.listpersons.ListpersonsView;
//import com.university.ui.views.note.NoteView;
import com.university.ui.views.liststudents.ListstudentsView;
import com.university.ui.views.modifyloginandpassword.ModifyloginandpasswordView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Optional;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("University-CRM");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(AboutView.class)) {
            nav.addItem(new SideNavItem("About", AboutView.class, LineAwesomeIcon.UNIVERSITY_SOLID.create()));

        }
        if (accessChecker.hasAccess(ListcoursesView.class)) {
            nav.addItem(
                    new SideNavItem("List courses", ListcoursesView.class, LineAwesomeIcon.LIST_ALT.create()));

        }
        if (accessChecker.hasAccess(ListpersonsView.class)) {
            nav.addItem(new SideNavItem("List persons", ListpersonsView.class,
                    LineAwesomeIcon.PERSON_BOOTH_SOLID.create()));

        }
        if (accessChecker.hasAccess(ListstudentsView.class)) {
            nav.addItem(new SideNavItem("List students", ListstudentsView.class,
                    LineAwesomeIcon.USER_GRADUATE_SOLID.create()));

        }
        if (accessChecker.hasAccess(GradesView.class)) {
            nav.addItem(new SideNavItem("Grades", GradesView.class, LineAwesomeIcon.ADDRESS_CARD_SOLID.create()));

        }
        if (accessChecker.hasAccess(AddPersonView.class)) {
            nav.addItem(
                    new SideNavItem("Add person", AddPersonView.class, LineAwesomeIcon.USER.create()));

        }
        if (accessChecker.hasAccess(AddCourseView.class)) {
            nav.addItem(new SideNavItem("Add course", AddCourseView.class, LineAwesomeIcon.BOOK_SOLID.create()));

        }
        if (accessChecker.hasAccess(ModifyloginandpasswordView.class)) {
            nav.addItem(new SideNavItem("Modify login and password", ModifyloginandpasswordView.class,
                    LineAwesomeIcon.KEY_SOLID.create()));

        }


        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());

            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
