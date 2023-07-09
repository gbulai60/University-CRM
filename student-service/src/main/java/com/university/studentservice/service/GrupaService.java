package com.university.studentservice.service;


import com.university.studentservice.entity.Group;

import java.util.List;

public interface GrupaService {
    public Group get(int id);
    public Group save(Group grupa);
    public void delete(int id);
    public List<Group> getAll();
}
