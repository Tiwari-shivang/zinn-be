package com.zinn.zinnbe.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zinn.zinnbe.models.Users;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
