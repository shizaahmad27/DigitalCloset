<!-- EnhancedOutfitCreator.vue - With Full Wardrobe Browser & Visual Outfit Preview -->
<template>
  <div class="w-full min-h-[calc(100vh-5rem)] px-8 py-8">
    <div class="creator-header flex justify-between items-center mb-8 flex-wrap gap-4">
      <h2 class="page-title text-4xl font-bold bg-gradient-to-r from-pink-500 to-purple-600 bg-clip-text text-transparent">
        Create Your Perfect Look ✨
      </h2>
      <div class="header-controls flex gap-4 items-center">
        <input
            v-model="outfitName"
            placeholder="Name your outfit..."
            class="outfit-name-input px-6 py-3 rounded-full border border-gray-200 bg-white/60 text-lg focus:outline-none focus:ring-2 focus:ring-pink-400"
        />
        <button class="save-btn px-8 py-3 rounded-full bg-gradient-to-r from-emerald-400 to-cyan-400 text-white font-semibold shadow-md hover:from-emerald-500 hover:to-cyan-500 transition disabled:opacity-50" @click="saveOutfit" :disabled="!canSave">
          💾 Save Outfit
        </button>
      </div>
    </div>

    <!-- Quick Actions Bar -->
    <div class="quick-actions-bar flex gap-4 mb-8 flex-wrap">
      <button
          @click="openWardrobeBrowser('top')"
          class="action-card bg-gradient-to-br from-pink-100 to-purple-100 p-4 rounded-2xl border-2 border-dashed border-pink-300 hover:border-pink-400 transition-all cursor-pointer group"
      >
        <div class="flex items-center gap-3">
          <span class="text-3xl">👚</span>
          <div>
            <h3 class="font-semibold text-gray-700">Browse Tops</h3>
            <p class="text-sm text-gray-500">{{ getItemCount('top') }} items</p>
          </div>
        </div>
      </button>

      <button
          @click="openWardrobeBrowser('bottom')"
          class="action-card bg-gradient-to-br from-blue-100 to-indigo-100 p-4 rounded-2xl border-2 border-dashed border-blue-300 hover:border-blue-400 transition-all cursor-pointer group"
      >
        <div class="flex items-center gap-3">
          <span class="text-3xl">👖</span>
          <div>
            <h3 class="font-semibold text-gray-700">Browse Bottoms</h3>
            <p class="text-sm text-gray-500">{{ getItemCount('bottom') }} items</p>
          </div>
        </div>
      </button>

      <button
          @click="openWardrobeBrowser('shoes')"
          class="action-card bg-gradient-to-br from-red-100 to-pink-100 p-4 rounded-2xl border-2 border-dashed border-red-300 hover:border-red-400 transition-all cursor-pointer group"
      >
        <div class="flex items-center gap-3">
          <span class="text-3xl">👠</span>
          <div>
            <h3 class="font-semibold text-gray-700">Browse Shoes</h3>
            <p class="text-sm text-gray-500">{{ getItemCount('shoes') }} items</p>
          </div>
        </div>
      </button>

      <button
          @click="openWardrobeBrowser('accessory')"
          class="action-card bg-gradient-to-br from-yellow-100 to-orange-100 p-4 rounded-2xl border-2 border-dashed border-yellow-300 hover:border-yellow-400 transition-all cursor-pointer group"
      >
        <div class="flex items-center gap-3">
          <span class="text-3xl">💍</span>
          <div>
            <h3 class="font-semibold text-gray-700">Browse Accessories</h3>
            <p class="text-sm text-gray-500">{{ getItemCount('accessory') }} items</p>
          </div>
        </div>
      </button>

      <button
          @click="openWardrobeBrowser('all')"
          class="action-card bg-gradient-to-br from-purple-100 to-pink-100 p-4 rounded-2xl border-2 border-dashed border-purple-300 hover:border-purple-400 transition-all cursor-pointer group"
      >
        <div class="flex items-center gap-3">
          <span class="text-3xl">✨</span>
          <div>
            <h3 class="font-semibold text-gray-700">Browse All</h3>
            <p class="text-sm text-gray-500">{{ wardrobeItems.length }} total items</p>
          </div>
        </div>
      </button>
    </div>

    <!-- Main Content Grid -->
    <div class="grid grid-cols-[1fr_400px] gap-8">
      <!-- Outfit Canvas -->
      <div class="outfit-canvas bg-white/40 rounded-3xl border border-white/40 p-8 relative overflow-hidden">
        <div class="canvas-container relative z-10">
          <h3 class="canvas-title text-center text-2xl font-semibold text-gray-600 mb-8">
            Drag items here or use browse buttons above
          </h3>

          <!-- Drop Zones -->
          <div class="drop-zones grid grid-cols-2 gap-6 mb-8">
            <!-- Top Section -->
            <div
                class="drop-zone top-zone col-span-2 min-h-[180px] border-4 border-dashed border-pink-200 rounded-2xl flex items-center justify-center bg-white/30 relative transition-all cursor-pointer group hover:border-pink-300 hover:bg-pink-50/50"
                :class="{ 'border-pink-400 bg-pink-50': dragOverZone === 'top' }"
                @drop="onDrop($event, 'top')"
                @dragover.prevent
                @dragenter.prevent
                @dragenter="dragOverZone = 'top'"
                @dragleave="dragOverZone = null"
                @click="openWardrobeBrowser('top')"
            >
              <div v-if="!outfitItems.top" class="drop-placeholder text-center text-gray-400 group-hover:text-pink-400 transition-colors">
                <span class="placeholder-icon block text-4xl mb-2 opacity-70">👚</span>
                <span class="placeholder-text text-lg font-medium">Click to add a top</span>
              </div>
              <div v-else class="outfit-item-container flex items-center justify-center w-full h-full relative">
                <img :src="outfitItems.top.imageUrl" :alt="outfitItems.top.name" class="outfit-item-image max-w-[120px] max-h-[150px] object-contain rounded-xl shadow-lg" />
                <div class="item-label absolute bottom-2 left-2 bg-black/70 text-white px-2 py-1 rounded text-xs">{{ outfitItems.top.name }}</div>
                <button class="remove-item-btn absolute top-2 right-2 w-8 h-8 rounded-full bg-red-500 text-white flex items-center justify-center text-sm hover:bg-red-600 transition" @click.stop="removeItem('top')">✕</button>
              </div>
            </div>

            <!-- Bottom Section -->
            <div
                class="drop-zone bottom-zone col-span-2 min-h-[180px] border-4 border-dashed border-blue-200 rounded-2xl flex items-center justify-center bg-white/30 relative transition-all cursor-pointer group hover:border-blue-300 hover:bg-blue-50/50"
                :class="{ 'border-blue-400 bg-blue-50': dragOverZone === 'bottom' }"
                @drop="onDrop($event, 'bottom')"
                @dragover.prevent
                @dragenter.prevent
                @dragenter="dragOverZone = 'bottom'"
                @dragleave="dragOverZone = null"
                @click="openWardrobeBrowser('bottom')"
            >
              <div v-if="!outfitItems.bottom" class="drop-placeholder text-center text-gray-400 group-hover:text-blue-400 transition-colors">
                <span class="placeholder-icon block text-4xl mb-2 opacity-70">👖</span>
                <span class="placeholder-text text-lg font-medium">Click to add bottoms</span>
              </div>
              <div v-else class="outfit-item-container flex items-center justify-center w-full h-full relative">
                <img :src="outfitItems.bottom.imageUrl" :alt="outfitItems.bottom.name" class="outfit-item-image max-w-[120px] max-h-[150px] object-contain rounded-xl shadow-lg" />
                <div class="item-label absolute bottom-2 left-2 bg-black/70 text-white px-2 py-1 rounded text-xs">{{ outfitItems.bottom.name }}</div>
                <button class="remove-item-btn absolute top-2 right-2 w-8 h-8 rounded-full bg-red-500 text-white flex items-center justify-center text-sm hover:bg-red-600 transition" @click.stop="removeItem('bottom')">✕</button>
              </div>
            </div>

            <!-- Shoes Section -->
            <div
                class="drop-zone shoes-zone min-h-[160px] border-4 border-dashed border-red-200 rounded-2xl flex items-center justify-center bg-white/30 relative transition-all cursor-pointer group hover:border-red-300 hover:bg-red-50/50"
                :class="{ 'border-red-400 bg-red-50': dragOverZone === 'shoes' }"
                @drop="onDrop($event, 'shoes')"
                @dragover.prevent
                @dragenter.prevent
                @dragenter="dragOverZone = 'shoes'"
                @dragleave="dragOverZone = null"
                @click="openWardrobeBrowser('shoes')"
            >
              <div v-if="!outfitItems.shoes" class="drop-placeholder text-center text-gray-400 group-hover:text-red-400 transition-colors">
                <span class="placeholder-icon block text-3xl mb-2 opacity-70">👠</span>
                <span class="placeholder-text text-sm font-medium">Click for shoes</span>
              </div>
              <div v-else class="outfit-item-container flex items-center justify-center w-full h-full relative">
                <img :src="outfitItems.shoes.imageUrl" :alt="outfitItems.shoes.name" class="outfit-item-image max-w-[100px] max-h-[120px] object-contain rounded-xl shadow-lg" />
                <div class="item-label absolute bottom-1 left-1 bg-black/70 text-white px-2 py-1 rounded text-xs">{{ outfitItems.shoes.name }}</div>
                <button class="remove-item-btn absolute top-2 right-2 w-6 h-6 rounded-full bg-red-500 text-white flex items-center justify-center text-xs hover:bg-red-600 transition" @click.stop="removeItem('shoes')">✕</button>
              </div>
            </div>

            <!-- Accessories Section -->
            <div
                class="drop-zone accessories-zone min-h-[160px] border-4 border-dashed border-yellow-200 rounded-2xl flex items-center justify-center bg-white/30 relative transition-all cursor-pointer group hover:border-yellow-300 hover:bg-yellow-50/50"
                :class="{ 'border-yellow-400 bg-yellow-50': dragOverZone === 'accessory' }"
                @drop="onDrop($event, 'accessory')"
                @dragover.prevent
                @dragenter.prevent
                @dragenter="dragOverZone = 'accessory'"
                @dragleave="dragOverZone = null"
                @click="openWardrobeBrowser('accessory')"
            >
              <div v-if="outfitItems.accessories.length === 0" class="drop-placeholder text-center text-gray-400 group-hover:text-yellow-500 transition-colors">
                <span class="placeholder-icon block text-3xl mb-2 opacity-70">💍</span>
                <span class="placeholder-text text-sm font-medium">Click for accessories</span>
              </div>
              <div v-else class="accessories-container flex flex-wrap gap-2 w-full justify-center items-center p-2">
                <div v-for="accessory in outfitItems.accessories" :key="accessory.id" class="accessory-item relative">
                  <img :src="accessory.imageUrl" :alt="accessory.name" class="accessory-image w-12 h-12 object-cover rounded-lg shadow-md" />
                  <button class="remove-accessory-btn absolute -top-1 -right-1 w-4 h-4 rounded-full bg-red-500 text-white flex items-center justify-center text-xs hover:bg-red-600 transition" @click.stop="removeAccessory(accessory.id)">✕</button>
                </div>
                <button
                    @click.stop="openWardrobeBrowser('accessory')"
                    class="add-more-accessory w-12 h-12 border-2 border-dashed border-yellow-400 rounded-lg flex items-center justify-center text-yellow-500 hover:border-yellow-500 hover:text-yellow-600 transition-colors"
                >
                  <span class="text-lg">+</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Enhanced Outfit Preview -->
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
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Enhanced Wardrobe Browser Modal -->
    <div v-if="showWardrobeBrowser" class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50" @click="closeWardrobeBrowser">
      <div class="bg-white/95 backdrop-blur-md rounded-3xl max-w-7xl max-h-[85vh] overflow-hidden w-[90vw]" @click.stop>
        <div class="p-6 border-b border-gray-200">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-3xl font-bold bg-gradient-to-r from-pink-500 to-purple-600 bg-clip-text text-transparent">
              {{ getBrowserTitle() }}
            </h3>
            <button @click="closeWardrobeBrowser" class="w-10 h-10 rounded-full bg-gray-200 hover:bg-gray-300 flex items-center justify-center transition">
              ✕
            </button>
          </div>

          <!-- Enhanced Filters -->
          <div class="flex gap-4 items-center flex-wrap">
            <div class="search-container">
              <input
                  v-model="searchQuery"
                  placeholder="Search items..."
                  class="px-4 py-2 border border-gray-300 rounded-xl bg-white/80 focus:outline-none focus:ring-2 focus:ring-pink-400"
              />
            </div>

            <div class="filter-container flex gap-2">
              <select v-model="filterBrand" class="px-3 py-2 border border-gray-300 rounded-xl bg-white/80 focus:outline-none focus:ring-2 focus:ring-pink-400">
                <option value="">All Brands</option>
                <option v-for="brand in availableBrands" :key="brand" :value="brand">{{ brand }}</option>
              </select>

              <select v-model="filterSeason" class="px-3 py-2 border border-gray-300 rounded-xl bg-white/80 focus:outline-none focus:ring-2 focus:ring-pink-400">
                <option value="">All Seasons</option>
                <option value="spring">Spring</option>
                <option value="summer">Summer</option>
                <option value="fall">Fall</option>
                <option value="winter">Winter</option>
                <option value="all_season">All Season</option>
              </select>
            </div>
          </div>
        </div>

        <div class="p-6 max-h-[calc(85vh-200px)] overflow-y-auto">
          <div class="grid grid-cols-6 gap-4">
            <div
                v-for="item in filteredBrowserItems"
                :key="item.id"
                class="cursor-pointer transform hover:scale-105 transition-transform"
                @click="selectItemFromBrowser(item)"
            >
              <ClothingCard :item="item" :draggable="false" />
            </div>
          </div>

          <div v-if="filteredBrowserItems.length === 0" class="text-center py-12">
            <span class="text-4xl opacity-50 block mb-2">🔍</span>
            <p class="text-gray-500">No items found matching your criteria</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ClothingCard from '../components/ClothingCard.vue'

// Enhanced sample data
const wardrobeItems = ref([
  { id: 1, name: 'Pink Blouse', imageUrl: '/api/placeholder/200/250', category: 'top', brand: 'Zara', season: 'spring', eventTypes: ['casual', 'date'] },
  { id: 2, name: 'Denim Jacket', imageUrl: '/api/placeholder/200/250', category: 'top', brand: 'Levi\'s', season: 'fall', eventTypes: ['casual'] },
  { id: 3, name: 'White Tee', imageUrl: '/api/placeholder/200/220', category: 'top', brand: 'H&M', season: 'summer', eventTypes: ['casual'] },
  { id: 4, name: 'Silk Blouse', imageUrl: '/api/placeholder/200/250', category: 'top', brand: 'Zara', season: 'all_season', eventTypes: ['formal', 'work'] },
  { id: 5, name: 'Black Jeans', imageUrl: '/api/placeholder/200/300', category: 'bottom', brand: 'H&M', season: 'all_season', eventTypes: ['casual', 'night_out'] },
  { id: 6, name: 'Dress Pants', imageUrl: '/api/placeholder/200/300', category: 'bottom', brand: 'Zara', season: 'all_season', eventTypes: ['formal', 'work'] },
  { id: 7, name: 'Summer Skirt', imageUrl: '/api/placeholder/200/250', category: 'bottom', brand: 'H&M', season: 'summer', eventTypes: ['casual', 'date'] },
  { id: 8, name: 'White Sneakers', imageUrl: '/api/placeholder/200/200', category: 'shoes', brand: 'Nike', season: 'all_season', eventTypes: ['casual', 'school'] },
  { id: 9, name: 'High Heels', imageUrl: '/api/placeholder/200/200', category: 'shoes', brand: 'Louboutin', season: 'all_season', eventTypes: ['formal', 'date'] },
  { id: 10, name: 'Boots', imageUrl: '/api/placeholder/200/220', category: 'shoes', brand: 'Dr. Martens', season: 'winter', eventTypes: ['casual'] },
  { id: 11, name: 'Gold Necklace', imageUrl: '/api/placeholder/150/150', category: 'accessory', brand: 'Pandora', season: 'all_season', eventTypes: ['date', 'formal'] },
  { id: 12, name: 'Silver Bracelet', imageUrl: '/api/placeholder/150/150', category: 'accessory', brand: 'Tiffany', season: 'all_season', eventTypes: ['casual', 'date'] },
  { id: 13, name: 'Designer Bag', imageUrl: '/api/placeholder/180/180', category: 'accessory', brand: 'Gucci', season: 'all_season', eventTypes: ['formal', 'work'] },
  { id: 14, name: 'Sunglasses', imageUrl: '/api/placeholder/160/120', category: 'accessory', brand: 'Ray-Ban', season: 'summer', eventTypes: ['casual'] },
  { id: 15, name: 'Watch', imageUrl: '/api/placeholder/140/140', category: 'accessory', brand: 'Apple', season: 'all_season', eventTypes: ['casual', 'work'] }
])

const outfitName = ref('')
const dragOverZone = ref(null)
const showWardrobeBrowser = ref(false)
const browserCategory = ref('')
const searchQuery = ref('')
const filterBrand = ref('')
const filterSeason = ref('')

const outfitItems = ref({
  top: null,
  bottom: null,
  shoes: null,
  accessories: []
})

// Computed properties
const hasItems = computed(() => {
  return outfitItems.value.top || outfitItems.value.bottom ||
      outfitItems.value.shoes || outfitItems.value.accessories.length > 0
})

const canSave = computed(() => {
  return outfitName.value.trim() && hasItems.value
})

const availableBrands = computed(() => {
  return [...new Set(wardrobeItems.value.map(item => item.brand))].sort()
})

const filteredBrowserItems = computed(() => {
  let items = wardrobeItems.value

  if (browserCategory.value !== 'all') {
    items = items.filter(item => item.category === browserCategory.value)
  }

  if (searchQuery.value) {
    items = items.filter(item =>
        item.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        item.brand.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  if (filterBrand.value) {
    items = items.filter(item => item.brand === filterBrand.value)
  }

  if (filterSeason.value) {
    items = items.filter(item => item.season === filterSeason.value)
  }

  return items
})

// Methods
const getItemCount = (category) => {
  return wardrobeItems.value.filter(item => item.category === category).length
}

const openWardrobeBrowser = (category) => {
  browserCategory.value = category
  showWardrobeBrowser.value = true
  searchQuery.value = ''
  filterBrand.value = ''
  filterSeason.value = ''
}

const closeWardrobeBrowser = () => {
  showWardrobeBrowser.value = false
  browserCategory.value = ''
}

const getBrowserTitle = () => {
  const titles = {
    all: 'Browse All Items',
    top: 'Browse Tops',
    bottom: 'Browse Bottoms',
    shoes: 'Browse Shoes',
    accessory: 'Browse Accessories'
  }
  return titles[browserCategory.value] || 'Browse Wardrobe'
}

const selectItemFromBrowser = (item) => {
  if (item.category === 'accessory') {
    if (!outfitItems.value.accessories.find(acc => acc.id === item.id)) {
      outfitItems.value.accessories.push(item)
    }
  } else {
    outfitItems.value[item.category] = item
  }

  if (item.category !== 'accessory') {
    closeWardrobeBrowser()
  }
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
      if (item.category === zone) {
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

  if (items.length === 0) return 'Empty'

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

  if (items.length === 0) return 'No Season'

  const seasons = [...new Set(items.map(item => item.season).filter(Boolean))]

  if (seasons.includes('all_season') || seasons.length > 1) {
    return 'Versatile'
  }

  return seasons[0] ? seasons[0].charAt(0).toUpperCase() + seasons[0].slice(1) : 'Mixed'
}

const getOutfitEvents = () => {
  const items = [
    outfitItems.value.top,
    outfitItems.value.bottom,
    outfitItems.value.shoes,
    ...outfitItems.value.accessories
  ].filter(Boolean)

  if (items.length === 0) return []

  const allEvents = items.flatMap(item => item.eventTypes || [])
  const eventCounts = allEvents.reduce((acc, event) => {
    acc[event] = (acc[event] || 0) + 1
    return acc
  }, {})

  // Return events that appear in at least half the items
  const threshold = Math.ceil(items.length / 2)
  return Object.entries(eventCounts)
      .filter(([event, count]) => count >= threshold)
      .map(([event]) => event.charAt(0).toUpperCase() + event.slice(1))
      .slice(0, 4) // Limit to 4 events
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
    season: getSeasonMix(),
    events: getOutfitEvents()
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
/* Custom scrollbar for modal */
.overflow-y-auto {
  scrollbar-width: thin;
  scrollbar-color: rgba(236, 72, 153, 0.3) transparent;
}

.overflow-y-auto::-webkit-scrollbar {
  width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
  background: transparent;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
  background: rgba(236, 72, 153, 0.3);
  border-radius: 4px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
  background: rgba(236, 72, 153, 0.5);
}

/* Action cards hover effects */
.action-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

/* Mannequin animation */
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

/* Grid responsive */
@media (max-width: 1200px) {
  .grid-cols-6 {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 768px) {
  .grid-cols-6 {
    grid-template-columns: repeat(3, 1fr);
  }

  .grid-cols-\[1fr_400px\] {
    grid-template-columns: 1fr;
  }

  .quick-actions-bar {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
