# ShipSelaa - Delivery Management System

A Spring Boot application for managing delivery requests with role-based access for Managers and Livreurs (delivery persons).

## 🏗️ Architecture Overview

### Class Hierarchy
- **User** (Abstract parent class)
  - **Manager** (Inherits from User)
  - **Livreur** (Inherits from User)

### Relationships
- **Manager** can create multiple **Demandes** (1:N)
- **Demande** can be assigned to one **Livreur** (N:1)
- **Livreur** can handle multiple **Demandes** but one at a time based on availability

## 📊 Data Models

### User (Abstract)
- `id`: Long
- `nom`: String
- `email`: String
- `password`: String

### Manager (extends User)
- Inherits all User attributes
- `demandes`: List<Demande> (One-to-Many relationship)

### Livreur (extends User)
- Inherits all User attributes
- `disponible`: boolean
- `demandes`: List<Demande> (One-to-Many relationship)

### Demande
- `id`: Long
- `lieuDepart`: String
- `lieuArrivee`: String
- `statut`: StatusDemande (enum)
- `manager`: Manager (Many-to-One)
- `livreur`: Livreur (Many-to-One, optional)

### StatusDemande (Enum)
- `EN_COURS`: Order in progress
- `LIVRE`: Order delivered

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL Server

### Setup
1. Clone the repository
2. Configure MySQL database in `application.properties`
3. Run the application: `mvn spring-boot:run`
4. Access the API at `http://localhost:8083`

### Database Configuration
The application is configured to work with MySQL. Update `application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shipselaa_db?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 📖 API Documentation

### Manager Operations

#### Create a new order (Créer une commande)
```http
POST /api/managers/{managerId}/demandes
Content-Type: application/json

{
  "lieuDepart": "Paris",
  "lieuArrivee": "Lyon"
}
```

#### Get manager's orders (Consulter les commandes)
```http
GET /api/managers/{managerId}/demandes
```

#### Update an order (Mettre à jour la commande)
```http
PUT /api/managers/{managerId}/demandes/{demandeId}
Content-Type: application/json

{
  "lieuDepart": "Updated departure",
  "lieuArrivee": "Updated arrival"
}
```

#### Cancel an order (Annuler la commande)
```http
DELETE /api/managers/{managerId}/demandes/{demandeId}
```

#### Assign order to livreur
```http
PUT /api/managers/{managerId}/demandes/{demandeId}/assign/{livreurId}
```

### Livreur Operations

#### Update order status (Mettre à jour le statut)
```http
PUT /api/livreurs/{livreurId}/demandes/{demandeId}/status?statut=LIVRE
```

#### Accept an order (Accepter une commande)
```http
PUT /api/livreurs/{livreurId}/demandes/{demandeId}/accept
```

#### Decline an order (Refuser une commande)
```http
PUT /api/livreurs/{livreurId}/demandes/{demandeId}/decline
```

#### Get livreur's orders
```http
GET /api/livreurs/{livreurId}/demandes
```

#### Update availability
```http
PUT /api/livreurs/{livreurId}/availability?disponible=true
```

### General Demande Operations

#### View all orders
```http
GET /api/demandes
```

#### View orders by status
```http
GET /api/demandes/status/EN_COURS
GET /api/demandes/status/LIVRE
```

#### Get specific order
```http
GET /api/demandes/{demandeId}
```

## 🧪 Sample Data

The application automatically creates sample data on startup:
- 1 Manager (manager@test.com / password123)
- 2 Livreurs (livreur1@test.com, livreur2@test.com / password123)
- 2 Sample Demandes

## 🔧 Business Logic

### Manager Capabilities
- ✅ Create new delivery requests
- ✅ View all their orders
- ✅ Update order details
- ✅ Cancel orders
- ✅ Assign orders to available livreurs

### Livreur Capabilities
- ✅ View assigned orders
- ✅ Accept or decline order assignments
- ✅ Update order status (EN_COURS → LIVRE)
- ✅ Manage availability status
- ✅ Automatic availability management (becomes unavailable when accepting, available when completing)

### System Features
- ✅ Inheritance hierarchy (User → Manager, Livreur)
- ✅ Proper JPA relationships and constraints
- ✅ Status management with enums
- ✅ Availability tracking for livreurs
- ✅ RESTful API design
- ✅ Automatic database schema creation
- ✅ Sample data initialization

## 🏃‍♂️ Running the Application

1. **Start MySQL** and ensure it's running on port 3306
2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
3. **Test the endpoints** using Postman, curl, or any REST client
4. **Check the console** for sample data creation confirmation

## 📝 Example Workflow

1. **Manager creates an order**:
   ```bash
   curl -X POST http://localhost:8083/api/managers/1/demandes \
   -H "Content-Type: application/json" \
   -d '{"lieuDepart":"Paris","lieuArrivee":"Lyon"}'
   ```

2. **Assign to available livreur**:
   ```bash
   curl -X PUT http://localhost:8083/api/managers/1/demandes/1/assign/1
   ```

3. **Livreur accepts the order**:
   ```bash
   curl -X PUT http://localhost:8083/api/livreurs/1/demandes/1/accept
   ```

4. **Livreur updates status to delivered**:
   ```bash
   curl -X PUT http://localhost:8083/api/livreurs/1/demandes/1/status?statut=LIVRE
   ```

The system automatically handles availability management and relationship constraints throughout this workflow.