// Community page functionality

let allMembers = [];
let filteredMembers = [];

// Initialize community page
document.addEventListener('DOMContentLoaded', function() {
    loadCommunityMembers();
});

// Load all community members
async function loadCommunityMembers() {
    try {
        const response = await fetch('/api/community/members');
        const data = await response.json();
        
        if (data.success && data.members) {
            allMembers = data.members;
            filteredMembers = [...allMembers];
            
            displayMembers(filteredMembers);
            updateCommunityStats(data);
        } else {
            document.getElementById('members-container').innerHTML = 
                '<p class="no-members">No community members found.</p>';
        }
    } catch (error) {
        console.error('Error loading community members:', error);
        document.getElementById('members-container').innerHTML = 
            '<p class="error">Unable to load community members. Please try again later.</p>';
    }
}

// Display members in the grid
function displayMembers(members) {
    const container = document.getElementById('members-container');
    container.innerHTML = '';
    
    if (members.length === 0) {
        container.innerHTML = '<p class="no-members">No members match your search criteria.</p>';
        return;
    }
    
    members.forEach(member => {
        const memberCard = createMemberCard(member);
        container.appendChild(memberCard);
    });
}

// Create member card element
function createMemberCard(member) {
    const card = document.createElement('div');
    card.className = 'member-card';
    
    const activityColor = getActivityColor(member.activityLevel);
    const roleIcon = member.role === 'VOLUNTEER' ? '🤝' : '🏠';
    
    card.innerHTML = `
        <div class="member-header">
            <div class="member-avatar">
                ${roleIcon}
            </div>
            <div class="member-basic-info">
                <h3 class="member-name">${member.name}</h3>
                <div class="member-role">${member.role === 'VOLUNTEER' ? 'Volunteer' : 'Resident'}</div>
            </div>
            <div class="member-rating">
                <div class="rating-stars">${generateStars(member.rating)}</div>
                <div class="rating-number">${member.rating.toFixed(1)}</div>
            </div>
        </div>
        
        <div class="member-stats">
            <div class="stat-item">
                <span class="stat-value">${member.totalRequests}</span>
                <span class="stat-label">Requests</span>
            </div>
            <div class="stat-item">
                <span class="stat-value">${member.totalReviews}</span>
                <span class="stat-label">Reviews</span>
            </div>
            <div class="stat-item">
                <span class="activity-badge" style="background-color: ${activityColor}">
                    ${member.activityLevel}
                </span>
            </div>
        </div>
        
        <div class="member-activity">
            <p class="activity-summary">${member.recentActivity}</p>
            <p class="join-date">Joined ${formatJoinDate(member.joinedAt)}</p>
        </div>
        
        <div class="member-actions">
            <button class="btn btn-primary" onclick="viewMemberDetails(${member.id})">
                View Profile
            </button>
            <button class="btn btn-secondary" onclick="contactMember(${member.id})">
                Contact
            </button>
        </div>
    `;
    
    return card;
}

// Generate star rating display
function generateStars(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    let stars = '';
    
    for (let i = 0; i < fullStars; i++) {
        stars += '⭐';
    }
    
    if (hasHalfStar) {
        stars += '⭐';
    }
    
    // Fill remaining with empty stars
    const totalStars = hasHalfStar ? fullStars + 1 : fullStars;
    for (let i = totalStars; i < 5; i++) {
        stars += '☆';
    }
    
    return stars;
}

// Get activity level color
function getActivityColor(activityLevel) {
    switch (activityLevel) {
        case 'Very Active': return '#28a745';
        case 'Active': return '#17a2b8';
        case 'Moderate': return '#ffc107';
        case 'New Member': return '#6c757d';
        default: return '#6c757d';
    }
}

// Format join date
function formatJoinDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now - date);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays < 7) {
        return `${diffDays} days ago`;
    } else if (diffDays < 30) {
        return `${Math.ceil(diffDays / 7)} weeks ago`;
    } else if (diffDays < 365) {
        return `${Math.ceil(diffDays / 30)} months ago`;
    } else {
        return `${Math.ceil(diffDays / 365)} years ago`;
    }
}

// Update community statistics
function updateCommunityStats(data) {
    const members = data.members;
    const totalMembers = members.length;
    const activeHelpers = members.filter(m => m.activityLevel === 'Very Active' || m.activityLevel === 'Active').length;
    const avgRating = members.reduce((sum, m) => sum + m.rating, 0) / totalMembers;
    const newMembers = members.filter(m => {
        const joinDate = new Date(m.joinedAt);
        const monthAgo = new Date();
        monthAgo.setMonth(monthAgo.getMonth() - 1);
        return joinDate > monthAgo;
    }).length;
    
    document.getElementById('total-members').textContent = totalMembers;
    document.getElementById('active-helpers').textContent = activeHelpers;
    document.getElementById('avg-rating').textContent = avgRating.toFixed(1);
    document.getElementById('new-members').textContent = newMembers;
}

// Filter members
function filterMembers() {
    const roleFilter = document.getElementById('role-filter').value;
    const activityFilter = document.getElementById('activity-filter').value;
    const ratingFilter = document.getElementById('rating-filter').value;
    const searchTerm = document.getElementById('member-search').value.toLowerCase();
    
    filteredMembers = allMembers.filter(member => {
        const matchesRole = !roleFilter || member.role === roleFilter;
        const matchesActivity = !activityFilter || member.activityLevel === activityFilter;
        const matchesRating = !ratingFilter || member.rating >= parseFloat(ratingFilter);
        const matchesSearch = !searchTerm || member.name.toLowerCase().includes(searchTerm);
        
        return matchesRole && matchesActivity && matchesRating && matchesSearch;
    });
    
    displayMembers(filteredMembers);
}

// Handle member search with debounce
let memberSearchTimeout;
function handleMemberSearch(event) {
    clearTimeout(memberSearchTimeout);
    memberSearchTimeout = setTimeout(() => {
        filterMembers();
    }, 300);
}

// Clear member search
function clearMemberSearch() {
    document.getElementById('member-search').value = '';
    filterMembers();
}

// View member details
async function viewMemberDetails(memberId) {
    try {
        const response = await fetch(`/api/community/members/${memberId}`);
        const member = await response.json();
        
        if (member.error) {
            alert(member.error);
            return;
        }
        
        displayMemberModal(member);
    } catch (error) {
        console.error('Error loading member details:', error);
        alert('Unable to load member details. Please try again.');
    }
}

// Display member details modal
function displayMemberModal(member) {
    document.getElementById('modal-member-name').textContent = member.name;
    
    const content = document.getElementById('member-details-content');
    content.innerHTML = `
        <div class="member-profile">
            <div class="profile-header">
                <div class="profile-avatar">${member.role === 'VOLUNTEER' ? '🤝' : '🏠'}</div>
                <div class="profile-info">
                    <h4>${member.name}</h4>
                    <p class="profile-role">${member.role === 'VOLUNTEER' ? 'Community Volunteer' : 'Community Resident'}</p>
                    <div class="profile-rating">
                        ${generateStars(member.rating)} (${member.rating.toFixed(1)})
                    </div>
                </div>
            </div>
            
            <div class="profile-stats">
                <div class="stat-box">
                    <div class="stat-number">${member.totalRequests}</div>
                    <div class="stat-label">Total Requests</div>
                </div>
                <div class="stat-box">
                    <div class="stat-number">${member.totalReviews}</div>
                    <div class="stat-label">Reviews Received</div>
                </div>
                <div class="stat-box">
                    <div class="stat-number">${formatJoinDate(member.joinedAt)}</div>
                    <div class="stat-label">Member Since</div>
                </div>
            </div>
            
            <div class="profile-sections">
                <div class="profile-section">
                    <h5>Recent Requests</h5>
                    <div class="recent-items">
                        ${member.recentRequests.length > 0 ? 
                            member.recentRequests.map(req => `
                                <div class="recent-item">
                                    <span class="item-title">${req.title}</span>
                                    <span class="item-status status-${req.status.toLowerCase()}">${req.status}</span>
                                </div>
                            `).join('') : 
                            '<p class="no-items">No recent requests</p>'
                        }
                    </div>
                </div>
                
                <div class="profile-section">
                    <h5>Recent Reviews</h5>
                    <div class="recent-items">
                        ${member.recentReviews.length > 0 ? 
                            member.recentReviews.map(review => `
                                <div class="recent-item">
                                    <span class="item-title">${review.requestTitle}</span>
                                    <span class="item-rating">${'⭐'.repeat(review.rating)}</span>
                                </div>
                            `).join('') : 
                            '<p class="no-items">No reviews yet</p>'
                        }
                    </div>
                </div>
            </div>
        </div>
    `;
    
    document.getElementById('member-modal').style.display = 'flex';
}

// Close member modal
function closeMemberModal() {
    document.getElementById('member-modal').style.display = 'none';
}

// Contact member (placeholder)
function contactMember(memberId) {
    alert('Contact feature coming soon! You can connect with members through help requests for now.');
}

// Refresh members
function refreshMembers() {
    loadCommunityMembers();
    document.getElementById('fab-menu').style.display = 'none';
}

// Floating Action Button functions
function toggleFabMenu() {
    const menu = document.getElementById('fab-menu');
    const isVisible = menu.style.display !== 'none';
    menu.style.display = isVisible ? 'none' : 'block';
}
