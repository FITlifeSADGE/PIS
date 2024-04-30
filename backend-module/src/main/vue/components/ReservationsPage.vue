<template>
  <div>
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Reservation ID</th>
            <th>Customer</th>
            <th>Room </th>
            <th>Services</th>
            <th>Start Date</th>
            <th>End Date</th>
            <!-- <th>State</th> -->
            <th>Cost</th>
            <th>Comming Time</th>
            <th>Leaving Time</th>
            <th>Business Guest</th>
            <th>Parking</th>
            <th>Edit</th>
            <th>Check-In/-Out</th>
          </tr>
        </thead>
        <tbody>
          <!-- Data rows -->
          <tr v-for="reservation in reservations" :key="reservation.ReservationID">
            <!-- Display reservation details -->
            <td>{{ reservation.ReservationID }}</td>
            <td>
              <span v-if="!reservation.editable">{{ reservation.CustomerName }}</span>
              <input v-if="reservation.editable" type="text" v-model="reservation.CustomerName">
            </td>
            <td>
              <span v-if="!reservation.editable">{{ reservation.RoomID }}</span>
              <input v-if="reservation.editable" type="text" v-model="reservation.RoomID">
            </td>
            <td>
              <span v-if="!reservation.editable">
                {{ reservation.ServiceIDs ? reservation.ServiceIDs.map(id => services_available.find(service => service.ID === id).Name).join(', ') : '' }}
              </span>
              <button v-else @click="showModalFunc(reservation)">Edit Services</button>
            </td>




            <td>
              <span v-if="!reservation.editable">{{ formatDate(reservation.Start) }}</span>
              <input v-if="reservation.editable" type="date" 
                    :style="{ 'border': invalidStartDate ? '2px solid red' : '' }"
                    :value="formatDate(reservation.Start)" 
                    @input="updateStartDate($event.target.value, reservation)" />
            </td>
            <td>
              <span v-if="!reservation.editable">{{ formatDate(reservation.End) }}</span>
              <input v-if="reservation.editable" type="date" 
                    :style="{ 'border': invalidEndDate ? '2px solid red' : '' }"
                    :value="formatDate(reservation.End)" 
                    @input="updateEndDate($event.target.value, reservation)" />
            </td>
            <!-- <td>
              <span v-if="!reservation.editable">{{ reservation.State }}</span>
              <select v-if="reservation.editable" v-model="reservation.State" :style="{ width: '130px' }">
                <option value="Confirmed">Confirmed</option>
                <option value="Pending">Pending</option>
              </select>
            </td> -->
            <td>
              <span v-if="!reservation.editable">{{ reservation.Cost }}</span>
              <input v-if="reservation.editable" type="number" min="0" v-model="reservation.Cost">
            </td>
            <td>
              <span v-if="!reservation.editable">{{ reservation.CommingTime }}</span>
              <input v-if="reservation.editable" type="time" v-model="reservation.CommingTime">
            </td>
            <td>
              <span v-if="!reservation.editable">{{ reservation.LeavingTime }}</span>
              <input v-if="reservation.editable" type="time" v-model="reservation.LeavingTime">
            </td>
            <td>
              <span v-if="!reservation.editable">{{ reservation.BusinessGuest ? 'Yes' : 'No' }}</span>
              <input v-if="reservation.editable" type="checkbox" v-model="reservation.BusinessGuest">
            </td>
            <td>
              <span v-if="!reservation.editable">{{ reservation.Parking ? 'Yes' : 'No' }}</span>
              <input v-if="reservation.editable" type="checkbox" v-model="reservation.Parking">
            </td>
            <td>
              <button v-if="!reservation.editable" class="edit-button" @click="toggleEdit(reservation)">Edit</button>
              <button v-else @click="updateReservation(reservation)" :disabled="!isDateValid">OK</button>
              <button v-if="reservation.editable" class="delete-button" @click="deleteReservation(reservation)">Delete</button>
            </td>
            <td>
              <button v-if="!reservation.editable" class="edit-button" @click="toggleCheckIn(reservation)">Check-In</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="isLoading">
    </div>
  
    <div v-if="popup" class="modal">
      <div class="modal-content">
        <p>Reservation</p>
        <table class="threadTable" style="width: 20%;">
          <tbody>
            <tr v-for="reservation in reservations" :key="reservationCheckIn.ReservationID" v-if="reservation.ReservationID === reservationCheckIn.ReservationID">
              <th> Room: </th><td>{{ reservation.RoomID }}</td>
              <th> Cost: </th><td>{{ reservation.Cost }}</td>
            </tr>
          </tbody>
        </table>
        <button class="button" @click="closePopupLCheckIn()">Check-In</button> 
        <button class="button" @click="closePopupCancel()">Cancel</button> 
      </div>
    </div>

    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="showModal = false">&times;</span>
        <h4>Select Services</h4>
          <div v-for="reservation in reservations" :key="selectedServices.ReservationID" v-if="reservation.ReservationID === selectedServices.ReservationID">
            <div v-for="service in services_available" :key="service.ID">
              <input type="checkbox" :id="service.ID" :value="service.ID" v-model="reservation.ServiceIDs">
              <label :for="service.ID">{{ getServiceName(service.ID) }}</label>
            </div>
          </div>
        <button @click="showModal = false">Confirm</button>
      </div>
    </div>

    
  </div>
</template>



<script>


export default {
  data() {
    return {
      reservations: [], // array to store reservation data
      services: [], // array to store service data
      services_available: [], // array to store available services
      popup: false,
      reservationCheckIn: [],
      showModal: false,
      selectedServices: [],
      isLoading: true,
      invalidEndDate: false,
      invalidStartDate: false,
    };
  },
  async mounted() {
    await this.fetchServices();
    await this.fetchReservationsAndServices();
    await this.fetchReservationServices();
    await this.fetchCustomerNames();
    this.isLoading = false;
  },
  computed: {
    isDateValid() {
      console.log('Invalid start date:', this.invalidStartDate);
      return !this.invalidStartDate && !this.invalidEndDate;
    },
  },
  methods: {
    async fetchServices() {
      try {
        const response = await fetch('/Home/Services/GetServices');
        const data = await response.json();
        this.services_available = data.map(service => ({ ID: service.ServiceID, Name: service.Name, ...service, editable: false }));
      } catch (error) {
        console.error('Error fetching services:', error);
      }
    },
    async fetchReservationsAndServices() {
      try {
        const response = await fetch('/Home/Reservations/GetReservations');
        const data = await response.json();
        this.reservations = data.map(reservation => ({ ...reservation, editable: false }));
      } catch (error) {
        console.error('Error fetching reservations:', error);
      }
    },
    async fetchReservationServices() {
      try {
        const response = await fetch('/Home/Reservations/GetReservationServices');
        const data = await response.json();
        // Create an object to store services indexed by their service ID
        const serviceMap = {};

        // Populate the serviceMap with service objects
        data.forEach(service => {
          const serviceID = service.ServiceID;
          if (!serviceMap[serviceID]) {
            serviceMap[serviceID] = {
              ServiceID: serviceID,
              reservations: []
            };
          }
          serviceMap[serviceID].reservations.push(service.ReservationID);
        });

        // Convert the serviceMap values back to an array of service objects
        this.services = Object.values(serviceMap);

        // Log or inspect the services array to ensure correct associations
        console.log('Services:', this.services);

        // Populate the ServiceIDs array for each reservation
        this.reservations.forEach(reservation => {
          const serviceIDs = data
            .filter(service => service.ReservationID === reservation.ReservationID)
            .map(service => service.ServiceID);
          reservation.ServiceIDs = serviceIDs;
        });
        console.log('Reservations with services:', this.reservations);
      } catch (error) {
        console.error('Error fetching reservation services:', error);
      }
    },


    async fetchCustomerNames() {
      try {
        // Extract unique customer IDs from reservations
        const customerIDs = [...new Set(this.reservations.map(reservation => reservation.CustomerID))];

        // Fetch customer data from server
        const response = await fetch(`/Home/Customer/GetCustomers`);
        const data = await response.json();
        console.log('Customer data:', data);

        // Loop through each customer ID to find and update reservations
        customerIDs.forEach(customerID => {
          const matchingReservations = this.reservations.filter(reservation => reservation.CustomerID === customerID);
          if (matchingReservations.length > 0) {
            matchingReservations.forEach(reservation => {
              data.forEach(customer => {
                if (customer.customerId === customerID) {
                  reservation.CustomerName = customer.person.email; // Assuming the API returns the customer's email as 'email'
                }
              });
            });
          }
        });
        console.log('Reservations:', this.reservations);
      } catch (error) {
        console.error('Error fetching customer names:', error);
      }
    },



    toggleEdit(reservation) {
      reservation.editable = !reservation.editable; // Toggle the editable property
    },
    updateReservation(reservation) {
  // Implementation of reservation update
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

    deleteReservation(reservation) {
      console.log('Deleting reservation:', reservation);
      reservation.editable = false; // Close the editing mode

      const index = this.reservations.indexOf(reservation);
      if (index !== -1) {
        this.reservations.splice(index, 1);
      }
      fetch('/Home/Reservations/DeleteReservation', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(reservation),
      })
      .then(response => {
        if (response.ok) {
          console.log('Reservation deleted successfully');
        } else {
          throw new Error('Failed to delete reservation');
        }
      })
      .catch(error => {
        console.error('Error deleting reservation:', error);
      });
    },
    closePopupCheckIn(){
    this.editTable = !this.editTable;
    this.popup = !this.popup;
    },
    closePopupCancel(){
      this.popup = !this.popup;
    },
    toggleCheckIn(Reservation){
      this.popup = !this.popup;
      this.reservationCheckIn = Reservation;
      console.log('Check-In:', this.reservationCheckIn);
    },
    showModalFunc(reservation) {
      this.showModal = true;
      this.selectedServices = reservation;
      console.log('Selected services:', this.selectedServices);
    },
    getServiceName(serviceId) {
      console.log('Service ID:', serviceId);
      console.log('Services:', this.services_available);
      const service_name = this.services_available.find(service => service.ID === serviceId).Name;
      console.log('Service name:', service_name);
      return service_name;
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
    updateStartDate(newValue, reservation) {
    if (!newValue) {
      reservation.Start = null;
      return;
    }

    const parts = newValue.split('-');
    const year = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1; // Měsíce jsou v JavaScriptu 0-indexované
    const day = parseInt(parts[2], 10);
    const date = new Date(year, month, day).getTime();
    if (date >= reservation.End) {
      this.invalidStartDate = true;
    }
    else {
      this.invalidStartDate = false;
      this.invalidEndDate = false;
    }
    reservation.Start = new Date(year, month, day).getTime(); // Aktualizace s novým časovým razítkem v ms
  },
  updateEndDate(newValue, reservation) {
    if (!newValue) {
      reservation.End = null;
      return;
    }

    const parts = newValue.split('-');
    const year = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1; // Měsíce jsou v JavaScriptu 0-indexované
    const day = parseInt(parts[2], 10);
    const date = new Date(year, month, day).getTime();
    if (date <= reservation.Start) {
      this.invalidEndDate = true;
    }
    else {
      this.invalidEndDate = false;
      this.invalidStartDate = false;
    }
    reservation.End = new Date(year, month, day).getTime(); // Aktualizace s novým časovým razítkem v ms
  },
  }
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
/* .modal {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: white;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
} */
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