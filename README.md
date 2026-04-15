# Pharmacy Prescription Verification & Controlled Substance Workflow System

## Live Demo

| | URL |
|---|---|
| **Frontend** | https://pharmacy-fullstack-chi.vercel.app |
| **Backend API** | https://pharmacy-backend-yczu.onrender.com/api |

> Note: Backend is hosted on Render's free tier — first request after inactivity may take ~50 seconds to wake up.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 18 + Vite + Tailwind CSS |
| Backend | Spring Boot 3 + Java 17 |
| Database | MySQL (Railway) |
| Frontend Hosting | Vercel |
| Backend Hosting | Render (Docker) |

---

## Project Structure

```
pharmacy-backend/    → Spring Boot 3 + Java 17 + MySQL
pharmacy-frontend/   → React (Vite) + Tailwind CSS
```

---

## Local Development

### Backend Setup

**Prerequisites:** Java 17+, Maven 3.8+, MySQL 8+

1. Create MySQL database:
```sql
CREATE DATABASE pharmacy_db;
```

2. Update credentials in `pharmacy-backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

3. Run the backend:
```bash
cd pharmacy-backend
mvn spring-boot:run
```

Backend runs at: `http://localhost:8080`

---

### Frontend Setup

**Prerequisites:** Node.js 18+, npm 9+

1. Create `pharmacy-frontend/.env.local`:
```
VITE_API_URL=http://localhost:8080/api
```

2. Run the frontend:
```bash
cd pharmacy-frontend
npm install
npm run dev
```

Frontend runs at: `http://localhost:5173`

---

## How to Use

### 1. Login Flow
- Go to the live URL or `http://localhost:5173`
- Select a role → Register first if no users exist → Login

### 2. Role Workflows

**PATIENT**
- Upload prescription (select medication, enter doctor info)
- View prescription status
- Request refill (only if DISPENSED + refills remaining)

**PHARMACIST**
- View all prescriptions
- Verify or Reject (UPLOADED → VERIFIED / REJECTED)
- Check drug interactions
- Dispense (APPROVED → DISPENSED, within 7 days of approval)

**PHARMACY_MANAGER**
- View VERIFIED prescriptions → Approve or Reject
- View pending controlled substance logs

**ADMIN**
- Add medications (mark controlled, set schedule, refill limits)
- View all users

---

## Business Rules Implemented

| Rule | Implementation |
|------|---------------|
| Only PATIENT uploads | Validated in PrescriptionService |
| Schedule II → 0 refills | Enforced in MedicationService + PrescriptionService |
| Schedule III/IV → max 5 refills | Enforced in MedicationService |
| 30-day validity | Checked on verify + refill |
| Manager approval required for controlled | Auto-creates ControlledSubstanceLog |
| SEVERE interaction → block dispense | Checked in DispensingService |
| CONTRAINDICATED → doctor confirmation | Flag check in DispensingService |
| Dispense within 7 days of approval | Enforced in DispensingService |
| Refill only if remainingRefills > 0 | Enforced in DispensingService |

---

## API Endpoints

### Users
```
POST   /api/users
GET    /api/users
GET    /api/users?role=PATIENT
```

### Medications
```
POST   /api/medications
GET    /api/medications
GET    /api/medications/controlled
```

### Prescriptions
```
POST   /api/prescriptions/upload
GET    /api/prescriptions
GET    /api/prescriptions/patient/{id}
GET    /api/prescriptions/{id}
PUT    /api/prescriptions/{id}/verify
PUT    /api/prescriptions/{id}/reject
PUT    /api/prescriptions/{id}/approve
```

### Dispensing
```
POST   /api/dispensing
GET    /api/dispensing/prescription/{id}
POST   /api/dispensing/refill
```

### Interactions
```
GET    /api/interactions/check/{prescriptionId}
GET    /api/interactions/patient/{patientId}
POST   /api/interactions/add/{prescriptionId}
```

### Controlled Substance Logs
```
GET    /api/controlled-logs
GET    /api/controlled-logs/patient/{id}
GET    /api/controlled-logs/pending-approval
```

### Reports
```
GET    /api/reports
```
