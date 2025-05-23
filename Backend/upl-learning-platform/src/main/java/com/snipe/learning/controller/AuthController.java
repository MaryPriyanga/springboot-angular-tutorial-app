package com.snipe.learning.controller;

import com.snipe.learning.AOP.HandlerService;
import com.snipe.learning.entity.User;
import com.snipe.learning.model.UserDTO;
import com.snipe.learning.service.AdminService;
import com.snipe.learning.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private AdminService userService;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HandlerService handlerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        return handlerService.handleServiceCall(() -> {
            userService.registerInstructor(userDTO);
            return null;
        }, "INFO.REGISTER_SUCCESS");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        return handlerService.handleServiceCall(() -> {
            String email = credentials.get("email");
            String password = credentials.get("password");
            User user = userService.login(email, password);
            String token = jwtUtil.generateToken(user);
            return Map.of("token", token);
        }, "INFO.LOGIN_SUCCESS");
    }
}
