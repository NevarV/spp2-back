package com.spp2.service.backproject.security.controllers;

import com.spp2.service.backproject.entities.Client;
import com.spp2.service.backproject.entities.ERole;
import com.spp2.service.backproject.entities.Role;
import com.spp2.service.backproject.repositories.ClientRepository;
import com.spp2.service.backproject.repositories.RoleRepository;
import com.spp2.service.backproject.security.components.JwtUtils;
import com.spp2.service.backproject.security.entities.JwtResponse;
import com.spp2.service.backproject.security.entities.LoginRequest;
import com.spp2.service.backproject.security.entities.MessageResponse;
import com.spp2.service.backproject.security.entities.SignupRequest;
import com.spp2.service.backproject.security.entities.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebSocketAuthController {

    private final SimpMessageSendingOperations messagingTemplate;

    private final AuthenticationManager authenticationManager;

    private final ClientRepository clientRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public WebSocketAuthController(SimpMessageSendingOperations messagingTemplate, AuthenticationManager authenticationManager, ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/auth/signup")
    public void createAccount(@Payload SignupRequest signUpRequest) {
//        if (clientRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }

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
    }

    @MessageMapping("/auth/signin")
    @SendToUser("/queue/auth/reply")
    public JwtResponse loginAccount(@Payload LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }


}
