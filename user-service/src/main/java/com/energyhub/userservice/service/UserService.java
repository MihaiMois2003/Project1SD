package com.energyhub.userservice.service;

import com.energyhub.userservice.config.RabbitMQConfig;
import com.energyhub.userservice.dto.UserSyncDTO;
import com.energyhub.userservice.model.User;
import com.energyhub.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        // Save user to database
        User savedUser = userRepository.save(user);

        // Send synchronization message to RabbitMQ
        UserSyncDTO syncDTO = new UserSyncDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_QUEUE, syncDTO);
        System.out.println("✅ User created and sync message sent: " + syncDTO);

        return savedUser;
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setRole(userDetails.getRole());
        user.setAddress(userDetails.getAddress());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}