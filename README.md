# Bookings

This is the backend of a small booking system.

It is written in Java Spring Boot and uses a SQLite in-memory database to store the bookings.
The frontend is written in React and runs in Node.js.

## How to run
- You need to have Java 17 installed
- Clone the repository
- Run `mvn clean install` to build the application
- Run `java -jar target/bookings-0.0.1-SNAPSHOT.jar` to start the application
- The application is expected to run on [http://localhost:8080](http://localhost:8080)
- The frontend is expected to run on [http://localhost:3000](http://localhost:3000)
- The frontend project can be found here: [bookings-fe](https://github.com/cristiancll/bookings-fe)
