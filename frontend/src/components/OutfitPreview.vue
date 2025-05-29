<template>
  <div class="outfit-preview-panel">
    <div class="preview-card bg-white/60 rounded-3xl border border-white/40 p-6">
      <h3 class="text-2xl font-bold bg-gradient-to-r from-pink-400 to-purple-400 bg-clip-text text-transparent mb-6">
        Outfit Preview
      </h3>

      <div v-if="!hasItems" class="empty-preview text-center py-12">
        <span class="text-6xl opacity-50 block mb-4">👗</span>
        <p class="text-gray-500">Start adding items to see your outfit come together!</p>
      </div>

      <div v-else class="outfit-visualization">
        <!-- Mannequin-style Preview -->
        <div class="mannequin-container bg-gradient-to-b from-gray-100 to-gray-200 rounded-2xl p-6 mb-6 relative min-h-[300px]">
          <!-- Body outline -->
          <div class="body-outline absolute inset-6 border-2 border-dashed border-gray-300 rounded-full opacity-30"></div>

          <!-- Top -->
          <div v-if="outfitItems.top" class="preview-top absolute top-4 left-1/2 transform -translate-x-1/2">
            <img :src="outfitItems.top.imageUrl" :alt="outfitItems.top.name" class="w-20 h-24 object-contain opacity-90" />
          </div>

          <!-- Bottom -->
          <div v-if="outfitItems.bottom" class="preview-bottom absolute top-20 left-1/2 transform -translate-x-1/2">
            <img :src="outfitItems.bottom.imageUrl" :alt="outfitItems.bottom.name" class="w-20 h-24 object-contain opacity-90" />
          </div>

          <!-- Shoes -->
          <div v-if="outfitItems.shoes" class="preview-shoes absolute bottom-4 left-1/2 transform -translate-x-1/2">
            <img :src="outfitItems.shoes.imageUrl" :alt="outfitItems.shoes.name" class="w-16 h-16 object-contain opacity-90" />
          </div>

          <!-- Accessories floating around -->
          <div v-for="(accessory, index) in outfitItems.accessories" :key="accessory.id"
               :class="[
                 'preview-accessory absolute',
                 index === 0 ? 'top-2 right-2' : '',
                 index === 1 ? 'top-2 left-2' : '',
                 index === 2 ? 'bottom-2 right-2' : '',
                 index >= 3 ? 'bottom-2 left-2' : ''
               ]">
            <img :src="accessory.imageUrl" :alt="accessory.name" class="w-8 h-8 object-contain opacity-80" />
          </div>
        </div>

        <!-- Outfit Stats -->
        <div class="outfit-stats space-y-4">
          <div class="stat-item bg-white/40 rounded-xl p-4">
            <h4 class="font-semibold text-gray-700 mb-2">Style Analysis</h4>
            <div class="flex gap-2">
              <span class="stat-tag px-3 py-1 bg-gradient-to-r from-purple-400 to-pink-400 text-white rounded-full text-sm font-medium">
                {{ getOutfitStyle() }}
              </span>
              <span class="stat-tag px-3 py-1 bg-gradient-to-r from-blue-400 to-purple-400 text-white rounded-full text-sm font-medium">
                {{ getSeasonMix() }}
              </span>
            </div>
          </div>

          <div class="stat-item bg-white/40 rounded-xl p-4">
            <h4 class="font-semibold text-gray-700 mb-2">Outfit Components</h4>
            <div class="space-y-2 text-sm">
              <div v-if="outfitItems.top" class="flex items-center gap-2">
                <span class="w-2 h-2 bg-pink-400 rounded-full"></span>
                <span>{{ outfitItems.top.name }} ({{ outfitItems.top.brand }})</span>
              </div>
              <div v-if="outfitItems.bottom" class="flex items-center gap-2">
                <span class="w-2 h-2 bg-blue-400 rounded-full"></span>
                <span>{{ outfitItems.bottom.name }} ({{ outfitItems.bottom.brand }})</span>
              </div>
              <div v-if="outfitItems.shoes" class="flex items-center gap-2">
                <span class="w-2 h-2 bg-red-400 rounded-full"></span>
                <span>{{ outfitItems.shoes.name }} ({{ outfitItems.shoes.brand }})</span>
              </div>
              <div v-for="accessory in outfitItems.accessories" :key="accessory.id" class="flex items-center gap-2">
                <span class="w-2 h-2 bg-yellow-400 rounded-full"></span>
                <span>{{ accessory.name }} ({{ accessory.brand }})</span>
              </div>
            </div>
          </div>

          <div class="stat-item bg-white/40 rounded-xl p-4">
            <h4 class="font-semibold text-gray-700 mb-2">Perfect For</h4>
            <div class="flex flex-wrap gap-2">
              <span v-for="event in getOutfitEvents()" :key="event"
                    class="event-tag px-2 py-1 bg-gradient-to-r from-green-400 to-blue-400 text-white rounded-lg text-xs font-medium">
                {{ event }}
              </span>
              <span v-if="getOutfitEvents().length === 0" class="text-gray-400">No specific occasion detected</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface OutfitItems {
  top: any
  bottom: any
  shoes: any
  accessories: any[]
}

const props = defineProps<{
  outfitItems: OutfitItems
  getOutfitStyle: () => string
  getSeasonMix: () => string
  getOutfitEvents: () => string[]
}>()

const hasItems = computed(() => {
  return props.outfitItems.top || props.outfitItems.bottom ||
    props.outfitItems.shoes || (props.outfitItems.accessories && props.outfitItems.accessories.length > 0)
})
</script>

<style scoped>
.outfit-preview-panel {
  /* You can add panel-specific styles here if needed */
}
.preview-card {
  background: rgba(255,255,255,0.6);
  border-radius: 1.5rem;
  border: 1px solid rgba(255,255,255,0.4);
  padding: 1.5rem;
}
.mannequin-container {
  position: relative;
  background: linear-gradient(145deg, #f8f9fa, #e9ecef);
}
.preview-top, .preview-bottom, .preview-shoes, .preview-accessory {
  transition: all 0.3s ease;
}
.preview-accessory {
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-5px); }
}
</style> 