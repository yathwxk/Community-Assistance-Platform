package com.helpdesk.service;

import com.helpdesk.entity.*;
import com.helpdesk.repository.AssignmentRepository;
import com.helpdesk.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Request createRequest(User user, String title, String description, 
                               RequestCategory category, RequestUrgency urgency) {
        Request request = new Request();
        request.setUser(user);
        request.setTitle(title);
        request.setDescription(description);
        request.setCategory(category);
        request.setUrgency(urgency);
        request.setStatus(RequestStatus.OPEN);

        return requestRepository.save(request);
    }

    public List<Request> getAllOpenRequests() {
        return requestRepository.findByStatusOrderByCreatedAtDesc(RequestStatus.OPEN);
    }

    public List<Request> getRequestsByUser(Long userId) {
        return requestRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }

    public List<Request> searchRequests(RequestCategory category, RequestUrgency urgency) {
        return requestRepository.findRequestsWithFilters(RequestStatus.OPEN, category, urgency);
    }

    public List<Request> searchRequests(RequestCategory category, RequestUrgency urgency, String searchTerm) {
        return requestRepository.findRequestsWithSearch(RequestStatus.OPEN, category, urgency, searchTerm);
    }

    public Assignment acceptRequest(Long requestId, User volunteer) {
        Optional<Request> requestOpt = requestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Request not found");
        }

        Request request = requestOpt.get();

        // Check if user is trying to accept their own request
        if (request.getUser().getId().equals(volunteer.getId())) {
            throw new RuntimeException("You cannot accept your own request");
        }

        // Check if request is still open
        if (request.getStatus() != RequestStatus.OPEN) {
            throw new RuntimeException("Request is no longer available");
        }

        // Check if already accepted by someone
        if (assignmentRepository.existsByRequestIdAndStatus(requestId, AssignmentStatus.ACCEPTED)) {
            throw new RuntimeException("Request has already been accepted by another volunteer");
        }

        // Create assignment
        Assignment assignment = new Assignment();
        assignment.setRequest(request);
        assignment.setVolunteer(volunteer);
        assignment.setStatus(AssignmentStatus.ACCEPTED);

        // Update request status
        request.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(request);

        return assignmentRepository.save(assignment);
    }

    public Request completeRequest(Long requestId, User volunteer) {
        Optional<Request> requestOpt = requestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Request not found");
        }

        Request request = requestOpt.get();

        // Find the assignment
        Optional<Assignment> assignmentOpt = assignmentRepository.findByRequestIdAndVolunteerId(requestId, volunteer.getId());
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("You are not assigned to this request");
        }

        Assignment assignment = assignmentOpt.get();

        // Update assignment status
        assignment.setStatus(AssignmentStatus.COMPLETED);
        assignment.setCompletedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        // Update request status
        request.setStatus(RequestStatus.COMPLETED);
        return requestRepository.save(request);
    }

    public Request updateRequest(Request request) {
        return requestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}
