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


window.$toast = app.config.globalProperties.$toast;
