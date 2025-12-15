package com.energyhub.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSyncDTO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String address;
}