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
        <button class="button" @click="ReturnToAllCustomers">Zpět na seznam</button>

        <button class="button" @click="ToggleTable()">{{ buttonLabel }}</button> 
        <button class="button buttonR" v-if="showButton" @click="removeChanges">Zrušit změny</button>

          <!-- <span v-if="customer.validationError" class="error-message">{{ customer.validationError }}</span> -->
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
              <!-- <tr><th>Allergy</th>
                <th>Handicap</th>
                <th>Address</th>
                <th>Subscription</th>
              </tr> -->
            </thead>
            <tbody v-if="customers && customers.length > 0">
              <tr v-for="customer in customers" :key="customer.PersonID">
                <td>{{ customer.LastName }}</td>
                <td>{{ customer.FirstName }}</td>
                <td>{{ customer.Email }}</td>
                <td>{{ customer.PhoneNumber }}</td>
                <td>{{ customer.DocumentNumber }}</td>
                <td>{{ formatDateOfBirth(customer.dateOfBirth) }}</td>
              </tr>
              <!-- <tr v-for="customer in customers" :key="customer.PersonID">
                <td>{{ customer.Allergy }}</td>
                <td>{{ customer.Handicap }}</td>
                <td>{{ customer.Address }}</td>
                <td>{{ customer.Subscription }}</td>
              </tr> -->
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
                <th>Save</th>
              </tr>
              <!-- <tr><th>Allergy</th>
                <th>Handicap</th>
                <th>Address</th>
                <th>Subscription</th>
              </tr> -->
            </thead>
            <tbody v-if="customers && customers.length > 0">
              <tr v-for="customer in customers" :key="customer.PersonID">
                <td>
                  <input v-model="customer.LastName"/>
                </td>
                <td>
                  <input v-model="customer.FirstName"/>
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
                  <input v-model="customer.PhoneNumber" @change="validatePhoneNumber(customer)" />
                </td>
                <td>
                  <input v-model="customer.DocumentNumber"/>
                </td>
                <td>
                  <input type="date" v-model="customer.dateOfBirth"/>
                  <!-- <input type="date" :value="formattedDateOfBirth(customer.dateOfBirth)" @input="formattedDateOfBirth(customer.dateOfBirth)" /> -->
                </td>
                <!-- FIXME: -->
                <td>
                <button class="ok-button" @click="updateToggleTable(customer)">Uložit</button> 
                
                </td>
              <!-- ------ -->
              </tr>
              <!-- <tr v-for="customer in customers" :key="customer.PersonID">
                <td>
                  <input v-model="customer.Allergy" @change="saveField(customer)" />
                </td>
                <td><input v-model="customer.Handicap" /></td>
                <td><input v-model="customer.Address" /></td>
                <td><input v-model="customer.Subscription" /></td>
              </tr> -->
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
        buttonLabel: 'Upravit informace',
        showButton: false,
        ID: null,
      };
    },
    mounted() {
      this.fetchCustomers(); // Volanie funkcie na načítanie údajov po načítaní komponentu
    },
    methods: {
      // ------------------------------------------------------------------------------------------------
      ToggleTable() {
        if(this.editTable == false)
        {
          this.showButton = false;
          this.buttonLabel = 'Upravit informace';
        }
        else
        {
          this.buttonLabel = 'Uložit změny';
          this.showButton = true;
        }
        this.editTable = !this.editTable;
      },
      updateToggleTable(customer) {        
      console.log('Updating customer:', customer);

        fetch('/Home/UpdateCustomer', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(customer),
        })
        .then(response => {
          if (response.ok) {
            console.log('Customer updated successfully');
          } else {
            throw new Error('Failed to update customer');
          }
        })
        .catch(error => {
          console.error('Error updating customer:', error);
        });
      },
      // ------------------------------------------------------------------------------------------------
      ReturnToAllCustomers() {
    // Přesměrování na stránku /Home/customers/detail s přidáním parametru ID
      this.$router.push('/Home/customers');
      },
      fetchCustomers() {
        fetch('/Home/customers/GetCustomer', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(this.$route.query.id), // Assuming you're sending an object with an ID property
        })
        .then(response => response.json())
        .then(data => {
          this.customers = data; // Set the fetched data to the customers variable
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
      formatDateOfBirth(dateOfBirth) {
        if (!dateOfBirth) return ''; // Handle case when dateOfBirth is null or undefined

        const dateObj = new Date(dateOfBirth);
        const month = dateObj.getMonth() + 1; // Months are zero-indexed
        const day = dateObj.getDate();
        const year = dateObj.getFullYear();

        // Pad single digit month and day with leading zero
        const formattedMonth = month < 10 ? `0${month}` : `${month}`;
        const formattedDay = day < 10 ? `0${day}` : `${day}`;

        return `${year}-${formattedMonth}-${formattedDay}`;
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
      removeChanges(){
        this.editTable = !this.editTable;
        this.showButton = false;
      }
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

  .buttonR {
    display: inline-block;
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
  </style>
  