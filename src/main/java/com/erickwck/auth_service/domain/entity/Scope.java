package com.erickwck.auth_service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_scopes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Scope {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "scope_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public static class SecurityScopes {

        public static final String SCOPE_FLIGHT_READ = "FLIGHT:READ";
        public static final String SCOPE_FLIGHT_WRITE = "FLIGHT:WRITE";
        public static final String SCOPE_FLIGHT_UPDATE = "FLIGHT:UPDATE";
        public static final String SCOPE_FLIGHT_DELETE = "FLIGHT:DELETE";
        public static final String SCOPE_BOOKING_READ =  "SCOPE_BOOKING:READ";

    }
}


