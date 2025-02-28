package com.apexon.catchIt.user.dto;

import com.apexon.catchIt.user.model.Roles;

public class UserDto {
    private Long id;

    public UserDto(Long id, String userName, String email, Roles role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
        /*this.isAccountExpired = isAccountExpired;
        this.isAccountLocked = isAccountLocked;
        this.isCredentialsExpired = isCredentialsExpired;*/
    }

    public UserDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {

        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    private String userName;
    private String email;
    private Roles role;


   /* private boolean isAccountExpired;
    private boolean isAccountLocked;
    private boolean  isCredentialsExpired;*/
}
