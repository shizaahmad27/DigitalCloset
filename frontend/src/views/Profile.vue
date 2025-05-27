<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Profile</h1>
    <div class="max-w-2xl mx-auto">
      <div class="card mb-8">
        <h2 class="text-xl font-semibold mb-4">Personal Information</h2>
        <form @submit.prevent="updateProfile" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Name</label>
            <input
              type="text"
              v-model="profile.name"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input
              type="email"
              v-model="profile.email"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            />
          </div>
          <button type="submit" class="btn btn-primary">Save Changes</button>
        </form>
      </div>
      
      <div class="card">
        <h2 class="text-xl font-semibold mb-4">Preferences</h2>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Style Preferences</label>
            <select
              v-model="profile.style"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            >
              <option value="casual">Casual</option>
              <option value="formal">Formal</option>
              <option value="sporty">Sporty</option>
              <option value="business">Business</option>
            </select>
          </div>
          <button class="btn btn-primary" @click="updatePreferences">Update Preferences</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const profile = ref({
  name: '',
  email: '',
  style: 'casual'
})

const updateProfile = () => {
  // TODO: Implement profile update logic
}

const updatePreferences = () => {
  // TODO: Implement preferences update logic
}
</script> 