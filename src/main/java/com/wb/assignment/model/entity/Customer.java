package com.wb.assignment.model.entity;

import com.wb.assignment.model.enums.CustomerStatus;
import com.wb.assignment.model.enums.CustomerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "legal_id")
        }
)
public class Customer implements Serializable {

    @Id
    @Column(name = "customer_id", length = 7, nullable = false, updatable = false)
    @Pattern(regexp = "\\d{7}", message = "Customer ID must be exactly 7 digits")
    private String customerId;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(name = "legal_id", nullable = false, length = 50, unique = true)
    private String legalId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, length = 20)
    private CustomerType customerType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private CustomerStatus status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Email
    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (Objects.isNull(this.status)) {
            this.status = CustomerStatus.ACTIVE;
        }
    }
}

