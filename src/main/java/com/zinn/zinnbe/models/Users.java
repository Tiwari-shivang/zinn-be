package com.zinn.zinnbe.models;

import java.security.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(name = "email", unique = true)
    private String email;
    private String name, password;

    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    @ManyToOne
    private Tenant tenant;

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private Role role;

    @Column(name = "created_at")
    private Timestamp createdAt;

}
