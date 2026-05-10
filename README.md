<div align="center">
  <h1>🚂 Train Ticketing System</h1>
  <p><i>A robust, thread-safe, and algorithmic solution for managing train schedules, routes, and bookings.</i></p>

  [![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](#)
  [![Architecture](https://img.shields.io/badge/Architecture-Layered-00599C?style=for-the-badge)](#)
  [![Build](https://img.shields.io/badge/Build-CLI-4CAF50?style=for-the-badge)](#)
</div>

---

## 🎯 About The Project

This project is my solution for the **Java Internship problem**. The core goal was to build a highly reliable system capable of managing train operations, schedules, and passenger bookings while effectively handling real-world constraints such as train capacity limits, dynamic delays, and waitlisting.

I aimed to build something beyond a simple CRUD application, focusing on **thread safety**, **algorithmic efficiency**, and **clean architecture**.

---

## 🛠️ System Architecture

I implemented a strict **Layered Architecture** to maintain clean code and separate concerns:

| Layer | Description |
| :--- | :--- |
| **Presentation** | A user-friendly CLI menu tailored for both passengers and system administrators. |
| **Service** | The "brains" of the application. Contains business logic, routing algorithms, and booking handlers. |
| **Data/Repository** | Manages in-memory storage, acting as the mock database layer. |
| **Models** | Plain Old Java Objects (POJOs) representing core domain entities (`Train`, `Station`, `Route`, etc.). |

---

## ✨ Key Features

### 1. Smart Routing (BFS Engine) 🗺️
Implemented a **Breadth-First Search (BFS)** algorithm to compute possible links between stations. It goes beyond direct routes—it intelligently finds multi-stop journeys requiring a changeover, strictly validating that connecting trains depart *after* the previous train arrives.

### 2. Administrator Controls 🛡️
Admins have a dedicated suite of tools to oversee network operations:
- **Manage Network:** Add, remove, or modify routes, trains, and stations.
- **Monitor Bookings:** View passenger manifests and capacity for any train.
- **Delay Management:** Report unexpected delays, automatically triggering mocked "email" notifications to all affected passengers.

### 3. Safety & Concurrency 🔒
To prevent race conditions (e.g., two people booking the last seat at the exact same millisecond), critical paths in the `BookingService` use Java's `synchronized` keyword and concurrent collections. The system ensures robust, one-at-a-time processing to completely eliminate the risk of overbooking.

---

## 🚀 The "Special Feature" (Automated Waitlist)

For the optional challenge, I designed an **Automated Waitlist Management System** integrated with a **Dynamic Pricing Engine**.

**The Problem:** In traditional systems, if a train is fully booked, the customer is rejected and potential revenue is lost.
**The Solution:**
1. If a train is full, the customer is seamlessly offered a spot on a **FIFO (First-In-First-Out) Waitlist**.
2. If an existing ticket is cancelled, the system automatically *pops* the next person off the waitlist and secures their ticket without manual intervention.
3. A **Dynamic Pricing Engine** analyzes capacity and departure times:
   - *Surge pricing* applies when a train approaches full capacity (>70% or >90%).
   - *Last-minute booking fees* apply within 24 hours of departure.

---

## 🚦 Usage Examples

### Finding a Route & Booking
```plaintext
Enter Departure Station ID (e.g., S1): S1
Enter Arrival Station ID (e.g., S2): S2

--- Available Routes ---
Found Route: R1 on Train InterCity Express
Current dynamic price per ticket: $50.0
Enter your email address: passenger@email.com
How many tickets do you want to book? 1

==================================================
EMAIL SENT TO: passenger@email.com
SUBJECT: Booking Confirmation
BODY: You have successfully booked 1 ticket(s) for Route R1.
==================================================
Booking successful! Total charged: $50.0
Your Booking ID is: 550e8400-e29b-41d4-a716-446655440000
```

### Handling a Full Train (Waitlist)
```plaintext
[ERROR]: Train is full! Only 0 seats available.
Would you like to join the waitlist? (Y/N): Y
Enter email for the waitlist: hopeful@email.com

Added to waitlist! You will be notified if a seat opens.
```

### Admin: Reporting a Delay
```plaintext
Enter Train ID to report delay (e.g., T1): T1
Enter delay in minutes: 30

==================================================
EMAIL SENT TO: passenger@email.com
SUBJECT: Train Delay Notification
BODY: Attention: Train InterCity Express is delayed by 30 minutes.
==================================================
Delay reported successfully. Notifications sent to affected customers.
```

---

## 💻 Setup and Running

**Environment:** Ensure you have **Java 8 or higher** installed.

### 1. Compile the Source Code
From the root directory, compile all `.java` files into the `target/classes` folder:
```bash
mkdir -p target/classes
javac -d target/classes $(find src/main/java -name "*.java")
```
*(On Windows PowerShell, use: `mkdir target\classes -Force; javac -d target\classes (Get-ChildItem -Path src\main\java -Recurse -Filter *.java).FullName`)*

### 2. Run the Application
Execute the compiled `Main` class:
```bash
java -cp target/classes presentation.Main
```

> **Note:** The `Main` class initializes with pre-configured mock data (Stations `S1` and `S2`, and Train `T1` with limited capacity) so you can test all features—including the waitlist—immediately!
