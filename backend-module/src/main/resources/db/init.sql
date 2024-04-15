CREATE TABLE IF NOT EXISTS Person (
    PersonID int not null primary key,
    LastName varchar(255),
    FirstName varchar(255),
    Email varchar(255),
    PhoneNumber varchar(13),
    DocumentNumber varchar(255),
    dateOfBirth date
);

CREATE TABLE IF NOT EXISTS Employee (
    EmployeeID int not null primary key,
    Assignment varchar(255),
    Password varchar(255),
    WorkShift date,
    FOREIGN KEY (EmployeeID) REFERENCES Person(PersonID)
);

CREATE TABLE IF NOT EXISTS Customer (
    CustomerID int not null primary key,
    Allergy varchar(255),
    Handicap BOOLEAN,
    Address varchar(255),
    Subscription BOOLEAN,
    FOREIGN KEY (CustomerID) REFERENCES Person(PersonID)
);

CREATE TABLE IF NOT EXISTS Reservation(
    ReservationID int not null primary key,
    Start DATE,
    End DATE,
    State varchar(255),
    Cost FLOAT,
    CommingTime DATE,
    LeavingTime DATE,
    BusinessGuest BOOLEAN,
    Parking BOOLEAN
);

CREATE TABLE IF NOT EXISTS Room(
    RoomID int not null primary key,
    TypeRoom varchar(255),
    Cost FLOAT,
    Equip varchar(255),
    State varchar(255),
    Beds INTEGER
);

CREATE TABLE IF NOT EXISTS Service(
    ServiceID int not null primary key,
    Name varchar(255),
    Cost FLOAT,
    Availability varchar(255),
    Description varchar(255)
);