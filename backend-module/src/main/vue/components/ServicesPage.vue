<template>
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
      fetch('/Home/Services') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          this.services = data; // Nastavenie údajov do premennej Services
        })
        .catch(error => {
          console.error('Error fetching Services:', error);
        });
    }
  }
};
</script>
