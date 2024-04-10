INSERT IGNORE INTO Person (PersonID, LastName, FirstName, Email, PhoneNumber, DocumentNumber, dateOfBirth)
VALUES 
(1, 'Smith', 'John', 'john.smith@example.com', '+1234567890', 'AB123456', '1990-05-15'),
(2, 'Johnson', 'Emily', 'emily.johnson@example.com', '+1987654321', 'CD987654', '1985-09-23'),
(3, 'Williams', 'Michael', 'michael.williams@example.com', '+1122334455', 'EF246810', '1978-12-10');

INSERT IGNORE INTO Employee (EmployeeID, Assignment, Password, WorkShift)
VALUES 
(1, 'Manager', 'password1', '2024-04-10'),
(2, 'Salesperson', 'password2', '2024-04-11');

INSERT IGNORE INTO Customer (CustomerID, Allergy, Handicap, Address, Subscription)
VALUES 
(3, 'Gluten intolerance', false, '789 Oak Street, Somewhere, USA', true);

INSERT IGNORE INTO Room (RoomID, TypeRoom, Cost, Equip, State, Beds)
VALUES 
(420, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(421, 'Double', 80.00, 'Desk, Chair, Wardrobe, Double Bed', 'Occupied', 2),
(422, 'Suite', 120.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa', 'Available', 3);

INSERT IGNORE INTO Service (Name, Cost, Availability, Description)
VALUES 
('Breakfast', 10.00, 'Available', 'Continental breakfast served from 7 AM to 10 AM'),
('Laundry', 20.00, 'Available', 'Laundry service available from 9 AM to 5 PM'),
('Airport Shuttle', 30.00, 'Available', 'Shuttle service to and from the airport available upon request');

-- at vyme
-- SELECT * FROM Employee Join Person on Employee.EmployeeID = Person.PersonID;
-- CLR K, CTRL C/CTRL U 