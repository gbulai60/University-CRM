package com.university.personservice.controller;

import com.university.personservice.entity.Person;
import com.university.personservice.entity.Role;
import com.university.personservice.service.PersonService;
import com.university.personservice.specification.PersonSpecifications;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MyRestController {
    @Autowired
    private PersonService personService;

    @GetMapping("/persons/{idnp}")
    @ResponseStatus(HttpStatus.OK)
    public Person getPerson(@PathVariable String idnp) {
        Person person = personService.get(idnp);
        return person;}
    @GetMapping("/persons")
    @ResponseStatus(HttpStatus.OK)
    public Page<Person> findPaginated(@RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      @RequestParam("firstName") Optional<String> firstName,
                                      @RequestParam("lastName") Optional<String> lastName,
                                      @RequestParam("startDate") Optional<LocalDate> startDate,
                                      @RequestParam("endDate") Optional<LocalDate> endDate,
                                      @RequestParam("role") Optional<Set<Role>> roles)
    {

        Specification<Person> specification= PersonSpecifications.createSpecification(firstName.orElse(""),lastName.orElse(""),startDate.orElse(null),endDate.orElse(null),roles.orElse(Set.of(Role.values())));

        return personService.findAll(specification,PageRequest.of(page.orElse(0), size.orElse(20)));
    }
    @PutMapping("/persons")
    @ResponseStatus(HttpStatus.OK)
    public Person updatePerson(@RequestBody Person person){
        personService.save(person);
        return person;}

    @PostMapping("/persons")
    @ResponseStatus(HttpStatus.CREATED)
    // @CrossOrigin(origins = "*")
    public Person addNewPerson(@RequestBody Person person){
        if(personService.get(person.getIdnp())!=null)
            return null;
        else{
     personService.save(person);
     return person;}
    }

    @DeleteMapping("/persons/{idnp}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void deletePerson(@PathVariable String idnp){
        personService.deleteByIdnp(idnp);
    }

}
