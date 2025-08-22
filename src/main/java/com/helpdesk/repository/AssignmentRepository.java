package com.helpdesk.repository;

import com.helpdesk.entity.Assignment;
import com.helpdesk.entity.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByVolunteerIdOrderByCreatedAtDesc(Long volunteerId);
    
    List<Assignment> findByRequestIdOrderByCreatedAtDesc(Long requestId);
    
    Optional<Assignment> findByRequestIdAndVolunteerId(Long requestId, Long volunteerId);
    
    List<Assignment> findByStatus(AssignmentStatus status);
    
    boolean existsByRequestIdAndStatus(Long requestId, AssignmentStatus status);
}
