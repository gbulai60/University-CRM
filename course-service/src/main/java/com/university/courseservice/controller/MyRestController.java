package com.university.courseservice.controller;

import com.university.courseservice.model.Course;
import com.university.courseservice.service.CourseService;
import com.university.courseservice.specification.CourseSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MyRestController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public Course addNewCourse(@RequestBody Course course){
        courseService.save(course);
        return course;
    }

    @PutMapping("/courses")
    @ResponseStatus(HttpStatus.OK)
    public Course updateCourse(@RequestBody Course course){
        courseService.save(course);
        return course;}

    @DeleteMapping("/courses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteCourse(@PathVariable int id){
        Course course=courseService.get(id);
        int Id=course.getId();
        courseService.delete(Id);

        return "Coursul cu ID "+Id+" a fost sters";
    }

    @GetMapping("/courses/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Course getCourse(@PathVariable int id) {
        Course course = courseService.get(id);
        return course;}
    @GetMapping("/courses")
    @ResponseStatus(HttpStatus.OK)
    public Page<Course> findPaginated(@RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      @RequestParam("name") Optional<String> name,
                                      @RequestParam("numCredit") Optional<Integer> numCredit,
                                      @RequestParam("numStudent") Optional<Integer> numStudent,
                                      @RequestParam("numHours") Optional<Integer> numHours) {

        Pageable pageable;
        Specification<Course> specification= CourseSpecifications.createSpecification(name.orElse(""),numCredit.orElse(0),numStudent.orElse(0),numHours.orElse(0));

        pageable=PageRequest.of(page.orElse(0), size.orElse(4));

        return courseService.list(pageable,specification);
    }

}
