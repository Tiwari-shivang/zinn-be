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

@Entity
@Table(name = "hirings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Hiring {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    @ManyToOne
    private Tenant tenant;

    private Date date;
    private String role, location;

    @Column(name = "total_candidates")
    private int totalCandidates;
    @Column(name = "total_openings")
    private int total_openings;
    @Column(name = "interviews_scheduled")
    private int interviewsScheduled;
    @Column(name = "offers_made")
    private int offersMade;
    private int hires;

    @Column(name = "created_at")
    private Date createdAt;
}
