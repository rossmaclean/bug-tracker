package com.ross.bugtracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class UserCredentials {

    @Id
    private String userName;
    private String fullName;
    private String encryptedPassword;
    private Role role;
}
