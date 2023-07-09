package com.university.studentservice.dao;

import com.university.studentservice.entity.Group;
import com.university.studentservice.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
Page<Student> findAll(Pageable pageable);
Page<Student> findAll(Specification<Student > specification, Pageable pageable);
}

