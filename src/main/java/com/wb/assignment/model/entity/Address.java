package com.wb.assignment.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String buildingNo;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String governorate;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String area;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String addressLine1;

    @NotBlank
    @Column(nullable = false, length = 10)
    private String addressLine2;
}

