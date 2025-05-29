<template>
  <div class="outfit-card bg-white/80 rounded-xl border border-gray-200 p-4 shadow-md">
    <h3 class="font-bold text-lg mb-1">{{ outfit.name }}</h3>
    <p class="text-gray-500 mb-2" v-if="outfit.description">{{ outfit.description }}</p>
    <div class="flex gap-2 flex-wrap">
      <div
        v-for="item in outfit.items"
        :key="item.id"
        class="w-16 h-20 flex flex-col items-center"
      >
        <img
          :src="item.imageUrl"
          :alt="item.name"
          class="w-14 h-16 object-cover rounded-md border"
        />
        <span class="text-xs mt-1 text-center">{{ item.name }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  outfit: Object
});
</script>

<style scoped>
.outfit-card {
  min-width: 220px;
  max-width: 320px;
}
</style> 