import Vue from 'vue';
//import HelloWorld from './components/HelloWorld.vue'; // Import your HelloWorld component
import LoginPage from './components/LoginPage.vue';

new Vue({
  el: '#app',
  render: h => h(LoginPage) // Render your HelloWorld component directly
});
