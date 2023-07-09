package com.university.studentservice.controller;


import com.university.studentservice.entity.Group;
import com.university.studentservice.entity.Student;
import com.university.studentservice.service.GrupaService;
import com.university.studentservice.service.StudentService;
import com.university.studentservice.specification.StudentSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class MyRestController {

    @Autowired
    StudentService studentService;
    @Autowired
    GrupaService grupaService;
    @GetMapping("/students")
    @ResponseStatus(HttpStatus.OK)
    public Page<Student> findPaginated(@RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                       @RequestParam("groupId") Optional<Integer> groupId) {
        if(groupId.isPresent()){
        Optional<Group> group= Optional.ofNullable(grupaService.get(groupId.get()));
        Specification<Student> specification= StudentSpecifications.createSpecification(group.orElse(null));
        return studentService.list(specification,PageRequest.of(page.orElse(0), size.orElse(20)));}
        else return studentService.list(PageRequest.of(page.orElse(0), size.orElse(20)));
    }
    @GetMapping("/students/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Student getStudentById(@PathVariable String id) {return studentService.get(id);}
    @GetMapping("/groups")
    @ResponseStatus(HttpStatus.OK)
    public List<Group> getAllGrupe(){return grupaService.getAll();}
    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student saveStudent(@RequestBody Student student){

        return studentService.save(student);

    }
    @PutMapping("/students")
    @ResponseStatus(HttpStatus.OK)
    public Student updateStudent(@RequestBody Student student){
        return studentService.save(student);}
    @DeleteMapping("/students/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("id") String id){
         studentService.delete(id);
    }

    @PostMapping("/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public Group saveGrupa(@RequestBody Group group){
        return grupaService.save(group);
    }

}
