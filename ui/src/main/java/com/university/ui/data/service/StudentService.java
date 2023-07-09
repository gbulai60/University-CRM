package com.university.ui.data.service;

import com.university.ui.data.entity.Group;
import com.university.ui.data.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentService {
     List<Group> getAllGroups();
     Student getByIdnp(String idnp);
     void update(Student object, String path);
     void delete(String id);

     Page<Student> list(Pageable pageable, Optional<Group> groupId);



}
