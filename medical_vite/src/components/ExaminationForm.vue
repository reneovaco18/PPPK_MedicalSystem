<template>
  <div>
    <h3>{{ isEditMode ? 'Edit Examination' : 'Create Examination' }}</h3>
    <form @submit.prevent="saveExamination">
      <!-- Patient ID -->
      <div>
        <label>Patient ID:</label>
        <input type="number" v-model="examinationData.patientId" required />
      </div>

      <!-- Examination Type -->
      <div>
        <label>Type:</label>
        <input v-model="examinationData.type" required />
      </div>

      <!-- Date/Time -->
      <div>
        <label>Date/Time:</label>
        <input type="datetime-local" v-model="examinationData.dateTime" required />
      </div>

      <button type="submit">Save</button>
      <button type="button" @click="$emit('closeForm')">Cancel</button>
    </form>

    <!-- File Upload (Only in Edit Mode) -->
    <div v-if="isEditMode">
      <h4>Upload Images</h4>
      <input type="file" multiple @change="onFileChange" />
      <button @click="uploadFiles" :disabled="selectedFiles.length === 0">Upload</button>

      <!-- Show Preview of Selected Files -->
      <div v-if="selectedFiles.length > 0">
        <h5>Selected Files:</h5>
        <ul>
          <li v-for="file in selectedFiles" :key="file.name">{{ file.name }}</li>
        </ul>
      </div>

      <!-- Show Previously Uploaded Files -->
      <div v-if="existingFiles.length > 0">
        <h4>Uploaded Images:</h4>
        <div v-for="file in existingFiles" :key="file.id">
          <img :src="file.filePath" alt="Examination Image" style="max-width: 200px; display: block; margin-bottom: 10px;" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';

export default {
  name: 'ExaminationForm',
  props: {
    existingExamination: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      selectedFiles: [],
      existingFiles: [],
      examinationData: {
        patientId: null,
        type: '',
        dateTime: '',
      },
    };
  },
  computed: {
    isEditMode() {
      return !!this.existingExamination;
    },
  },
  mounted() {
    if (this.isEditMode) {
      // Populate form fields if editing
      this.examinationData = {
        patientId: this.existingExamination.patient?.id,
        type: this.existingExamination.type,
        dateTime: this.existingExamination.dateTime,
      };

      // Load existing uploaded files
      this.fetchExistingFiles();
    }
  },
  methods: {
    async saveExamination() {
      try {
        const payload = {
          patient: { id: this.examinationData.patientId },
          type: this.examinationData.type,
          dateTime: this.examinationData.dateTime,
        };

        if (this.isEditMode) {
          await axiosClient.put(`/examinations/${this.existingExamination.id}`, payload);
          alert('Examination updated');
        } else {
          await axiosClient.post('/examinations', payload);
          alert('Examination created');
        }
        this.$emit('refreshList');
        this.$emit('closeForm');
      } catch (err) {
        console.error(err);
        alert('Error saving examination');
      }
    },

    async fetchExistingFiles() {
      try {
        const res = await axiosClient.get(`/examinations/${this.existingExamination.id}`);
        if (res.data.files) {
          this.existingFiles = res.data.files;
        }
      } catch (err) {
        console.error('Error fetching existing files:', err);
      }
    },

    onFileChange(event) {
      this.selectedFiles = Array.from(event.target.files); // Convert FileList to Array
    },

    async uploadFiles() {
      if (!this.selectedFiles.length || !this.existingExamination) return;
      const formData = new FormData();
      this.selectedFiles.forEach(file => formData.append('files', file));

      try {
        await axiosClient.post(`/examination-files/${this.existingExamination.id}/upload-multiple`, formData, {
          headers: {'Content-Type': 'multipart/form-data'},
        });

        alert('Files uploaded successfully');
        this.fetchExistingFiles(); // Refresh uploaded images
      } catch (err) {
        console.error(err);
        alert('Error uploading files');
      }
    },
  },
};
</script>
