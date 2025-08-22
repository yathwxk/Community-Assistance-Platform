package com.helpdesk.service;

import com.helpdesk.entity.Assignment;
import com.helpdesk.entity.Request;
import com.helpdesk.entity.Review;
import com.helpdesk.entity.User;
import com.helpdesk.repository.AssignmentRepository;
import com.helpdesk.repository.RequestRepository;
import com.helpdesk.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserService userService;

    public Review createReview(Long requestId, Long volunteerId, Integer rating, String comment) {
        // Verify request exists and is completed
        Optional<Request> requestOpt = requestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Request not found");
        }

        Request request = requestOpt.get();
        if (request.getStatus() != com.helpdesk.entity.RequestStatus.COMPLETED) {
            throw new RuntimeException("Request must be completed before reviewing");
        }

        // Verify volunteer exists
        Optional<User> volunteerOpt = userService.findById(volunteerId);
        if (volunteerOpt.isEmpty()) {
            throw new RuntimeException("Volunteer not found");
        }

        User volunteer = volunteerOpt.get();

        // Check if review already exists
        if (reviewRepository.existsByRequestIdAndVolunteerId(requestId, volunteerId)) {
            throw new RuntimeException("Review already exists for this request and volunteer");
        }

        // Verify assignment exists
        Optional<Assignment> assignmentOpt = assignmentRepository.findByRequestIdAndVolunteerId(requestId, volunteerId);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("No assignment found for this request and volunteer");
        }

        // Create review
        Review review = new Review();
        review.setRequest(request);
        review.setVolunteer(volunteer);
        review.setRating(rating);
        review.setComment(comment);

        Review savedReview = reviewRepository.save(review);

        // Update volunteer's average rating
        userService.updateUserRating(volunteerId);

        return savedReview;
    }

    public List<Review> getReviewsForVolunteer(Long volunteerId) {
        return reviewRepository.findByVolunteerIdOrderByCreatedAtDesc(volunteerId);
    }

    public List<Review> getReviewsForRequest(Long requestId) {
        return reviewRepository.findByRequestIdOrderByCreatedAtDesc(requestId);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public boolean hasReviewForRequest(Long requestId, Long volunteerId) {
        return reviewRepository.existsByRequestIdAndVolunteerId(requestId, volunteerId);
    }
}
