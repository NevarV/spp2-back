package com.spp2.service.backproject.controllers;

import com.spp2.service.backproject.entities.User;
import com.spp2.service.backproject.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MainController {

    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/search")
    public List<User> searchByName(@RequestParam("Name") String name) {
        return userRepository.findByName(name);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userRepository.getOne(id);
    }

    @PutMapping("/users/{id}/edit")
    public ResponseEntity editUser(@RequestBody User user, @PathVariable("id") Long id) {
        HttpStatus status;
        user.setId(id);
        try {
            userRepository.save(user);
            status = HttpStatus.OK;
        } catch (Exception ex) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity(status);
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody User user) {
        HttpStatus status;
        try {
            userRepository.save(user);
            status = HttpStatus.OK;
        } catch (Exception ex) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity(status);
    }

    @PutMapping("/users")
    public ResponseEntity editUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
