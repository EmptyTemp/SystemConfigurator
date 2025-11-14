# SystemConfigurator
  Java console application for managing monitoring system settings with database support. 
## Features
- **Database Storage** - SQLite for persistent configuration
- **Cross-Platform** - Works on Windows and Linux  
- **ANSI-Colored Interface** - Rich console experience
- **Input Validation** - Type-safe validation with meaningful error messages
- **Professional Architecture** - Maven-based build system
- **Self-Contained** - Single JAR with all dependencies
## Usage
  javac Configure.java,
  java Configure.
## Technologies
- **Java 17**
- **SQLite Database**
- **Maven** - Build and dependency management
- **ANSI Colors** - Cross-platform console UI
## Installation & Usage
### Prerequisites
- Java 17 or higher
## Installation & Usage
### Windows:
- **Option 1:** Double-click "start.bat"
- **Option 2:** 
cmd
cd configurator
start.bat
### Linux/macOS:
cd configurator
chmod +x start.sh
./start.sh
## Project Structure
SystemConfigurator/
└── configurator/
    ├── src/main/java/com/empty/
    │   └── Configure.java          # Main application
    ├── target/
    │   └── SystemConfigurator-1.0.jar  # Executable JAR
    ├── pom.xml                     # Maven configuration
    ├── start.bat                   # Windows startup script
    └── start.sh                    # Linux/macOS startup script
## Configuration Settings
Manages three core system thresholds:
- CPU Threshold (50-100%) - CPU usage monitoring limit
- RAM Threshold (50-100%) - Memory usage monitoring limit
- Disk Threshold (50-100%) - Disk usage monitoring limit
## Key Features
### Database Integration
- SQLite database for persistent storage
- Automatic table initialization with default values
- Prepared statements for security
- Connection pooling and proper resource management
### Validation System
- Custom validation rules for each setting
- Range checking with user-friendly error messages
- Large number handling and type safety
- Empty input handling (preserves current values)
### User Experience
- Real-time input validation with hints
- ANSI-colored console output
- Clear error messages with suggested ranges
## Roadmap
- Monitoring service for metric collection
- Web dashboard with real-time graphs
- Alerting system based on thresholds
## Contributing
This project is part of a learning journey to Java development. Feedback and suggestions are welcome!
