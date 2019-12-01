package com.ross.bugtracker.model;

import lombok.Data;

@Data
public class UserRegistrationModel {

    private String username;
    private String fullname;
    private String password;
    private String confirmPassword;
    private Role role;
}
