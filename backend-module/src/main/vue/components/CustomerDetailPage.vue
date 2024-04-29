<template>
  <div class="table-container">
    <button class="button" @click="ReturnToAllCustomers">Zpět na seznam</button>
    <button class="button" @click="ToggleTable()">{{ buttonLabel }}</button> 
    
    <div v-if="popup" class="modal">
      <div class="modal-content">
        <p>Unsaved changes will be lost</p>
        <button class="button" @click="closePopupLeave">Leave</button> 
        <button class="button" @click="closePopupStay">Stay</button> 
      </div>
    </div>
    
    <p v-if="error">error</p>
    <div class="tableWrapper">
      <div class="tableCustomers">
        <table v-if="editTable" class="threadTable">
          <tbody v-if="customers && customers.length > 0">
            <tr v-for="(customer, index) in customers" :key="index">
              <th>Last Name:</th><td>{{ customer.LastName }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'first_name_' + index">
              <th>First Name:</th><td>{{ customer.FirstName }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'email_' + index">
              <th>Email:</th><td>{{ customer.Email }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'phone_number_' + index">
              <th>Phone Number:</th><td>{{ formatPhoneNumber(customer.PhonePreselection, customer.PhoneNumber) }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'document_number_' + index">
              <th>Document Number:</th><td>{{ customer.DocumentNumber }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'date_of_birth_' + index">
              <th>Date of Birth:</th><td>{{ formatDate(customer.dateOfBirth) }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'allergy_' + index">
              <th>Allergy:</th><td>{{ customer.Allergy }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'handicap_' + index">
              <th>Handicap:</th><td>{{ formatHandicap(customer.Handicap) }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'address_' + index">
              <th>Address:</th><td>{{ customer.Address }}</td>
            </tr>
            <tr v-for="(customer, index) in customers" :key="'subscription_' + index">
              <th>Subscription:</th><td>{{ formatSubscription(customer.Subscription) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- CUSTOMER -->
    <!-- SERVICES -->

    <!--<table v-if="editTable">
      <thead>
        <tr>
          <th>Name</th>
          <th>Cost</th>
          <th>Description</th>
          <th>Extra</th>
          <th>Edit</th>
        </tr>
      </thead>
      <tbody>
        <!-- v-if="services && services.length > 0" -->
       <!-- <tr v-if="addingNew">
          <td>
            <select v-model="newService"  placeholder="Select a service">
              <option v-for="service in uniqueServices" :value="service">{{ service.Name }}</option>
            </select>
          </td>
          <td>{{ newService.Cost }}</td>
          
          <td>{{ newService.Description }}</td>
          <td><input type="text" v-model="newService.Extra" placeholder="description request"></td>
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
        <td><input type="text" v-model="filters.Description"></td>
        <td><input type="text" v-model="filters.Extra"></td>
        <td></td>
        </tr>

        <tr v-for="service in filteredServices" :key="service.ServiceID">
          <td>{{ service.Name }}</td>
          <td>{{ service.Cost }}</td>
          <td>{{ service.Description }}</td>
          <td v-else><input type="text" v-model="service.Description" :style="{ width: getServiceInputWidth(service.Description) }"></td> -->
          <!--<td v-if="!service.editable">{{ service.Extra }}</td>
          <td v-else><input type="text" v-model="service.Extra" :style="{ width: getServiceInputWidth(service.Extra) }"></td>
         
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

    <!-- RESERVATION -->
    <!-- CUSTOMER -->
    <!-- <div class="tableCustomers">
      <table v-if="!editTable" class="threadTable">
        <tbody v-if="customers && customers.length > 0">
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Last Name:</th><td><input type="text" v-model="customer.LastName"/></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>First Name:</th><td><input type="text" v-model="customer.FirstName"/></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Email:</th><td><input type="email" v-model="customer.Email"/></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Phone Number:</th><td><select v-model="customer.PhonePreselection">
              <option value='+420'>+420</option>
              <option value='+421'>+421</option>
              <option value='+69'>+49</option>
            </select>
            <input type="text" v-model="customer.PhoneNumber"/></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Document Number:</th><td><input type="text" v-model="customer.DocumentNumber"/></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Date of Birth:</th><td><input type="date" v-model="customer.dateOfBirth"/></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Allergy:</th><td><input type="text" v-model="customer.Allergy"/></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Handicap:</th><td><select v-model="customer.Handicap">
              <option value=true>Ano</option>
              <option value=false>Ne</option>
            </select></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Address:</th><td><input type="text" v-model="customer.Address" /></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <th>Subscription:</th><td><select v-model="customer.Subscription">
              <option value=true>Přihlášen</option>
              <option value=false>Odhlášen</option>
            </select></td>
          </tr>
          <tr v-for="customer in customers" :key="customer.PersonID">
            <td>
              <button class="ok-button" @click="updateToggleTable(customer)">Uložit</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div> -->


    <div class="tableCustomers">
      <table v-if="!editTable" class="threadTable">
        <tbody v-if="customers && customers.length > 0">
          <tr v-for="(customer, index) in customers" :key="index">
            <th>Last Name:</th><td><input type="text" v-model="customer.LastName"/></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'first_name_' + index">
            <th>First Name:</th><td><input type="text" v-model="customer.FirstName"/></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'email_' + index">
            <th>Email:</th><td><input type="email" v-model="customer.Email"/></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'phone_number_' + index">
            <th>Phone Number:</th><td><select v-model="customer.PhonePreselection">
              <option value='+420'>+420</option>
              <option value='+421'>+421</option>
              <option value='+69'>+49</option>
            </select>
            <input type="text" v-model="customer.PhoneNumber"/></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'document_number_' + index">
            <th>Document Number:</th><td><input type="text" v-model="customer.DocumentNumber"/></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'date_of_birth_' + index">
            <th>Date of Birth:</th><td><input type="date" v-model="customer.dateOfBirth"/></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'allergy_' + index">
            <th>Allergy:</th><td><input type="text" v-model="customer.Allergy"/></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'handicap_' + index">
            <th>Handicap:</th><td><select v-model="customer.Handicap">
              <option value=true>Ano</option>
              <option value=false>Ne</option>
            </select></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'address_' + index">
            <th>Address:</th><td><input type="text" v-model="customer.Address" /></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'subscription_' + index">
            <th>Subscription:</th><td><select v-model="customer.Subscription">
              <option value=true>Přihlášen</option>
              <option value=false>Odhlášen</option>
            </select></td>
          </tr>
          <tr v-for="(customer, index) in customers" :key="'button_' + index">
            <td colspan="2">
              <button class="ok-button" @click="updateToggleTable(customer)">Uložit</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- <table v-if="!editTable" class="tableData">
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
          
          <tr><input type="date" :value="formatDate(customer.dateOfBirth)"/> </tr>
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
    </table> -->
</div>

</template>

<script>
export default {
data() {
  return {
    editTable: true,
    customers: [], // pole na uchovávanie údajov
    buttonLabel: 'Upravit informace',
    ID: null,
    error: false,
    addingNew: false,
    addingNewReservation: false,
    popup: false,
    filters: {
      Name: '',
      Cost: '',
      Description: '',
      Extra: ''
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
    },
    reservations: [], // array to store reservation data
      services: [], // array to store service data
      services_available: [], // array to store available services
  };
},
computed: {
  filteredServices() {
    // Filter services based on filter criteria
    return this.services.filter(service => {
      return (
        service.Name.toLowerCase().includes(this.filters.Name.toLowerCase()) &&
        service.Cost.toString().includes(this.filters.Cost) &&
        service.Description.replace(/\s/g, '').toLowerCase().includes(this.filters.Description.replace(/\s/g, '').toLowerCase()) &&
        service.Extra.replace(/\s/g, '').toLowerCase().includes(this.filters.Extra.replace(/\s/g, '').toLowerCase())
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
  },
  uniqueServices() {
    const uniqueNames = [];
    const uniqueServices = [];
    this.services.forEach(service => {
      if (!uniqueNames.includes(service.Name)) {
        uniqueNames.push(service.Name);
        uniqueServices.push(service);
      }
    });
    return uniqueServices;
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
    {
      this.popup = !this.popup;
    }
    else{
      this.buttonLabel = 'Zrušit změny';
      this.editTable = !this.editTable;
    }
  },
  closePopupLeave(){
    this.buttonLabel = 'Upravit informace';
    this.editTable = !this.editTable;
    this.popup = !this.popup;
  },
  closePopupStay(){
    this.popup = !this.popup;
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
  formatPhoneNumber(PhonePreselection, phoneNumber)
  {
    return `${PhonePreselection} ${phoneNumber}`;
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
  formatHandicap(Handicap){
    if (typeof Handicap === 'string') {
      if (Handicap === 'true') {
        return 'Ano';
      }
      else {
        return 'Ne';
      }
    }
    if (typeof Handicap === 'boolean') {
      if (Handicap === true) {
        return 'Ano';
      }
      else {
        return 'Ne';
      }
    }
  },
  formatSubscription(Subscription){
    if (typeof Subscription === 'string') {
      if (Subscription === 'true') {
        return 'Přihlášen';
      }
      else {
        return 'Odhlášen';
      }
    }
    if (typeof Subscription === 'boolean') {
      if (Subscription === true) {
        return 'Přihlášen';
      }
      else {
        return 'Odhlášen';
      }
    }
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
.tableWrapper {
    display: flex;
    justify-content: flex-start;
    margin-top: 20px;
  }

.tableCustomers {
  display: flex;
  flex-direction: row;
  align-items: stretch;
}

.tableData {
  margin-left: 20px;
  width: 150%;
}

.tableData table {
  width: 150%;
  border-collapse: collapse;
}

.tableData tr {
  padding: 8px;
  text-align: left;
  height: 35px;

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
/* border: 1px solid #ddd; */
/* padding: 8px; */
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

.modal {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  /* width: 150px;
  height: 100px; */
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: white;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

</style>