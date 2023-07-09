package com.university.studentservice.specification;

import com.university.studentservice.entity.Group;
import com.university.studentservice.entity.Student;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;


public class StudentSpecifications {

    public static Specification<Student> createSpecification(Group group) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (group != null ) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("group"), group));
            }

            return predicate;
        };
    }
}