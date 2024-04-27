<template>
   <!-- Step 1: Create Customer -->
<div v-if="currentStep === 1">
  <h2>Create Customer</h2>
  <form @submit.prevent="createCustomer">
    <label for="lastName">Last Name:</label>
    <input type="text" id="lastName" v-model="customer.lastName" required>
    
    <label for="firstName">First Name:</label>
    <input type="text" id="firstName" v-model="customer.firstName" required>
    
    <label for="email">Email:</label>
    <input type="email" id="email" v-model="customer.email" required>
    
    <label for="phone">Phone Number:</label>
    <input type="text" id="phone" v-model="customer.phoneNumber" required>
    
    <label for="documentNumber">Document Number:</label>
    <input type="text" id="documentNumber" v-model="customer.documentNumber" required>
    
    <label for="dateOfBirth">Date of Birth:</label>
    <input type="date" id="dateOfBirth" v-model="customer.dateOfBirth" required>
    
    <label for="allergy">Allergy:</label>
    <input type="text" id="allergy" v-model="customer.allergy">
    
    <label for="handicap">Handicap:</label>
    <input type="checkbox" id="handicap" v-model="customer.handicap">
    
    <label for="address">Address:</label>
    <textarea id="address" v-model="customer.address"></textarea>
    
    <button type="submit">Next</button>
  </form>

  
      <!-- Step 2: Select Room -->
      <div v-if="currentStep === 2">
        <h2>Select Room</h2>
        <!-- Room selection goes here -->
        <button @click="previousStep">Back</button>
        <button @click="nextStep">Next</button>
      </div>
  
      <!-- Step 3: Add Services -->
      <div v-if="currentStep === 3">
        <h2>Add Services</h2>
        <!-- Service selection goes here -->
        <button @click="previousStep">Back</button>
        <button @click="nextStep">Next</button>
      </div>
  
      <!-- Step 4: Additional Information -->
      <div v-if="currentStep === 4">
        <h2>Additional Information</h2>
        <!-- Additional information form goes here -->
        <button @click="previousStep">Back</button>
        <button @click="createReservation">Create Reservation</button>
      </div>
  
      <!-- Success Message -->
      <div v-if="reservationCreated">
        <h2>Reservation Created Successfully!</h2>
        <!-- Display reservation details here -->
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
  return {
    currentStep: 1,
    reservationCreated: false,
   customer: {
    LastName: '',
    FirstName: '',
    Email: '',
    PhoneNumber: '',
    DocumentNumber: '',
    DateOfBirth: '',
    Allergy: '',
    Handicap: false,
    Address: '',
    CustomerID : ''
   },
    customers: [], // Add this line to define an empty array for customers
  };
},

    methods: {
      nextStep() {
        this.currentStep += 1;
      },
      previousStep() {
        this.currentStep -= 1;
      },
      createCustomer() {
  if (
    this.customer.lastName &&
    this.customer.firstName &&
    this.customer.email &&
    this.customer.phoneNumber &&
    this.customer.documentNumber &&
    this.customer.dateOfBirth
  ) {
    // Generate a unique customer ID
    let maxCustomerId = Math.max(...this.customers.map(cust => cust.customerId), 0); // Make sure to handle the case where there are no existing customers
    this.customer.customerId = maxCustomerId + 1;

    // Perform any additional logic or API calls here
    fetch('/Home/AddCustomer', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(this.customer),
    })
    .then(response => {
      if (response.ok) {
        console.log('Customer added successfully');
        // Move to the next step or perform any other necessary actions
        this.nextStep();
      } else {
        throw new Error('Failed to add customer');
      }
    })
    .catch(error => {
      console.error('Error adding customer:', error);
    });
  } else {
    alert('Please fill in all fields for the new customer.');
  }
},


      createReservation() {
        // TODO: Implement reservation creation logic
        // Once the reservation is created, set reservationCreated to true
        // and display the success message and reservation details
        this.reservationCreated = true;
      },
    },
  };
  </script>

<style scoped>
.edit-button{
  padding: 10px 20px;
  margin-bottom: 10px;
  border: none;
  border-radius: 5px;
  background-color: #2196f3;
  color: white;
  font-size: 16px;
  cursor: pointer;
}
.edit-button:hover {
  background-color: #13568e;
}
.ok-button{
  padding: 10px 10px;
  margin-bottom: 10px;
  border: none;
  border-radius: 5px;
  background-color: #2196f3;
  color: white;
  font-size: 16px;
  cursor: pointer;
}
.ok-button:hover {
  background-color: #13568e;
}
.delete-button{
  padding: 10px 10px;
  margin-bottom: 10px;
  border: none;
  border-radius: 5px;
  background-color: #f32f21;
  color: white;
  font-size: 16px;
  cursor: pointer;
}
.delete-button:hover {
  background-color: #951e16;
}
input[type="text"],
input[type="date"],
input[type="time"] {
  padding: 8px; /* upravte podle potřeby */
  border: none; /* odstranění ohraničení */
  border-radius: 4px; /* zaoblené rohy */
  font-size: 16px; /* velikost písma */
  border: 1px solid #2196F3;
}
input[type="number"] {
  padding: 8px; /* upravte podle potřeby */
  border: none; /* odstranění ohraničení */
  border-radius: 4px; /* zaoblené rohy */
  font-size: 16px; /* velikost písma */
  border: 1px solid #2196F3;
}
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: inner-spin-button; /* Nastavení výchozího vzhledu */
  appearance: inner-spin-button;
  color: #2196F3; /* Barva šipek */
  font-size: 16px; /* Velikost písma šipek */
}
select{
  padding: 8px; /* upravte podle potřeby */
  border: none; /* odstranění ohraničení */
  border-radius: 4px; /* zaoblené rohy */
  font-size: 16px; /* velikost písma */
  border: 1px solid #2196F3;
}
</style>