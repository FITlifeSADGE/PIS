<template>
  <div class="table-container">
    <table>
      <thead>
        <tr>
          <th>Type</th>
          <th>Cost</th>
          <th>Equipment</th>
          <th>State</th>
          <th>Beds</th>
        </tr>
      </thead>
      <tbody v-if="rooms && rooms.length > 0">
        <tr v-for="room in rooms" :key="room.RoomID">
          <td>{{ room.TypeRoom }}</td>
          <td>{{ room.Cost }}</td>
          <td>{{ room.Equip }}</td>
          <td>{{ room.State }}</td>
          <td>{{ room.Beds }}</td>
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
      rooms: [] // pole na uchovávanie údajov
    };
  },
  mounted() {
    this.fetchRooms(); // Volanie funkcie na načítanie údajov po načítaní komponentu
  },
  methods: {
    fetchRooms() {
      fetch('/Home/Rooms') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          this.rooms = data; // Nastavenie údajov do premennej rooms
        })
        .catch(error => {
          console.error('Error fetching rooms:', error);
        });
    }
  }
};
</script>
