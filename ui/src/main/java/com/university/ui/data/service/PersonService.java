package com.university.ui.data.service;

import com.university.ui.data.Role;
import com.university.ui.data.entity.Person;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface PersonService {
     void update(Person person);
     void delete(String idnp);

    Person getPerson(String idnp);
    Page<Person> list(Pageable pageable, Optional<String> firstName, Optional<String> lastName, Optional<String> startDate, Optional<String> endDate, Optional<Set<Role>> roles);
}
