package com.helpdesk.controller;

import com.helpdesk.entity.Review;
import com.helpdesk.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/requests/{requestId}")
    public ResponseEntity<?> submitReview(@PathVariable Long requestId, @RequestBody Map<String, Object> request) {
        try {
            Long volunteerId = Long.valueOf(request.get("volunteerId").toString());
            Integer rating = Integer.valueOf(request.get("rating").toString());
            String comment = (String) request.get("comment");

            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Rating must be between 1 and 5"));
            }

            Review review = reviewService.createReview(requestId, volunteerId, rating, comment);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Review submitted successfully");
            response.put("review", Map.of(
                "id", review.getId(),
                "rating", review.getRating(),
                "comment", review.getComment() != null ? review.getComment() : "",
                "createdAt", review.getCreatedAt().toString()
            ));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to submit review"));
        }
    }

    @GetMapping("/volunteers/{volunteerId}")
    public ResponseEntity<?> getVolunteerReviews(@PathVariable Long volunteerId) {
        try {
            List<Review> reviews = reviewService.getReviewsForVolunteer(volunteerId);

            List<Map<String, Object>> reviewList = reviews.stream().map(review -> {
                Map<String, Object> reviewMap = new HashMap<>();
                reviewMap.put("id", review.getId());
                reviewMap.put("rating", review.getRating());
                reviewMap.put("comment", review.getComment() != null ? review.getComment() : "");
                reviewMap.put("createdAt", review.getCreatedAt().toString());
                reviewMap.put("request", Map.of(
                    "id", review.getRequest().getId(),
                    "title", review.getRequest().getTitle()
                ));
                return reviewMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("reviews", reviewList));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch reviews"));
        }
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<?> getRequestReviews(@PathVariable Long requestId) {
        try {
            List<Review> reviews = reviewService.getReviewsForRequest(requestId);

            List<Map<String, Object>> reviewList = reviews.stream().map(review -> {
                Map<String, Object> reviewMap = new HashMap<>();
                reviewMap.put("id", review.getId());
                reviewMap.put("rating", review.getRating());
                reviewMap.put("comment", review.getComment() != null ? review.getComment() : "");
                reviewMap.put("createdAt", review.getCreatedAt().toString());
                reviewMap.put("volunteer", Map.of(
                    "id", review.getVolunteer().getId(),
                    "name", review.getVolunteer().getName(),
                    "rating", review.getVolunteer().getRating()
                ));
                return reviewMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("reviews", reviewList));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch reviews"));
        }
    }
}
