package com.university.personservice.service;

import com.university.personservice.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PersonService {

    public Person get(String idnp);
    public void save(Person person);
    public void deleteByIdnp(String idnp);

    Page<Person> findAll(Specification<Person> specification, Pageable pageable);
    Page<Person> findAll(Pageable pageable);
}

