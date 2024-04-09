CREATE TABLE IF NOT EXISTS Person (
    PersonID int not null primary key,
    LastName varchar(255),
    FirstName varchar(255),
    Email varchar(255),
    PhoneNumber varchar(13),
    DocumentNumber varchar(255),
    dateOfBirth date
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

CREATE TABLE IF NOT EXISTS Employee (
    Assignment varchar(255),
    pPassword varchar(255),
    workShift date
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
    Name varchar(255),
    Cost FLOAT,
    Availability varchar(255),
    Description varchar(255)
);

CREATE TABLE IF NOT EXISTS Customer (
    Allergy varchar(255),
    Handicap BOOLEAN,
    Adress varchar(255),
    Subscription BOOLEAN
);