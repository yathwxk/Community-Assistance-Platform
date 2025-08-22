package com.helpdesk.controller;

import com.helpdesk.entity.User;
import com.helpdesk.service.UserService;
import com.helpdesk.service.RequestService;
import com.helpdesk.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/members")
    public ResponseEntity<?> getCommunityMembers() {
        try {
            List<User> users = userService.findAllUsers();

            List<Map<String, Object>> membersList = users.stream().map(user -> {
                Map<String, Object> memberInfo = new HashMap<>();
                memberInfo.put("id", user.getId());
                memberInfo.put("name", user.getName());
                memberInfo.put("email", user.getEmail());
                memberInfo.put("role", user.getRole().toString());
                memberInfo.put("rating", user.getRating());
                memberInfo.put("joinedAt", user.getCreatedAt().toString());
                
                // Get user's request count
                int requestCount = requestService.getRequestsByUser(user.getId()).size();
                memberInfo.put("totalRequests", requestCount);
                
                // Get user's reviews count (as volunteer)
                int reviewCount = reviewService.getReviewsForVolunteer(user.getId()).size();
                memberInfo.put("totalReviews", reviewCount);
                
                // Calculate activity level
                String activityLevel = calculateActivityLevel(requestCount, reviewCount, user.getRating());
                memberInfo.put("activityLevel", activityLevel);
                
                // Get recent activity summary
                String recentActivity = getRecentActivitySummary(user, requestCount, reviewCount);
                memberInfo.put("recentActivity", recentActivity);
                
                return memberInfo;
            }).collect(Collectors.toList());

            // Sort by rating (highest first)
            membersList.sort((a, b) -> Double.compare((Double) b.get("rating"), (Double) a.get("rating")));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("members", membersList);
            response.put("totalMembers", membersList.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch community members"));
        }
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMemberDetails(@PathVariable Long id) {
        try {
            var userOpt = userService.findById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Member not found"));
            }

            User user = userOpt.get();
            var requests = requestService.getRequestsByUser(id);
            var reviews = reviewService.getReviewsForVolunteer(id);

            Map<String, Object> memberDetails = new HashMap<>();
            memberDetails.put("id", user.getId());
            memberDetails.put("name", user.getName());
            memberDetails.put("email", user.getEmail());
            memberDetails.put("role", user.getRole().toString());
            memberDetails.put("rating", user.getRating());
            memberDetails.put("joinedAt", user.getCreatedAt().toString());
            memberDetails.put("totalRequests", requests.size());
            memberDetails.put("totalReviews", reviews.size());
            
            // Recent requests
            var recentRequests = requests.stream()
                .limit(5)
                .map(request -> Map.of(
                    "id", request.getId(),
                    "title", request.getTitle(),
                    "category", request.getCategory().toString(),
                    "status", request.getStatus().toString(),
                    "createdAt", request.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
            memberDetails.put("recentRequests", recentRequests);
            
            // Recent reviews
            var recentReviews = reviews.stream()
                .limit(5)
                .map(review -> Map.of(
                    "id", review.getId(),
                    "rating", review.getRating(),
                    "comment", review.getComment() != null ? review.getComment() : "",
                    "requestTitle", review.getRequest().getTitle(),
                    "createdAt", review.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
            memberDetails.put("recentReviews", recentReviews);

            return ResponseEntity.ok(memberDetails);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch member details"));
        }
    }

    private String calculateActivityLevel(int requestCount, int reviewCount, double rating) {
        int totalActivity = requestCount + reviewCount;
        
        if (totalActivity >= 10 && rating >= 4.0) {
            return "Very Active";
        } else if (totalActivity >= 5 && rating >= 3.5) {
            return "Active";
        } else if (totalActivity >= 2) {
            return "Moderate";
        } else {
            return "New Member";
        }
    }

    private String getRecentActivitySummary(User user, int requestCount, int reviewCount) {
        if (requestCount > 0 && reviewCount > 0) {
            return "Posts requests and helps others";
        } else if (requestCount > 0) {
            return "Posts help requests";
        } else if (reviewCount > 0) {
            return "Helps community members";
        } else {
            return "Recently joined the community";
        }
    }
}
