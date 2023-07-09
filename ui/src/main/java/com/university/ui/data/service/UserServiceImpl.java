package com.university.ui.data.service;

import com.university.ui.data.dao.UserRepository;
import com.university.ui.data.entity.User;
import java.util.Optional;

import jakarta.mail.MessagingException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final JavaMailSender mailSender;
    private final UserRepository repository;

    public UserServiceImpl(JavaMailSender mailSender, UserRepository repository) {
        this.mailSender = mailSender;
        this.repository = repository;
    }
    @Override
    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public User update(User entity) {
        return repository.save(entity);
    }
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User findByIdnp(String idnp) {
        return repository.findByIdnp(idnp);
    }
    @Override
    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
    @Override
    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }
    @Override
    public int count() {
        return (int) repository.count();
    }
    @Override
    public String generateRandomPassword(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    @Override
    public void sendEmail(String emailAddress, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailAddress );
        message.setSubject("Login credentials");
        message.setText("Your login: "+emailAddress+"\nYour generated password: "+password);
        mailSender.send(message);
    }

}
