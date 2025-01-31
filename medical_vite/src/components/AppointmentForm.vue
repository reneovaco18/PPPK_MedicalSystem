<template>
  <div>
    <h3>{{ isEditMode ? 'Edit' : 'Create' }} Appointment</h3>
    <form @submit.prevent="saveAppointment">
      <!-- Patient ID -->
      <div>
        <label>Patient ID:</label>
        <input
            type="number"
            v-model="appointmentData.patientId"
            required
        />
      </div>

      <!-- Examination Type: use a <select> so the user must choose from the enum values -->
      <div>
        <label>Type:</label>
        <select
            v-model="appointmentData.type"
            required
        >
          <option
              v-for="examType in examOptions"
              :key="examType"
              :value="examType"
          >
            {{ examType }}
          </option>
        </select>
      </div>

      <!-- DateTime -->
      <div>
        <label>Date/Time:</label>
        <input
            type="datetime-local"
            v-model="appointmentData.dateTime"
            required
        />
      </div>

      <button type="submit">
        Save
      </button>
      <button
          type="button"
          @click="$emit('closeForm')"
      >
        Cancel
      </button>
    </form>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';

export default {
  name: 'AppointmentForm',
  props: {
    existingAppointment: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      appointmentData: {
        patientId: '',
        type: '',
        dateTime: '',
      },
      // The list of enum values from your Java ExaminationType enum:
      examOptions: [
        'GP',
        'KRV',
        'X_RAY',
        'CT',
        'MR',
        'ULTRA',
        'EKG',
        'ECHO',
        'EYE',
        'DERM',
        'DENTA',
        'MAMMO',
        'NEURO',
      ],
    };
  },
  computed: {
    isEditMode() {
      return !!this.existingAppointment;
    },
  },
  mounted() {
    // If in edit mode, populate the form with existing data
    if (this.isEditMode) {
      this.appointmentData = {
        patientId: this.existingAppointment.patient?.id,
        type: this.existingAppointment.type,
        dateTime: this.existingAppointment.dateTime,
      };
    }
  },
  methods: {
    async saveAppointment() {
      try {
        // Construct payload as the back end expects
        const payload = {
          patient: {id: this.appointmentData.patientId},
          type: this.appointmentData.type,
          dateTime: this.appointmentData.dateTime,
        };

        if (this.isEditMode) {
          await axiosClient.put(`/appointments/${this.existingAppointment.id}`, payload);
          alert('Appointment updated');
        } else {
          await axiosClient.post('/appointments', payload);
          alert('Appointment created');
        }
        this.$emit('refreshList');
        this.$emit('closeForm');
      } catch (err) {
        console.error(err);
        alert('Error saving appointment');
      }
    },
  },
};
</script>
