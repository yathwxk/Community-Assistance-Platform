# 🏘️ Neighbourhood Help Desk: A Community Assistance Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

**A modern community-driven web application where neighbors help neighbors**

[🚀 Quick Start](#-quick-start) • [📱 Features](#-features) • [🛠️ Tech Stack](#️-tech-stack) • [📖 API Docs](#-api-endpoints) • [🤝 Contributing](#-contributing)

</div>

---

## 🌟 **Overview**

Neighborhood Help Desk is a full-stack web application that connects community members to share resources, offer assistance, and build stronger neighborhoods. Whether you need to borrow tools, get tutoring help, or assist with errands, this platform makes community cooperation simple and trustworthy.

## 📱 **Features**

### 🏠 **For Residents**
- **📝 Post Help Requests** - Need a ladder, tutoring, or help with errands? Create detailed requests with categories and urgency levels
- **📊 Track Progress** - Monitor request status from "Open" → "Accepted" → "Completed"
- **⭐ Rate & Review** - Build community trust through a 5-star rating system
- **🔍 Search & Filter** - Find specific types of help or browse by category/urgency

### 🤝 **For Volunteers**
- **👀 Browse Requests** - View all available help requests in your community
- **🎯 Smart Filtering** - Find requests by category, urgency, or search terms
- **✅ Accept & Complete** - Volunteer for requests and track your contributions
- **🏆 Build Reputation** - Earn ratings and become a trusted community helper

### 🏘️ **Community Features**
- **👥 Member Directory** - See all community members, their ratings, and activity levels
- **📈 Community Stats** - View total members, active helpers, and community health metrics
- **🔍 Member Profiles** - Detailed profiles showing request history, reviews, and reputation
- **🎯 Activity Insights** - Understand community dynamics and find trusted helpers

### 🚀 **Advanced Features**
- **🔄 Real-time Updates** - Dynamic status changes and live statistics
- **📱 Responsive Design** - Works perfectly on desktop, tablet, and mobile
- **🛡️ Secure Authentication** - BCrypt password hashing and session management
- **🎨 Modern UI/UX** - Clean, intuitive interface with smooth animations
- **📤 Share Requests** - Copy links to share specific requests
- **🔧 Request Management** - Edit, delete, and cancel requests as needed

## 🛠️ **Tech Stack**

### **Backend**
- **Java 17** - Modern Java features and performance
- **Spring Boot 3.2.0** - Enterprise-grade framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Maven** - Dependency management and build tool

### **Database**
- **MySQL 8.x** - Reliable relational database with full ACID compliance

### **Frontend**
- **HTML5** - Modern semantic markup
- **CSS3** - Advanced styling with Flexbox/Grid
- **Vanilla JavaScript** - No framework dependencies
- **AJAX/Fetch API** - Asynchronous data loading

### **Architecture**
- **RESTful APIs** - Clean, stateless communication
- **MVC Pattern** - Separation of concerns
- **Repository Pattern** - Data access abstraction
- **Service Layer** - Business logic encapsulation

## 📋 **Prerequisites**

- ☕ **Java 17+** ([Download](https://adoptium.net/))
- 🔧 **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- 🗄️ **MySQL 8.x** ([Download](https://dev.mysql.com/downloads/mysql/)) - **Required, no alternatives**

> 📖 **Need help with MySQL setup?** See our [MySQL Setup Guide](docs/MYSQL_SETUP.md)

## 🚀 **Quick Start**

### 1️⃣ **Clone the Repository**
```bash
git clone https://github.com/yourusername/neighborhood-help-desk.git
cd neighborhood-help-desk
```

> **Note**: Replace `yourusername` with your actual GitHub username

### 2️⃣ **Setup MySQL Database**
```sql
# Connect to MySQL
mysql -u root -p

# The database will be created automatically by the application
# Just ensure MySQL 8.x is running on port 3306

# Optional: Create dedicated user
CREATE USER 'helpdesk_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON neighborhood_help_desk.* TO 'helpdesk_user'@'localhost';
FLUSH PRIVILEGES;
```

> **Important**: This project requires MySQL 8.x. No other databases are supported.

### 3️⃣ **Configure Application**
```bash
# Copy template and update with your settings
cp src/main/resources/application-template.properties src/main/resources/application.properties

# Edit application.properties with your database credentials
```

### 4️⃣ **Run the Application**
```bash
# Install dependencies and start
mvn clean spring-boot:run
```

### 5️⃣ **Access the Application**
🌐 **Open your browser:** http://localhost:8080

## 🎯 **Usage Guide**

### **Getting Started**
1. **🏠 Visit Homepage** - Browse existing requests and community stats
2. **📝 Post Requests** - Click "Post Request" to create help requests
3. **👥 Explore Community** - Visit "Community" to see all neighbors and their ratings
4. **🤝 Help Others** - Accept requests and build your reputation

### **Sample Workflow**
1. **Alice** posts: *"Need a ladder for 2 hours to clean gutters"*
2. **Bob** sees the request and clicks *"Accept Request"*
3. **Alice** gets notified that Bob will help
4. After completion, **Alice** rates Bob ⭐⭐⭐⭐⭐
5. **Bob's** community reputation increases

## 🗂️ **Project Structure**

```
neighborhood-help-desk/
├── 📁 src/main/java/com/helpdesk/
│   ├── 🏗️ config/          # Security & data initialization
│   ├── 🎮 controller/      # REST API endpoints
│   ├── 📊 entity/          # Database models
│   ├── 🗄️ repository/     # Data access layer
│   ├── ⚙️ service/         # Business logic
│   └── 🚀 NeighborhoodHelpDeskApplication.java
├── 📁 src/main/resources/
│   ├── 🎨 static/          # Frontend assets
│   │   ├── 🎨 css/style.css
│   │   ├── ⚡ js/app.js & community.js
│   │   ├── 🏠 index.html
│   │   ├── 👥 community.html
│   │   └── 📝 post_request.html
│   └── ⚙️ application.properties
├── 📄 README.md
├── 🚫 .gitignore
└── 📦 pom.xml
```

## 🔌 **API Endpoints**

### **Authentication**
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### **Requests**
- `GET /api/requests` - Get all requests (with optional filters)
- `POST /api/requests` - Create new request
- `PUT /api/requests/{id}` - Update request
- `DELETE /api/requests/{id}` - Delete request
- `PUT /api/requests/{id}/accept` - Accept a request
- `PUT /api/requests/{id}/complete` - Mark request as completed
- `PUT /api/requests/{id}/cancel` - Cancel request

### **Community**
- `GET /api/community/members` - Get all community members
- `GET /api/community/members/{id}` - Get member details

### **Reviews**
- `POST /api/reviews/requests/{requestId}` - Submit review
- `GET /api/reviews/volunteers/{volunteerId}` - Get volunteer reviews

### **Health Check**
- `GET /api/health` - Application health status
- `GET /api/test` - API test endpoint

## 🐳 **Docker Support**

### **Using Docker Compose (Recommended)**
```bash
# Start all services (app + MySQL + phpMyAdmin)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### **Manual Docker Build**
```bash
# Build image
docker build -t neighborhood-help-desk .

# Run with external MySQL
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/neighborhood_help_desk \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  neighborhood-help-desk
```

## 🧪 **Testing**

### **Manual Testing Checklist**
- [ ] User registration and login
- [ ] Post new help request
- [ ] Browse and filter requests
- [ ] Accept and complete requests
- [ ] Rate and review helpers
- [ ] View community members
- [ ] Search functionality
- [ ] Mobile responsiveness

### **API Testing**
```bash
# Test health endpoint
curl http://localhost:8080/api/health

# Test requests endpoint
curl http://localhost:8080/api/requests

# Test community endpoint
curl http://localhost:8080/api/community/members
```


## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


<div align="center">

**⭐ Star this repository if you find it helpful!**

**Built with ❤️ for stronger communities**

[🔝 Back to top](#-neighborhood-help-desk)

</div>
