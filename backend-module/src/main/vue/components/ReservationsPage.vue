<template>
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
          <th>State</th>
          <th>Cost</th>
          <th>Comming Time</th>
          <th>Leaving Time</th>
          <th>Business Guest</th>
          <th>Parking</th>
          <th>Edit</th>
        </tr>
      </thead>
      <tbody>
        <!-- Data rows -->
        <tr v-for="reservation in reservations" :key="reservation.ReservationID">
          <!-- Display reservation details -->
          <td>{{ reservation.ReservationID }}</td>
          <td>
            <span v-if="!reservation.editable">{{ reservation.CustomerID }}</span>
            <input v-if="reservation.editable" type="text" v-model="reservation.CustomerID">
          </td>
          <td>
            <span v-if="!reservation.editable">{{ reservation.RoomID }}</span>
            <input v-if="reservation.editable" type="text" v-model="reservation.RoomID">
          </td>
          <td>
  <span v-if="!reservation.editable">
    {{ reservation.ServiceIDs ? reservation.ServiceIDs.map(id => services_available.find(service => service.ID === id).Name).join(', ') : '' }}
  </span>
  <select v-if="reservation.editable" v-model="reservation.ServiceIDs" multiple>
    <option v-for="service in services_available" :key="service.ID" :value="service.ID">{{ service.Name }}</option>
  </select>
</td>




          <td>
  <span v-if="!reservation.editable">{{ new Date(reservation.Start).toISOString().split('T')[0] }}</span>
  <input v-if="reservation.editable" type="date" v-model="reservation.Start">
</td>
<td>
  <span v-if="!reservation.editable">{{ new Date(reservation.End).toISOString().split('T')[0] }}</span>
  <input v-if="reservation.editable" type="date" v-model="reservation.End">
</td>
          <td>
            <span v-if="!reservation.editable">{{ reservation.State }}</span>
            <select v-if="reservation.editable" v-model="reservation.State" :style="{ width: '130px' }">
              <option value="Confirmed">Confirmed</option>
              <option value="Pending">Pending</option>
            </select>
          </td>
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
            <button v-if="reservation.editable" class="ok-button" @click="updateReservation(reservation)">OK</button>
            <button v-if="reservation.editable" class="delete-button" @click="deleteReservation(reservation)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>



<script>


export default {
  data() {
    return {
      reservations: [], // array to store reservation data
      services: [], // array to store service data
      services_available: [], // array to store available services
    };
  },
  mounted() {
    this.fetchReservationsAndServices();
  },
  methods: {
    fetchReservationsAndServices() {
      fetch('/Home/Reservations/GetReservations')
        .then(response => response.json())
        .then(data => {
          this.reservations = data.map(reservation => ({ ...reservation, editable: false }));
          // After fetching reservations data, call the method to fetch reservation services
          this.fetchReservationServices(); 
        })
        .catch(error => {
          console.error('Error fetching reservations:', error);
        });
    },
    fetchReservationServices() {
  fetch('/Home/Reservations/GetReservationServices')
    .then(response => response.json())
    .then(data => {
      // Create an object to store services indexed by their service ID
      const serviceMap = {};

      this.fetchServices(); // Fetch available services
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
    })
    .catch(error => {
      console.error('Error fetching services:', error);
    });
},

fetchServices() {
  fetch('/Home/Services/GetServices')
    .then(response => response.json())
    .then(data => {
      // Create an array of service objects with an 'ID' property
      this.services_available = data.map(service => ({ ID: service.ServiceID, Name: service.Name, ...service, editable: false }));
    })
    .catch(error => {
      console.error('Error fetching services:', error);
    });
},

fetchCustomerNames() {
    // Extract unique customer IDs from reservations
    const customerIDs = [...new Set(this.reservations.map(reservation => reservation.CustomerID))];
    // Fetch customer names for each customer ID
    customerIDs.forEach(customerID => {
      fetch(`/Home/Customer/GetCustomerName?id=${customerID}`)
        .then(response => response.json())
        .then(data => {
          // Find the reservation with the corresponding customer ID and update its customer name
          const reservation = this.reservations.find(reservation => reservation.CustomerID === customerID);
          if (reservation) {
            reservation.CustomerName = data.name; // Assuming the API returns the customer's name as 'name'
          }
        })
        .catch(error => {
          console.error(`Error fetching customer name for ID ${customerID}:`, error);
        });
    });
  },

    toggleEdit(reservation) {
      reservation.editable = !reservation.editable; // Toggle the editable property
    },
    updateReservation(reservation) {
  // Implementation of reservation update
  console.log('Updating reservation:', reservation);
  reservation.editable = false; // Close the editing mode

  // Format the date to 'YYYY-MM-DD' format
  reservation.Start = new Date(reservation.Start).toISOString().split('T')[0];
  reservation.End = new Date(reservation.End).toISOString().split('T')[0];
  
  // Send the data to the server
  fetch('/Home/UpdateReservation', {
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

    updateReservationServicees(reservation) {
      // Implementation of reservation update
      console.log('Updating reservation services:', reservation);
      reservation.editable = false; // Close the editing mode

      // Send the data to the server
      fetch('/Home/UpdateReservationServices', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(reservation),
      })
      .then(response => {
        if (response.ok) {
          console.log('Reservation Services updated successfully');
        } else {
          throw new Error('Failed to update reservation services');
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
      fetch('/Home/DeleteReservation', {
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
</style>
