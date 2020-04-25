package com.spp2.service.backproject.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.spp2.service.backproject.entities.User;
import com.spp2.service.backproject.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {

    private final UserRepository userRepository;

    public Query(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ROLE_READ')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_READ')")
    public List<User> getUsersByName(String name) {
        return userRepository.findByName(name);
    }

    @PreAuthorize("hasRole('ROLE_WRITE')")
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }
}
