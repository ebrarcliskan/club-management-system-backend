package org.announcement_management_service.announcement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.announcement_management_service.announcement.dto.AnnouncementRequest;
import org.announcement_management_service.announcement.dto.AnnouncementResponse;
import org.announcement_management_service.announcement.service.AnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLUB_MANAGER')")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @Valid @RequestBody AnnouncementRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        return new ResponseEntity<>(announcementService.createAnnouncement(request, userId), HttpStatus.CREATED);
    }
    
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncementsByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(announcementService.getAnnouncementsByClub(clubId));
    }
    
    @GetMapping("/club/{clubId}/active")
    public ResponseEntity<List<AnnouncementResponse>> getActiveAnnouncementsByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(announcementService.getActiveAnnouncementsByClub(clubId));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<AnnouncementResponse>> getActiveAnnouncements() {
        return ResponseEntity.ok(announcementService.getActiveAnnouncements());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementResponse> getAnnouncementById(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLUB_MANAGER')")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementRequest request) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncementsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(announcementService.getAnnouncementsByAuthor(authorId));
    }
} 