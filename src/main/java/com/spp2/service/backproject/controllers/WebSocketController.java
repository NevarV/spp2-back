package com.spp2.service.backproject.controllers;

import com.spp2.service.backproject.entities.User;
import com.spp2.service.backproject.repositories.UserRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;

    private final UserRepository userRepository;

    public WebSocketController(SimpMessageSendingOperations messagingTemplate, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @SubscribeMapping("/topic/users")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @MessageMapping("/users")
    @SendToUser("/queue/users")
    public List<User> processMessageFromClient() {
        return userRepository.findAll();
    }

    @MessageMapping("/users/filter")
    @SendToUser("/queue/users")
    public List<User> getUsersByName(@Payload String name) {
        return userRepository.findByName(name);
    }

    @MessageMapping("/users/{id}")
    @SendToUser("/queue/users/edit")
    public User getUser(@DestinationVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @MessageMapping("/users/new")
    @SendTo("/topic/users")
    public List<User> createUser(@Payload User user) {
        userRepository.save(user);
        return userRepository.findAll();
    }

    @MessageMapping("/users/edit")
    @SendTo("/topic/users")
    public List<User> editUser(@Payload User user) {
        userRepository.save(user);
        return userRepository.findAll();
    }

    @MessageMapping("/users/{id}/delete")
    @SendTo("/topic/users")
    public List<User> deleteUser(@DestinationVariable Long id) {
        userRepository.deleteById(id);
        return userRepository.findAll();
    }

    @MessageExceptionHandler
    @SendToUser(destinations="/queue/errors")
    public String handleException(Exception e) {
        messagingTemplate.convertAndSend("/errors", e.getMessage());
        return e.getMessage();
    }

}
