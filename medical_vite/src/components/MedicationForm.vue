<template>
  <div>
    <h3>{{ isEditMode ? 'Edit Medication' : 'Create Medication' }}</h3>
    <form @submit.prevent="saveMedication">
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

      <div>
        <label>Patient ID:</label>
        <input type="number" v-model.number="medicationData.patientId" required />
      </div>

      <div>
        <label>Name:</label>
        <input type="text" v-model="medicationData.name" required />
      </div>

      <div>
        <label>Dosage:</label>
        <input type="text" v-model="medicationData.dosage" required />
      </div>
      <button type="submit">Save</button>
      <button type="button" @click="$emit('closeForm')">Cancel</button>
    </form>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';

export default {
  name: 'MedicationForm',
  props: {
    existingMedication: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      errorMessage: '',
      medicationData: {
        patientId: null,
        name: '',
        dosage: '',
      },
    };
  },
  computed: {
    isEditMode() {
      return !!this.existingMedication;
    },
  },
  mounted() {
    if (this.isEditMode) {
      this.medicationData = {
        patientId: this.existingMedication.patient?.id,
        name: this.existingMedication.name,
        dosage: this.existingMedication.dosage,
      };
    }
  },
  methods: {
    async saveMedication() {
      this.errorMessage = '';
      try {
        const payload = {
          patient: {id: this.medicationData.patientId},
          name: this.medicationData.name,
          dosage: this.medicationData.dosage,
        };
        if (this.isEditMode) {
          await axiosClient.put(`/medications/${this.existingMedication.id}`, payload);
          alert('Medication updated');
        } else {
          await axiosClient.post('/medications', payload);
          alert('Medication created');
        }
        this.$emit('refreshList');
        this.$emit('closeForm');
      } catch (err) {
        console.error(err);
        this.errorMessage = err.response?.data?.message || 'Error saving medication';
      }
    },
  },
};
</script>
