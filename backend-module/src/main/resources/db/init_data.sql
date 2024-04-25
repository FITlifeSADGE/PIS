INSERT IGNORE INTO Person (PersonID, LastName, FirstName, Email, PhonePreselection, PhoneNumber, DocumentNumber, dateOfBirth)
VALUES 
(1, 'Smith', 'John', 'john.smith@example.com', '+123','456789088', 'AB123456', '1990-05-15'),
(2, 'Johnson', 'Emily', 'emily.johnson@example.com', '+123','765432112', 'CD987654', '1985-09-23'),
(3, 'Williams', 'Michael', 'michael.williams@example.com', '+112', '233445500', 'EF246810', '1978-12-10');

INSERT IGNORE INTO Employee (EmployeeID, Assignment, Password, WorkShift)
VALUES 
(1, 'Manager', 'password1', '2024-04-10'),
(2, 'Salesperson', 'password2', '2024-04-11');

INSERT IGNORE INTO Customer (CustomerID, Allergy, Handicap, Address, Subscription)
VALUES 
(3, 'Gluten intolerance', false, '789 Oak Street, Somewhere, USA', true);

INSERT IGNORE INTO Service (ServiceID, Name, Cost, Availability, Description, Extra)
VALUES 
(33, 'Breakfast', 10.00, 'Available', 'Continental breakfast served from 7 AM to 10 AM', '-'),
(34, 'Laundry', 20.00, 'Available', 'Laundry service available from 9 AM to 5 PM', 'Dont mix colors!'),
(35, 'Airport Shuttle', 30.00, 'Available', 'Shuttle service to and from the airport available upon request', '-');
-- (36, 'Pancakes', 12.00, 'Available', 'Pancakes', 'Add extra topings');

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


INSERT IGNORE INTO Reservation (ReservationID, Start, End, State, Cost, CommingTime, LeavingTime, BusinessGuest, Parking)
VALUES
(11, '2024-05-01', '2024-05-05', 'Confirmed', 500.00, '2024-05-01', '2024-05-05', 0, 0),
(12, '2024-06-10', '2024-06-15', 'Pending', 800.00, '2024-06-10', '2024-06-15', 1, 1),
(13, '2024-07-20', '2024-07-25', 'Confirmed', 700.00, '2024-07-20', '2024-07-25', 0, 1),
(14, '2024-08-15', '2024-08-20', 'Cancelled', 600.00, '2024-08-15', '2024-08-20', 1, 0),
(15, '2024-09-01', '2024-10-01', 'Confirmed', 1500.00, '2024-09-01', '2024-10-01', 0, 1);
-- at vyme
-- SELECT * FROM Employee Join Person on Employee.EmployeeID = Person.PersonID;
-- CLR K, CTRL C/CTRL U 