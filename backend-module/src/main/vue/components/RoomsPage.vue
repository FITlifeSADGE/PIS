<template>
  <div class="table-container">
    <table>
      <thead>
        <tr>
          <th>Type</th>
          <th>Cost</th>
          <th>Equip</th>
          <th>State</th>
          <th>Beds</th>
          <th>Edit</th>
        </tr>
      </thead>
      <tbody>

        <!-- Add new -->
        <tr v-if="addingNew">
          <td><input type="text" v-model="newRoom.TypeRoom" ></td>
          <td><input type=number min="1" v-model="newRoom.Cost" ></td>
          <td><input type="text" v-model="newRoom.Equip"></td>
          <td> 
            <select v-model="newRoom.State" :style="{ width: '130px' }" >
              <option value="Available">Available</option>
              <option value="Occupied">Occupied</option>
            </select>
          </td>
          <td><input type=number min="1" v-model="newRoom.Beds"></td>
          <td>
            <button @click="addNewRoom" class="edit-button" >OK</button>
            <button @click="cancelNewRoom" class="delete-button">Cancel</button>
          </td>
        </tr>
        <tr v-else>
          <td colspan="5" style="text-align: center;">
            <button @click="toggleAddNew" class="edit-button">Add New</button>
          </td>
        </tr>

        <!-- Filter row -->
        <tr>
          <td><input type="text" v-model="filters.TypeRoom"></td>
          <td><input type="text" v-model="filters.Cost"></td>
          <td><input type="text" v-model="filters.Equip"></td>
          <td> 
            <select v-model="filters.State" :style="{ width: '130px' }">
              <option value="Available">Available</option>
              <option value="Occupied">Occupied</option>
              <option value="">Do Not Index</option>
            </select>
          </td>
          <td><input type="text" v-model="filters.Beds"></td>
          <td></td> <!-- Empty cell for buttons -->
        </tr>

        <!-- Data rows -->
        <tr v-for="room in filteredRooms" :key="room.RoomID">
          <td v-if="!room.editable">{{ room.TypeRoom }}</td>
          <td v-else><input type="text" v-model="room.TypeRoom" :style="{ width: getRoomInputWidth(room.TypeRoom) }"></td>
          <td v-if="!room.editable">{{ room.Cost }}</td>
          <td v-else><input type= number min="0" v-model="room.Cost" :style="{ width: '50px' }"></td>
          <td v-if="!room.editable">{{ room.Equip }}</td>
          <td v-else><input type="text" v-model="room.Equip" :style="{ width: getRoomInputWidth(room.Equip) }"></td>
          <td v-if="!room.editable">{{ room.State }}</td>
          <td v-else> 
            <select v-model="room.State" :style="{ width: '130px' }">
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
</template>

<script>
import Parent from './Parent.vue';

export default {
  data() {
    return {
      rooms: [], // pole na uchovávanie údajov
      filters: {
        TypeRoom: '',
        Cost: '',
        Equip: '',
        State: '',
        Beds: ''
      },
      addingNew: false,
      newRoom: {
        TypeRoom: 'Double',
        Cost: '15',
        Equip: 'Desk, Chair, Wardrobe, Bed',
        State: 'Available',
        Beds: '2'
      }
    };
  },
  computed: {
    filteredRooms() {
      // Filter rooms based on filter criteria
      return this.rooms.filter(room => {
        return (
          room.TypeRoom.toLowerCase().includes(this.filters.TypeRoom.toLowerCase()) &&
          room.Cost.toString().includes(this.filters.Cost) &&
          room.Equip.toLowerCase().includes(this.filters.Equip.toLowerCase()) &&
          room.State.toLowerCase().includes(this.filters.State.toLowerCase()) &&
          room.Beds.toString().includes(this.filters.Beds)
        );
      });
    }
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
      console.log('Deleting room:', room);
      room.editable = false; // Zatvorenie editovacieho režimu

      const index = this.rooms.indexOf(room);
      if (index !== -1) 
      {
        this.rooms.splice(index, 1);
      }
      fetch('/Home/DeleteRoom', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(room),
      })
      .then(response => {
        if (response.ok) {
          console.log('Room deleted successfully');
        } else {
          throw new Error('Failed to delete Room');
        }
      })
      .catch(error => {
        console.error('Error deleting room:', error);
      });
    },

    getRoomInputWidth(text) {
      // Funkcia na získanie šírky textového poľa na základe dĺžky textu
      return text ? `${text.length * 12}px` : '100px'; // 8px na jeden znak, predvolená šírka je 100px
    },
    toggleAddNew() {
      this.addingNew = true;
    },
    addNewRoom() 
    {
      if (
      this.newRoom.TypeRoom &&
      this.newRoom.Cost &&
      this.newRoom.Equip &&
      this.newRoom.State &&
      this.newRoom.Beds
      ) {

      this.rooms.push({ ...this.newRoom, editable: false });
      fetch('/Home/AddRoom', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.newRoom),
      })
      .then(response => {
        if (response.ok) {
          console.log('Room added successfully');
        } else {
          throw new Error('Failed to add room');
        }
      })
      .catch(error => {
        console.error('Error adding room:', error);
      });

      this.newRoom = {
        TypeRoom: 'Double',
        Cost: '15',
        Equip: 'Desk, Chair, Wardrobe, Bed',
        State: 'Available',
        Beds: '2'
      };
      this.addingNew = false;
      } 
      else 
      {
        alert('Fill in all fields for new Room.');
      }
    },
    cancelNewRoom() {
      this.newRoom = {
        TypeRoom: 'Double',
        Cost: '15',
        Equip: 'Desk, Chair, Wardrobe, Bed',
        State: 'Available',
        Beds: '2'
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
</style>
