import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginPage from './components/LoginPage.vue';
import HomePage from './components/HomePage.vue';
import LoginSuccess from './components/LoginSuccess.vue';
import RoomsPage from './components/RoomsPage.vue';
import ServicesPage from './components/ServicesPage.vue';

Vue.use(VueRouter);

const router = new VueRouter({
  mode: 'history',
  routes: [
    {
      path: '/Home',
      component: HomePage
    },
    {
      path: '/Home/login',
      component: LoginPage
    },
    {
      path: '/Home/success',
      component: LoginSuccess
    },
    {
      path: '/Home/Rooms',
      component: RoomsPage
    },
    {
      path: '/Home/Services',
      component: ServicesPage
    }
  ]
});

new Vue({
  el: '#app',
  router,
  render: h => h('router-view') // Tento řádek říká Vue, aby vykreslovalo komponentu na základě URL adresy.
});
