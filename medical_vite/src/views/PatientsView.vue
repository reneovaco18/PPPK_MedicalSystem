<template>
  <div>
    <h2>Patients</h2>

    <!-- Button to show/hide the create form -->
    <button @click="showCreateForm = !showCreateForm">
      {{ showCreateForm ? 'Cancel' : 'Create New Patient' }}
    </button>

    <!-- New Patient Form -->
    <div v-if="showCreateForm">
      <patient-form
          @closeForm="showCreateForm = false"
          @refreshList="fetchPatients"
      />
    </div>

    <!-- CSV Export -->
    <button @click="downloadCSV">Export Patients CSV</button>

    <!-- OIB Search -->
    <div>
      <h3>Search by OIB</h3>
      <input v-model="searchOib" placeholder="Enter OIB" />
      <button @click="searchByOib">Search</button>
      <div v-if="foundPatient">
        Found patient: {{ foundPatient.firstName }} {{ foundPatient.lastName }}
        <button @click="clearSearchOib">Clear</button>
      </div>
    </div>

    <!-- Last Name Search -->
    <div>
      <h3>Search by Last Name</h3>
      <input v-model="searchLastName" placeholder="Enter last name" />
      <button @click="searchByLastName">Search</button>
      <div v-if="foundPatientsByLastName.length > 0">
        <h4>Found patient(s):</h4>
        <ul>
          <li v-for="p in foundPatientsByLastName" :key="p.id">
            {{ p.firstName }} {{ p.lastName }} (OIB: {{ p.oib }}, ID: {{ p.id }})
          </li>
        </ul>
        <button @click="clearSearchLastName">Clear</button>
      </div>
    </div>

    <!-- All Patients -->
    <h3>All Patients</h3>
    <ul>
      <li v-for="p in patients" :key="p.id">
        <!-- SHOW PATIENT ID HERE -->
        (ID: {{ p.id }}) {{ p.firstName }} {{ p.lastName }} (OIB: {{ p.oib }})
        <button @click="editPatient(p)">Edit</button>
        <button @click="deletePatient(p.id)">Delete</button>
      </li>
    </ul>

    <!-- Edit Form -->
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
import PatientForm from '../components/PatientForm.vue';

export default {
  name: 'PatientsView',
  components: { PatientForm },
  data() {
    return {
      showCreateForm: false,
      patients: [],
      selectedPatient: null,

      // OIB
      searchOib: '',
      foundPatient: null,

      // Last Name
      searchLastName: '',
      foundPatientsByLastName: [],
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
      this.selectedPatient = { ...patient };
    },
    downloadCSV() {
      axiosClient
          .get('/patients/export', { responseType: 'blob' })
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data], { type: 'text/csv' }));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'patients.csv');
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
          })
          .catch(error => {
            console.error('CSV Download error:', error);
          });
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
    // OIB
    async searchByOib() {
      if (!this.searchOib) return;
      try {
        const res = await axiosClient.get(`/patients/search/oib?oib=${this.searchOib}`);
        this.foundPatient = res.data;
      } catch (error) {
        alert('Patient not found or error occurred');
      }
    },
    clearSearchOib() {
      this.foundPatient = null;
      this.searchOib = '';
    },
    // Last Name
    async searchByLastName() {
      if (!this.searchLastName) return;
      try {
        const res = await axiosClient.get(`/patients/search/lastName?lastName=${this.searchLastName}`);
        this.foundPatientsByLastName = res.data;
      } catch (error) {
        alert('No patients found or error occurred');
      }
    },
    clearSearchLastName() {
      this.foundPatientsByLastName = [];
      this.searchLastName = '';
    },
  },
  mounted() {
    this.fetchPatients();
  },
};
</script>
