<template>
  <div>
    <h2>Appointments</h2>
    <button @click="showCreateForm = !showCreateForm">
      {{ showCreateForm ? 'Cancel' : 'Create New Appointment' }}
    </button>

    <div v-if="showCreateForm">
      <appointment-form
          @closeForm="showCreateForm = false"
          @refreshList="fetchAppointments"
      />
    </div>

    <h3>Appointments by Patient ID</h3>
    <input v-model="searchPatientId" placeholder="Patient ID" />
    <button @click="getAppointmentsByPatient">Search</button>
    <div v-if="patientAppointments.length > 0">
      <ul>
        <li v-for="appt in patientAppointments" :key="appt.id">
          [{{ appt.type }}] - {{ appt.dateTime }}
        </li>
      </ul>
    </div>

    <h3>All Appointments</h3>
    <ul>
      <li v-for="appt in appointments" :key="appt.id">
        (Patient ID: {{ appt.patient?.id }}) [{{ appt.type }}] - {{ appt.dateTime }}
        <button @click="editAppointment(appt)">Edit</button>
        <button @click="deleteAppointment(appt.id)">Delete</button>
      </li>
    </ul>

    <div v-if="selectedAppointment">
      <h3>Edit Appointment</h3>
      <appointment-form
          :existingAppointment="selectedAppointment"
          @closeForm="selectedAppointment = null"
          @refreshList="fetchAppointments"
      />
    </div>
  </div>
</template>

<script>
import axiosClient from '../api/axiosClient';
import AppointmentForm from '../components/AppointmentForm.vue';

export default {
  name: 'AppointmentsView',
  components: { AppointmentForm },
  data() {
    return {
      appointments: [],
      showCreateForm: false,
      selectedAppointment: null,
      searchPatientId: '',
      patientAppointments: [],
    };
  },
  methods: {
    async fetchAppointments() {
      try {
        const res = await axiosClient.get('/appointments');
        // Note: your controller might not have a GET /appointments for “all,”
        // so you might need to create it or skip listing all.
        this.appointments = res.data;
      } catch (err) {
        console.error(err);
      }
    },
    editAppointment(appt) {
      this.selectedAppointment = { ...appt };
    },
    async deleteAppointment(id) {
      if (!confirm('Are you sure?')) return;
      try {
        await axiosClient.delete(`/appointments/${id}`);
        alert('Appointment deleted');
        this.fetchAppointments();
      } catch (err) {
        console.error(err);
      }
    },
    async getAppointmentsByPatient() {
      if (!this.searchPatientId) return;
      try {
        const res = await axiosClient.get(`/appointments/patient/${this.searchPatientId}`);
        this.patientAppointments = res.data;
      } catch (err) {
        console.error(err);
      }
    },
  },
  mounted() {
    // If you do not have “GET /api/appointments” for listing all,
    // you can remove this or add such an endpoint to your back end.
    this.fetchAppointments();
  },
};
</script>
