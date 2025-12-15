package com.energyhub.authservice.consumer;

import com.energyhub.authservice.dto.UserSyncDTO;
import com.energyhub.authservice.model.User;
import com.energyhub.authservice.model.User.Role;
import com.energyhub.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSyncConsumer {

    private final UserRepository userRepository;

    @RabbitListener(queues = "user.sync")
    public void consumeUserSync(UserSyncDTO userDTO) {
        System.out.println("📥 [AUTH-SERVICE] Received user sync: " + userDTO);

        try {
            User existingUser = userRepository.findByUsername(userDTO.getUsername())
                    .orElse(null);

            if (existingUser == null) {
                User newUser = new User();
                // newUser.setId(userDTO.getId());
                newUser.setUsername(userDTO.getUsername());
                newUser.setEmail(userDTO.getEmail());
                newUser.setPassword(userDTO.getPassword());
                newUser.setRole(Role.valueOf(userDTO.getRole()));
                newUser.setAddress(userDTO.getAddress());

                userRepository.save(newUser);
                System.out.println("✅ [AUTH-SERVICE] Created user in authdb: " + userDTO.getUsername());
            } else {
                existingUser.setEmail(userDTO.getEmail());
                existingUser.setPassword(userDTO.getPassword());
                existingUser.setRole(Role.valueOf(userDTO.getRole()));
                existingUser.setAddress(userDTO.getAddress());

                userRepository.save(existingUser);
                System.out.println("🔄 [AUTH-SERVICE] Updated user in authdb: " + userDTO.getUsername());
            }
        } catch (Exception e) {
            System.err.println("❌ [AUTH-SERVICE] Error syncing user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}