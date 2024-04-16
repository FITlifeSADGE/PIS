<template>
  <div class="Services">
    <div class="header">
      <h1>Hotel Services</h1>
      <div class="logout">
        <button class="button" @click="logout">Log out</button>
      </div>
    </div>
    <div class="content">
      <div class="toolbar">
        <button class="button" @click="viewRooms">View Rooms</button>
        <button class="button" @click="createReservation">Create Reservation</button>
        <button class="button" @click="viewReservations">View Reservations</button>
        <button class="button" @click="viewCustomers">View Customers</button>
      </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Cost</th>
              <th>Availability</th>
              <th>Description</th>
              <th>Edit</th>
            </tr>
          </thead>
          <tbody v-if="services && services.length > 0">
            <tr v-for="service in services" :key="service.ServiceID">
              <td v-if="!service.editable">{{ service.Name }}</td>
              <td v-else><input type="text" v-model="service.Name" :style="{ width: getServiceInputWidth(service.Name) }"></td>
              <td v-if="!service.editable">{{ service.Cost }}</td>
              <td v-else><input type=number min="0" v-model="service.Cost" :style="{ width: '50px' }"></td>
              <td v-if="!service.editable">{{ service.Availability }}</td>
              <td v-else><input type="text" v-model="service.Availability" :style="{ width: getServiceInputWidth(service.Availability) }"></td>
              <td v-if="!service.editable">{{ service.Description }}</td>
              <td v-else><input type="text" v-model="service.Description" :style="{ width: getServiceInputWidth(service.Description) }"></td>
              <td>
                <button v-if="!service.editable" class="edit-button" @click="toggleEdit(service)">Edit</button>
                <button v-else class="ok-button" @click="updateService(service)">OK</button>
                <button v-if="service.editable" class="delete-button" @click="deleteService(service)">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      services: [] // pole na uchovávanie údajov
    };
  },
  mounted() {
    this.fetchServices(); // Volanie funkcie na načítanie údajov po načítaní komponentu
  },
  methods: {
    fetchServices() {
      fetch('/Home/Services') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          // Nastavenie údajov do premennej Services a pridanie atribútu editable pre úpravu
          this.services = data.map(service => ({ ...service, editable: false }));
        })
        .catch(error => {
          console.error('Error fetching Services:', error);
        });
    },
    logout() {
      console.log('Logout');
    },
    viewRooms() {
      this.$router.push('/Home/Rooms');
    },
    createReservation() {
      console.log('Create Reservation');
    },
    viewReservations() {
      console.log('View Reservations');
    },
    viewCustomers() {
      console.log('View Customers');
    },
    toggleEdit(service) {
      service.editable = !service.editable; // Prepnutie hodnoty editable
    },

    updateService(service) {
      // Implementácia aktualizácie služby
      console.log('Updating service:', service);
      service.editable = false; // Zatvorenie editovacieho režimu

      // Odoslanie údajov na server
      fetch('/Home/UpdateService', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(service),
      })
      .then(response => {
        if (response.ok) {
          console.log('Service updated successfully');
        } else {
          throw new Error('Failed to update service');
        }
      })
      .catch(error => {
        console.error('Error updating service:', error);
      });
    },


    
    deleteService(service) {
      // Implementácia odstránenia služby
      console.log('Deleting service:', service);
    },
    getServiceInputWidth(text) {
      // Funkcia na získanie šírky textového poľa na základe dĺžky textu
      return text ? `${text.length * 12}px` : '100px'; // 8px na jeden znak, predvolená šírka je 100px
    }
  }
};
</script>
  
  <style scoped>
  .hotel-management {
    margin: 0;
    font-family: Arial, sans-serif;
  }
  .header {
    background-color: transparent;
    border-bottom: 2px solid #2196F3;
    color: #2196F3;
    padding: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .logout button {
    padding: 10px;
    border: none;
    border-radius: 5px;
    background-color: #f44336;
    color: white;
    font-size: 16px;
    cursor: pointer;
  }
  .logout button:hover {
    background-color: #d32f2f;
  }
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
  .toolbar {
    display: flex;
    flex-direction: column;
    width: calc(100vw / 6);
    margin-right: 20px; /* Add margin to align table with first button */
  }
  .button {
    padding: 10px 20px;
    margin-bottom: 10px;
    border: none;
    border-radius: 5px;
    background-color: #2196f3;
    color: white;
    font-size: 16px;
    cursor: pointer;
  }
  .button:hover {
    background-color: #13568e;
  }
  
  .content {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px;
  }
  .table-container {
    flex: 1;
    padding: 10px;
    border-radius: 10px;
  }
  table {
    width: 100%;
    border-collapse: collapse;
  }
  th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }
  th {
    background-color: #2196F3;
    color: white;
  }
  input[type="text"] {
  padding: 8px; /* upravte podle potřeby */
  border: none; /* odstranění ohraničení */
  border-radius: 4px; /* zaoblené rohy */
  font-size: 16px; /* velikost písma */
  border: 1px solid #2196F3;
  }
  input[type=number] {
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
  </style>
  