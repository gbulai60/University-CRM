package com.university.studentcourseservice.controller;

import com.university.studentcourseservice.entity.CursStudentKey;
import com.university.studentcourseservice.entity.StudentCourse;
import com.university.studentcourseservice.service.StudentCourseService;
import com.university.studentcourseservice.specification.StudentCourseSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MyRestController {
    @Autowired
    private StudentCourseService studentCourseService;
    @GetMapping("/studentCourse")
    @ResponseStatus(HttpStatus.OK)
    public Page<StudentCourse> findPaginated(@RequestParam("page") Optional<Integer> page,
                                             @RequestParam("size") Optional<Integer> size,
                                             @RequestParam("courseId") Optional<Integer> courseId,
                                             @RequestParam("studentIdnp") Optional<String> studentIdnp){
        if(courseId.isPresent() || studentIdnp.isPresent()){
            Specification<StudentCourse> specification= StudentCourseSpecifications.createSpecification(courseId.orElse(null),studentIdnp.orElse(null));
            return studentCourseService.list(specification,PageRequest.of(page.orElse(0), size.orElse(20)));
        }
        else return studentCourseService.list( PageRequest.of(page.orElse(0), size.orElse(20)));
    }
    @GetMapping("/studentCourse/{courseId}/{studentIdnp}")
    @ResponseStatus(HttpStatus.OK)
    public StudentCourse getStudentCourse(@PathVariable int courseId,@PathVariable String studentIdnp) {
        return studentCourseService.getStudentCourse(courseId,studentIdnp);}

    @PostMapping("/studentCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentCourse addNewRelationStudentCourse(@RequestBody StudentCourse studentCourse){

        studentCourseService.save(studentCourse);
        return studentCourse;
    }
    @PutMapping("/studentCourse")
    @ResponseStatus(HttpStatus.OK)
    public StudentCourse updateRelationStudentCourse(@RequestBody StudentCourse studentCourse){
        studentCourseService.save(studentCourse);
        return studentCourse;
    }
    @DeleteMapping("/studentCourse/{studentIdnp}/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteRelation(@PathVariable String studentIdnp, @PathVariable  int courseId){

        studentCourseService.delete(courseId,studentIdnp);
        return "Relatia a fost stearsa cu sucess";
    }
    @DeleteMapping("/studentCourse/course/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByCourseId( @PathVariable  int courseId){
        studentCourseService.deleteByCourseId(courseId);
    }
    @DeleteMapping("/studentCourse/student/{studentIdnp}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllByStudentIdnp( @PathVariable  String studentIdnp){
        studentCourseService.deleteByStudentIdnp(studentIdnp);
    }
}
