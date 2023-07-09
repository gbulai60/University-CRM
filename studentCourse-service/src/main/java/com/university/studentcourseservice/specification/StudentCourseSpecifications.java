package com.university.studentcourseservice.specification;

import com.university.studentcourseservice.entity.StudentCourse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class StudentCourseSpecifications {

    public static Specification<StudentCourse> createSpecification(Integer courseId,String studentIdnp) {
        return (Root<StudentCourse> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (courseId != null ) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("courseId"), courseId));
            }
            if (studentIdnp != null ) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("studentIdnp"), studentIdnp));
            }

            return predicate;
        };
    }
}