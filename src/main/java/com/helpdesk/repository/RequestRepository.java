package com.helpdesk.repository;

import com.helpdesk.entity.Request;
import com.helpdesk.entity.RequestCategory;
import com.helpdesk.entity.RequestStatus;
import com.helpdesk.entity.RequestUrgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    
    List<Request> findByStatus(RequestStatus status);
    
    List<Request> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    
    List<Request> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Request> findByCategory(RequestCategory category);
    
    List<Request> findByUrgency(RequestUrgency urgency);
    
    @Query("SELECT r FROM Request r WHERE r.status = :status AND " +
           "(:category IS NULL OR r.category = :category) AND " +
           "(:urgency IS NULL OR r.urgency = :urgency) " +
           "ORDER BY r.createdAt DESC")
    List<Request> findRequestsWithFilters(@Param("status") RequestStatus status,
                                        @Param("category") RequestCategory category,
                                        @Param("urgency") RequestUrgency urgency);

    @Query("SELECT r FROM Request r WHERE r.status = :status AND " +
           "(:category IS NULL OR r.category = :category) AND " +
           "(:urgency IS NULL OR r.urgency = :urgency) AND " +
           "(:searchTerm IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY r.createdAt DESC")
    List<Request> findRequestsWithSearch(@Param("status") RequestStatus status,
                                       @Param("category") RequestCategory category,
                                       @Param("urgency") RequestUrgency urgency,
                                       @Param("searchTerm") String searchTerm);
}
