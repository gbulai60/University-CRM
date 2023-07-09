package com.university.ui.data.service;
import com.university.ui.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface UserService {
    public Optional<User> get(Long id);

    public User update(User entity);

    public void delete(Long id);
    public User findByIdnp(String idnp);

    public Page<User> list(Pageable pageable);

    public Page<User> list(Pageable pageable, Specification<User> filter) ;

    public int count();
    public String generateRandomPassword(int length);
    public void sendEmail(String email,String password);
}
