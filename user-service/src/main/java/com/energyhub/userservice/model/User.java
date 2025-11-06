package com.energyhub.userservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User entity representing application users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Unique username for the user", example = "john_doe")
    private String username;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Encrypted password of the user", example = "password123")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Role of the user in the system", example = "ADMIN")
    private Role role;

    @Schema(description = "Physical address of the user", example = "123 Main St, Springfield")
    private String address;

    public enum Role {
        ADMIN,
        CLIENT
    }
}
