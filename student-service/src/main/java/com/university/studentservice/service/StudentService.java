package com.university.studentservice.service;


import com.university.studentservice.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface StudentService {
    public Student get(String Idnp);
    public Student save(Student student);
    public void delete(String id);
    Page<Student> list(Specification<Student> specification, Pageable pageable);
    Page<Student> list(Pageable pageable);


}
