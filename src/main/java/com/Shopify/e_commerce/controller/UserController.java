package com.Shopify.e_commerce.controller;

import com.Shopify.e_commerce.entity.User;
import com.Shopify.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // CREATE (Add new user)
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // READ (Get all users)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ (Get user by ID)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // UPDATE (Edit user)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            return userRepository.save(existingUser);
        } else {
            return null; // or throw custom error
        }
    }

    // DELETE (Remove user)
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully!";
    }

    // REGISTER (register all users)
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // LOGIN (login all users)
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        User existingUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (existingUser != null) {
            return "Login Successful! Welcome " + existingUser.getName();
        } else {
            return "Invalid email or password!";
        }
    }

}
