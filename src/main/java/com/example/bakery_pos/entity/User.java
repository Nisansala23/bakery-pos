package com.example.bakery_pos.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements UserDetails {
=======
import java.util.Objects;

@Entity
@Table(name = "users") // Use a plural table name for consistency
public class User {
>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

<<<<<<< HEAD
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new ArrayList<>();

    // 🔧 No-arg constructor required by JPA
    public User() {}

    // ✅ Custom constructor for signup
=======
    // You can add more fields like 'registrationDate', 'roles', etc.

    // Default constructor (required by JPA)
    public User() {
    }

>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
<<<<<<< HEAD
        this.roles = new ArrayList<>();
    }

    // 🔐 Spring Security method: return roles as authorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    // Spring Security required methods
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    public Collection<Role> getRoles() { return roles; }
    public void setRoles(Collection<Role> roles) { this.roles = roles; }
}
=======
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}
>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c
