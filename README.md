# Guided Tours Management System

A Java console application that plans guided tours for an organisation working in a local area. Configurators set up places and tour types, volunteers give their availability, and visitors book tours. Data is stored in MySQL through JDBC.

This was a team project for the Software Engineering course (Ingegneria del Software) at the University of Brescia. We built it in 2025, in four incremental versions.

## What it does

The app has three roles, each with its own console menu. The interface is in Italian.

- **Configurator.** Sets up the area, places, tour types and volunteers, or loads them from an initial data set, and can add or remove them later. Sets excluded dates and the maximum number of bookings per visitor. Generates the monthly tour plan and closes each collection phase.
- **Volunteer.** Sees the tour types they lead, gives their availability for the month and checks their confirmed tours.
- **Visitor.** Signs up or logs in, browses proposed, confirmed and cancelled tours, books a tour, and checks or cancels their bookings.

A tour type has a title, description, meeting point, weekdays, start time, duration, scheduling period, minimum and maximum participants, a ticket flag and its volunteers. Each tour goes from *proposed* to *full*, then to *confirmed* or *cancelled* when bookings close, and finally to *done*. The system follows a monthly cycle: it collects volunteer availability and excluded dates, generates the plan, takes bookings, then closes them.

## Architecture

```
src/
├── app/          MainApp: wires models, views and controllers
├── controller/   25 controllers, one per use case or role
├── model/        27 domain classes and enums
│   └── dao/      9 classes: DAOs for each aggregate + the MySQL connection
└── view/         25 console views (menus and input via the course library mylib)
```

- **MVC**, with responsibilities assigned following GRASP. `ParametriSistema` is a GoF Singleton.
- **Persistence.** There is one DAO per aggregate on JDBC. Every query is a parameterised `PreparedStatement`, so user input never ends up in the SQL string.
- **Design by Contract.** Javadoc documents class invariants, preconditions and postconditions, and the code checks them with `assert`.
- **Size.** 87 classes, about 7,500 lines of Java.

## Tech stack

Java 11+, MySQL 8, JDBC (MySQL Connector/J 9.3.0), Eclipse, and the University of Brescia course library `it.unibs.fp.mylib` (`InputDati`, `MyMenu`).

## Running it

1. Create the database and load the schema:

   ```bash
   mysql -u root -p -e "CREATE DATABASE ingesw"
   mysql -u root -p ingesw < schema.sql
   ```

2. Check the connection settings in `src/model/dao/ConnessioneDBModel.java`. The default is `jdbc:mysql://localhost:3306/ingesw`.
3. In Eclipse, import this project together with the course library project `UniBSFpLib`, then add MySQL Connector/J to the build path.
4. Run `app.MainApp`. Add `-ea` to the VM arguments so the contract assertions are checked.

## Known limitations

- Passwords are stored and compared in plain text. The next step is salted hashing (for example PBKDF2).
- There are no automated tests yet. JUnit tests for the model and the DAOs would be the first addition.

## Team

Brilant Gashi (team lead), Laiba and Roxana.
