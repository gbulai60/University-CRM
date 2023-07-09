package com.university.personservice.service;

import com.university.personservice.dao.PersonRepository;
import com.university.personservice.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Override
    public Person get(String idnp) {
        Person person=null;
        Optional<Person> optional = Optional.ofNullable(personRepository.findByIdnp(idnp));
        if(optional.isPresent()) person=optional.get();
        return person;
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }
    @Override
    @Transactional
    public void deleteByIdnp(String idnp) {
        personRepository.removeByIdnp(idnp);
    }

    @Override
    public Page<Person> findAll( Pageable pageable) {
        return personRepository.findAll(pageable);
    }
    @Override
    public Page<Person> findAll(Specification<Person> specification, Pageable pageable) {
        return personRepository.findAll(specification,pageable);
    }

}
