package com.university.courseservice.service;

import com.university.courseservice.dao.CourseRepository;
import com.university.courseservice.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void delete(int id) {
        courseRepository.deleteById(id);
    }

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public Course get(int id) {
        Course course=null;
        Optional<Course> optional = courseRepository.findById(id);
        if(optional.isPresent()) course=optional.get();
        return course;
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Page<Course> list(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> list(Pageable pageable, Specification<Course> filter) {
        return courseRepository.findAll(filter,pageable);
    }
}
