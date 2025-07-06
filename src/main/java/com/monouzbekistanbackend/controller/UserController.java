package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.dto.user.*;
import com.monouzbekistanbackend.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        try {
            String userAgent = request.getHeader("User-Agent");
            System.out.println("User-Agent: " + userAgent);
            if (userAgent != null && userAgent.toLowerCase().contains("mobile")) {
                System.out.println("Request is from a mobile device");
            } else {
                System.out.println("Request is from desktop or unknown");
            }

            UserResponse userResponse = userService.createUser(userRequest);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            UserLoginResponse userLoginResponse = userService.loginUser(userLoginRequest);
            return ResponseEntity.ok(userLoginResponse);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/forgot-username")
    public ResponseEntity<?> forgotUsername(@RequestBody ForgotUsernameRequest forgotUsernameRequest) {
        try {
            ForgotUsernameResponse forgotPassword = userService.forgotUsername(forgotUsernameRequest);
            return ResponseEntity.ok(forgotPassword);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reset-username")
    public ResponseEntity<?> resetUsername(@RequestBody ResetUsernameRequest resetUsernameRequest) {
        try {
            ForgotUsernameResponse resetUsername = userService.resetUsername(resetUsernameRequest);
            return ResponseEntity.ok(resetUsername);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getUserByTelegramUserId")
    public ResponseEntity<?> getUserByTelegramUserId(@RequestParam("telegramUserId") Long telegramUserId) {
        try {
            UserResponse userByTelegramUserId = userService.getUserByTelegramUserId(telegramUserId);
            return ResponseEntity.ok(userByTelegramUserId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
