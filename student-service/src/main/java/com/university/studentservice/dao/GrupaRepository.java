package com.university.studentservice.dao;


import com.university.studentservice.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GrupaRepository extends JpaRepository<Group,Integer>, JpaSpecificationExecutor<Group> {
}

