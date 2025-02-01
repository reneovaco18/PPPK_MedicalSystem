import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index.js';
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';
import './style.css';

const app = createApp(App);

app.use(router);
app.use(Toast, {
    timeout: 3000,
    position: 'top-right',
});

app.mount('#app');

// Expose the toast instance globally for use in non-component files (such as axios interceptors)
window.$toast = app.config.globalProperties.$toast;
