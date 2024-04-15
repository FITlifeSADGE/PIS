<template>
    <div>
      <h1>Vítejte na homepage</h1>
      <router-link to="/Home/pokus">
        <button>LogIn</button>
        </router-link>
    </div>
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
  