<template>
  <div>
    <h2>Examinations</h2>
    <button @click="showCreateForm = !showCreateForm">
      {{ showCreateForm ? 'Cancel' : 'Create New Examination' }}
    </button>

    <div v-if="showCreateForm">
      <examination-form
          @closeForm="showCreateForm = false"
          @refreshList="fetchExaminations"
      />
    </div>

    <!-- Search Examinations by Patient ID -->
    <div>
      <h3>Search Examinations by Patient ID</h3>
      <input v-model="searchPatientId" placeholder="Enter Patient ID" />
      <button @click="searchByPatientId">Search</button>

      <div v-if="searchResults.length > 0">
        <h4>Search Results:</h4>
        <ul>
          <li v-for="exam in searchResults" :key="exam.id">
            <strong>Type:</strong> {{ exam.type }} /
            <strong>Date:</strong> {{ exam.dateTime }}
            <router-link :to="{ name: 'ExaminationDetails', params: { id: exam.id } }">
              View Details
            </router-link>
          </li>
        </ul>
      </div>
    </div>

    <h3>All Examinations</h3>
    <ul>
      <li v-for="exam in examinations" :key="exam.id">
        <strong>Type:</strong> {{ exam.type }} -
        <strong>Date:</strong> {{ exam.dateTime }}
        <!-- Fix patient name display -->
        (Patient ID: {{ exam.patient?.id || 'N/A' }},
        Name: {{ exam.patient?.firstName || exam.patient?.name || 'Unknown' }} {{ exam.patient?.lastName || '' }})

        <!-- Link to the details page -->
        <router-link :to="{ name: 'ExaminationDetails', params: { id: exam.id } }">
          View Details
        </router-link>

        <button @click="editExamination(exam)">Edit</button>
        <button @click="deleteExamination(exam.id)">Delete</button>

        <!-- Show images if available -->
        <div v-if="exam.files && exam.files.length > 0">
          <h4>Uploaded Images:</h4>
          <div v-for="file in exam.files" :key="file.id">
            <img
                v-if="file.filePath"
                :src="getImageUrl(file.filePath)"
                alt="Examination Image"
                style="max-width: 200px; display: block; margin-bottom: 10px;"
            />
          </div>
        </div>
      </li>
    </ul>

    <div v-if="selectedExamination">
      <h3>Edit Examination</h3>
      <examination-form
          :existingExamination="selectedExamination"
          @closeForm="selectedExamination = null"
          @refreshList="fetchExaminations"
      />
    </div>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';
import ExaminationForm from '../components/ExaminationForm.vue';

export default {
  name: 'ExaminationsView',
  components: { ExaminationForm },
  data() {
    return {
      examinations: [],
      showCreateForm: false,
      selectedExamination: null,
      searchPatientId: '',
      searchResults: [],
    };
  },
  methods: {
    async fetchExaminations() {
      try {
        const res = await axiosClient.get('/examinations');
        this.examinations = res.data;
      } catch (err) {
        console.error(err);
        alert('Failed to fetch examinations.');
      }
    },
    editExamination(exam) {
      this.selectedExamination = { ...exam };
    },
    async deleteExamination(examId) {
      if (!confirm('Are you sure you want to delete this examination?')) return;
      try {
        await axiosClient.delete(`/examinations/${examId}`);
        alert('Examination deleted successfully.');
        this.fetchExaminations();
      } catch (err) {
        console.error(err);
        alert('Failed to delete examination.');
      }
    },
    async searchByPatientId() {
      if (!this.searchPatientId) return;
      try {
        const res = await axiosClient.get(`/examinations/patient/${this.searchPatientId}`);
        this.searchResults = res.data;
      } catch (err) {
        console.error(err);
        alert('Error searching by patient ID.');
      }
    },
    getImageUrl(filePath) {
      if (!filePath) return '';
      if (filePath.startsWith('http')) {
        return filePath;
      }
      return `https://your-backend-url.com/${filePath}`;
    },
  },
  mounted() {
    this.fetchExaminations();
  },
};
</script>
