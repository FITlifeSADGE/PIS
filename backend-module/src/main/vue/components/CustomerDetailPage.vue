<template>
  <div class="table-container">
    <button class="button" @click="ReturnToAllCustomers">Zpět na seznam</button>

    <button class="button" @click="ToggleTable()">{{ buttonLabel }}</button> 
    <button class="button buttonR" v-if="showButton" @click="removeChanges">Zrušit změny</button>
    <p v-if="error">error</p>

    <div class="tableCustomers">
      <table v-if="editTable">
        <thead>
          <td>
            <tr><th>Last Name</th></tr>
            <tr><th>First Name</th></tr>
            <tr><th>Email</th></tr>
            <tr><th>Phone Number</th></tr>
            <tr><th>Document Number</th></tr>
            <tr><th>Date of Birth</th></tr>
            <tr><th>Allergy</th></tr>
            <tr><th>Handicap</th></tr>
            <tr><th>Address</th></tr>
            <tr><th>Subscription</th></tr>
          </td>
        </thead>
      </table>
      <table v-if="editTable" class="tableData">
        <tbody v-if="customers && customers.length > 0">
          <td v-for="customer in customers" :key="customer.PersonID">
            <tr>{{ customer.LastName }}</tr>
            <tr>{{ customer.FirstName }}</tr>
            <tr>{{ customer.Email }}</tr>
            <tr>{{ formatPhoneNumber(customer.PhoneNumber) }}</tr>
            <tr>{{ customer.DocumentNumber }}</tr>
            <tr>{{ formatDateOfBirth(customer.dateOfBirth) }}</tr>
            <tr>{{ customer.Allergy }}</tr>
            <tr>{{ formatHandicap(customer.Handicap) }}</tr>
            <tr>{{ customer.Address }}</tr>
            <tr>{{ formatSubscription(customer.Subscription) }}</tr>
          </td>
        </tbody>
      </table>
    </div>

    <table v-if="editTable">
      <thead>
        <tr>
          <th>Name</th>
          <th>Cost</th>
          <th>Availability</th>
          <th>Description</th>
        </tr>
      </thead>
      <tbody v-if="services && services.length > 0">

        <!-- <tr v-if="addingNew">
        <td><input type="text" v-model="newService.Name" placeholder="name of service"></td>
        <td><input type=number min="1" v-model="newService.Cost"></td>
        <td> 
          <select v-model="newService.Availability" :style="{ width: '130px' }" >
            <option value="Available">Available</option>
            <option value="Closed">Closed</option>
          </select>
        </td>
        <td><input type="text" v-model="newService.Description" placeholder="description of service"></td>
        <td>
          <button @click="addNewService" class="edit-button" >OK</button>
          <button @click="cancelNewService" class="delete-button" >Cancel</button>
        </td>
        </tr>
        <tr v-else>
        <td colspan="5" style="text-align: center;">
          <button @click="toggleAddNew" class="edit-button">Add New</button>
        </td>
        </tr>
        </tr> -->

        <tr>
        <td><input type="text" v-model="filters.Name"></td>
        <td><input type=number min="0" v-model="filters.Cost"></td>
        <td> 
          <select v-model="filters.Availability" :style="{ width: '130px' }">
            <option value="Available">Available</option>
            <option value="Closed">Closed</option>
            <option value="">Do Not Index</option>
          </select>
        </td>
        <td><input type="text" v-model="filters.Description"></td>
        <td></td>
        </tr>
        <tr v-for="service in services" :key="service.ServiceID">
          <td>{{ service.Name }}</td>
          <td>{{ service.Cost }}</td>
          <td>{{ service.Availability }}</td>
          <td>{{ service.Description }}</td>
        </tr>
      </tbody>
    </table>

  <div class="tableCustomers">
    <table v-if="!editTable">
      <thead>
        <td>
          <tr><th>Last Name</th></tr>
          <tr><th>First Name</th></tr>
          <tr><th>Email</th></tr>
          <tr><th>Phone Number</th></tr>
          <tr><th>Document Number</th></tr>
          <tr><th>Date of Birth</th></tr>
          <tr><th>Allergy</th></tr>
          <tr><th>Handicap</th></tr>
          <tr><th>Address</th></tr>
          <tr><th>Subscription</th></tr>
        </td>
      </thead>
    </table>
    <table v-if="!editTable">
      <tbody v-if="customers && customers.length > 0">
        <td v-for="customer in customers" :key="customer.PersonID">
          <tr><input v-model="customer.LastName"/></tr>
          <tr><input v-model="customer.FirstName"/></tr>
          <tr><input type="email" v-model="customer.Email" :style="{ width: '200px' }"/></tr>
          <tr><select v-model="customer.PhonePreselection" :style="{ width: '100px' }">
              <option value='+420'>+420</option>
              <option value='+421'>+421</option>
              <option value='+69'>+49</option>
            </select>
            <input v-model="customer.PhoneNumber"/></tr>
          <tr><input v-model="customer.DocumentNumber"/></tr>
          
            <!-- FIXME: je treba opravit placeholder nefunguje-->
          <tr><input type="date" :value="formatDateOfBirth(customer.dateOfBirth)"/> </tr>
            <!-- <input type="date" :value="formattedDateOfBirth(customer.dateOfBirth)" @input="formattedDateOfBirth(customer.dateOfBirth)" /> -->
          <tr><input v-model="customer.Allergy" @change="saveField(customer)" /></tr>
          <tr><select v-model="customer.Handicap" :style="{ width: '100px' }">
              <option value=true>Ano</option>
              <option value=false>Ne</option>
            </select></tr>
          <tr><input v-model="customer.Address" /></tr>
          <tr><select v-model="customer.Subscription" :style="{ width: '100px' }">
              <option value=true>Přihlášen</option>
              <option value=false>Odhlášen</option>
            </select></tr>
          <tr><button class="ok-button" @click="updateToggleTable(customer)">Uložit</button></tr>
        </td>
      </tbody>
    </table>
  </div>
</div>

</template>

<script>
export default {
data() {
  return {
    editTable: true,
    customers: [], // pole na uchovávanie údajov
    services: [], 
    buttonLabel: 'Upravit informace',
    showButton: false,
    ID: null,
    error: false,
    addingNew: false,
    filters: {
      Name: '',
      Cost: '',
      Availability: '',
      Description: ''
    },
    newService: {
      Name: '',
      Cost: '10',
      Availability: 'Available',
      Description: '',
      ServiceID: ''
    }
  };
},
computed: {
  filteredServices() {
    // Filter services based on filter criteria
    return this.services.filter(service => {
      return (
        service.Name.toLowerCase().includes(this.filters.Name.toLowerCase()) &&
        service.Cost.toString().includes(this.filters.Cost) &&
        service.Availability.toLowerCase().includes(this.filters.Availability.toLowerCase()) &&
        service.Description.replace(/\s/g, '').toLowerCase().includes(this.filters.Description.replace(/\s/g, '').toLowerCase())
      );
    });
  }
},
mounted() {
  const personID = this.$route.params.personID;
  console.log('Person ID:', personID);
  this.fetchCustomers(personID); // Volanie funkcie na načítanie údajov po načítaní komponentu
  this.fetchServices(); // Volanie funkcie na načítanie údajov po načítaní komponentu
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

    // validatePhoneNumber(customer.PhoneNumber);
    // validateEmail(customer.Email);
    if(!customer.dateOfBirth){
      customer.dateOfBirth = null;
    }

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
    this.showButton = false;
    this.buttonLabel = 'Upravit informace';
    this.editTable = !this.editTable;
  },
  // ------------------------------------------------------------------------------------------------
  ReturnToAllCustomers() {
    this.$router.push('/Home/Customers');
  },
  fetchServices() {
    fetch('/Home/Customers/GetServices', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then(response => response.json())
    .then(data => {
      this.services = data;
    })
    .catch(error => {
      console.error('Error fetching services:', error);
    });
  },
  fetchCustomers(personID) {
    fetch('/Home/Customers/GetCustomer', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(personID),
    })
    .then(response => response.json())
    .then(data => {
      this.customers = data;
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
  formatPhoneNumber(phoneNumber)
  {
    const countryCode = phoneNumber.slice(1, 4); // Assuming country code length is 3
    const restOfNumber = phoneNumber.slice(4, phoneNumber.length - 1);

    // Construct the formatted phone number
    return `+${countryCode} ${restOfNumber}`;
  },
  formatDateOfBirth(dateOfBirth)
  {
    if (!dateOfBirth) return ''; 

    const dateObj = new Date(dateOfBirth);
    const month = dateObj.getMonth() + 1;
    const day = dateObj.getDate();
    const year = dateObj.getFullYear();

    const formattedMonth = month < 10 ? `0${month}` : `${month}`;
    const formattedDay = day < 10 ? `0${day}` : `${day}`;

    return `${year}-${formattedMonth}-${formattedDay}`;
  },
  formatHandicap(){
    if (this.customers.Handicap == true)
      return 'Ano';
    else
      return 'Ne';
  },
  formatSubscription(){
    if (this.customers.Subscription == true)
      return 'Přihlášen';
    else
      return 'Odhlášen';
  },
  // -----------------------------------------------------------------------------------------------
  validatePhoneNumber(phoneNumber){
    // +420 xxx xxx xxx - length + 3 9
    if(phoneNumber.length!=9)
      console.log('wrong number format');
    if(!this.inputString.split('').every(char => /^\d$/.test(char)))
      console.log('wrong number format');
  },
  validateEmail(email) {
    if (!email) {
      console.log('Email is required');
      return; // Exit early if email is not provided
    }

    if (!/\S+@\S+\.\S+/.test(email)) {
      console.log('Invalid email format');
      if (!/@/.test(email)) {
        console.log('Missing @ symbol');
      } else if (!/\.\S+/.test(email)) {
        console.log('Missing domain part (e.g., .com)');
      } else {
        console.log('Email is incomplete');
      }
    }
  },
  cancelEdit() {
    fetchCustomers();
  },
  removeChanges(){
    this.editTable = !this.editTable;
    this.showButton = false;
  },
  // -----------------------------------------------------------------------------------------------
  toggleAddNew() {
      this.addingNew = true;
     },
  addNewService() {
    if (this.newService.Name && this.newService.Cost && this.newService.Availability) 
    {

      let maxServiceID = Math.max(...this.services.map(service => service.ServiceID));
      this.newService.ServiceID = maxServiceID + 1;

      this.services.push({ ...this.newService, editable: false });

      fetch('/Home/AddService', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.newService),
      })
      .then(response => {
        if (response.ok) {
          console.log('Service added successfully');
        } else {
          throw new Error('Failed to add service');
        }
      })
      .catch(error => {
        console.error('Error adding service:', error);
      });

      this.newService = {
        Name: '',
        Cost: '10',
        Availability: 'Available',
        Description: '',
        ServiceID: ''
      };
      this.addingNew = false;
      }
        else 
      {
        alert('Fill in all fields in for new Service.');
      }
    },
    cancelNewService() {
    this.newService = {
      Name: '',
      Cost: '10',
      Availability: 'Available',
      Description: '',
      ServiceID: ''
    };
    this.addingNew = false;
  },
  toggleEdit(service) {
    service.editable = !service.editable; // Prepnutie hodnoty editable
  },
  updateService(service) {
    // Implementácia aktualizácie služby
    console.log('Updating service:', service);
    service.editable = false; // Zatvorenie editovacieho režimu

    // Odoslanie údajov na server
    fetch('/Home/Customer/UpdateService', {
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
    console.log('Deleting service:', service);
    service.editable = false; // Zatvorenie editovacieho režimu

    const index = this.services.indexOf(service);
    if (index !== -1) 
    {
      this.services.splice(index, 1);
    }
    fetch('/Home/Customer/DeleteService', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(service),
    })
    .then(response => {
      if (response.ok) {
        console.log('Service deleted successfully');
      } else {
        throw new Error('Failed to delete Service');
      }
    })
    .catch(error => {
      console.error('Error deleting Service:', error);
    });
  },
}
};
</script>

<style scoped>
.tableCustomers{
display: flex;
}
.tableData{
margin-left: 200px;
}
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
