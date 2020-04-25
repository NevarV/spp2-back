package com.spp2.service.backproject.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.spp2.service.backproject.entities.User;
import com.spp2.service.backproject.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

    private final UserRepository userRepository;

    public Mutation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ROLE_WRITE')")
    public boolean addUser(User user) {
        user.setId(0);
        var successfully = true;
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Cannot add user " + e.getMessage());
            successfully = false;
        }
        return successfully;
    }

    @PreAuthorize("hasRole('ROLE_WRITE')")
    public boolean editUser(User user) {
        var successfully = true;
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Cannot edit user " + e.getMessage());
            successfully = false;
        }
        return successfully;
    }

    @PreAuthorize("hasRole('ROLE_WRITE')")
    public boolean deleteUser(long id) {
        var successfully = true;
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Cannot delete user " + e.getMessage());
            successfully = false;
        }
        return successfully;
    }

}
