package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 2/9/17.
 */

public class ApplicationUser {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private ApplicationToken applicationToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ApplicationToken getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(ApplicationToken applicationToken) {
        this.applicationToken = applicationToken;
    }
}
