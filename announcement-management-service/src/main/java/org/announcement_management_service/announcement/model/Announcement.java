package org.announcement_management_service.announcement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "announcements")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title cannot be empty")
    @Column(nullable = false)
    private String title;
    
    @NotBlank(message = "Content cannot be empty")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @NotNull(message = "Club ID is required")
    @Column(nullable = false)
    private Long clubId;
    
    @NotNull(message = "Author ID is required")
    @Column(nullable = false)
    private Long authorId;
    
    private boolean isActive = true;
    
    private LocalDateTime expirationDate;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
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
} 