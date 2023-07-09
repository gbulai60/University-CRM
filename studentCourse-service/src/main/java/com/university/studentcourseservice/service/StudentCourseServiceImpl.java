package com.university.studentcourseservice.service;

import com.university.studentcourseservice.dao.StudentCourseRepository;
import com.university.studentcourseservice.entity.CursStudentKey;
import com.university.studentcourseservice.entity.StudentCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {
    @Autowired
    StudentCourseRepository studentCourseRepository;


    @Override
    public void save(StudentCourse studentCourse) {
        studentCourseRepository.save(studentCourse);
    }

    @Override
    public void delete(int courseId,String studentIdnp) {
        CursStudentKey id=new CursStudentKey(courseId,studentIdnp);
        studentCourseRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByCourseId(int courseId) {
        studentCourseRepository.removeByCourseId(courseId);
    }
    @Transactional
    @Override
    public void deleteByStudentIdnp(String studentIdnp) {
        studentCourseRepository.removeAllByStudentIdnp(studentIdnp);
    }

    @Override
    public Page<StudentCourse> list(Pageable pageable) {
        return studentCourseRepository.findAll(pageable);
    }

    @Override
    public Page<StudentCourse> list(Specification<StudentCourse> specification, Pageable pageable) {
        return studentCourseRepository.findAll(specification,pageable);
    }

    @Override
    public StudentCourse getStudentCourse(int courseId,String studentIdnp) {
        return studentCourseRepository.getStudentCourseByCourseIdAndStudentIdnp(courseId,studentIdnp);
    }
}
