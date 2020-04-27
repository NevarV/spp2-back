package com.spp2.service.backproject.security.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.spp2.service.backproject.entities.Client;
import com.spp2.service.backproject.entities.ERole;
import com.spp2.service.backproject.entities.Role;
import com.spp2.service.backproject.repositories.ClientRepository;
import com.spp2.service.backproject.repositories.RoleRepository;
import com.spp2.service.backproject.security.entities.MessageResponse;
import com.spp2.service.backproject.security.entities.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AuthMutationResolver implements GraphQLMutationResolver {

    private final ClientRepository clientRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    public AuthMutationResolver(ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (clientRepository.existsByUsername(signUpRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!", false);
        }

        // Create new user's account
        Client client = new Client(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_READ)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_WRITE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_READ)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        client.setRoles(roles);
        clientRepository.save(client);

        return new MessageResponse("User registered successfully!", true);
    }


}
