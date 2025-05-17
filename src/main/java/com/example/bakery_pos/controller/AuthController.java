package com.example.bakery_pos.controller;

import com.example.bakery_pos.entity.User;
import com.example.bakery_pos.repository.UserRepository;
import com.example.bakery_pos.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;  // Inject JwtUtil
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> signupData) {
        String username = signupData.get("username");
        String password = signupData.get("password");
        String email = signupData.get("email");

        // Check if username or email already exists
        if (userRepository.findByUsername(username).isPresent()) {
            return new ResponseEntity<>(Map.of("error", "Username already exists"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            return new ResponseEntity<>(Map.of("error", "Email already exists"), HttpStatus.BAD_REQUEST);
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);

        // Create a new User object
        User newUser = new User(username, hashedPassword, email);

        // Save the user to the database
        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "Signup successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // Find the user by username
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check if the password matches
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user); // Pass the User object
                return ResponseEntity.ok(Map.of("token", token));
            }
        }

        return new ResponseEntity<>(Map.of("error", "Invalid username or password"), HttpStatus.UNAUTHORIZED);
    }
}