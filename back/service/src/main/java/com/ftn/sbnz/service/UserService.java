package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.repository.users.IUserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;

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
