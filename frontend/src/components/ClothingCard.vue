<!-- ClothingCard.vue  -->
<template>
  <div
    class="clothing-card bg-white/80 rounded-xl border border-gray-200 overflow-hidden shadow-sm hover:shadow-md transition-all cursor-pointer"
    :draggable="draggable"
    @dragstart="onDragStart"
    @click="$emit('click', item)"
  >
    <div class="relative aspect-[3/4]">
      <img
        :src="item.imageUrl"
        :alt="item.name"
        class="w-full h-full object-cover"
      />
      <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent"></div>
      <div class="absolute bottom-0 left-0 right-0 p-3 text-white">
        <h3 class="font-semibold text-sm truncate">{{ item.name }}</h3>
        <p class="text-xs opacity-90">{{ item.brand }}</p>
      </div>
    </div>
    <div class="p-3">
      <div class="flex flex-wrap gap-1">
        <span
          v-for="event in item.eventTypes"
          :key="event"
          class="px-2 py-1 bg-pink-100 text-pink-600 rounded-full text-xs"
        >
          {{ event }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const props = defineProps<{
  item: {
    id: string
    name: string
    brand: string
    imageUrl: string
    category: string
    eventTypes: string[]
  }
  draggable?: boolean
}>()

const emit = defineEmits(['click', 'dragstart'])

const onDragStart = (event: DragEvent) => {
  if (event.dataTransfer) {
    event.dataTransfer.setData('text/plain', JSON.stringify(props.item))
    event.dataTransfer.effectAllowed = 'move'
    emit('dragstart', props.item)
  }
}
</script>

<style scoped>
.clothing-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.clothing-card:hover {
  transform: translateY(-2px);
}

.clothing-card:active {
  transform: translateY(0);
}
</style>