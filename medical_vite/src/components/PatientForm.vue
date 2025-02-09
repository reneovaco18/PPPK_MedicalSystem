<template>
  <div>
    <h3>{{ isEditMode ? 'Edit Patient' : 'Create Patient' }}</h3>
    <form @submit.prevent="savePatient">
      <div>
        <label>First Name:</label>
        <input v-model="patientData.firstName" required />
      </div>
      <div>
        <label>Last Name:</label>
        <input v-model="patientData.lastName" required />
      </div>
      <div>
        <label>OIB:</label>
        <input v-model="patientData.oib" required />
      </div>
      <div>
        <label>Date of Birth:</label>
        <input type="date" v-model="patientData.dateOfBirth" required />
      </div>
      <div>
        <label>Gender (M/F):</label>
        <input v-model="patientData.gender" maxlength="1" required />
      </div>
      <button type="submit">Save</button>
      <button type="button" @click="$emit('closeForm')">Cancel</button>
    </form>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';

export default {
  name: 'PatientForm',
  props: {
    existingPatient: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      patientData: {
        firstName: '',
        lastName: '',
        oib: '',
        dateOfBirth: '',
        gender: '',
      },
    };
  },
  computed: {
    isEditMode() {
      return !!this.existingPatient;
    },
  },
  mounted() {
    if (this.isEditMode) {

      this.patientData = { ...this.existingPatient };
    }
  },
  methods: {
    async savePatient() {
      try {
        if (this.isEditMode) {

          await axiosClient.put(`/patients/${this.existingPatient.id}`, this.patientData);
          alert('Patient updated successfully');
        } else {

          await axiosClient.post('/patients', this.patientData);
          alert('Patient created successfully');
        }
        this.$emit('refreshList');
        this.$emit('closeForm');
      } catch (err) {
        console.error(err);
        alert('Error saving patient');
      }
    },
  },
};
</script>
