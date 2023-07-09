package com.university.studentservice.service;


import com.university.studentservice.dao.StudentRepository;
import com.university.studentservice.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student get(String  id) {
        Student student=null;
        Optional<Student> optional= studentRepository.findById(id);
        if(optional.isPresent()) student=optional.get();
        return student;
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void delete(String id) {
        studentRepository.deleteById(id);

    }

    @Override
    public Page<Student> list(Specification<Student> specification, Pageable pageable) {
        return studentRepository.findAll(specification,pageable);
    }

    @Override
    public Page<Student> list(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }


}