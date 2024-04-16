<template>
  <div class="Rooms">
    <div class="header">
      <h1>Home Page</h1>
      <div class="logout">
        <button class="button" @click="logout">Log out</button>
      </div>
    </div>
    <div class="content">
      <div class="toolbar">
        <button class="button" @click="manageHotelServices">Hotel Services</button>
        <button class="button" @click="createReservation">Create Reservation</button>
        <button class="button" @click="viewReservations">View Reservations</button>
        <button class="button" @click="viewCustomers">View Customers</button>
      </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>Type</th>
              <th>Cost</th>
              <th>Equipment</th>
              <th>State</th>
              <th>Beds</th>
              <th>Edit</th>
            </tr>
          </thead>
          <tbody v-if="rooms && rooms.length > 0">
            <tr v-for="room in rooms" :key="room.RoomID">
              <td v-if="!room.editable">{{ room.TypeRoom }}</td>
              <td v-else><input type="text" v-model="room.TypeRoom" :style="{ width: getRoomInputWidth(room.TypeRoom) }"></td>
              <td v-if="!room.editable">{{ room.Cost }}</td>
              <td v-else><input type= number min="0" v-model="room.Cost" :style="{ width: '50px' }"></td>
              <td v-if="!room.editable">{{ room.Equip }}</td>
              <td v-else><input type="text" v-model="room.Equip" :style="{ width: getRoomInputWidth(room.Equip) }"></td>
              <td v-if="!room.editable">{{ room.State }}</td>
              <td v-else> 
                <select v-model="room.State" :style="{ width: '100px' }">
                  <option value="Available">Available</option>
                  <option value="Occupied">Occupied</option>
                </select>
              </td>
              <td v-if="!room.editable">{{ room.Beds }}</td>
              <td v-else><input type= number min="1" v-model="room.Beds" :style="{ width: '50px' }"></td>
              <td>
              <button v-if="!room.editable" class="edit-button" @click="toggleEdit(room)">Edit</button>
              <button v-else class="ok-button" @click="updateRoom(room)">OK</button>
              <button v-if="room.editable" class="delete-button" @click="deleteRoom(room)">Delete</button>  
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
      rooms: [] // pole na uchovávanie údajov
    };
  },
  mounted() {
    this.fetchRooms(); // Volanie funkcie na načítanie údajov po načítaní komponentu
  },
  methods: {
    fetchRooms() {
      fetch('/Home/Rooms/GetRooms') // Zavolanie vášho servletu, ktorý vráti údaje z databázy
        .then(response => response.json())
        .then(data => {
          this.rooms = data.map(room => ({ ...room, editable: false })); // Nastavenie údajov do premennej rooms
        })
        .catch(error => {
          console.error('Error fetching rooms:', error);
        });
    },
    logout() {
      console.log('Logout');
    },
    manageHotelServices() {
      this.$router.push('/Home/Services');
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
    toggleEdit(room) {
      room.editable = !room.editable; // Prepnutie hodnoty editable
    },

    updateRoom(room) {
      // Implementácia aktualizácie služby
      console.log('Updating room:', room);
      room.editable = false; // Zatvorenie editovacieho režimu

      // Odoslanie údajov na server
      fetch('/Home/UpdateRoom', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(room),
      })
      .then(response => {
        if (response.ok) {
          console.log('Room updated successfully');
        } else {
          throw new Error('Failed to update Room');
        }
      })
      .catch(error => {
        console.error('Error updating room:', error);
      });
    },


    
    deleteRoom(room) {
      // Implementácia odstránenia služby
      console.log('Deleting room:', room);
    },
    getRoomInputWidth(text) {
      // Funkcia na získanie šírky textového poľa na základe dĺžky textu
      return text ? `${text.length * 12}px` : '100px'; // 8px na jeden znak, predvolená šírka je 100px
    }
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
  color: #2196F3;
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
