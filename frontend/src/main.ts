declare module '*.vue'{
    import Vue from 'vue';
    import default Vue;
};

import { createApp } from 'vue'
import App from './App.vue'
import router from './router';

createApp(App).use(router).mount('#app')
