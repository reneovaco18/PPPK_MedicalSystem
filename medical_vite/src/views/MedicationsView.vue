<template>
  <div>
    <h2>Medications</h2>
    <button @click="toggleCreateForm">
      {{ showCreateForm ? 'Cancel' : 'Create New Medication' }}
    </button>
    <div v-if="showCreateForm">
      <medication-form @closeForm="toggleCreateForm" @refreshList="fetchMedications" />
    </div>
    <h3>All Medications</h3>
    <ul>
      <li v-for="med in medications" :key="med.id">
        (Patient ID: {{ med.patient?.id }}) {{ med.name }} - {{ med.dosage }}
        <button @click="editMedication(med)">Edit</button>
        <button @click="deleteMedication(med.id)">Delete</button>
      </li>
    </ul>
    <div v-if="selectedMedication">
      <h3>Edit Medication</h3>
      <medication-form :existingMedication="selectedMedication" @closeForm="selectedMedication = null" @refreshList="fetchMedications" />
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
    toggleCreateForm() {
      this.showCreateForm = !this.showCreateForm;
    },
    editMedication(med) {
      this.selectedMedication = {...med};
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
