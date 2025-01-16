import { createRouter, createWebHistory } from 'vue-router';
import PatientsView from '../views/PatientsView.vue';
import ExaminationsView from '../views/ExaminationsView.vue';
import AppointmentsView from '../views/AppointmentsView.vue';
import MedicalRecordsView from '../views/MedicalRecordsView.vue';

const routes = [
    { path: '/', redirect: '/patients' },
    { path: '/patients', component: PatientsView },
    { path: '/examinations', component: ExaminationsView },
    { path: '/appointments', component: AppointmentsView },
    { path: '/medical-records', component: MedicalRecordsView },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
