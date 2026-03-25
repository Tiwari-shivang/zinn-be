package com.zinn.zinnbe.models;

import java.util.Date;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attritions")
public class Attrition {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    private Date date;
    private String department, location;

    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    @ManyToOne
    private Tenant tenant;

    @Column(name = "total_employees")
    private int totalEmployees;
    @Column(name = "attrition_count")
    private int attritionCount;
    @Column(name = "attrition_rate")
    private float attritionRate;
}
