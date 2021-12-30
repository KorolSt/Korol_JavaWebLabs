package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.UserRole;

import java.util.Objects;

public class User extends Entity {
    private static final long serialVersionUID = -6246976392769756740L;

    private String login;
    private String password;
    private UserRole role;

    private String name;
    private String surname;
    private String phone;
    private String email;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole userRole) {
        this.role = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "User [login=" + login + ", password=" + password + ", role=" + role +
                ", name=" + name + ", surname=" + surname + ", phone=" + phone +
                ", email='" + email + ']';
    }
}
