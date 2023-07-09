package com.university.ui.data.service;

import com.university.ui.data.entity.Course;
import com.university.ui.data.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    public List<Course> getAll();
    public Page<Course> list(Pageable pageable);
    public Page<Course> list(Pageable pageable, Optional<String> name,Optional<String> numStudent,Optional<String > numCredit,Optional<String> numHours);
    void update(Course course);
    public long count();
    void delete(int id);
    Course getCourse(int id);

}
