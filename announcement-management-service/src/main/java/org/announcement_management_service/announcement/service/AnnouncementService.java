package org.announcement_management_service.announcement.service;

import lombok.RequiredArgsConstructor;
import org.announcement_management_service.announcement.dto.AnnouncementRequest;
import org.announcement_management_service.announcement.dto.AnnouncementResponse;
import org.announcement_management_service.announcement.exception.AnnouncementNotFoundException;
import org.announcement_management_service.announcement.model.Announcement;
import org.announcement_management_service.announcement.repository.AnnouncementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final Logger logger = LoggerFactory.getLogger(AnnouncementService.class);
    private final AnnouncementRepository announcementRepository;
    
    @Transactional
    public AnnouncementResponse createAnnouncement(AnnouncementRequest request, Long authorId) {
        logger.info("Creating new announcement with title: {}", request.getTitle());
        
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setClubId(request.getClubId());
        announcement.setAuthorId(authorId);
        announcement.setExpirationDate(request.getExpirationDate());
        
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        logger.info("Created announcement with ID: {}", savedAnnouncement.getId());
        
        return convertToResponse(savedAnnouncement);
    }
    
    public List<AnnouncementResponse> getAnnouncementsByClub(Long clubId) {
        logger.info("Fetching announcements for club ID: {}", clubId);
        return announcementRepository.findByClubId(clubId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<AnnouncementResponse> getActiveAnnouncementsByClub(Long clubId) {
        logger.info("Fetching active announcements for club ID: {}", clubId);
        return announcementRepository.findByClubIdAndIsActiveTrue(clubId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<AnnouncementResponse> getActiveAnnouncements() {
        logger.info("Fetching all active announcements");
        return announcementRepository.findByIsActiveTrueAndExpirationDateAfterOrExpirationDateIsNull(LocalDateTime.now())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public AnnouncementResponse getAnnouncementById(Long id) {
        logger.info("Fetching announcement with ID: {}", id);
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found with id: " + id));
        return convertToResponse(announcement);
    }
    
    @Transactional
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request) {
        logger.info("Updating announcement with ID: {}", id);
        
        Announcement existingAnnouncement = announcementRepository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found with id: " + id));
        
        existingAnnouncement.setTitle(request.getTitle());
        existingAnnouncement.setContent(request.getContent());
        existingAnnouncement.setExpirationDate(request.getExpirationDate());
        
        Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);
        logger.info("Updated announcement with ID: {}", updatedAnnouncement.getId());
        
        return convertToResponse(updatedAnnouncement);
    }
    
    @Transactional
    public void deleteAnnouncement(Long id) {
        logger.info("Deleting announcement with ID: {}", id);
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found with id: " + id));
        announcementRepository.delete(announcement);
        logger.info("Deleted announcement with ID: {}", id);
    }
    
    public List<AnnouncementResponse> getAnnouncementsByAuthor(Long authorId) {
        logger.info("Fetching announcements for author ID: {}", authorId);
        return announcementRepository.findByAuthorId(authorId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private AnnouncementResponse convertToResponse(Announcement announcement) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setTitle(announcement.getTitle());
        response.setContent(announcement.getContent());
        response.setClubId(announcement.getClubId());
        response.setAuthorId(announcement.getAuthorId());
        response.setActive(announcement.isActive());
        response.setExpirationDate(announcement.getExpirationDate());
        response.setCreatedAt(announcement.getCreatedAt());
        response.setUpdatedAt(announcement.getUpdatedAt());
        return response;
    }
} 