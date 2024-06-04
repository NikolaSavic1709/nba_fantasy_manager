package com.ftn.sbnz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.sbnz.DTO.users.JWTToken;
import com.ftn.sbnz.DTO.users.UserCredentialsDTO;
import com.ftn.sbnz.model.models.user.Administrator;
import com.ftn.sbnz.model.models.user.User;
import com.ftn.sbnz.service.UserService;
import com.ftn.sbnz.utils.TokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

//    @Autowired
//    private IAdminService adminService;
//
//    @Autowired
//    private IRegisteredUserService registeredUserService;
//
//    @Autowired
//    private IUserActivationService userActivationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentialsDTO userCredentialsDTO) throws Exception {


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentialsDTO.getEmail(), userCredentialsDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();


        return ResponseEntity.ok(new JWTToken(jwt, expiresIn));
    }


    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logoutUser () {
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).body("Successful logout.");
    }



//    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
//    public ResponseEntity<?> register(@RequestParam("registrationData") String registration,
//                                      @RequestPart("image") MultipartFile imageFile) {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            RegistrationDTO registrationDTO = objectMapper.readValue(registration, RegistrationDTO.class);
//            RegisteredUser newUser = new RegisteredUser(registrationDTO);
//            Map<String,Object> map = grafanaApiService.CreateAllUserComponents(registrationDTO.getEmail(),registrationDTO.getPassword());
//            newUser.setGrafanaFolderId(map.get("folderId").toString());
//            newUser.setGrafanaToken(map.get("tokenKey").toString());
//            registeredUserService.register(newUser, imageFile);
//        } catch (MailSendingException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while sending mail, possible inactive email address");
//        }  catch (EmailAlreadyUsedException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (IOException e) {
//            return ResponseEntity.badRequest().body("Invalid data");
//        }
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/activate")
//    public ResponseEntity<?> activateUser(@RequestBody @Valid AccountActivationDTO accountActivationDTO) {
//
//        try {
//            userActivationService.activate(accountActivationDTO);
//            return ResponseEntity.status(HttpStatus.OK).body("Successful account activation!");
//        } catch (EntityNotFoundException | InvalidActivationResourceException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid activation");
//        } catch (UserActivationExpiredException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Activation expired. Register again!");
//        } catch (SpamAuthException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//        }
//    }

    @GetMapping(value = "/auth/{id}")
    public ResponseEntity<?> authorize(@Min(value = 1) @PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        if (tokenUtils.getIdFromToken(authHeader.substring(7)) != id)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist!");

        return ResponseEntity.status(HttpStatus.OK).body("Correct id");

    }

}
