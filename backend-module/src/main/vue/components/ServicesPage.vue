<template>
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
      <tbody>

        <!-- Add new -->
        <tr v-if="addingNew">
          <td><input type="text" v-model="newService.Name" placeholder="name of service"></td>
          <td><input type=number min="1" v-model="newService.Cost"></td>
          <td> 
            <select v-model="newService.Availability" :style="{ width: '130px' }" >
              <option value="Available">Available</option>
              <option value="Occupied">Not Available</option>
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

        <!-- Filter row -->
        <tr>
          <td><input type="text" v-model="filters.Name"></td>
          <td><input type=number min="0" v-model="filters.Cost"></td>
          <td> 
            <select v-model="filters.Availability" :style="{ width: '130px' }">
              <option value="Available">Available</option>
              <option value="Occupied">Not Available</option>
            </select>
          </td>
          <td><input type="text" v-model="filters.Description"></td>
          <td></td> <!-- Empty cell for buttons -->
        </tr>

        <!-- Data rows -->
        <tr v-for="service in filteredServices" :key="service.ServiceID">
          <td v-if="!service.editable">{{ service.Name }}</td>
          <td v-else><input type="text" v-model="service.Name" :style="{ width: getServiceInputWidth(service.Name) }"></td>
          <td v-if="!service.editable">{{ service.Cost }}</td>
          <td v-else><input type=number min="1" v-model="service.Cost" :style="{ width: '50px' }"></td>
          <td v-if="!service.editable">{{ service.Availability }}</td>
          <td v-else> 
            <select v-model="service.Availability" :style="{ width: '130px' }">
              <option value="Available">Available</option>
              <option value="Occupied">Not Available</option>
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
  </div>
</template>
  
<script>
import Parent from './Parent.vue';

export default {
  data() {
    return {
      services: [], // pole na uchovávanie údajov
      filters: {
        Name: '',
        Cost: '',
        Availability: '',
        Description: ''
      },
      addingNew: false,
      newService: {
        Name: '',
        Cost: '10',
        Availability: 'Available',
        Description: ''
      }
    };
  },
  computed: {
    filteredServices() {
      // Filter services based on filter criteria
      return this.services.filter(service => {
        return (
          service.Name.includes(this.filters.Name) &&
          service.Cost.toString().includes(this.filters.Cost) &&
          service.Availability.includes(this.filters.Availability) &&
          service.Description.includes(this.filters.Description)
        );
      });
    }
  },
  mounted() {
    this.fetchServices(); // Volanie funkcie na načítanie údajov po načítaní komponentu
  },
  methods: {
    fetchServices() {
      fetch('/Home/Services') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          // Nastavenie údajov do premennej services a pridanie atribútu editable pre úpravu
          this.services = data.map(service => ({ ...service, editable: false }));
        })
        .catch(error => {
          console.error('Error fetching services:', error);
        });
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
      console.log('Deleting service:', service);
      service.editable = false; // Zatvorenie editovacieho režimu

      const index = this.services.indexOf(service);
      if (index !== -1) 
      {
        this.services.splice(index, 1);
      }
      fetch('/Home/DeleteService', {
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
    toggleAddNew() {
      this.addingNew = true;
    },
    addNewService() {

      if (
      this.newService.Name &&
      this.newService.Cost &&
      this.newService.Availability) 
      {

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
          Description: ''
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
        Description: ''
      };
      this.addingNew = false;
    }
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
select{
  padding: 8px; /* upravte podle potřeby */
  border: none; /* odstranění ohraničení */
  border-radius: 4px; /* zaoblené rohy */
  font-size: 16px; /* velikost písma */
  border: 1px solid #2196F3;
}
</style>
  
