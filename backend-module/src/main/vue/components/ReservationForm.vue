<template>
  <div class="reservation-form">
    <h2>Create a Reservation</h2>
    <form @submit.prevent="submitForm">
      <div>
        <label for="customer-id">Customer Name</label>
        <select id="customer-id" v-model="reservation.CustomerID" required>
          <option value="" disabled>Select a customer</option>
          <option v-for="customer in customers" :key="customer.customerId" :value="customer.customerId">
            {{ customer.person.email }}
          </option>
        </select>
        <button v-if="!showNewCustomerForm" @click.prevent="addNewCustomer" :disabled="!isCreateButtonEnabled">New Customer</button>
        <button v-else @click.prevent="addNewCustomer">Hide Creation</button>
      </div>
      <div>
        <label for="room-id">Rooms</label>
        <select id="room-id" v-model="reservation.RoomID" required>
          <option value="" disabled>Select a room</option>
          <option v-for="room in rooms" :value="room.RoomID">{{ room.RoomID }} (Beds: {{ room.Beds }})</option>
        </select>
      </div>
      <div>
        <label for="start-date">Start Date</label>
        <input id="start-date" type="date" v-model="reservation.Start" required>
      </div>
      <div>
        <label for="end-date">End Date</label>
        <input id="end-date" type="date" v-model="reservation.End" required>
        <span v-if="!isCreateButtonEnabled" class="warning-icon" title="End datum musí být po Start datum">&#9888;</span>
      </div>
      <div>
        <label for="coming-time">Check-in</label>
        <input id="coming-time" type="time" v-model="reservation.ComingTime" required>
      </div>
      <div>
        <label for="leaving-time">Check-out</label>
        <input id="leaving-time" type="time" v-model="reservation.LeavingTime" required>
      </div>
      <div>
        <label for="business-guest">Business Guest</label>
        <input id="business-guest" type="checkbox" v-model="reservation.BusinessGuest">
      </div>
      <div>
        <label for="parking">Parking</label>
        <input id="parking" type="checkbox" v-model="reservation.Parking">
      </div>
      <div>
  <label for="cost">Cost</label>
  <input id="cost" type="number" min="0" step="0.01" v-model="totalCost" readonly>
</div>
      <button type="submit" :disabled="!isCreateButtonEnabled">Create Reservation</button>
    </form>

     <!-- Display reservation details if successfully created -->
     <div v-if="reservationCreated">
      <h2>Reservation Details</h2>
      <p><strong>Customer Name:</strong> {{ selectedCustomerName }}</p>
      <p><strong>Room:</strong> {{ selectedRoom }}</p>
      <p><strong>Start Date:</strong> {{ reservation.Start }}</p>
      <p><strong>End Date:</strong> {{ reservation.End }}</p>
      <p><strong>Cost:</strong> {{ reservation.Cost }}</p>
      <p><strong>Coming Time:</strong> {{ reservation.ComingTime }}</p>
      <p><strong>Leaving Time:</strong> {{ reservation.LeavingTime }}</p>
      <p><strong>Business Guest:</strong> {{ reservation.BusinessGuest }}</p>
      <p><strong>Parking:</strong> {{ reservation.Parking }}</p>

 <!-- Option to add services -->
 <h3>Add Services</h3>
 <button @click="showModal = true">Select Services</button>
 <router-link to="/Home/Reservations">
    <button>Continue without services</button>
  </router-link>

 <div v-if="showModal" class="modal">
    <div class="modal-content">
      <span class="close" @click="showModal = false">&times;</span>
      <h4>Select Services</h4>
      <div v-for="service in services" :key="service.ServiceID">
        <input type="checkbox" :id="service.ServiceID" :value="service.ServiceID" v-model="selectedServices">
        <label :for="service.ServiceID">{{ service.Name }}</label>
      </div>
      <button @click="addReservationServices">Add Services</button>
    </div>
  </div>

  <ul>
    <li v-for="serviceId in reservation.Services" :key="serviceId">{{ getServiceName(serviceId) }}</li>
  </ul>
</div>


  <div v-if="showNewCustomerForm && !reservationCreated">
      <h2>Create a New Customer</h2>
      <form @submit.prevent="createCustomer">
        <div>
            <label for="new-customer-firstname">First Name</label>
            <input id="new-customer-firstname" v-model="newCustomer.FirstName" required>
          </div>
          <div>
            <label for="new-customer-lastname">Last Name</label>
            <input id="new-customer-lastname" v-model="newCustomer.LastName" required>
          </div>
          <div>
            <label for="new-customer-email">Email</label>
            <input id="new-customer-email" v-model="newCustomer.Email" required type="email">
            <span v-if="!isEmailUnique" class="warning-icon" title="Zákazník s daným emailem již existuje">&#9888;</span>
          </div>
          <div>
            <label for="new-customer-phone">Phone</label>
            <input id="new-customer-phone" v-model="newCustomer.PhoneNumber" required type="tel">
            <span v-if="!isPhoneNumberUnique" class="warning-icon" title="Zákazník s tímto číslem již existuje">&#9888;</span>
          </div>
          <div>
            <label for="new-customer-document">Document Number</label>
            <input id="new-customer-document" v-model="newCustomer.DocumentNumber" required type="number">
          </div>
          <div>
            <label for="new-customer-dob">Date of Birth</label>
            <input id="new-customer-dob" type="date" v-model="newCustomer.DateOfBirth" required>
          </div>
          <div>
            <label for="new-customer-phone-preselection">Phone Preselection</label>
            <input id="new-customer-phone-preselection" v-model="newCustomer.PhonePreselection" required pattern="\+\d{2,3}">
          </div>
          <div>
            <label for="new-customer-allergy">Allergy</label>
            <input id="new-customer-allergy" v-model="newCustomer.Allergy" required>
          </div>
          <div>
            <label for="new-customer-handicap">Handicap</label>
            <input id="new-customer-handicap" type="checkbox" v-model="newCustomer.Handicap">
          </div>
          <div>
            <label for="new-customer-address">Address</label>
            <input id="new-customer-address" v-model="newCustomer.Address" required>
          </div>
          <div>
            <label for="new-customer-subscription">Subscription</label>
            <input id="new-customer-subscription" type="checkbox" v-model="newCustomer.Subscription">
          </div>
        
          <button type="submit" :disabled="!isCustomerUnique">Create Customer</button>
        </form>
      </div>
    </div>
</template>

<script>
export default {
  data() {
    return {
      customers: [],
      rooms: [],
      services: [],
      reservation: {
        CustomerID: '',
        RoomID: '',
        Start: this.getFormattedDate(),
        End: this.getFormattedDate(7),
        Cost: 0,
        ComingTime: '14:00',
        LeavingTime: '10:00',
        BusinessGuest: false,
        Parking: false,
        reservationId: 0
      },
      newCustomer: {
        LastName: '',
        FirstName: '',
        Email: 'konza.hulich@example.com',
        PhonePreselection: '+420',
        PhoneNumber: '777777777',
        DocumentNumber: '123456789',
        DateOfBirth: '1998-01-01',
        Allergy: 'None',
        Handicap: false,
        Address: 'Bozetechova 1/2, 612 00 Brno',
        Subscription: false
    
      },
      ReservationID : 0,
      showNewCustomerForm: false,
      reservationCreated: false,
      showAddServicesButton: true,
      selectedCustomerName: '',
      selectedRoom: '',
      newServiceName: '',
      services: [],
      selectedServices: [],
      isCustomerUnique: true,
      isEmailUnique: true,
      isPhoneNumberUnique: true,
      showModal: false,
    };
  },
  mounted() {
    this.fetchCustomers(); 
    this.fetchRooms();
    this.fetchServices();// Volanie funkcie na načítanie údajov po načítaní komponentu
  },
  computed: {
  totalCost() {
    if (this.reservation.Start && this.reservation.End && this.reservation.RoomID) {
      const startDate = new Date(this.reservation.Start);
      const endDate = new Date(this.reservation.End);
      const days = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24));
      const room = this.rooms.find(room => room.RoomID === this.reservation.RoomID);
      let cost = days * room.Cost;

      if (this.reservation.BusinessGuest) {
        // Add additional cost for business guest
        cost += 50;
      }

      if (this.reservation.Parking) {
        // Add additional cost for parking
        cost += 10;
      }

      return cost.toFixed(2);
    }

    return 0;
  },
  isCreateButtonEnabled() {
    const startDate = new Date(this.reservation.Start);
    const endDate = new Date(this.reservation.End);
    return endDate > startDate;
  }
},
  methods: {
    async submitForm() {
      this.reservation.Cost = this.totalCost;
      try {
        const response = await fetch('/Home/Reservations/AddReservation', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.reservation)
        });

        if (response.ok) {
          const data = await response.json();
          console.log('Reservation created successfully');

           // Check the reservationId value
            console.log('Reservation ID:', data.reservationId);

          this.ReservationID = data.reservationId;
          console.log('Reservation ID:', this.ReservationID);

          // Clear the form or redirect to another page
          this.reservationCreated = true;
          this.selectedCustomerName = this.customers.find(customer => customer.customerId === this.reservation.CustomerID).person.firstName + ' ' + this.customers.find(customer => customer.customerId === this.reservation.CustomerID).person.lastName;
          this.selectedRoom = this.rooms.find(room => room.RoomID === this.reservation.RoomID).RoomID;
        } else {
          console.error('Failed to create reservation');
          // Handle error case
        }
      } catch (error) {
        console.error('An error occurred:', error);
        // Handle error case
      }
    },

    getServiceName(serviceId) {
      return this.services.find(service => service.ServiceID === serviceId).Name;
    },

    updateReservation(reservation) {
  // Implementation of reservation update
  reservation.reservationId = this.ReservationID;
  console.log('Updating reservation:', reservation);
  reservation.editable = false; // Close the editing mode

  // Format the date to 'YYYY-MM-DD' format
  reservation.Start = this.formatDate(reservation.Start);
  reservation.End = this.formatDate(reservation.End);
  
  //format the parking and business guest to integer
  reservation.Parking = reservation.Parking ? 1 : 0;
  reservation.BusinessGuest = reservation.BusinessGuest ? 1 : 0;

  // Send the data to the server
  fetch('/Home/Reservations/UpdateReservation', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(reservation),
  })
  .then(response => {
    if (response.ok) {
      console.log('Reservation updated successfully');
    } else {
      throw new Error('Failed to update reservation');
    }
  })
  .catch(error => {
    console.error('Error updating reservation:', error);
  });
},

    fetchCustomers() {
  fetch('/Home/Customer/GetCustomers') // Replace with the appropriate endpoint URL of your servlet
    .then(response => response.json())
    .then(data => {
      // Set the retrieved customer data to the "customers" variable
      this.customers = data.map(customer => ({ ...customer, editable: false }));
      this.isUniqueEmail(this.newCustomer.Email);
      this.isUniquePhoneNumber(this.newCustomer.PhoneNumber);
    })
    .catch(error => {
      console.error('Error fetching customers:', error);
    });
},
fetchRooms() {
      fetch('/Home/Rooms/GetRooms') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          this.rooms = data.map(room => ({ ...room, editable: false })); // Nastavenie údajov do premennej rooms
        })
        .catch(error => {
          console.error('Error fetching rooms:', error);
        });
    },


  fetchServices() {
      fetch('/Home/Services/GetServices') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          this.services = data.map(service => ({ ...service, editable: false })); // Nastavenie údajov do premennej rooms
        })
        .catch(error => {
          console.error('Error fetching services:', error);
        });
    },

addNewCustomer() {
      this.showNewCustomerForm = !this.showNewCustomerForm;
    },

    createCustomer() {
      // Send a POST request to the servlet to create a new customer
      fetch('/Home/Customer/AddCustomer', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.newCustomer),
      })
        .then((response) => response.json())
        .then((data) => {
          // Add the newly created customer to the customers array
          this.customers.push(data);
          
          // Close the popup form
          this.showNewCustomerForm = false;

          // Set the newly created customer as the selected value in the select element
          this.reservation.CustomerID = data.customerId;
          this.isUniqueEmail(this.newCustomer.Email);
          this.isUniquePhoneNumber(this.newCustomer.PhoneNumber);
        })
        .catch((error) => {
          console.error('Error creating customer:', error);
          // Handle error case
        });
    },

    addReservationServices(){
      // Send a POST request to the servlet to add a new service to the reservation
      console.log('Selected services:', this.selectedServices);
      console.log('Reservation:', this.reservation);
      if (this.selectedServices.length > 0) {
        this.selectedServices.forEach(service => { 
          let serviceCost = parseFloat(this.services.find(s => s.ServiceID === service).Cost);
          this.reservation.Cost = parseFloat(this.reservation.Cost) + serviceCost;
        });
      }
      console.log('New reservation: ', this.reservation);
      console.log('Total cost:', this.reservation.Cost);
      this.updateReservation(this.reservation);
      fetch('/Home/Reservations/AddReservationServices', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ reservationId: this.ReservationID, serviceIds: this.selectedServices }),
      })
        .then((response) => {
          if (response.ok) {
            this.showModal = false;
            this.$router.push('/Home/Reservations');
          } else {
            throw new Error('Adding new reservation failed');
          }
        })
        .then((data) => {
          this.newServiceName = '';

           // Hide the button
          this.showAddServicesButton = false;
        })
        .catch((error) => {
          console.error('Error adding service:', error);
          // Handle error case
        });

    },
    getFormattedDate(days = 0) {
      const today = new Date();
      today.setDate(today.getDate() + days);
      const day = today.getDate().toString().padStart(2, '0');
      const month = (today.getMonth() + 1).toString().padStart(2, '0');
      const year = today.getFullYear();
      return `${year}-${month}-${day}`;
    },
    isUniqueEmail(email) {
      const existingCustomer = this.customers.find(customer =>
        customer.person.email === email);
      if (existingCustomer) {
        this.isCustomerUnique = false;
        this.isEmailUnique = false;
        return false; // Není unikátní, protože zákazník již existuje
      } else {
        return true; // Je unikátní, protože takový zákazník neexistuje
      }
    },
    isUniquePhoneNumber(phoneNumber) {
      const existingCustomer = this.customers.find(customer =>
        customer.person.phoneNumber === phoneNumber
      );
      if (existingCustomer) {
        this.isCustomerUnique = false;
        this.isPhoneNumberUnique = false;
        return false; // Není unikátní, protože zákazník již existuje
      } else {
        return true; // Je unikátní, protože takový zákazník neexistuje
      }
    },
    formatDate(ReservDate) {
      if (!ReservDate) return ''; 

      const dateObj = new Date(ReservDate);
      const month = dateObj.getMonth() + 1;
      const day = dateObj.getDate();
      const year = dateObj.getFullYear();

      const formattedMonth = month < 10 ? `0${month}` : `${month}`;
      const formattedDay = day < 10 ? `0${day}` : `${day}`;

      return `${year}-${formattedMonth}-${formattedDay}`;
    },
  },
  watch: {
    'reservation.End'(newVal, oldVal) {
    if (new Date(this.reservation.End) <= new Date(this.reservation.Start)) {
      this.showNewCustomerForm = false;
    }
  },
  'reservation.Start'(newVal, oldVal) {
    if (new Date(this.reservation.End) <= new Date(this.reservation.Start)) {
      this.showNewCustomerForm = false;
    }
  },
   'newCustomer.Email'(newVal, oldVal) {
      this.isEmailUnique = this.isUniqueEmail(this.newCustomer.Email);
      if (this.isEmailUnique && this.isPhoneNumberUnique) {
        this.isCustomerUnique = true;
      } else {
        this.isCustomerUnique = false;
      }
    },
    'newCustomer.PhoneNumber'(newVal, oldVal) {
      this.isPhoneNumberUnique = this.isUniquePhoneNumber(this.newCustomer.PhoneNumber);
      if (this.isEmailUnique && this.isPhoneNumberUnique) {
        this.isCustomerUnique = true;
      } else {
        this.isCustomerUnique = false;
      }
    }
  }
};
</script>

<style>
.reservation-form {
  max-width: 500px;
  margin: 0 auto;
}

.reservation-form h2 {
  text-align: center;
}

.reservation-form form {
  display: flex;
  flex-direction: column;
}

.reservation-form label {
  margin-bottom: 0.5rem;
}

.reservation-form input,
.reservation-form select {
  margin-bottom: 1rem;
}
.warning-icon {
  color: red; /* Nastaví barvu ikony na červenou */
  margin-left: 5px; /* Přidá malý odstup od inputu */
  font-size: 18px; /* Zvětší velikost ikony */
}

.modal {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 300px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.close {
  float: right;
  font-size: 28px;
  cursor: pointer;
}
</style>