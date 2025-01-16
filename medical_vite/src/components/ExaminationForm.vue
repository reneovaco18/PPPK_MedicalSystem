<template>
  <div>
    <h3>{{ isEditMode ? 'Edit Examination' : 'Create Examination' }}</h3>
    <form @submit.prevent="saveExamination">
      <!-- Patient ID (assuming user enters it or select from dropdown if you want) -->
      <div>
        <label>Patient ID:</label>
        <input type="number" v-model="examinationData.patientId" required />
      </div>

      <!-- Examination Type (GP, KRV, X_RAY, etc.) -->
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

    <!-- Optional file upload (only if isEditMode is true and you want to upload a file) -->
    <div v-if="isEditMode">
      <h4>Upload File for This Examination</h4>
      <input type="file" @change="onFileChange" />
      <button @click="uploadFile" :disabled="!selectedFile">Upload</button>
    </div>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';

// Notice we must pass "patient" as an object in the back-end, but
// for simplicity, we store only the patientId here and reconstruct the object.

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
      selectedFile: null,
      examinationData: {
        patientId: null,
        type: '',
        dateTime: '',
        // filePath is handled separately with upload
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
      // Convert existingExamination into a structure we can save
      // Remember your back-end expects an object like:
      // { patient: { id: X }, type: 'GP', dateTime: ... }
      // We'll store patientId for the form, so let's do that:
      this.examinationData = {
        patientId: this.existingExamination.patient?.id,
        type: this.existingExamination.type,
        dateTime: this.existingExamination.dateTime,
      };
    }
  },
  methods: {
    async saveExamination() {
      try {
        // Reconstruct the JSON structure for back-end
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
    onFileChange(e) {
      this.selectedFile = e.target.files[0];
    },
    async uploadFile() {
      if (!this.selectedFile || !this.existingExamination) return;
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      try {
        await axiosClient.post(`/examinations/${this.existingExamination.id}/upload`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
        alert('File uploaded successfully');
      } catch (err) {
        console.error(err);
        alert('Error uploading file');
      }
    },
  },
};
</script>
