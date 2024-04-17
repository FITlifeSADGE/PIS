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
</template>
  
<script>
import Parent from './Parent.vue';

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
      fetch('/Home/Services/GetServices') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          // Nastavenie údajov do premennej Services a pridanie atribútu editable pre úpravu
          this.services = data.map(service => ({ ...service, editable: false }));
        })
        .catch(error => {
          console.error('Error fetching Services:', error);
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
  
