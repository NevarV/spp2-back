package com.spp2.service.backproject.security.services;

import com.spp2.service.backproject.entities.Client;
import com.spp2.service.backproject.repositories.ClientRepository;
import com.spp2.service.backproject.security.entities.UserDetailsImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    public UserDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(client);
    }
}
