<!-- OutfitCreator.vue - Drag & Drop Outfit Builder -->
<template>
  <div class="outfit-creator">
    <div class="creator-header">
      <h2 class="page-title">Create Your Perfect Look ✨</h2>
      <div class="header-controls">
        <input
          v-model="outfitName"
          placeholder="Name your outfit..."
          class="outfit-name-input" />
        <button class="save-btn" @click="saveOutfit" :disabled="!canSave">
          💾 Save Outfit
        </button>
      </div>
    </div>

    <div class="creator-layout">
      <!-- Wardrobe Sidebar -->
      <div class="wardrobe-sidebar">
        <div class="sidebar-header">
          <h3 class="sidebar-title">Your Wardrobe</h3>
          <CategoryFilter 
            :categories="categories"
            :selected-category="selectedCategory"
            @category-change="onCategoryChange" />
        </div>
        
        <div class="sidebar-items">
          <ClothingCard
            v-for="item in filteredItems"
            :key="item.id"
            :item="item"
            class="sidebar-item"
            @dragstart="onDragStart" />
        </div>
      </div>

      <!-- Outfit Canvas -->
      <div class="outfit-canvas">
        <div class="canvas-container">
          <h3 class="canvas-title">Drag items here to create your outfit</h3>
          
          <!-- Drop Zones -->
          <div class="drop-zones">
            <!-- Top Section -->
            <div class="drop-zone top-zone"
                 @drop="onDrop($event, 'top')"
                 @dragover.prevent
                 @dragenter.prevent
                 :class="{ 'drag-over': dragOverZone === 'top' }"
                 @dragenter="dragOverZone = 'top'"
                 @dragleave="dragOverZone = null">
              <div v-if="!outfitItems.top" class="drop-placeholder">
                <span class="placeholder-icon">👚</span>
                <span class="placeholder-text">Drop a top here</span>
              </div>
              <div v-else class="outfit-item-container">
                <img :src="outfitItems.top.imageUrl" :alt="outfitItems.top.name" class="outfit-item-image">
                <button class="remove-item-btn" @click="removeItem('top')">✕</button>
              </div>
            </div>

            <!-- Bottom Section -->
            <div class="drop-zone bottom-zone"
                 @drop="onDrop($event, 'bottom')"
                 @dragover.prevent
                 @dragenter.prevent
                 :class="{ 'drag-over': dragOverZone === 'bottom' }"
                 @dragenter="dragOverZone = 'bottom'"
                 @dragleave="dragOverZone = null">
              <div v-if="!outfitItems.bottom" class="drop-placeholder">
                <span class="placeholder-icon">👖</span>
                <span class="placeholder-text">Drop bottoms here</span>
              </div>
              <div v-else class="outfit-item-container">
                <img :src="outfitItems.bottom.imageUrl" :alt="outfitItems.bottom.name" class="outfit-item-image">
                <button class="remove-item-btn" @click="removeItem('bottom')">✕</button>
              </div>
            </div>

            <!-- Shoes Section -->
            <div class="drop-zone shoes-zone"
                 @drop="onDrop($event, 'shoes')"
                 @dragover.prevent
                 @dragenter.prevent
                 :class="{ 'drag-over': dragOverZone === 'shoes' }"
                 @dragenter="dragOverZone = 'shoes'"
                 @dragleave="dragOverZone = null">
              <div v-if="!outfitItems.shoes" class="drop-placeholder">
                <span class="placeholder-icon">👠</span>
                <span class="placeholder-text">Drop shoes here</span>
              </div>
              <div v-else class="outfit-item-container">
                <img :src="outfitItems.shoes.imageUrl" :alt="outfitItems.shoes.name" class="outfit-item-image">
                <button class="remove-item-btn" @click="removeItem('shoes')">✕</button>
              </div>
            </div>

            <!-- Accessories Section -->
            <div class="drop-zone accessories-zone"
                 @drop="onDrop($event, 'accessory')"
                 @dragover.prevent
                 @dragenter.prevent
                 :class="{ 'drag-over': dragOverZone === 'accessory' }"
                 @dragenter="dragOverZone = 'accessory'"
                 @dragleave="dragOverZone = null">
              <div v-if="outfitItems.accessories.length === 0" class="drop-placeholder">
                <span class="placeholder-icon">💍</span>
                <span class="placeholder-text">Drop accessories here</span>
              </div>
              <div v-else class="accessories-container">
                <div v-for="accessory in outfitItems.accessories" :key="accessory.id" class="accessory-item">
                  <img :src="accessory.imageUrl" :alt="accessory.name" class="accessory-image">
                  <button class="remove-accessory-btn" @click="removeAccessory(accessory.id)">✕</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Outfit Preview -->
          <div v-if="hasItems" class="outfit-preview">
            <h4 class="preview-title">Outfit Preview</h4>
            <div class="preview-tags">
              <span class="preview-tag">{{ getOutfitStyle() }}</span>
              <span class="preview-tag">{{ getSeasonMix() }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ClothingCard from '../components/ClothingCard.vue'
import CategoryFilter from '../components/CategoryFilter.vue'

// Sample data - replace with actual API calls
const wardrobeItems = ref([
  { id: 1, name: 'Pink Blouse', imageUrl: '/api/placeholder/200/250', category: 'top', brand: 'Zara', season: 'spring', eventTypes: ['casual', 'date'] },
  { id: 2, name: 'Denim Jacket', imageUrl: '/api/placeholder/200/250', category: 'top', brand: 'Levi\'s', season: 'fall', eventTypes: ['casual'] },
  { id: 3, name: 'Black Jeans', imageUrl: '/api/placeholder/200/300', category: 'bottom', brand: 'H&M', season: 'all_season', eventTypes: ['casual', 'night_out'] },
  { id: 4, name: 'White Sneakers', imageUrl: '/api/placeholder/200/200', category: 'shoes', brand: 'Nike', season: 'all_season', eventTypes: ['casual', 'school'] },
  { id: 5, name: 'Gold Necklace', imageUrl: '/api/placeholder/150/150', category: 'accessory', brand: 'Pandora', season: 'all_season', eventTypes: ['date', 'formal'] }
])

const outfitName = ref('')
const selectedCategory = ref('all')
const dragOverZone = ref(null)

const outfitItems = ref({
  top: null,
  bottom: null,
  shoes: null,
  accessories: []
})

const categories = ref([
  { value: 'all', label: '✨ All', count: 5 },
  { value: 'top', label: '👚 Tops', count: 2 },
  { value: 'bottom', label: '👖 Bottoms', count: 1 },
  { value: 'shoes', label: '👠 Shoes', count: 1 },
  { value: 'accessory', label: '💍 Accessories', count: 1 }
])

const filteredItems = computed(() => {
  if (selectedCategory.value === 'all') {
    return wardrobeItems.value
  }
  return wardrobeItems.value.filter(item => 
    item.category === selectedCategory.value
  )
})

const hasItems = computed(() => {
  return outfitItems.value.top || outfitItems.value.bottom || 
         outfitItems.value.shoes || outfitItems.value.accessories.length > 0
})

const canSave = computed(() => {
  return outfitName.value.trim() && hasItems.value
})

const onCategoryChange = (category) => {
  selectedCategory.value = category
}

const onDragStart = (item) => {
  // Handled by ClothingCard component
}

const onDrop = (event, zone) => {
  event.preventDefault()
  dragOverZone.value = null
  
  try {
    const item = JSON.parse(event.dataTransfer.getData('text/plain'))
    
    if (zone === 'accessory') {
      if (item.category === 'accessory' && !outfitItems.value.accessories.find(acc => acc.id === item.id)) {
        outfitItems.value.accessories.push(item)
      }
    } else {
      if (item.category === zone || (zone === 'bottom' && item.category === 'bottom')) {
        outfitItems.value[zone] = item
      }
    }
  } catch (error) {
    console.error('Error parsing dropped item:', error)
  }
}

const removeItem = (zone) => {
  outfitItems.value[zone] = null
}

const removeAccessory = (accessoryId) => {
  outfitItems.value.accessories = outfitItems.value.accessories.filter(
    acc => acc.id !== accessoryId
  )
}

const getOutfitStyle = () => {
  const items = [
    outfitItems.value.top,
    outfitItems.value.bottom,
    outfitItems.value.shoes,
    ...outfitItems.value.accessories
  ].filter(Boolean)
  
  const eventTypes = items.flatMap(item => item.eventTypes || [])
  const mostCommon = eventTypes.reduce((acc, type) => {
    acc[type] = (acc[type] || 0) + 1
    return acc
  }, {})
  
  const dominantStyle = Object.keys(mostCommon).reduce((a, b) => 
    mostCommon[a] > mostCommon[b] ? a : b, 'casual'
  )
  
  return dominantStyle.charAt(0).toUpperCase() + dominantStyle.slice(1)
}

const getSeasonMix = () => {
  const items = [
    outfitItems.value.top,
    outfitItems.value.bottom,
    outfitItems.value.shoes,
    ...outfitItems.value.accessories
  ].filter(Boolean)
  
  const seasons = [...new Set(items.map(item => item.season).filter(Boolean))]
  
  if (seasons.includes('all_season') || seasons.length > 1) {
    return 'Versatile'
  }
  
  return seasons[0] ? seasons[0].charAt(0).toUpperCase() + seasons[0].slice(1) : 'Mixed'
}

const saveOutfit = () => {
  if (!canSave.value) return
  
  const outfit = {
    name: outfitName.value,
    items: {
      top: outfitItems.value.top?.id,
      bottom: outfitItems.value.bottom?.id,
      shoes: outfitItems.value.shoes?.id,
      accessories: outfitItems.value.accessories.map(acc => acc.id)
    },
    style: getOutfitStyle(),
    season: getSeasonMix()
  }
  
  console.log('Saving outfit:', outfit)
  // Here you would call your API to save the outfit
  
  // Reset form
  outfitName.value = ''
  outfitItems.value = {
    top: null,
    bottom: null,
    shoes: null,
    accessories: []
  }
  
  // Show success message
  alert('✨ Outfit saved successfully!')
}
</script>

<style scoped>
.outfit-creator {
  max-width: 1600px;
  margin: 0 auto;
  padding: 2rem;
  min-height: calc(100vh - 100px);
}

.creator-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ec4899, #8b5cf6, #06b6d4);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.header-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.outfit-name-input {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  font-size: 1rem;
  color: #374151;
  placeholder-color: #9ca3af;
  outline: none;
  transition: all 0.3s ease;
  min-width: 200px;
}

.outfit-name-input:focus {
  background: rgba(255, 255, 255, 0.4);
  border-color: #ec4899;
  box-shadow: 0 0 20px rgba(236, 72, 153, 0.3);
}

.save-btn {
  padding: 0.75rem 2rem;
  background: linear-gradient(135deg, #10b981, #06b6d4);
  color: white;
  border: none;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.4);
}

.save-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.6);
}

.save-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

.creator-layout {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 2rem;
  height: calc(100vh - 200px);
}

@media (max-width: 1200px) {
  .creator-layout {
    grid-template-columns: 1fr;
    height: auto;
  }
}

.wardrobe-sidebar {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 1.5rem;
  height: fit-content;
  max-height: 100%;
  overflow: hidden;
}

.sidebar-header {
  margin-bottom: 1.5rem;
}

.sidebar-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 1rem;
  background: linear-gradient(135deg, #6b46c1, #ec4899);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.sidebar-items {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  max-height: calc(100vh - 400px);
  overflow-y: auto;
  padding-right: 0.5rem;
}

.sidebar-item {
  transform: scale(0.9);
}

.outfit-canvas {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(30px);
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 2rem;
  position: relative;
  overflow: hidden;
}

.outfit-canvas::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 80%, rgba(236, 72, 153, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(139, 92, 246, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, rgba(6, 182, 212, 0.05) 0%, transparent 50%);
  pointer-events: none;
}

.canvas-container {
  position: relative;
  z-index: 1;
}

.canvas-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: 600;
  color: #6b7280;
  margin-bottom: 2rem;
}

.drop-zones {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: auto auto auto;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.top-zone {
  grid-column: 1 / -1;
}

.bottom-zone {
  grid-column: 1 / -1;
}

.shoes-zone {
  grid-column: 1;
}

.accessories-zone {
  grid-column: 2;
}

.drop-zone {
  min-height: 200px;
  border: 3px dashed rgba(236, 72, 153, 0.3);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
  position: relative;
}

.drop-zone.drag-over {
  border-color: #ec4899;
  background: rgba(236, 72, 153, 0.1);
  transform: scale(1.02);
  box-shadow: 0 10px 30px rgba(236, 72, 153, 0.3);
}

.drop-placeholder {
  text-align: center;
  color: #9ca3af;
}

.placeholder-icon {
  display: block;
  font-size: 3rem;
  margin-bottom: 0.5rem;
  opacity: 0.7;
}

.placeholder-text {
  font-size: 1.1rem;
  font-weight: 500;
}

.outfit-item-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.outfit-item-image {
  max-width: 150px;
  max-height: 180px;
  object-fit: contain;
  border-radius: 15px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.remove-item-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.9);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  transition: all 0.3s ease;
}

.remove-item-btn:hover {
  background: #dc2626;
  transform: scale(1.1);
}

.accessories-container {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  width: 100%;
  justify-content: center;
  align-items: center;
  padding: 1rem;
}

.accessory-item {
  position: relative;
}

.accessory-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.remove-accessory-btn {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #ef4444;
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
}

.outfit-preview {
  text-align: center;
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(15px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.4);
}

.preview-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 1rem;
  background: linear-gradient(135deg, #ec4899, #8b5cf6);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.preview-tags {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.preview-tag {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #a78bfa, #ec4899);
  color: white;
  border-radius: 20px;
  font-weight: 500;
  font-size: 0.875rem;
}
</style>
