package com.university.studentservice.service;

import com.university.studentservice.dao.GrupaRepository;
import com.university.studentservice.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupaServiceImpl implements GrupaService{
    @Autowired
    private GrupaRepository grupaRepository;

    @Override
    public Group get(int id) {
        Group grupa=null;
        Optional<Group> optional =grupaRepository.findById(id);
        if(optional.isPresent()) grupa=optional.get();
        return grupa;
    }

    @Override
    public Group save(Group grupa) {
   return grupaRepository.save(grupa);
    }

    @Override
    public void delete(int id) {
        grupaRepository.deleteById(id);
    }

    @Override
    public List<Group> getAll() {
        return grupaRepository.findAll();
    }
}
