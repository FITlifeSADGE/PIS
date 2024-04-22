<template>
  <div class="table-container">
    <button class="button" @click="ReturnToAllCustomers">Zpět na seznam</button>

    <button class="button" @click="ToggleTable()">{{ buttonLabel }}</button> 
    <p v-if="error">error</p>

    <div class="tableCustomers">
      <table v-if="editTable" class="threadTable">
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
            <tr>{{ formatDate(customer.dateOfBirth) }}</tr>
            <tr>{{ customer.Allergy }}</tr>
            <tr>{{ formatHandicap(customer.Handicap) }}</tr>
            <tr>{{ customer.Address }}</tr>
            <tr>{{ formatSubscription(customer.Subscription) }}</tr>
          </td>
        </tbody>
      </table>
    </div>

    <!-- CUSTOMER -->
    <!-- SERVICES -->

    <table v-if="editTable">
      <thead>
        <tr>
          <th>Name</th>
          <th>Cost</th>
          <th>Availability</th>
          <th>Description</th>
          <th>Edit</th>
        </tr>
      </thead>
      <tbody>
        <!-- v-if="services && services.length > 0" -->
        <tr v-if="addingNew">
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

        <tr>
        <td><input type="text" v-model="filters.Name"></td>
        <td><input type=number min="0" v-model="filters.Cost"></td>
        <td> 
          <select v-model="filters.Availability" :style="{ width: '140px' }">
            <option value="Available">Available</option>
            <option value="Closed">Closed</option>
            <option value="">Do Not Index</option>
          </select>
        </td>
        <td><input type="text" v-model="filters.Description"></td>
        <td></td>
        </tr>

        <tr v-for="service in filteredServices" :key="service.ServiceID">
          <td v-if="!service.editable">{{ service.Name }}</td>
          <td v-else><input type="text" v-model="service.Name" :style="{ width: getServiceInputWidth(service.Name) }"></td>
          <td v-if="!service.editable">{{ service.Cost }}</td>
          <td v-else><input type=number min="1" v-model="service.Cost" :style="{ width: '50px' }"></td>
          <td v-if="!service.editable">{{ service.Availability }}</td>
          <td v-else> 
            <select v-model="service.Availability" :style="{ width: '130px' }">
              <option value="Available">Available</option>
              <option value="Closed">Closed</option>
            </select>
          </td>
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

    <!-- SERVICES -->
    <!-- RESERVATION -->

    <table v-if="editTable">
      <thead>
        <tr>
          <th>Start</th>
          <th>End</th>
          <th>State</th>
          <th>Cost</th>
          <th>Check-In</th>
          <th>Check-Out</th>
          <th>Business Guest</th>
          <th>Parking</th>
          <th>Edit</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="addingNewReservation">
          <td><input type=date v-model="newReservation.Start"></td>
          <td><input type=date v-model="newReservation.End"></td>
          <td> 
            <select v-model="filtersR.State" :style="{ width: '140px' }">
              <option value="Available">Cancelled</option>
              <option value="Closed">Comfirmed</option>
              <option value="Pending">Pending</option>
              <option value="">Do Not Index</option>
            </select>
          </td>
          <td><input type=number min="0" v-model="filtersR.Cost" ></td>
          <td><input type=date v-model="newReservation.CommingTime"></td>
          <td><input type=date v-model="newReservation.LeavingTime"></td>
          <td> 
            <select v-model="filtersR.BusinessGuest" :style="{ width: '140px' }">
              <option value="0">No</option>
              <option value="1">Yes</option>
              <option value="">Do Not Index</option>
            </select>
          </td>
          <td> 
            <select v-model="filtersR.Parking" :style="{ width: '140px' }">
              <option value="0">No</option>
              <option value="1">Yes</option>
              <option value="">Do Not Index</option>
            </select>
          </td>
          <td>
            <button @click="addNewReservation" class="edit-button" >OK</button>
            <button @click="cancelNewReservation" class="delete-button" >Cancel</button>
          </td>
        </tr>
        <tr v-else>
          <td colspan="9" style="text-align: center;">
            <button @click="toggleAddNewReservation" class="edit-button">Add New</button>
          </td>
        </tr>
        <tr>
          <td><input type=date v-model="filtersR.Start"></td>
          <td><input type=date v-model="filtersR.End"></td>
          <td> 
              <select v-model="filtersR.State" :style="{ width: '140px' }">
              <option value="Available">Cancelled</option>
              <option value="Closed">Comfirmed</option>
              <option value="Pending">Pending</option>
              <option value="">Do Not Index</option>
            </select>
          </td>
          <td><input type=number min="0" v-model="filtersR.Cost"></td>
          <td><input type=date v-model="filtersR.CommingTime"></td>
          <td><input type=date v-model="filtersR.LeavingTime"></td>
          <td> 
            <select v-model="filtersR.BusinessGuest" :style="{ width: '140px' }">
              <option value="0">No</option>
              <option value="1">Yes</option>
              <option value="">Do Not Index</option>
            </select>
          </td>
          <td> 
            <select v-model="filtersR.Parking" :style="{ width: '140px' }">
              <option value="0">No</option>
              <option value="1">Yes</option>
              <option value="">Do Not Index</option>
            </select>
          </td>
          <td></td>
        </tr>

        <!-- <tr v-for="reservation in reservations" :key="reservation.ReservationID"> -->
        <tr v-if="!reservation.editableR" v-for="reservation in filteredReservations" :key="reservation.ReservationID">
          <td>{{ formatDate(reservation.Start) }}</td>
          <td>{{ formatDate(reservation.End) }}</td>
          <td>{{ reservation.State }}</td>
          <td>{{ reservation.Cost }}</td>
          <td>{{ formatDate(reservation.CommingTime) }}</td>
          <td>{{ formatDate(reservation.LeavingTime) }}</td>
          <td>{{ reservation.BusinessGuest }}</td>
          <td>{{ reservation.Parking }}</td>
          <td><button class="edit-button" @click="toggleEditReservation(reservation)">Edit</button></td>
        </tr>
        <tr v-if="reservation.editableR" v-for="reservation in filteredReservations" :key="reservation.ReservationID">
          <td><input type="date" :value="formatDate(reservation.Start)" /></td>
          <td><input type="date" :value="formatDate(reservation.End)" /></td>
          <td>
            <select v-model="reservation.State" :style="{ width: '130px' }">
              <option value="Confirmed">Confirmed</option>
              <option value="Pending">Pending</option>
              <option value="Cancelled">Cancelled</option>
            </select>
          </td>
          <td><input type="text" v-model="reservation.LastName" :style="{ width: getReservationInputWidth(reservation.LastName) }"></td>
          <td><input type="text" v-model="reservation.FirstName" :style="{ width: getReservationInputWidth(reservation.FirstName) }"></td>
          <td><input type=number min="1" v-model="reservation.Cost" :style="{ width: '50px' }"></td>
          <td><input type="date" :value="formatDate(reservation.CommingTime)" /></td>
          <td><input type="date" :value="formatDate(reservation.LeavingTime)" /></td>
          <td>
            <select v-model="reservation.BusinessGuest" :style="{ width: '130px' }">
              <option value=0>No</option>
              <option value=1>Yes</option>
            </select>
          </td>
          <td>
            <select v-model="reservation.Parking" :style="{ width: '130px' }">
              <option value=0>No</option>
              <option value=1>Yes</option>
            </select>
          </td>
          <td>
            <button class="ok-button" @click="updateReservation(reservation)">OK</button>
            <button class="delete-button" @click="deleteReservation(reservation)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- RESERVATION -->
    <!-- CUSTOMER -->
  <div class="tableCustomers">
    <table v-if="!editTable" class="threadTable">
      <thead >
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
    <table v-if="!editTable" class="tableData">
      <tbody v-if="customers && customers.length > 0">
        <td v-for="customer in customers" :key="customer.PersonID">
          <tr><input type="text" v-model="customer.LastName"/></tr>
          <tr><input type="text" v-model="customer.FirstName"/></tr>
          <tr><input type="email" v-model="customer.Email" :style="{ width: '200px' }"/></tr>
          <tr><select v-model="customer.PhonePreselection" :style="{ width: '100px' }">
              <option value='+420'>+420</option>
              <option value='+421'>+421</option>
              <option value='+69'>+49</option>
            </select>
            <input type="text" v-model="customer.PhoneNumber"/></tr>
          <tr><input type="text" v-model="customer.DocumentNumber"/></tr>
          
            <!-- FIXME: je treba opravit placeholder nefunguje-->
          <tr><input type="date" :value="formatDate(customer.dateOfBirth)"/> </tr>
            <!-- <input type="date" :value="formattedDateOfBirth(customer.dateOfBirth)" @input="formattedDateOfBirth(customer.dateOfBirth)" /> -->
          <tr><input type="text" v-model="customer.Allergy" @change="saveField(customer)" /></tr>
          <tr><select v-model="customer.Handicap" :style="{ width: '100px' }">
              <option value=true>Ano</option>
              <option value=false>Ne</option>
            </select></tr>
          <tr><input type="text" v-model="customer.Address" /></tr>
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
    reservations: [], 
    buttonLabel: 'Upravit informace',
    ID: null,
    error: false,
    addingNew: false,
    addingNewReservation: false,
    filters: {
      Name: '',
      Cost: '',
      Availability: '',
      Description: ''
    },
    filtersR: {
      Start: null,            // Initialize as null or new Date()
      End: null,              // Initialize as null or new Date()
      State: '',
      Cost: '',
      CommingTime: null,      // Initialize as null or new Date()
      LeavingTime: null,      // Initialize as null or new Date()
      BusinessGuest: null,
      Parking: null
    },
    newService: {
      Name: '',
      Cost: '10',
      Availability: 'Available',
      Description: '',
      ServiceID: ''
    },
    newReservation: {
      Start: '2024-04-09',
      End: '2024-04-09',
      State: 'Pending',
      Cost: '600',
      CommingTime: '2024-04-09',
      LeavingTime: '2024-04-09',
      BusinessGuest: 'No',
      Parking: 'No',
      ReservationID: ''
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
  },
  filteredReservations() {
    return this.reservations.filter(reservation => {
      return (
        (!this.filtersR.Start || reservation.Start >= this.filtersR.Start) &&
        (!this.filtersR.End || reservation.End <= this.filtersR.End) &&
        reservation.State.toLowerCase().includes(this.filtersR.State.toLowerCase()) &&
        reservation.Cost.toString().includes(this.filtersR.Cost) &&
        (!this.filtersR.CommingTime || reservation.CommingTime >= this.filtersR.CommingTime) &&
        (!this.filtersR.LeavingTime || reservation.LeavingTime <= this.filtersR.LeavingTime) &&
        (this.filtersR.BusinessGuest === null || reservation.BusinessGuest === this.filtersR.BusinessGuest) &&
        (this.filtersR.Parking === null || reservation.Parking === this.filtersR.Parking)
      );
    });
  }
},
mounted() {
  const personID = this.$route.params.personID;
  console.log('Person ID:', personID);
  this.fetchCustomers(personID); // Volanie funkcie na načítanie údajov po načítaní komponentu
  this.fetchServices(); // Volanie funkcie na načítanie údajov po načítaní komponentu
  this.fetchReservations(); // Volanie funkcie na načítanie údajov po načítaní komponentu
},
methods: {
  // ------------------------------------------------------------------------------------------------
  ToggleTable() {
    if(this.editTable == false)
      this.buttonLabel = 'Upravit informace';
    else
      this.buttonLabel = 'Zrušit změny';
    this.editTable = !this.editTable;
  },

  updateToggleTable(customer) {        
    this.validatePhoneNumber(customer.PhoneNumber);
    this.validateEmail(customer.Email);
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
    this.buttonLabel = 'Upravit informace';
    this.editTable = !this.editTable;
  },
  // ---------------------------------------- FETCH --------------------------------------------------------

  fetchServices() {
    fetch('/Home/Customers/GetServices') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
      .then(response => response.json())
      .then(data => {
        // Nastavenie údajov do premennej services a pridanie atribútu editable pre úpravu
        this.services = data.map(service => ({ ...service, editable: false }));
      })
      .catch(error => {
        console.error('Error fetching services:', error);
      });
  },
  fetchReservations() {
    fetch('/Home/Customers/GetReservations') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
      .then(response => response.json())
      .then(data => {
        // Nastavenie údajov do premennej services a pridanie atribútu editableR pre úpravu
        this.reservations = data.map(reservation => ({ ...reservation, editableR: false }));
      })
      .catch(error => {
        console.error('Error fetching reservations:', error);
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
  // ---------------------------------------- FETCH --------------------------------------------------------
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
  // ---------------------------------------------- FORMATS -------------------------------------------------
  formatPhoneNumber(phoneNumber)
  {
    const countryCode = phoneNumber.slice(1, 4); // Assuming country code length is 3
    const restOfNumber = phoneNumber.slice(4, phoneNumber.length - 1);

    // Construct the formatted phone number
    return `+${countryCode} ${restOfNumber}`;
  },
  formatDate(dateOfBirth)
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
  // ---------------------------------------------- FORMATS -------------------------------------------------
  // ---------------------------------------------- CUSTOMER -------------------------------------------------
  ReturnToAllCustomers() {
    this.$router.push('/Home/Customers');
  },
  validatePhoneNumber(phoneNumber){
    // +420 xxx xxx xxx - length + 3 9
    if(phoneNumber.length!=9)
      console.log('wrong number format');
    if(!(phoneNumber.split('').every(char => /^\d$/.test(char))))
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
  },
  // -------------------------------------------- SERVICES ---------------------------------------------------
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
  getServiceInputWidth(text) {
      // Funkcia na získanie šírky textového poľa na základe dĺžky textu
      return text ? `${text.length * 12}px` : '100px'; // 8px na jeden znak, predvolená šírka je 100px
  },
  // --------------------- RESERVATION ------------------------
  toggleAddNewReservation() {
  this.addingNewReservation = true;
  },
  addNewReservation() {
  if (this.newReservation.Name && this.newReservation.Cost && this.newReservation.Availability) 
  {

    let maxReservationID = Math.max(...this.reservations.map(reservation => reservation.ReservationID));
    this.newReservation.ReservationID = maxReservationID + 1;

    this.reservations.push({ ...this.newReservation, editableR: false });

    fetch('/Home/AddReservation', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(this.newReservation),
    })
    .then(response => {
      if (response.ok) {
        console.log('Reservation added successfully');
      } else {
        throw new Error('Failed to add reservation');
      }
    })
    .catch(error => {
      console.error('Error adding reservation:', error);
    });

    this.newReservation = {
      Name: '',
      Cost: '10',
      Availability: 'Available',
      Description: '',
      ReservationID: ''
    };
    this.addingNewReservation = false;
    }
      else 
    {
      alert('Fill in all fields in for new Reservation.');
    }
  },
  cancelNewReservation() {
  this.newReservation = {
    Name: '',
    Cost: '10',
    Availability: 'Available',
    Description: '',
    ReservationID: ''
  };
  this.addingNewReservation = false;
  },
  toggleEditReservation(reservation) {
    reservation.editableR = !reservation.editablR; // Prepnutie hodnoty editableR
  },
  updateReservation(reservation) {
    // Implementácia aktualizácie služby
    console.log('Updating reservation:', reservation);
    reservation.editableR = false; // Zatvorenie editovacieho režimu

    // Odoslanie údajov na server
    fetch('/Home/Customer/UpdateReservation', {
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
    reservation.editableR = false; // Zatvorenie editovacieho režimu

    const index = this.reservations.indexOf(reservation);
    if (index !== -1) 
    {
      this.reservations.splice(index, 1);
    }
    fetch('/Home/Customer/DeleteReservation', {
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
        throw new Error('Failed to delete Reservation');
      }
    })
    .catch(error => {
      console.error('Error deleting Reservation:', error);
    });
  },
  getReservationInputWidth(text) {
      // Funkcia na získanie šírky textového poľa na základe dĺžky textu
      return text ? `${text.length * 12}px` : '100px'; // 8px na jeden znak, predvolená šírka je 100px
    },
  
}
};
</script>

<style scoped>
.tableCustomers{
display: flex;
}

.tableData tr {
  line-height: 30px;  
  margin-left: 500px;
}
.threadTable td{
  width: 300px;
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
margin-bottom: 15px;
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

input[type="date"],
input[type="email"],
input[type="text"],
input[type=number] {
  padding: 8px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  border: 1px solid #2196F3;
}

input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: inner-spin-button;
  appearance: inner-spin-button;
  color: #2196F3;
  font-size: 16px; 
}
select{
  padding: 8px; 
  border: none; 
  border-radius: 4px;
  font-size: 16px; 
  border: 1px solid #2196F3;
}

</style>
