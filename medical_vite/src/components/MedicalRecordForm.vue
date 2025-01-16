<template>
  <div>
    <h3>{{ isEditMode ? 'Edit Medical Record' : 'Create Medical Record' }}</h3>
    <form @submit.prevent="saveRecord">
      <div>
        <label>Patient ID:</label>
        <input type="number" v-model="recordData.patientId" required/>
      </div>
      <div>
        <label>Illness Name:</label>
        <input v-model="recordData.illnessName" required/>
      </div>
      <div>
        <label>Start Date:</label>
        <input type="date" v-model="recordData.startDate" required/>
      </div>
      <div>
        <label>End Date (optional):</label>
        <input type="date" v-model="recordData.endDate"/>
      </div>
      <button type="submit">Save</button>
      <button type="button" @click="$emit('closeForm')">Cancel</button>
    </form>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';

export default {
  name: 'MedicalRecordForm',
  props: {
    existingRecord: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      recordData: {
        patientId: '',
        illnessName: '',
        startDate: '',
        endDate: '',
      },
    };
  },
  computed: {
    isEditMode() {
      return !!this.existingRecord;
    },
  },
  mounted() {
    if (this.isEditMode) {
      // Fill form with existing data
      this.recordData = {
        patientId: this.existingRecord.patient?.id, // from back-end
        illnessName: this.existingRecord.illnessName,
        startDate: this.existingRecord.startDate,
        endDate: this.existingRecord.endDate,
      };
    }
  },
  methods: {
    async saveRecord() {
      try {
        // Reconstruct payload
        const payload = {
          patient: { id: this.recordData.patientId },
          illnessName: this.recordData.illnessName,
          startDate: this.recordData.startDate,
          endDate: this.recordData.endDate || null,
        };

        if (this.isEditMode) {
          await axiosClient.put(`/medical-records/${this.existingRecord.id}`, payload);
          alert('Medical record updated');
        } else {
          await axiosClient.post('/medical-records', payload);
          alert('Medical record created');
        }

        this.$emit('refreshList');
        this.$emit('closeForm');
      } catch (err) {
        console.error(err);
        alert('Error saving record');
      }
    },
  },
};
</script>
