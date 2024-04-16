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
  
          <button class="button" @click="toggleTable">{{ buttonLabel }}</button>
          <button class="button" @click="cancelEdit">Zrušit změny</button>
          <!-- <span v-if="customer.validationError" class="error-message">{{ customer.validationError }}</span> -->
          <h1>{{$route.query.id}}</h1>
          <table v-if="editTable">
            <thead>
              <tr>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Email</th>
                <th>Phone Number</th>
                <th>Document Number</th>
                <th>Date of Birth</th>
              </tr>
            </thead>
            <tbody v-if="customers && customers.length > 0">
              <tr v-for="customer in customers" :key="customer.PersonID">
                <td>{{ customer.LastName }}</td>
                <td>{{ customer.FirstName }}</td>
                <td>{{ customer.Email }}</td>
                <td>{{ customer.PhoneNumber }}</td>
                <td>{{ customer.DocumentNumber }}</td>
                <td>{{ customer.dateOfBirth }}</td>
              </tr>
            </tbody>
          </table>
  
          <table v-if="!editTable">
            <thead>
              <tr>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Email</th>
                <th>Phone Number</th>
                <th>Document Number</th>
                <th>Date of Birth</th>
              </tr>
            </thead>
            <tbody v-if="customers && customers.length > 0">
              <tr v-for="customer in customers" :key="customer.PersonID">
                <td>
                  <input v-model="customer.LastName" @change="saveField(customer)" />
                </td>
                <td>
                  <input v-model="customer.FirstName" @change="saveField(customer)" />
                </td>
                  <!-- ------------------------------------------------------------------------------------------------ -->
                <!-- <td>
                  <input type="email" v-model="customer.Email" @blur="validateEmail(customer)" />
                </td>
                <span v-if="customer.validationError" class="error-message">{{ customer.validationError }}</span> -->
                <td>
                  <input type="email" v-model="customer.Email" @change="validateEmail(customer)" />
                  <span v-if="customer.validationError" class="error-message">{{ customer.validationError }}</span>
                </td>
                <!-- ------------------------------------------------------------------------------------------------ -->
                <td>
                  <!-- @change="saveField(customer)"  -->
                  <input v-model="customer.PhoneNumber" @change="validatePhoneNumber(customer)" />
                </td>
                <td>
                  <input v-model="customer.DocumentNumber" @change="saveField(customer)" />
                </td>
                <td>
                  <input type="date" v-model="customer.dateOfBirth" @change="saveField(customer)" />
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
        editTable: true,
        customers: [], // pole na uchovávanie údajov
        buttonLabel: 'Edit informations'
      };
    },
    mounted() {
      this.fetchCustomers(); // Volanie funkcie na načítanie údajov po načítaní komponentu
    },
    methods: {
      // ------------------------------------------------------------------------------------------------
      saveField(customer) {
        console.log("Updated customer:", customer);
        
      },
      toggleTable() {
        if(this.editTable == false)
          this.buttonLabel = 'Edit informations';
        else
          this.buttonLabel = 'Save changes';
        this.editTable = !this.editTable;
      },
      // ------------------------------------------------------------------------------------------------
  
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
  
      // -----------------------------------------------------------------------------------------------
      validatePhoneNumber(customer){
        const phoneNumber = customer.PhoneNumber;
        if(!/\S+/.test(phoneNumber)){
          customer.validationError = 'BAD';
          return false;
        }else{
        customer.validationError = '';
        return true;
        }
      },
      validateEmail(customer) {
        const email = customer.Email;
        // if (!email) {
        //   customer.validationError = 'Email is required';
        //   return false;
        // }
        if (!/\S+@\S+/.test(email)) {
          customer.validationError = 'Chybí @examle';
          return false;
        }else if(!/\S+\.\S+/.test(email)){
          customer.validationError = 'Chybí .com';
          return false;
        }else if(!/\S+@\S+\.\S+/.test(email)){
          customer.validationError = 'Chybí obsah emailu';
          return false;
        }
        // Clear validation error if email is valid
        customer.validationError = '';
        return true;
      },
      cancelEdit() {
        // TODO:
        fetchCustomers();
      },
      // -----------------------------------------------------------------------------------------------
  
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
  