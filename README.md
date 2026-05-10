🚂 Train Ticketing System
Hi there! This is my solution for the Java Internship problem. The goal was to build a system that manages train schedules, routes, and bookings while handling real-world constraints like capacity and delays.  

🛠️ How I Approached the Problem
When I first looked at the requirements, I wanted to build something more than just a basic CRUD app. I focused on making the system thread-safe and algorithmicly sound.

I followed a layered architecture to keep things clean:

Presentation Layer: A CLI menu for users and admins.

Service Layer: Where the "brains" of the app live (routing, booking logic).

Data/Repository Layer: Managing in-memory storage.

Models: Plain Java objects representing our core entities.

✨ Key Features
1. Smart Routing (The BFS Engine)
I implemented a Breadth-First Search (BFS) algorithm to find possible links between stations. It doesn't just look for direct trains; it can find routes that require a changeover, provided the next train departs after the previous one arrives.  

2. Administrator Controls
Admins have their own suite of tools to keep the network running:  

Add, remove, or modify routes, trains, and stations.

View all current bookings for a specific train.

Report delays—which automatically triggers "email" notifications to every passenger on that train.

3. Safety & Concurrency
I used the synchronized keyword in the BookingService to ensure that even if two people try to book the last seat at the exact same millisecond, the system will correctly process them one at a time and prevent overbooking.  

🚀 The "Special Feature" (Problem 2)
For the optional part of the challenge, I decided to tackle Automated Waitlist Management.

The Problem: In most systems, if a train is full, the customer just loses out.
My Solution: * If a user tries to book a full train, the system offers them a spot on a FIFO (First-In-First-Out) Waitlist.

The moment an existing booking is cancelled, the system doesn't just leave the seat empty; it automatically "pops" the next person off the waitlist and secures their ticket.

I also integrated a Dynamic Pricing Engine that increases ticket costs if the train is nearly full or if it's a last-minute booking.

🚦 Usage Examples
Finding a Route & Booking
Plaintext
Enter Departure Station ID: S1
Enter Arrival Station ID: S2

Found Route: R1 (InterCity Express)
Current Ticket Price (Surge Applied): $65.0
Enter your email: passenger@email.com
How many tickets? 1

[EMAIL SENT]: Booking Confirmation for Route R1 sent to passenger@email.com.
Booking Successful!
Handling a Full Train (Waitlist)
Plaintext
[ERROR]: Only 0 seats available. 
Would you like to join the waitlist? (Y/N): Y
Enter email: hopeful@email.com

Added to waitlist! You'll be notified if a seat opens up.
Admin: Reporting a Delay
Plaintext
Enter Train ID: T1
Enter delay (minutes): 30

[EMAIL SENT]: To passenger@email.com - Your train T1 is delayed by 30 mins.
Delay reported successfully.
🛠️ Setup and Running
Environment: Ensure you have Java 8 or higher installed.

Compilation:

Bash
javac presentation/Main.java
Run:

Bash
java presentation.Main
Note: I've included some "Mock Data" in the Main class so the app is ready to test immediately with stations S1 and S2 and train T1.
