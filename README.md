# SystemConfigurator
  Java console application for managing monitoring system settings with database support. 
## Features
- **Database Storage** - SQLite for persistent configuration
- **Cross-Platform** - Works on Windows and Linux  
- **ANSI-Colored Interface** - Rich console experience
- **Input Validation** - Type-safe validation with meaningful error messages
- **Architecture** - Maven-based build system
- **Self-Contained** - Single JAR with all dependencies
- **One-Click Launch** - Simple startup scripts
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
```cmd
cd configurator
start.bat
```
### Linux/macOS:
```bash
cd configurator
chmod +x start.sh
./start.sh
```
## Project Structure
```
SystemConfigurator/
├── .idea/
│   └── inspectionProfiles/
│       └── Project_Default.xml
├── configurator/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └──empty/
│   │       │          └── Configure.java          # Main application
│   │       └── resources/
│   │           └── simplelogger.properties
│   ├── target/
│   │   └── SystemConfigurator-1.0.jar  # Executable JAR
│   ├── pom.xml                     # Maven configuration
│   ├── start.bat                   # Windows startup script
│   └── start.sh                    # Linux/macOS startup script
├── .gitignore
├── LICENSE
└── README.md
```
### Key Directories
- **`configurator/`** - Main application module with Maven structure
- **`src/main/java/`** - Source code following Java package conventions  
- **`src/main/resources/`** - Configuration files and application resources
- **`target/`** - Build outputs and executable JAR
- **`.idea/inspectionProfiles/`** - IDE code inspection settings
### Core files
- **`Configure.java`** - Application entry point with SQLite database integration
- **`pom.xml`** - Maven build configuration and dependencies
- **`simplelogger.properties`** - SLF4J logging configuration
- **`start.bat/start.sh`** - Cross-platform launch scripts
- **`SystemConfigurator-1.0.jar`** - Self-contained executable
- **`Project_Default.xml`** - IDE inspection profile for clean code
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
## P.S.
A self-initiated project to practice Java and software architecture. Feedback and suggestions are welcome!
