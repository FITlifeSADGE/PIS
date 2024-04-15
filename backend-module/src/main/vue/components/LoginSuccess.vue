<template>
    <div>
      <h1>Dobrej login bráško</h1>
      <router-link to="/Home/login">
        <button>LogOut</button>
        </router-link>
    </div>

    <!-- <div>
    <h2>List of Customers</h2>
    <ul>
      <li v-for="customer in customers" :key="customer.CustomerID">
        {{ customer.FirstName }} {{ customer.LastName }} - {{ customer.Email }}
      </li>
    </ul>
  </div> -->

  </template>
  
  <script>
  export default {
  }
  </script>
  
  <style scoped>
  /* Styly můžete přidat podle vašich preferencí */
  h1 {
    font-size: 24px;
    color: #333;
  }
  
  button {
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  </style>

<!-- <script>
import axios from 'axios';

export default {
  data() {
    return {
      customers: []
    };
  },
  mounted() {
    // axios.get('http://localhost:9080/api/customers')
      .then(response => {
        this.customers = response.data;
      })
      .catch(error => {
        console.error('Error fetching customers:', error);
      });
  }
};
</script> -->