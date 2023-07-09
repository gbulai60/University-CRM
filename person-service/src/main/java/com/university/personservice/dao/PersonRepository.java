package com.university.personservice.dao;

import com.university.personservice.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PersonRepository extends JpaRepository<Person,Integer>, JpaSpecificationExecutor<Person> {

    public Person findByIdnp(String idnp);
    public void removeByIdnp(String idnp);
    public Page<Person> findAll(Specification<Person> specification, Pageable pageable);
    public Page<Person> findAll(Pageable pageable);

}
