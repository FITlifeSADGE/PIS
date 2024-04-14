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
                <input type="password" id="password" v-model="password" required class="form-input"> 
            </div> 
            <button type="submit" class="login-button">Login</button> 
        </form>
        <p v-if="loginMessage">{{ loginMessage }}</p> <!-- Zobrazit zprávu o přihlášení -->
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
</style> 

<script>
export default {
  data() {
    return {
      email: '',
      password: '',
      loginMessage: '' // Přidána data pro zprávu o přihlášení
    };
  },
  methods: {
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
          this.$router.push('/Home/success');
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
