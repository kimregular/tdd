package com.example.tdd;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dataKey;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(length = 1000)
    private String hashedPassword;
}
