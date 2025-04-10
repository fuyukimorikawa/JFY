package com.example.demo.entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Session_metadata {

    private String passwordHash;
    private Instant expiresAt;
}
