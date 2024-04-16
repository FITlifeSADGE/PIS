<template>
    <div class="hotel-management">
      <div class="header">
        <h1>Hotel Management</h1>
        <div class="logout">
          <button class="button" @click="logout">Odhlásenie</button>
        </div>
      </div>
      <div class="content">
        <div class="toolbar">
          <button class="button" @click="manageHotelServices">Správa služieb hotela</button>
          <button class="button" @click="createReservation">Vytvorenie rezervácie</button>
          <button class="button" @click="viewReservations">Zobrazenie rezervácií</button>
          <button class="button" @click="viewCustomers">Zobrazenie zoznamu zákazníkov</button>
        </div>
        <div class="table-container">
          <!-- <span v-if="customer.validationError" class="error-message">{{ customer.validationError }}</span> -->
  
          <table>
            <thead>
              <tr>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Email</th>
                <th></th>
              </tr>
            </thead>
            <tbody v-if="customers && customers.length > 0">
              <tr v-for="customer in customers" :key="customer.PersonID">
                <td>{{ customer.LastName }}</td>
                <td>{{ customer.FirstName }}</td>
                <td>{{ customer.Email }}</td>
                <td>
                    <button class="button" @click="getDetail(customer.PersonID)">View details</button>
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
        customers: [], // pole na uchovávanie údajov
      };
    },
    mounted() {
      this.fetchCustomers(); // Volanie funkcie na načítanie údajov po načítaní komponentu
    },
    methods: {
    fetchCustomers() {
    fetch('/Home/customers/GetCustomers') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
        this.customers = data; // Nastavenie údajov do premennej customers
        })
        .catch(error => {
        console.error('Error fetching customers:', error);
        });
      },
    //   getDetail(customerID) {
    //     fetch('/Home/customers/GetCustomer', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json',
    //     },
    //     body: JSON.stringify({
    //         personID: customerID,
    //     })
    //     })
    //     .then(response => {
    //     if (response.ok) {
    //         console.log('Login successful');
    //         this.$router.push('/Home/customers');
    //     } else {
    //         throw new Error('Login failed');
    //     }
    //     })
    //     .then(data => {
    //     // Zpracovat úspěšnou odpověď
    //     this.loginMessage = data;
    //     })
    //     .catch(error => {
    //     console.error('Error during login:', error);
    //     this.loginMessage = 'Špatný email nebo heslo'; // Pokud se nepodaří spojit nebo obdržet odpověď
    //     });
    // },
    getDetail(personID) {
    // Přesměrování na stránku /Home/customers/detail s přidáním parametru ID
    this.$router.push({ path: '/Home/customers/detail', query: { id: personID } });
    },
      logout() {
        console.log('Logout');
      },
      manageHotelServices() {
        console.log('Manage Hotel Services');
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
  table td:last-child {
    text-align: center;
  }
  </style>
  