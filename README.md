# Live Match Score System

A real-time match scoring system built with Spring Boot that allows users to create matches, update scores, and subscribe to live match updates using WebSocket communication.

## 🏆 Features

- **Real-time Match Creation**: Create new matches with custom team names
- **Live Score Updates**: Update match scores in real-time with WebSocket communication
- **Match Subscriptions**: Subscribe to live match updates and receive instant notifications
- **Comprehensive Statistics**: 
  - View total goals scored by any team
  - Monitor number of active matches
  - Get current match scores
- **Thread-safe Operations**: Concurrent match state management
- **Console-based Interface**: User-friendly command-line client application

## 🏗️ Architecture

The system follows a client-server architecture with two main components:

- **Match Server** (`/match`): Spring Boot backend that manages matches and broadcasts updates
- **Match Client** (`/matchclient`): Console-based Java client for user interaction

## 🛠️ Technologies Used

- **Java 17** - Programming language
- **Spring Boot** - Backend framework
- **Spring WebSocket** - Real-time communication
- **STOMP Protocol** - WebSocket messaging protocol
- **Maven** - Build automation and dependency management
- **Project Lombok** - Reducing boilerplate code

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/live-match-score-system.git
   cd live-match-score-system
   ```

2. **Start the Match Server**
   ```bash
   cd match
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   The server will start on `http://localhost:8080`

3. **Run the Match Client** (in a new terminal)
   ```bash
   cd matchclient
   ./mvnw clean install
   ./mvnw compile exec:java -Dexec.mainClass="com.example.matchclient.MatchclientApplication"
   ```

## 📋 Usage Guide

The client application provides three main functionalities:

### 1. Create Match
- Enter team names for the match
- Receive a unique match ID
- Update scores throughout the match
- End the match when completed

### 2. Get Match Statistics
- View the number of currently active matches
- Check total goals scored by a specific team across all matches
- Monitor overall system activity

### 3. Subscribe to Match Updates
- Enter a match ID to subscribe
- Receive real-time score updates
- Automatically exit when the match ends

## 🔌 API Reference

### REST Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/create` | Create a new match |
| `GET` | `/activeCount` | Get count of active matches |
| `GET` | `/total/{team}` | Get total goals for a team |
| `GET` | `/score/{matchId}` | Get current score for a match |

### WebSocket Endpoints

| Endpoint | Type | Description |
|----------|------|-------------|
| `ws://localhost:8080/ws` | Connection | WebSocket connection endpoint |
| `/app/match/update/{matchId}` | Send | Update match score |
| `/app/match/end/{matchId}` | Send | End a match |
| `/topic/match/{matchId}` | Subscribe | Receive match updates |

## 📁 Project Structure

```
live-match-score-system/
├── match/                          # Server Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/match/
│   │   │   │       ├── config/         # WebSocket configuration
│   │   │   │       ├── controller/     # REST and WebSocket controllers
│   │   │   │       ├── model/          # Data models
│   │   │   │       └── service/        # Business logic
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
├── matchclient/                    # Client Application
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── com/example/matchclient/
│   │               ├── MatchclientApplication.java
│   │               └── [Other client classes]
│   └── pom.xml
└── README.md
```

## 🔧 Configuration

### Server Configuration
The server runs on port 8080 by default. You can modify this in `application.properties`:


### WebSocket Configuration
WebSocket endpoints are configured to handle STOMP messaging protocol for real-time communication.


## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Made with ❤️ by Karim Magdy**
