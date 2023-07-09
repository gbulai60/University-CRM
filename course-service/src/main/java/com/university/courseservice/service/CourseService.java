package com.university.courseservice.service;

import com.university.courseservice.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CourseService {
    public void delete(int id);
    public void save(Course course);
    public Course get(int id);
    public List<Course> getAll();
    public Page<Course> list(Pageable pageable);
    public Page<Course> list(Pageable pageable, Specification<Course> filter) ;
}
