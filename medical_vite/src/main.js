import { createApp } from 'vue';
import App from './App.vue'; // or './App.vue' if you keep it in src/
import router from './router/index.js';
import './style.css';

createApp(App).use(router).mount('#app');
