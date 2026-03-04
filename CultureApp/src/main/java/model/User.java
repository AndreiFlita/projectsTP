package model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String nume;
    private String email;
    private String parola;
    private String rol; // user, artist, admin
    private LocalDateTime dataInregistrarii;
    private String bio;
    private String interese;
    private String state; // active, banned

    public User() {
        this.dataInregistrarii = LocalDateTime.now();
        this.rol = "user";
        this.state = "active";
    }

    public User(String nume, String email, String parola) {
        this();
        this.nume = nume;
        this.email = email;
        this.parola = parola;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getDataInregistrarii() {
        return dataInregistrarii;
    }

    public void setDataInregistrarii(LocalDateTime dataInregistrarii) {
        this.dataInregistrarii = dataInregistrarii;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getInterese() {
        return interese;
    }

    public void setInterese(String interese) {
        this.interese = interese;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isBanned() {
        return "banned".equals(state);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", nume='" + nume + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}