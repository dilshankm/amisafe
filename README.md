# AmISafe Backend Service

This is the backend API for the **AmISafe** mobile application. It is built with **Spring Boot** and serves as the core engine powering crime-related data processing, and user profile handling.

---

## 🚀 Features

* RESTful API built with **Spring Boot**
* Integrated with MongoDB Atlas
* Dockerized for consistent deployment
* CI/CD enabled via GitHub Actions

---

## 📁 Project Structure

```
amisafe/
├── .github/workflows/       # GitHub Actions CI/CD config
├── .mvn/                    # Maven wrapper scripts
├── src/                     # Main Java source code
│   ├── main/java/...        # Controllers, Services, Configs, DTOs
│   └── resources/           # application.yml and templates
├── Dockerfile               # For containerizing the backend
├── docker-compose.yml       # Optional multi-service orchestration
├── pom.xml                  # Maven project configuration
└── README.md                # This documentation
```

---

## ⚙️ Technologies Used

* **Java 21**
* **Spring Boot 3**
* **MongoDB Atlas**
* **Docker**
* **Maven**

---

## 🧪 Running the Project Locally

### 1. Clone the Repository

```bash
git clone https://github.com/dilshankm/amisafe.git
cd amisafe
```

### 2. Set Up Environment

Ensure you have the following:

* Java 21+
* Maven
* MongoDB Atlas URI (set in `application.yml`)

### 3. Build and Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Visit: `http://localhost:8080`

---

## 🐳 Docker Setup

### Build Image

```bash
docker build -t amisafe-backend .
```

### Run Container

```bash
docker run -p 8080:8080 amisafe-backend
```

---

## 🧪 API Endpoints

| Method | Endpoint                | Description                       |
| ------ | ----------------------- | --------------------------------- |
| GET    | `/api/health`           | Health check endpoint             |
| POST   | `/api/alert/push`       | Send push notifications           |
| POST   | `/api/alert/email`      | Send email alerts                 |
| POST   | `/api/v1/crimes/reload` | Reload cached crime data          |
| GET    | `/api/v1/crimes/nearby` | Get crimes by location and radius |
| POST   | `/api/v1/users`         | Create a new user                 |
| GET    | `/api/v1/users/{email}` | Retrieve user details by email    |
| PATCH  | `/api/v1/users/{email}` | Update user details               |
| DELETE | `/api/v1/users/{email}` | Delete user by email              |

---

## 🔒 Security

* Email credentials and secrets should be managed via environment variables or a secure secret store.

---

## 🧹 Future Improvements

* JWT-based authentication
* Role-based access control
* Logging and monitoring integration

---

## 🤝 License

MIT License © Dilshan Udara Kodithuwakku Maddege
