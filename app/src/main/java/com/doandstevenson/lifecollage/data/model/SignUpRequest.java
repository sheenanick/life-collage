package com.doandstevenson.lifecollage.data.model;

/**
 * Created by Sheena on 2/8/17.
 */

public class SignUpRequest {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    public SignUpRequest(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
