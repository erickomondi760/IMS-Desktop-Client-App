# 🖥️ IMS Desktop Client App

A **JavaFX-based Inventory Management System (IMS)** desktop client application. This project provides a graphical interface for managing inventory, purchase orders, credit notes, and product profiles, designed for businesses that need a lightweight desktop solution.

---

## 📌 Features
- User-friendly **JavaFX GUI**  
- Inventory management (add, update, view items)  
- Purchase order (LPO) creation and editing  
- Credit notes and order guide management  
- Product profile reporting  
- Exportable reports (e.g., LPOs, product profiles)  
- Screenshots included for UI previews  

---

## 🛠️ Tech Stack
- **Language:** Java  
- **Framework:** JavaFX  
- **Build Tool:** Maven  
- **UI Styling:** CSS  

---

## 📂 Project Structure
```
IMS-Desktop-Client-App/
│── .idea/                     # IDE configuration
│── .mvn/wrapper/              # Maven wrapper files
│── bin/                       # Compiled binaries
│── demo/                      # Demo resources
│── src/main/                  # Source code (controllers, views, models)
│── images/                    # Screenshots (UI previews, reports)
│── pom.xml                    # Maven project configuration
│── mvnw / mvnw.cmd            # Maven wrapper scripts
│── .gitignore                 # Git ignore rules
│── README.md                  # Documentation
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+  
- Maven 3.8+  

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/erickomondi760/IMS-Desktop-Client-App.git
   cd IMS-Desktop-Client-App
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn javafx:run
   ```

---

## 📸 Screenshots

## 📸 Screenshots

### Home Page
![Home Page](src/images/Home%20page.png)  
*The main dashboard providing quick access to core IMS features.*

### Inventory Page
![Inventory Page](src/images/Inventory%20page.png)  
*Displays current stock levels and allows adding, editing, or removing items.*

### LPO Editor
![LPO Editor](src/images/LPO%20Editor.png)  
*Interface for creating and editing Local Purchase Orders (LPOs).*

### LPOs Page
![LPOs Page](src/images/LPOs%20Page.png)  
*Shows a list of all generated LPOs with options to manage them.*

### Invoicing Page
![Invoicing Page](src/images/Invoicing%20page.png)  
*Handles customer invoicing and payment tracking.*

### Exporting LPOs
![Exporting LPOs](src/images/Exporting%20lpos.png)  
*Feature to export LPOs into external formats for reporting or sharing.*

### Generated LPO Report
![Generated LPO Report](src/images/Generated%20LPO%20Report.png)  
*Sample report generated from LPO data.*

### Company and Branch Manager Page
![Company and Branch Manager Page](src/images/Company%20and%20branch%20manager%20page.png)  
*Manages company details and branch manager assignments.*

### Sample Product Profile Report
![Sample Product Profile Report](src/images/Sample%20product%20profile%20report.png)  
*Detailed product profile report with inventory and transaction history.*


---

## ☁️ Deployment

### Option 1: Local Execution
Run directly via Maven or your IDE (IntelliJ/Eclipse).

### Option 2: Executable JAR
Package the app:
```bash
mvn clean package
```
Run:
```bash
java -jar target/ims-desktop-client-app.jar
```

---

## 🔄 CI/CD Setup (GitHub Actions)
Automate builds and tests with GitHub Actions:

```yaml
name: Java CI with Maven

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout code
      uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Build with Maven
      run: mvn clean install

    - name: Run tests
      run: mvn test
```

---

## 🤝 Contributing
Contributions are welcome!  
1. Fork the repo  
2. Create a new branch (`feature-xyz`)  
3. Commit changes  
4. Open a Pull Request  

---

## 📜 License
This project is licensed under the MIT License.

