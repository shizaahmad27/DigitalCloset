<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Outfit Recommendations</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="outfit in recommendedOutfits" :key="outfit.id" class="card">
        <div class="grid grid-cols-2 gap-2">
          <ClothingCard
            v-for="item in outfit.items"
            :key="item.id"
            :item="item"
          />
        </div>
        <div class="mt-4">
          <h3 class="font-semibold">{{ outfit.name }}</h3>
          <p class="text-sm text-gray-600">{{ outfit.description }}</p>
          <button class="btn btn-primary mt-2" @click="saveOutfit(outfit)">Save Outfit</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ClothingCard from '../components/ClothingCard.vue'

const recommendedOutfits = ref([]) // TODO: Fetch recommendations from API

const saveOutfit = (outfit) => {
  // TODO: Implement save outfit logic
}
</script> 