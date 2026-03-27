package com.zinn.zinnbe.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zinn.zinnbe.models.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
}
