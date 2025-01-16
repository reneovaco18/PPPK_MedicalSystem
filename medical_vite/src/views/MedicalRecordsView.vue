<template>
  <div>
    <h2>Medical Records</h2>
    <button @click="showCreateForm = !showCreateForm">
      {{ showCreateForm ? 'Cancel' : 'Create Medical Record' }}
    </button>

    <div v-if="showCreateForm">
      <medical-record-form
          @closeForm="showCreateForm = false"
          @refreshList="fetchRecordsForPatient"
      />
    </div>

    <h3>Get Records by Patient ID</h3>
    <input v-model="searchPatientId" placeholder="Enter Patient ID" />
    <button @click="fetchRecordsForPatient">Fetch</button>

    <ul>
      <li v-for="record in records" :key="record.id">
        Illness: {{ record.illnessName }} ({{ record.startDate }} - {{ record.endDate || 'Ongoing' }})
        <button @click="editRecord(record)">Edit</button>
        <button @click="deleteRecord(record.id)">Delete</button>
      </li>
    </ul>

    <div v-if="selectedRecord">
      <h3>Edit Medical Record</h3>
      <medical-record-form
          :existingRecord="selectedRecord"
          @closeForm="selectedRecord = null"
          @refreshList="fetchRecordsForPatient"
      />
    </div>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';
import MedicalRecordForm from '../components/MedicalRecordForm.vue';

export default {
  name: 'MedicalRecordsView',
  components: { MedicalRecordForm },
  data() {
    return {
      searchPatientId: '',
      records: [],
      showCreateForm: false,
      selectedRecord: null,
    };
  },
  methods: {
    async fetchRecordsForPatient() {
      if (!this.searchPatientId) return;
      try {
        const res = await axiosClient.get(`/medical-records/patient/${this.searchPatientId}`);
        this.records = res.data;
      } catch (err) {
        console.error(err);
        alert('Error fetching records');
      }
    },
    editRecord(record) {
      this.selectedRecord = { ...record };
    },
    async deleteRecord(id) {
      if (!confirm('Are you sure?')) return;
      try {
        await axiosClient.delete(`/medical-records/${id}`);
        alert('Record deleted');
        this.fetchRecordsForPatient();
      } catch (err) {
        console.error(err);
      }
    },
  },
};
</script>
