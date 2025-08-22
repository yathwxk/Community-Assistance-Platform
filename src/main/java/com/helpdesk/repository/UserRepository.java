package com.helpdesk.repository;

import com.helpdesk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.volunteer.id = :volunteerId")
    Double getAverageRatingForVolunteer(@Param("volunteerId") Long volunteerId);
}
