package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "carts")
public class Carts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "session_id")
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CartItems> items = new ArrayList<>();


    public boolean isAnonymousCart() {
        return user == null && sessionId != null;
    }

    public boolean isUserCart() {
        return user != null;
    }
}
