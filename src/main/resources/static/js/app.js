// Global utility functions

// Get current user from localStorage
function getCurrentUser() {
    const userStr = localStorage.getItem('currentUser');
    return userStr ? JSON.parse(userStr) : null;
}

// Check if user is authenticated
function checkAuth() {
    const currentUser = getCurrentUser();
    if (!currentUser) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

// Logout function
function logout() {
    localStorage.removeItem('currentUser');
    window.location.href = 'login.html';
}

// Format date for display
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
}

// Format category for display
function formatCategory(category) {
    const categoryMap = {
        'TOOLS': '🔧 Tools & Equipment',
        'TUTORING': '📚 Tutoring & Education',
        'ERRANDS': '🛒 Errands & Shopping',
        'TRANSPORTATION': '🚗 Transportation',
        'HOUSEHOLD': '🏠 Household Help',
        'GARDENING': '🌱 Gardening',
        'TECHNOLOGY': '💻 Technology Help',
        'OTHER': '📋 Other'
    };
    return categoryMap[category] || category;
}

// Format urgency for display
function formatUrgency(urgency) {
    const urgencyMap = {
        'LOW': '🟢 Low',
        'MEDIUM': '🟡 Medium',
        'HIGH': '🔴 High'
    };
    return urgencyMap[urgency] || urgency;
}

// Format status for display
function formatStatus(status) {
    const statusMap = {
        'OPEN': 'Open',
        'ACCEPTED': 'Accepted',
        'COMPLETED': 'Completed',
        'CANCELLED': 'Cancelled'
    };
    return statusMap[status] || status;
}

// Create request card element
function createRequestCard(request, showActions = true) {
    const card = document.createElement('div');
    card.className = 'request-card';

    // For now, show accept button for all open requests (no authentication)
    const canAccept = request.status === 'OPEN';

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
            <span>Posted by: ${request.user.name}</span>
            <span>Rating: ⭐ ${request.user.rating.toFixed(1)}</span>
        </div>

        <div class="request-meta">
            <span>Posted: ${formatDate(request.createdAt)}</span>
        </div>

        ${showActions && canAccept ? `
            <div class="request-actions">
                <button class="btn btn-primary" onclick="acceptRequest(${request.id})">
                    Accept Request
                </button>
            </div>
        ` : ''}
    `;

    return card;
}

// Accept request function
async function acceptRequest(requestId) {
    if (!confirm('Do you want to accept this request?')) {
        return;
    }

    try {
        const response = await fetch(`/api/requests/${requestId}/accept`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                volunteerId: 1 // Default volunteer ID for testing
            })
        });

        const data = await response.json();

        if (data.success) {
            alert('Request accepted successfully!');
            // Reload the page to refresh the requests
            window.location.reload();
        } else {
            alert(data.error || 'Failed to accept request');
        }
    } catch (error) {
        console.error('Accept request error:', error);
        alert('An error occurred. Please try again.');
    }
}

// Complete request function
async function completeRequest(requestId) {
    const currentUser = getCurrentUser();
    if (!currentUser) {
        window.location.href = 'login.html';
        return;
    }
    
    if (!confirm('Are you sure you want to mark this request as completed?')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/requests/${requestId}/complete`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                volunteerId: currentUser.id
            })
        });
        
        const data = await response.json();
        
        if (data.success) {
            alert('Request marked as completed!');
            // Reload the page to refresh the requests
            window.location.reload();
        } else {
            alert(data.error || 'Failed to complete request');
        }
    } catch (error) {
        console.error('Complete request error:', error);
        alert('An error occurred. Please try again.');
    }
}

// Show error message
function showError(message) {
    const errorDiv = document.getElementById('error-message');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    } else {
        alert(message);
    }
}

// Show success message
function showSuccess(message) {
    const successDiv = document.getElementById('success-message');
    if (successDiv) {
        successDiv.textContent = message;
        successDiv.style.display = 'block';
    } else {
        alert(message);
    }
}

// Hide messages
function hideMessages() {
    const errorDiv = document.getElementById('error-message');
    const successDiv = document.getElementById('success-message');
    
    if (errorDiv) errorDiv.style.display = 'none';
    if (successDiv) successDiv.style.display = 'none';
}
