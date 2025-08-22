package com.helpdesk.repository;

import com.helpdesk.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByVolunteerIdOrderByCreatedAtDesc(Long volunteerId);
    
    List<Review> findByRequestIdOrderByCreatedAtDesc(Long requestId);
    
    Optional<Review> findByRequestIdAndVolunteerId(Long requestId, Long volunteerId);
    
    boolean existsByRequestIdAndVolunteerId(Long requestId, Long volunteerId);
}
