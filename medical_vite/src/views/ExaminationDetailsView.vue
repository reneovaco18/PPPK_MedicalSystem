<template>
  <div v-if="examination">
    <h2>Examination #{{ examination.id }} Details</h2>
    <p><strong>Type:</strong> {{ examination.type }}</p>
    <p><strong>Date/Time:</strong> {{ formattedDateTime }}</p>
    <p><strong>Patient ID:</strong> {{ examination.patient?.id || 'N/A' }}</p>
    <p><strong>Patient Name:</strong>
      {{ examination.patient?.firstName || examination.patient?.name || 'Unknown' }}
      {{ examination.patient?.lastName || '' }}
    </p>

    <h3>Attached Images</h3>
    <div v-if="examination.files && examination.files.length > 0">
      <div v-for="file in examination.files" :key="file.id" class="image-container">
        <img
            v-if="file.filePath"
            :src="getImageUrl(file.filePath)"
            alt="Examination Image"
            class="exam-image"
        />
      </div>
    </div>
    <div v-else>
      <p>No images attached.</p>
    </div>

    <button @click="goBack">Back to Examinations</button>
  </div>
  <div v-else>
    <p>Loading examination details...</p>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';

export default {
  name: 'ExaminationDetailsView',
  data() {
    return {
      examination: null,
    };
  },
  computed: {
    formattedDateTime() {
      if (!this.examination || !this.examination.dateTime) return '';
      const date = new Date(this.examination.dateTime);
      return date.toLocaleString();
    },
  },
  methods: {
    async fetchExamination() {
      const { id } = this.$route.params;
      try {
        const res = await axiosClient.get(`/examinations/${id}`);
        this.examination = res.data;
      } catch (err) {
        console.error('Error fetching examination details:', err);
        alert('Failed to load examination details.');
        this.$router.push('/examinations');
      }
    },
    goBack() {
      this.$router.push('/examinations');
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
    this.fetchExamination();
  },
};
</script>

<style scoped>
.image-container {
  margin-bottom: 15px;
}
.exam-image {
  max-width: 400px;
  height: auto;
  border: 1px solid #ccc;
  padding: 5px;
}
</style>
