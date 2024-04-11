import Vue from 'vue';
import HelloWorld from './components/HelloWorld.vue'; // Import your HelloWorld component

new Vue({
  el: '#app',
  render: h => h(HelloWorld) // Render your HelloWorld component directly
});
