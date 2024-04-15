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
              </tr>
            </thead>
            <tbody v-if="services && services.length > 0">
            <tr v-for="service in services" :key="service.ServiceID">
              <td>{{ service.Name }}</td>
              <td>{{ service.Cost }}</td>
              <td>{{ service.Availability }}</td>
              <td>{{ service.Description }}</td>
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
            this.services = data; // Nastavenie údajov do premennej Services
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
  </style>
  