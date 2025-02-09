import { createRouter, createWebHistory } from 'vue-router';
import PatientsView from '../views/PatientsView.vue';
import ExaminationsView from '../views/ExaminationsView.vue';
import ExaminationDetailsView from '../views/ExaminationDetailsView.vue';
import AppointmentsView from '../views/AppointmentsView.vue';
import MedicalRecordsView from '../views/MedicalRecordsView.vue';
import MedicationsView from '../views/MedicationsView.vue';

const routes = [
    { path: '/', redirect: '/patients' },
    { path: '/medications', component: MedicationsView },
    { path: '/patients', component: PatientsView },
    { path: '/examinations', component: ExaminationsView },
    { path: '/examinations/:id', component: ExaminationDetailsView, name: 'ExaminationDetails' },
    { path: '/appointments', component: AppointmentsView },
    { path: '/medical-records', component: MedicalRecordsView },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
