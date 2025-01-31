<template>
  <div>
    <h2>Medications</h2>

    <!-- Button to show/hide the create form -->
    <button @click="showCreateForm = !showCreateForm">
      {{ showCreateForm ? 'Cancel' : 'Create New Medication' }}
    </button>

    <div v-if="showCreateForm">
      <medication-form
          @closeForm="showCreateForm = false"
          @refreshList="fetchMedications"
      />
    </div>

    <!-- Search by Patient ID -->
    <div>
      <h3>Search Medications by Patient ID</h3>
      <input v-model="searchPatientId" placeholder="Enter Patient ID" />
      <button @click="fetchMedicationsByPatient">Search</button>
    </div>

    <!-- Display Medications -->
    <h3>All Medications</h3>
    <ul>
      <li v-for="med in medications" :key="med.id">
        (Patient ID: {{ med.patient?.id }}) {{ med.name }} - {{ med.dosage }}
        <button @click="editMedication(med)">Edit</button>
        <button @click="deleteMedication(med.id)">Delete</button>
      </li>
    </ul>

    <!-- Edit Medication -->
    <div v-if="selectedMedication">
      <h3>Edit Medication</h3>
      <medication-form
          :existingMedication="selectedMedication"
          @closeForm="selectedMedication = null"
          @refreshList="fetchMedications"
      />
    </div>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';
import MedicationForm from '../components/MedicationForm.vue';

export default {
  name: 'MedicationsView',
  components: { MedicationForm },
  data() {
    return {
      medications: [],
      showCreateForm: false,
      selectedMedication: null,
      searchPatientId: '',
    };
  },
  methods: {
    async fetchMedications() {
      try {
        const res = await axiosClient.get('/medications');
        this.medications = res.data;
      } catch (err) {
        console.error(err);
      }
    },
    async fetchMedicationsByPatient() {
      if (!this.searchPatientId) return;
      try {
        const res = await axiosClient.get(`/medications/patient/${this.searchPatientId}`);
        this.medications = res.data;
      } catch (err) {
        console.error(err);
      }
    },
    editMedication(med) {
      this.selectedMedication = { ...med };
    },
    async deleteMedication(id) {
      if (!confirm('Are you sure?')) return;
      try {
        await axiosClient.delete(`/medications/${id}`);
        alert('Medication deleted');
        this.fetchMedications();
      } catch (err) {
        console.error(err);
      }
    },
  },
  mounted() {
    this.fetchMedications();
  },
};
</script>
