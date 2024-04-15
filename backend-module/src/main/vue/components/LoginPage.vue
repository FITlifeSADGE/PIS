<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

<template>
  <div class="login-container">
    <h2 class="login-title">Login</h2>
    <form @submit.prevent="login" class="login-form">
      <div class="form-field">
        <label for="username" class="form-label">Username:</label>
        <input type="email" id="username" v-model="email" required class="form-input" placeholder="example@example.com">
      </div>
      <div class="form-field">
        <label for="password" class="form-label">Password:</label>
        <input :type="passwordFieldType" id="password" v-model="password" required class="form-input">
      </div>
      <button type="button" @mousedown="switchVisibility" @mouseup="hide" class="login-button">Show Password</button>
      <span class="display-eye" @click="switchVisibility">
        <i :class="passwordFieldType === 'password' ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
      </span>
      <button type="submit" class="login-button">Login</button>
    </form>
    <p v-if="loginMessage">{{ loginMessage }}</p>
  </div>
</template>

<style scoped> 
.login-container { display: flex; flex-direction: column; align-items: center; margin-top: 100px; } 
.login-title { font-size: 2rem; margin-bottom: 30px; color: #333; } 
.login-form { display: flex; flex-direction: column; width: 300px; } 
.form-field { margin-bottom: 15px; } .form-label { font-size: 0.9rem; margin-bottom: 5px; color: #666; } 
.form-input { padding: 10px; border: 1px solid #ccc; border-radius: 3px; font-size: 0.9rem; } 
.login-button { padding: 10px; background-color: #4CAF50; color: white; border: none; border-radius: 3px; font-size: 0.9rem; cursor: pointer; } 
.login-button:hover { background-color: #45a049; } 
.display-eye {
  /* position: relative; */
  cursor: pointer;
}

.display-eye i {
  font-size: 30px;
}
</style> 

<script>
export default {
data() {
  return {
    email: '',
    password: '',
    loginMessage: '', // Přidána data pro zprávu o přihlášení
    passwordFieldType: 'password'
  };
},
methods: {
  switchVisibility(){
    this.passwordFieldType = this.passwordFieldType === 'password' ? 'text' : 'password';
  },
  hide(){
    this.passwordFieldType = this.passwordFieldType === 'text' ? 'password' : 'text';
  },
  login() {
    fetch('/Home/loginVerify', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username: this.email,
        password: this.password
      })
    })
    .then(response => {
      if (response.ok) {
        console.log('Login successful');
        this.$router.push('/Home/Rooms');
      } else {
        throw new Error('Login failed');
      }
    })
    .then(data => {
      // Zpracovat úspěšnou odpověď
      this.loginMessage = data;
    })
    .catch(error => {
      console.error('Error during login:', error);
      this.loginMessage = 'Špatný email nebo heslo'; // Pokud se nepodaří spojit nebo obdržet odpověď
    });
  }
}
};
</script>
