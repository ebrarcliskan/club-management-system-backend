package org.announcement_management_service.announcement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private Long clubId;
    private Long authorId;
    private boolean isActive;
    private LocalDateTime expirationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 