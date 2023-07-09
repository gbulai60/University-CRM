package com.university.courseservice.specification;

import com.university.courseservice.model.Course;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecifications {

    public static Specification<Course> createSpecification(String name, int numCredit, int numStudent, int numHours) {
        return (Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (numCredit > 0) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("numCredit"), numCredit));
            }

            if (numStudent > 0) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("numStudent"), numStudent));
            }

            if (numHours > 0) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("numHours"),  numHours));
            }

            return predicate;
        };
    }
}