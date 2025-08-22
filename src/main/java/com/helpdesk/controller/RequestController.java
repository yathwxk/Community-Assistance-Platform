package com.helpdesk.controller;

import com.helpdesk.entity.*;
import com.helpdesk.service.RequestService;
import com.helpdesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllRequests(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) String search) {
        try {
            List<Request> requests;

            RequestCategory requestCategory = null;
            RequestUrgency requestUrgency = null;

            if (category != null && !category.isEmpty()) {
                try {
                    requestCategory = RequestCategory.valueOf(category.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid category"));
                }
            }

            if (urgency != null && !urgency.isEmpty()) {
                try {
                    requestUrgency = RequestUrgency.valueOf(urgency.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid urgency"));
                }
            }

            if (search != null && !search.trim().isEmpty()) {
                requests = requestService.searchRequests(requestCategory, requestUrgency, search.trim());
            } else if (requestCategory != null || requestUrgency != null) {
                requests = requestService.searchRequests(requestCategory, requestUrgency);
            } else {
                requests = requestService.getAllOpenRequests();
            }

            List<Map<String, Object>> requestList = requests.stream().map(request -> {
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("id", request.getId());
                requestMap.put("title", request.getTitle());
                requestMap.put("description", request.getDescription());
                requestMap.put("category", request.getCategory().toString());
                requestMap.put("urgency", request.getUrgency().toString());
                requestMap.put("status", request.getStatus().toString());
                requestMap.put("createdAt", request.getCreatedAt().toString());
                requestMap.put("user", Map.of(
                    "id", request.getUser().getId(),
                    "name", request.getUser().getName(),
                    "rating", request.getUser().getRating()
                ));
                return requestMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("requests", requestList));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch requests"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            String categoryStr = (String) request.get("category");
            String urgencyStr = (String) request.get("urgency");

            if (title == null || description == null || categoryStr == null || urgencyStr == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
            }

            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }

            RequestCategory category;
            RequestUrgency urgency;

            try {
                category = RequestCategory.valueOf(categoryStr.toUpperCase());
                urgency = RequestUrgency.valueOf(urgencyStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid category or urgency"));
            }

            Request newRequest = requestService.createRequest(userOpt.get(), title, description, category, urgency);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Request created successfully");
            response.put("request", Map.of(
                "id", newRequest.getId(),
                "title", newRequest.getTitle(),
                "description", newRequest.getDescription(),
                "category", newRequest.getCategory().toString(),
                "urgency", newRequest.getUrgency().toString(),
                "status", newRequest.getStatus().toString(),
                "createdAt", newRequest.getCreatedAt().toString()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to create request"));
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Long volunteerId = Long.valueOf(request.get("volunteerId").toString());

            Optional<User> volunteerOpt = userService.findById(volunteerId);
            if (volunteerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Volunteer not found"));
            }

            Assignment assignment = requestService.acceptRequest(id, volunteerOpt.get());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Request accepted successfully");
            response.put("assignment", Map.of(
                "id", assignment.getId(),
                "status", assignment.getStatus().toString(),
                "createdAt", assignment.getCreatedAt().toString()
            ));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to accept request"));
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeRequest(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Long volunteerId = Long.valueOf(request.get("volunteerId").toString());

            Optional<User> volunteerOpt = userService.findById(volunteerId);
            if (volunteerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Volunteer not found"));
            }

            Request completedRequest = requestService.completeRequest(id, volunteerOpt.get());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Request completed successfully");
            response.put("request", Map.of(
                "id", completedRequest.getId(),
                "status", completedRequest.getStatus().toString()
            ));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to complete request"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        try {
            Optional<Request> requestOpt = requestService.findById(id);
            if (requestOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Request not found"));
            }

            Request request = requestOpt.get();

            // Only allow deletion of OPEN requests
            if (request.getStatus() != RequestStatus.OPEN) {
                return ResponseEntity.badRequest().body(Map.of("error", "Can only delete open requests"));
            }

            requestService.deleteRequest(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Request deleted successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to delete request"));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable Long id) {
        try {
            Optional<Request> requestOpt = requestService.findById(id);
            if (requestOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Request not found"));
            }

            Request request = requestOpt.get();
            request.setStatus(RequestStatus.CANCELLED);
            Request updatedRequest = requestService.updateRequest(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Request cancelled successfully");
            response.put("request", Map.of(
                "id", updatedRequest.getId(),
                "status", updatedRequest.getStatus().toString()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to cancel request"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Optional<Request> requestOpt = requestService.findById(id);
            if (requestOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Request not found"));
            }

            Request existingRequest = requestOpt.get();

            // Only allow editing of OPEN requests
            if (existingRequest.getStatus() != RequestStatus.OPEN) {
                return ResponseEntity.badRequest().body(Map.of("error", "Can only edit open requests"));
            }

            // Update fields if provided
            if (request.containsKey("title")) {
                existingRequest.setTitle((String) request.get("title"));
            }
            if (request.containsKey("description")) {
                existingRequest.setDescription((String) request.get("description"));
            }
            if (request.containsKey("category")) {
                try {
                    RequestCategory category = RequestCategory.valueOf(((String) request.get("category")).toUpperCase());
                    existingRequest.setCategory(category);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid category"));
                }
            }
            if (request.containsKey("urgency")) {
                try {
                    RequestUrgency urgency = RequestUrgency.valueOf(((String) request.get("urgency")).toUpperCase());
                    existingRequest.setUrgency(urgency);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid urgency"));
                }
            }

            Request updatedRequest = requestService.updateRequest(existingRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Request updated successfully");
            response.put("request", Map.of(
                "id", updatedRequest.getId(),
                "title", updatedRequest.getTitle(),
                "description", updatedRequest.getDescription(),
                "category", updatedRequest.getCategory().toString(),
                "urgency", updatedRequest.getUrgency().toString(),
                "status", updatedRequest.getStatus().toString()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to update request"));
        }
    }
}
