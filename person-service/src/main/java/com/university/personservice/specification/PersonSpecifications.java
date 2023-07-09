package com.university.personservice.specification;

import com.university.personservice.entity.Person;
import com.university.personservice.entity.Role;
import jakarta.persistence.criteria.*;
import org.hibernate.type.EntityType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Set;


public class PersonSpecifications {

    public static Specification<Person> createSpecification(String firstName, String lastName, LocalDate startDate, LocalDate endDate, Set<Role> roleSet) {
        return (Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (firstName != null && !firstName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("firstName"), firstName));
            }



            if (lastName != null && !lastName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("lastName"), lastName));
            }
            if (startDate != null && endDate!=null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("dateOfBirth"), startDate,endDate));
            }
            Join<EntityType, Role> rolesJoin = root.joinSet("roles");

           predicate = criteriaBuilder.and(predicate,rolesJoin.in(roleSet));

            return predicate;
        };
    }
}