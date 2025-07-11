package com.ikeda.operational.controllers;

import com.ikeda.operational.configs.security.AuthenticationCurrentUserService;
import com.ikeda.operational.configs.security.UserDetailsImpl;
import com.ikeda.operational.models.UserModel;
import com.ikeda.operational.services.UserService;
import com.ikeda.operational.specifications.SpecificationTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/users")
@RestController
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    final UserService userService;
    final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserController(UserService userService, AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.userService = userService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }


    @PreAuthorize("hasAnyRole('COORDINATOR')")
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       Pageable pageable,
                                                       Authentication authentication){
        UUID currentUserId = authenticationCurrentUserService.getCurrrentUser().getUserId();
        logger.info(String.format("Authentication userId {%s} - getAllUsers", currentUserId));

        Page<UserModel> userModelPage = userService.findAll(spec, pageable);
        if (!userModelPage.isEmpty()){
            for (UserModel user: userModelPage.toList()){
                user.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getOneUser(user.getUserId(), authentication)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId,
                                             Authentication authentication){
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrrentUser();
        logger.info(String.format("Authentication userId {%s} - getOneUser do userId {%s}", userDetails.getUserId(), userId));

        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_COORDINATOR"))){
            return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId).get());
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}