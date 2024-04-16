import Vue from 'vue';
import VueRouter from 'vue-router';
import LoginPage from './components/LoginPage.vue';
import HomePage from './components/HomePage.vue';
import LoginSuccess from './components/LoginSuccess.vue';
import CustomerPage from './components/CustomersPage.vue';
import CustomerDetail from './components/CustomerDetailPage.vue';

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
      path: '/Home/customers',
      component: CustomerPage
    },
    {
      path: '/Home/customers/detail',
      component: CustomerDetail
    }
  ]
});

new Vue({
  el: '#app',
  router,
  render: h => h('router-view') // Tento řádek říká Vue, aby vykreslovalo komponentu na základě URL adresy.
});
