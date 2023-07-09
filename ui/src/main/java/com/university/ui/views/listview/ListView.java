//package com.university.ui.views.listview;
//
//import com.university.ui.data.entity.Person;
//import com.university.ui.data.service.PersonService;
//import com.university.ui.views.MainLayout;
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.dependency.Uses;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.icon.Icon;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import jakarta.annotation.security.RolesAllowed;
//
//import java.util.List;
//
//@PageTitle("Lista studentilor")
//@Route(value = "grid-with-filters3", layout = MainLayout.class)
//@RolesAllowed("ADMIN")
//@Uses(Icon.class)
//public class ListView extends Div {
//
//
//    private PersonService personService;
//
//    public ListView( PersonService personService) {
//
//        this.personService = personService;
//        setSizeFull();
//        addClassNames("listastudentilor-view");
//
//
//        VerticalLayout layout = new VerticalLayout( createGrid());
//        layout.setSizeFull();
//        layout.setPadding(false);
//        layout.setSpacing(false);
//        add(layout);
//    }
//
//
//    private Component createGrid(){
//        Grid<Person> grid = new Grid<>(Person.class, false);
//        grid.addColumn(Person::getFirstName).setHeader("First name");
//        grid.addColumn(Person::getLastName).setHeader("Last name");
//        grid.addColumn(Person::getEmail).setHeader("Email");
//
//
//        List<Person> people = personService.someRestCall();
//
//
//        grid.setItems(people);
//        return grid;
//    }
//
//
//}
