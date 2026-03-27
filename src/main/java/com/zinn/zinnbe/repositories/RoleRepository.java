package com.zinn.zinnbe.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zinn.zinnbe.models.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
