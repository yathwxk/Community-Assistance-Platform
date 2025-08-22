// Dashboard functionality

let currentReviewRequestId = null;
let currentReviewVolunteerId = null;

// Initialize dashboard
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    
    const currentUser = getCurrentUser();
    if (currentUser) {
        document.getElementById('user-name').textContent = currentUser.name;
        document.getElementById('welcome-name').textContent = currentUser.name;
    }
    
    // Load initial data
    loadAvailableRequests();
    
    // Setup star rating
    setupStarRating();
});

// Tab switching
function showTab(tabName) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // Remove active class from all tab buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Show selected tab
    document.getElementById(tabName + '-tab').classList.add('active');
    
    // Add active class to clicked button
    event.target.classList.add('active');
    
    // Load data for the selected tab
    switch(tabName) {
        case 'available':
            loadAvailableRequests();
            break;
        case 'my-requests':
            loadMyRequests();
            break;
        case 'my-assignments':
            loadMyAssignments();
            break;
    }
}

// Load available requests
async function loadAvailableRequests() {
    try {
        const category = document.getElementById('category-filter').value;
        const urgency = document.getElementById('urgency-filter').value;
        
        let url = '/api/requests';
        const params = new URLSearchParams();
        
        if (category) params.append('category', category);
        if (urgency) params.append('urgency', urgency);
        
        if (params.toString()) {
            url += '?' + params.toString();
        }
        
        const response = await fetch(url);
        const data = await response.json();
        
        const container = document.getElementById('available-requests');
        container.innerHTML = '';
        
        if (data.requests && data.requests.length > 0) {
            data.requests.forEach(request => {
                const card = createRequestCard(request, true);
                container.appendChild(card);
            });
        } else {
            container.innerHTML = '<p class="no-requests">No requests available at the moment.</p>';
        }
    } catch (error) {
        console.error('Error loading available requests:', error);
        document.getElementById('available-requests').innerHTML = 
            '<p class="error">Unable to load requests. Please try again later.</p>';
    }
}

// Load user's requests
async function loadMyRequests() {
    const currentUser = getCurrentUser();
    if (!currentUser) return;
    
    try {
        const response = await fetch('/api/requests');
        const data = await response.json();
        
        // Filter requests by current user
        const myRequests = data.requests ? data.requests.filter(request => 
            request.user.id === currentUser.id
        ) : [];
        
        const container = document.getElementById('my-requests');
        container.innerHTML = '';
        
        if (myRequests.length > 0) {
            myRequests.forEach(request => {
                const card = createMyRequestCard(request);
                container.appendChild(card);
            });
        } else {
            container.innerHTML = '<p class="no-requests">You haven\'t posted any requests yet. <a href="post_request.html">Post your first request</a></p>';
        }
    } catch (error) {
        console.error('Error loading my requests:', error);
        document.getElementById('my-requests').innerHTML = 
            '<p class="error">Unable to load your requests. Please try again later.</p>';
    }
}

// Load user's assignments (as volunteer)
async function loadMyAssignments() {
    // For now, we'll show a placeholder since we don't have a specific endpoint
    // In a real implementation, you'd have an endpoint like /api/assignments/my
    const container = document.getElementById('my-assignments');
    container.innerHTML = '<p class="no-requests">Assignment tracking coming soon...</p>';
}

// Create card for user's own requests
function createMyRequestCard(request) {
    const card = document.createElement('div');
    card.className = 'request-card';
    
    card.innerHTML = `
        <div class="request-header">
            <div>
                <h3 class="request-title">${request.title}</h3>
                <div class="request-meta">
                    <span class="status-badge status-${request.status.toLowerCase()}">${formatStatus(request.status)}</span>
                    <span class="urgency-badge urgency-${request.urgency.toLowerCase()}">${formatUrgency(request.urgency)}</span>
                </div>
            </div>
        </div>
        
        <p class="request-description">${request.description}</p>
        
        <div class="request-meta">
            <span>${formatCategory(request.category)}</span>
            <span>Posted: ${formatDate(request.createdAt)}</span>
        </div>
        
        <div class="request-actions">
            ${request.status === 'OPEN' ? `
                <button class="btn btn-secondary" onclick="editRequest(${request.id})">Edit</button>
                <button class="btn btn-secondary" onclick="deleteRequest(${request.id})">Delete</button>
            ` : ''}
            ${request.status === 'COMPLETED' ? `
                <button class="btn btn-primary" onclick="openReviewModal(${request.id})">Rate Helper</button>
            ` : ''}
        </div>
    `;
    
    return card;
}

// Filter requests
function filterRequests() {
    loadAvailableRequests();
}

// Edit request (placeholder)
function editRequest(requestId) {
    alert('Edit functionality coming soon...');
}

// Delete request (placeholder)
function deleteRequest(requestId) {
    if (confirm('Are you sure you want to delete this request?')) {
        alert('Delete functionality coming soon...');
    }
}

// Review Modal Functions
function openReviewModal(requestId) {
    currentReviewRequestId = requestId;
    // For now, we'll use a placeholder volunteer ID
    // In a real implementation, you'd get this from the assignment data
    currentReviewVolunteerId = 1; // Placeholder
    
    document.getElementById('review-modal').style.display = 'flex';
}

function closeReviewModal() {
    document.getElementById('review-modal').style.display = 'none';
    currentReviewRequestId = null;
    currentReviewVolunteerId = null;
    
    // Reset form
    document.getElementById('review-form').reset();
    document.getElementById('rating-value').value = '';
    document.querySelectorAll('.star').forEach(star => {
        star.classList.remove('active');
    });
}

// Setup star rating
function setupStarRating() {
    const stars = document.querySelectorAll('.star');
    
    stars.forEach(star => {
        star.addEventListener('click', function() {
            const rating = parseInt(this.dataset.rating);
            document.getElementById('rating-value').value = rating;
            
            // Update star display
            stars.forEach((s, index) => {
                if (index < rating) {
                    s.classList.add('active');
                } else {
                    s.classList.remove('active');
                }
            });
        });
        
        star.addEventListener('mouseover', function() {
            const rating = parseInt(this.dataset.rating);
            
            stars.forEach((s, index) => {
                if (index < rating) {
                    s.style.color = '#ffc107';
                } else {
                    s.style.color = '#ddd';
                }
            });
        });
    });
    
    // Reset on mouse leave
    document.querySelector('.star-rating').addEventListener('mouseleave', function() {
        const currentRating = parseInt(document.getElementById('rating-value').value) || 0;
        
        stars.forEach((s, index) => {
            if (index < currentRating) {
                s.style.color = '#ffc107';
            } else {
                s.style.color = '#ddd';
            }
        });
    });
}

// Submit review
document.getElementById('review-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const rating = document.getElementById('rating-value').value;
    const comment = document.getElementById('comment').value;
    
    if (!rating) {
        alert('Please select a rating');
        return;
    }
    
    try {
        const response = await fetch(`/api/reviews/requests/${currentReviewRequestId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                volunteerId: currentReviewVolunteerId,
                rating: parseInt(rating),
                comment: comment
            })
        });
        
        const data = await response.json();
        
        if (data.success) {
            alert('Review submitted successfully!');
            closeReviewModal();
            loadMyRequests(); // Refresh the requests
        } else {
            alert(data.error || 'Failed to submit review');
        }
    } catch (error) {
        console.error('Submit review error:', error);
        alert('An error occurred. Please try again.');
    }
});
