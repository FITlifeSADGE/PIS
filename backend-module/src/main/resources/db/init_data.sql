-- Inserting sample data into the Person table
INSERT IGNORE INTO Person (PersonID, LastName, FirstName, Email, PhonePreselection, PhoneNumber, DocumentNumber, dateOfBirth)
VALUES 
(1, 'Smith', 'John', 'john.smith@example.com', '+123','456789088', 'AB123456', '1990-05-15'),
(2, 'Johnson', 'Emily', 'emily.johnson@example.com', '+123','765432112', 'CD987654', '1985-09-23'),
(3, 'Williams', 'Michael', 'michael.williams@example.com', '+112', '233445500', 'EF246810', '1978-12-10');

-- Inserting sample data into the Employee table
INSERT IGNORE INTO Employee (EmployeeID, Assignment, Password, WorkShift)
VALUES 
(1, 'Manager', 'password1', '2024-04-10'),
(2, 'Salesperson', 'password2', '2024-04-11');

-- Inserting sample data into the Customer table
INSERT IGNORE INTO Customer (CustomerID, Allergy, Handicap, Address, Subscription)
VALUES 
(3, 'Gluten intolerance', false, '789 Oak Street, Somewhere, USA', true);

INSERT IGNORE INTO Service (ServiceID, Name, Cost, Availability, Description)
VALUES 
    (33, 'Breakfast', 10.00, 'Available', 'Continental breakfast served from 7 AM to 10 AM'),
    (34, 'Laundry', 20.00, 'Available', 'Laundry service available from 9 AM to 5 PM'),
    (35, 'Airport Shuttle', 30.00, 'Available', 'Shuttle service to and from the airport available upon request');

INSERT IGNORE INTO Room (RoomID, TypeRoom, Cost, Equip, State, Beds)
VALUES 
(420, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(421, 'Double', 80.00, 'Desk, Chair, Wardrobe, Double Bed', 'Occupied', 2),
(422, 'Suite', 120.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa', 'Available', 3),
(423, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(424, 'Double', 80.00, 'Desk, Chair, Wardrobe, Double Bed', 'Available', 2),
(425, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Occupied', 1),
(426, 'Suite', 120.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa', 'Available', 3),
(427, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(428, 'Double', 80.00, 'Desk, Chair, Wardrobe, Double Bed', 'Occupied', 2),
(429, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(430, 'Suite', 120.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa', 'Available', 3),
(431, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(432, 'Double', 80.00, 'Desk, Chair, Wardrobe, Double Bed', 'Occupied', 2),
(433, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(434, 'Suite', 120.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa', 'Available', 3),
(435, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(436, 'Double', 80.00, 'Desk, Chair, Wardrobe, Double Bed', 'Available', 2),
(437, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Occupied', 1),
(438, 'Suite', 120.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa', 'Available', 3),
(439, 'Single', 50.00, 'Desk, Chair, Wardrobe', 'Available', 1),
(440, 'Double', 90.00, 'Desk, Chair, Wardrobe, Double Bed, Mini Fridge', 'Available', 2),
(441, 'Suite', 150.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa, Mini Bar', 'Occupied', 4),
(442, 'Single', 60.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(443, 'Double', 100.00, 'Desk, Chair, Wardrobe, Double Bed, Mini Fridge', 'Available', 2),
(444, 'Single', 70.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Occupied', 1),
(445, 'Suite', 180.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa, Mini Bar', 'Available', 4),
(446, 'Single', 60.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(447, 'Double', 90.00, 'Desk, Chair, Wardrobe, Double Bed, Mini Fridge', 'Occupied', 2),
(448, 'Single', 70.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(449, 'Suite', 150.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa, Mini Bar', 'Available', 4),
(450, 'Single', 60.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(451, 'Double', 90.00, 'Desk, Chair, Wardrobe, Double Bed, Mini Fridge', 'Occupied', 2),
(452, 'Single', 70.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(453, 'Suite', 180.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa, Mini Bar', 'Available', 4),
(454, 'Single', 60.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(455, 'Double', 90.00, 'Desk, Chair, Wardrobe, Double Bed, Mini Fridge', 'Occupied', 2),
(456, 'Single', 70.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(457, 'Suite', 150.00, 'Desk, Chair, Wardrobe, King Size Bed, Sofa, Mini Bar', 'Available', 4),
(458, 'Single', 60.00, 'Desk, Chair, Wardrobe, Single Bed, Coffee Maker', 'Available', 1),
(459, 'Double', 90.00, 'Desk, Chair, Wardrobe, Double Bed, Mini Fridge', 'Occupied', 2);


-- Inserting sample data into the Reservation table
INSERT IGNORE INTO Reservation (ReservationID, CustomerID, RoomID, Start, End, State, Cost, CommingTime, LeavingTime, BusinessGuest, Parking)
VALUES
(1, 3, 420, '2024-04-10', '2024-04-12', 'Confirmed', 500.00, '08:00:00', '12:00:00', 0, 0),
(2, 3, 421, '2024-04-10', '2024-04-12', 'Pending', 800.00, '10:00:00', '14:00:00', 1, 1),
(3, 3, 422, '2024-04-10', '2024-04-12', 'Confirmed', 700.00, '09:00:00', '13:00:00', 0, 1),
(4, 3, 423, '2024-04-10', '2024-04-12', 'Paid', 1200.00, '09:00:00', '13:00:00', 0, 1);




-- Inserting sample data into the ReservationService table
INSERT IGNORE INTO ReservationService (ReservationID, ServiceID)
VALUES
(1, 33),  -- ReservationID 1 is associated with ServiceID 1 (Breakfast)
(1, 34),  -- ReservationID 1 is associated with ServiceID 2 (Laundry)
(2, 34),  -- ReservationID 2 is associated with ServiceID 1 (Breakfast)
(3, 35);  -- ReservationID 3 is associated with ServiceID 3 (Airport Shuttle)