package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.user.User;
import com.ftn.sbnz.model.repository.users.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User get(Integer id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User save(User user) { return userRepository.save(user);};

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }

    private User getByResource(String activationResource) throws EntityNotFoundException{
        return userRepository.findByEmail(activationResource).orElseThrow(EntityNotFoundException::new);
    }


}
