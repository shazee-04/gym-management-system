[![Java](https://img.shields.io/badge/Java-21-4c8cfa.svg)](https://openjdk.org/projects/jdk/21/)
[![Build](https://img.shields.io/badge/Build-Apache%20Ant-blue.svg)](https://ant.apache.org/manual/index.html)
[![Database](https://img.shields.io/badge/Database-MySQL%208.x-4479A1.svg)](https://dev.mysql.com/doc/)  

<div align="center" style="width: 100%;">
    <img src="https://github.com/user-attachments/assets/581eaace-ad32-4af7-8ab2-807df3c89b06" alt="GMS Preview" width="100%"/>
</div>

<br>

A desktop application for managing a fitness center, built with Java Swing and styled with FlatLaf. It covers member and trainer management, attendance tracking, workout plans, equipment inventory, product sales, payments, and one-click JasperReports. The app persists data in MySQL and ships with an Ant-based build for easy packaging.


### Features

- Modern dark UI using FlatLaf (Mac Dark theme)
- Authentication and role gating via Login form
- Dashboard with live KPIs
    - Members
    - Trainers
    - Daily logins
    - Monthly payments
    - Product sales
- Modules/panels
  - Members, Trainers, Workout Plans, Attendance, Progress Tracking
  - Equipment, Other Products, Payment Records
  - Generate Reports (JasperReports) and Database Backup
- Reporting
  - Members, Trainers, Attendance, Payments, Product Sales, Equipment
  - JasperReports viewer integration
- Database
  - MySQL (via Connector/J), centralized helper in `connection.MySQL`
  - One-click backup using `mysqldump` (configurable)
- Utilities
  - Custom dialogs, rounded corners, logging, date/time helpers, regex validation

##

### Requirements

- Java Development Kit (JDK) 21
- Apache Ant (if building from CLI)
- MySQL Server 8.x
- A MySQL user with sufficient privileges to read/write and perform backups (if using Backup)

Optional (recommended): NetBeans IDE (the project contains NetBeans metadata and uses CopyLibs).


### Technologies

- Java Swing (UI)
- FlatLaf 3.6 (Look and Feel)
- MySQL Connector/J 8.4.0
- JasperReports 6.21.3 and companions
- Apache Commons (BeanUtils, Collections, Collections4, Digester, Logging)
- JSVG 1.4.0 (SVG rendering), JCalendar 1.4, Barbecue (barcodes)
- iText/OpenPDF (PDF support in reports)
- Apache Ant (build), NetBeans project layout

All third-party jars are vendored under `lib/` and wired in `nbproject/project.properties`.

##

### Installation

#### 1. Clone the repo

```bash
git clone https://github.com/shazee-04/gym-management-system.git
cd gym-management-system
```

#### 2. Configure the database connection

This project expects a configuration file on the application classpath: `resources/db.properties` . Create it before running the app.

Example `resources/db.properties`:

```properties
db.host=localhost
db.port=3306
db.name=gms
db.user=root
db.password=secret

# Full paths to the MySQL client tools (used for backup)
db.mysql_path=C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe
db.mysqldump_path=C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe
```

Place the file under your project/classpath (for NetBeans, put it in `resources/` and ensure it's on the compile/run classpath; when packaging, include it in the jar or next to it and adjust your run classpath accordingly).

For backup, a MySQL client option file is used. Create `src/resources/mysql.cnf` (git-ignored) with:

```ini
[client]
user=root
password=secret
host=localhost
port=3306
```

Note: Backup uses `--defaults-extra-file=src/resources/mysql.cnf`. If you run from a packaged jar or a different working directory, ensure the relative path remains valid or adjust the code/path.

#### 3. Prepare the database schema

Use the bundled SQL file (recommended):
  - A ready-made schema script is included at `backups/gms-database-init.sql`.
  - It creates the database `gms` and all required tables (no seed data).
  - Ensure your `db.properties` uses `db.name=gms`.
  - Run it via MySQL Workbench (File > Open SQL Script… > Execute) or from the CLI:

    ```powershell
    # PowerShell (adjust mysql.exe path and credentials as needed)
    & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < .\backups\gms-database-init.sql
    ```

After running, manually insert at least one admin user into the `admin` table (since the script does not include seed data) and toggle its active status via the `status` reference.

##

### Build and Run

#### Option A: NetBeans (recommended)

- Open the project folder in NetBeans.
- Ensure your Project Properties target Java Platform is JDK 21.
- Right-click the project > Clean and Build.
- Run. NetBeans will set `main.class=gui.SplashSceen` and copy required jars to `dist/lib/`.

#### Option B: Ant (CLI)

```powershell
# Windows PowerShell
ant clean jar
java -jar .\dist\Gym_Management_System.jar
```

Ant will produce `dist/Gym_Management_System.jar` and `dist/lib/` with all runtime dependencies (via NetBeans CopyLibs). The manifest will reference the correct Class-Path and Main-Class.

##

### Usage

- Launch the application. You'll see a splash, then the login form.
- Log in using credentials stored in the `admin` table (with `status` join for active users).
- After login, the main interface provides:
  - Sidebar navigation across modules
  - Dashboard metrics automatically aggregated from the database
  - Generate reports via the Generate Reports panel
  - Backup data button to create a timestamped SQL file under `backups/`

Reports come from compiled Jasper templates located under `src/report/`.


### Configuration

- Main class: `gui.SplashSceen` (set in `nbproject/project.properties`)
- DB configuration: `resources/db.properties` (classloader-loaded by `connection.MySQL`)
- Backup option file: `src/resources/mysql.cnf`
- UI theme: FlatLaf Mac Dark (`FlatMacDarkLaf.setup()` in `SplashSceen`/`MainInterface`)

You can switch to a different FlatLaf theme by replacing the setup call.

##

### Repository Structure

```
.
├─ build.xml                         # Ant entry point; imports nbproject/build-impl.xml
├─ manifest.mf                       # Manifest skeleton; main is injected at build
├─ README.md                         # Project documentation
├─ backups/                          # Database scripts and backups
│  └─ gms-database-init.sql          # Creates `gms` DB and all required tables
├─ lib/                              # Vendored third-party jars (UI, DB, reports, utils)
│  ├─ absolutelayout/                # NetBeans layout helper
│  ├─ CopyLibs/                      # NetBeans CopyLibs task jar
│  ├─ hamcrest/, junit_4/            # Test libraries (JUnit 4 + Hamcrest)
│  └─ *.jar                          # FlatLaf, JasperReports, MySQL, Commons, etc.
├─ nbproject/                        # NetBeans project metadata and wiring
│  ├─ build-impl.xml                 # Generated Ant targets
│  ├─ project.properties             # Classpath, main class, JDK, dist settings
│  └─ ...
├─ src/
│  ├─ connection/
│  │  └─ MySQL.java                  # Central JDBC connection + queries + backup
│  ├─ gui/                           # Top-level frames and entry points
│  │  ├─ SplashSceen.(java|form)     # Splash, sets FlatLaf, shows Login
│  │  ├─ LoginForm.(java|form)       # Auth; opens MainInterface
│  │  └─ MainInterface.(java|form)   # Main window, cards for panels, dashboard
│  ├─ img/                           # UI assets (e.g., banner.svg, favicon)
│  ├─ panels/                        # Feature modules
│  │  ├─ *.panel.(java|form)         # Members, Trainers, Attendance, etc.
│  │  └─ dialog/                     # Dialogs (ManageMember, ManageTrainer, Attendance)
│  ├─ report/                        # Compiled JasperReports templates (.jasper)
│  ├─ resources/                     # Local-only config (examples provided)
│  │  ├─ db.properties.example       # Copy to db.properties and edit
│  │  └─ mysql.cnf.example           # Copy to mysql.cnf and edit
│  ├─ util/                          # UI and system utilities (icons, logging,rounding, time)
│  └─ validation/                    # `Regex` patterns helper
└─ .gitignore                        # Ignores db.properties and mysql.cnf
```

##

### Contributing

- Fork the repo and create a feature branch.
- Keep UI code organized under the appropriate panel or dialog.
- Follow the existing logging pattern (use `util.LoggerUtil`).
- Write small, focused commits.
- Open a Pull Request against `main` with a clear description and screenshots when UI changes are involved.

If you plan to introduce database schema changes, include migration notes or SQL.

##

### Documentation

- Code is organized by feature. Start at `gui.MainInterface` to understand navigation and KPIs.
- Reporting entry-points live in `panels.GenerateReportsPanel` and `.jasper` templates under `src/report/`.
- Database helper lives in `connection.MySQL`.

##

### Acknowledgements

- [FlatLaf](https://www.formdev.com/flatlaf/) for a clean, modern look
- [JasperReports](https://community.jaspersoft.com/project/jasperreports-library) for reporting
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) for JDBC connectivity
- Apache Commons, JSVG, JCalendar, Barbecue, iText/OpenPDF and others shipped in `lib/`

##

### Notes

- License: No explicit license is provided. If you intend to use or redistribute this code, please open an issue to clarify licensing.
- Security: The current login and queries use string concatenation; consider parameterized queries and password hashing before production use.
- Backup file path uses a relative `src/resources/mysql.cnf`. Adjust for packaged deployments.
- Database initialization: The provided `backups/gms-database-init.sql` sets up schema only. Add an initial admin in `admin` and required lookup rows in `status`/`membership` as needed.
