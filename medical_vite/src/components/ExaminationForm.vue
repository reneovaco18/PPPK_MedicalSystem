<template>
  <div>
    <h3>{{ isEditMode ? 'Edit Examination' : 'Create Examination' }}</h3>
    <form @submit.prevent="saveExamination">

      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>


      <div>
        <label>Patient ID:</label>
        <input type="number" v-model.number="examinationData.patientId" required />
      </div>


      <div>
        <label>Type:</label>
        <select v-model="examinationData.type" required>
          <option disabled value="">Please select</option>
          <option v-for="option in examOptions" :key="option" :value="option">
            {{ option }}
          </option>
        </select>
      </div>


      <div>
        <label>Date/Time:</label>
        <input type="datetime-local" v-model="examinationData.dateTime" required />
      </div>

      <button type="submit">Save</button>
      <button type="button" @click="$emit('closeForm')">Cancel</button>
    </form>


    <div v-if="isEditMode">
      <h4>Upload Images</h4>
      <input type="file" multiple @change="onFileChange" />
      <button @click="uploadFiles" :disabled="selectedFiles.length === 0">Upload</button>


      <div v-if="selectedFiles.length > 0">
        <h5>Selected Files:</h5>
        <ul>
          <li v-for="file in selectedFiles" :key="file.name">{{ file.name }}</li>
        </ul>
      </div>


      <div v-if="existingFiles.length > 0">
        <h4>Uploaded Images:</h4>
        <div v-for="file in existingFiles" :key="file.id">
          <img :src="file.filePath" alt="Examination Image" class="exam-image" />
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
      errorMessage: '',
      selectedFiles: [],
      existingFiles: [],
      examOptions: [
        'GP', 'KRV', 'X_RAY', 'CT', 'MR', 'ULTRA', 'EKG', 'ECHO', 'EYE', 'DERM', 'DENTA', 'MAMMO', 'NEURO'
      ],
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
      this.examinationData = {
        patientId: this.existingExamination.patient?.id,
        type: this.existingExamination.type,
        dateTime: this.existingExamination.dateTime,
      };
      this.fetchExistingFiles();
    }
  },
  methods: {
    async saveExamination() {
      this.errorMessage = '';
      try {
        const payload = {
          patient: { id: this.examinationData.patientId },
          type: this.examinationData.type,
          dateTime: this.examinationData.dateTime,
        };

        if (this.isEditMode) {
          await axiosClient.put(`/examinations/${this.existingExamination.id}`, payload);
          this.$emit('refreshList');
          this.$emit('closeForm');
        } else {
          await axiosClient.post('/examinations', payload);
          this.$emit('refreshList');
          this.$emit('closeForm');
        }
      } catch (err) {
        console.error(err);
        this.errorMessage = err.response?.data?.message || 'Error saving examination';
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
      this.selectedFiles = Array.from(event.target.files);
    },
    async uploadFiles() {
      if (!this.selectedFiles.length || !this.existingExamination) return;
      const formData = new FormData();
      this.selectedFiles.forEach(file => formData.append('files', file));
      try {
        await axiosClient.post(`/examination-files/${this.existingExamination.id}/upload-multiple`, formData, {
          headers: {'Content-Type': 'multipart/form-data'},
        });
        await this.fetchExistingFiles();
        this.selectedFiles = [];
      } catch (err) {
        console.error(err);
        this.errorMessage = err.response?.data?.message || 'Error uploading files';
      }
    },
  },
};
</script>
