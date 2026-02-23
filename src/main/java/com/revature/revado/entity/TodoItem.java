package com.revature.revado.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Data
@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoItem {
    @Id
    /*@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;
    private ItemStatus status = ItemStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "created_by_id", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private User assignedTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ItemStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
