<template>
  <div>
    <h2>Patients</h2>

    <!-- Button to show/hide the create form -->
    <button @click="showCreateForm = !showCreateForm">
      {{ showCreateForm ? 'Cancel' : 'Create New Patient' }}
    </button>

    <div v-if="showCreateForm">
      <patient-form
          @closeForm="showCreateForm = false"
          @refreshList="fetchPatients"
      />
    </div>

    <!-- Searching by OIB -->
    <div>
      <h3>Search by OIB</h3>
      <input v-model="searchOib" placeholder="Enter OIB"/>
      <button @click="searchByOib">Search</button>
      <div v-if="foundPatient">
        Found patient: {{ foundPatient.firstName }} {{ foundPatient.lastName }}
        <button @click="clearSearch">Clear</button>
      </div>
    </div>

    <!-- List all patients -->
    <h3>All Patients</h3>
    <ul>
      <li v-for="p in patients" :key="p.id">
        {{ p.firstName }} {{ p.lastName }} (OIB: {{ p.oib }})
        <button @click="editPatient(p)">Edit</button>
        <button @click="deletePatient(p.id)">Delete</button>
      </li>
    </ul>

    <!-- Edit form if selectedPatient is set -->
    <div v-if="selectedPatient">
      <h3>Edit Patient</h3>
      <patient-form
          :existingPatient="selectedPatient"
          @closeForm="selectedPatient = null"
          @refreshList="fetchPatients"
      />
    </div>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';
import PatientForm from '../components/PatientForm.vue'; // or wherever your form is

export default {
  name: 'PatientsView',
  components: { PatientForm },
  data() {
    return {
      showCreateForm: false,
      patients: [],
      selectedPatient: null,
      searchOib: '',
      foundPatient: null,
    };
  },
  methods: {
    async fetchPatients() {
      try {
        const res = await axiosClient.get('/patients');
        this.patients = res.data;
      } catch (err) {
        console.error(err);
      }
    },
    editPatient(patient) {
      // Shallow copy to avoid immediate binding
      this.selectedPatient = { ...patient };
    },
    async deletePatient(patientId) {
      if (!confirm('Are you sure you want to delete this patient?')) return;
      try {
        await axiosClient.delete(`/patients/${patientId}`);
        alert('Patient deleted successfully');
        this.fetchPatients();
      } catch (err) {
        console.error(err);
      }
    },
    async searchByOib() {
      if (!this.searchOib) return;
      try {
        const res = await axiosClient.get(`/patients/search/oib?oib=${this.searchOib}`);
        this.foundPatient = res.data;
      } catch (error) {
        alert('Patient not found or error occurred');
      }
    },
    clearSearch() {
      this.foundPatient = null;
      this.searchOib = '';
    },
  },
  mounted() {
    this.fetchPatients();
  },
};
</script>
