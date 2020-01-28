package com.spp2.service.backproject.controllers;

import com.spp2.service.backproject.entities.User;
import com.spp2.service.backproject.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/users")
    public ResponseEntity createUser(User user) {
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity editUser(User user) {
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUser(User user) {
        userRepository.delete(user);
        return new ResponseEntity(HttpStatus.OK);
    }
}
