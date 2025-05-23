package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Order> order;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Rating> rating;

}
