# Payment Gateway Dashboard & Checkout System

This project is a **full-stack payment gateway simulation** that includes a merchant dashboard and a checkout page with UPI and Card payment options.  
It demonstrates end-to-end payment flow using **React, Node.js, Express, Docker, and a database**.

---

## 🚀 Features

### Merchant Dashboard
- View API Key & API Secret
- Total Transactions count
- Total Successful Amount (₹)
- Success Rate (%)
- Transactions list with:
  - Payment ID
  - Order ID
  - Amount
  - Method (UPI / Card)
  - Status
  - Created time

### Checkout Page
- Order-based checkout (`order_id` via URL)
- Displays order amount
- Payment methods:
  - UPI
  - Card
- Real payment creation via backend
- Success confirmation with payment ID

---

## 🏗 Tech Stack

**Frontend**
- React (Vite)
- Fetch API

**Backend**
- Node.js
- Express.js
- REST APIs

**Infrastructure**
- Docker
- Docker Compose
- Nginx (for frontend serving)

## ▶️ How to Run the Project

### 1️⃣ Start Everything Using Docker

```bash
docker-compose up --build
---

## 📂 Project Structure

