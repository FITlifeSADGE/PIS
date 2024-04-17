import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginPage from './components/LoginPage.vue';
import HomePage from './components/HomePage.vue';
import LoginSuccess from './components/LoginSuccess.vue';
import RoomsPage from './components/RoomsPage.vue';
import ServicesPage from './components/ServicesPage.vue';
import Parent from './components/Parent.vue';

import { library } from '@fortawesome/fontawesome-svg-core';
import { faEye,faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faEye, faEyeSlash);
Vue.component('font-awesome-icon', FontAwesomeIcon);

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
      component: Parent,
      children: [
        {
          path: '',
          component: RoomsPage
        }
      ]
    },
    {
      path: '/Home/Services',
      component: Parent,
      children: [
        {
          path: '',
          component: ServicesPage
        }
      ]
    }
  ]
});

new Vue({
  el: '#app',
  router,
  render: h => h('router-view') // Tento řádek říká Vue, aby vykreslovalo komponentu na základě URL adresy.
});
