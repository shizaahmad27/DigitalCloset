<!-- WardrobeGrid.vue - Fixed Pinterest-style masonry grid -->
<template>
  <div class="wardrobe-container">
    <!-- Header -->
    <div class="wardrobe-header">
      <h2 class="section-title">Your Magical Wardrobe ✨</h2>
      <div class="header-actions">
        <CategoryFilter
            :categories="categories"
            :selected-category="selectedCategory"
            @category-change="onCategoryChange" />
        <button class="add-btn" @click="$emit('add-item')">
          <span class="text-xl">➕</span>
          Add New Item
        </button>
      </div>
    </div>

    <!-- Grid Container -->
    <div ref="gridContainer" class="masonry-grid">
      <ClothingCard
          v-for="item in filteredItems"
          :key="item.id"
          :item="item"
          class="grid-item"
          @click="onItemClick"
          @favorite="onFavorite"
          @edit="onEdit"
          @delete="onDelete"
          @dragstart="onDragStart" />
    </div>

    <!-- Empty state -->
    <div v-if="filteredItems.length === 0" class="empty-state">
      <div class="empty-icon">👗</div>
      <h3 class="empty-title">Your wardrobe is waiting for magic!</h3>
      <p class="empty-description">Upload your first clothing item to get started</p>
      <button class="magic-btn" @click="$emit('add-item')">
        ✨ Add Your First Item
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import ClothingCard from './ClothingCard.vue'
import CategoryFilter from './CategoryFilter.vue'

const props = defineProps({
  items: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['add-item', 'item-click', 'favorite', 'edit', 'delete', 'dragstart'])

const gridContainer = ref(null)
const selectedCategory = ref('all')

const categories = ref([
  { value: 'all', label: '✨ All Items', count: 0 },
  { value: 'top', label: '👚 Tops', count: 0 },
  { value: 'bottom', label: '👖 Bottoms', count: 0 },
  { value: 'shoes', label: '👠 Shoes', count: 0 },
  { value: 'accessory', label: '💍 Accessories', count: 0 },
  { value: 'full_outfit', label: '👗 Full Outfits', count: 0 }
])

const filteredItems = computed(() => {
  if (selectedCategory.value === 'all') {
    return props.items
  }
  return props.items.filter(item =>
      item.category.toLowerCase() === selectedCategory.value
  )
})

const onCategoryChange = (category) => {
  selectedCategory.value = category
}

const onItemClick = (item) => {
  emit('item-click', item)
}

const onFavorite = (item) => {
  emit('favorite', item)
}

const onEdit = (item) => {
  emit('edit', item)
}

const onDelete = (item) => {
  emit('delete', item)
}

const onDragStart = (item) => {
  emit('dragstart', item)
}

// Update category counts
const updateCategoryCounts = () => {
  categories.value.forEach(category => {
    if (category.value === 'all') {
      category.count = props.items.length
    } else {
      category.count = props.items.filter(item =>
          item.category.toLowerCase() === category.value
      ).length
    }
  })
}

onMounted(() => {
  updateCategoryCounts()
})
</script>

<style scoped>
.wardrobe-container {
  width: 100%;
  padding: 2rem;
}

.wardrobe-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ec4899, #8b5cf6, #06b6d4);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 4px 20px rgba(236, 72, 153, 0.3);
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.add-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: linear-gradient(135deg, #f472b6, #ec4899);
  color: white;
  border: none;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(236, 72, 153, 0.4);
  white-space: nowrap;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(236, 72, 153, 0.6);
}

.masonry-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
  width: 100%;
}

@media (max-width: 1600px) {
  .masonry-grid {
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 1.5rem;
  }
}

@media (max-width: 1200px) {
  .masonry-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  }
}

@media (max-width: 768px) {
  .masonry-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 1rem;
  }
}

@media (max-width: 480px) {
  .masonry-grid {
    grid-template-columns: 1fr;
  }

  .wardrobe-header {
    flex-direction: column;
    align-items: stretch;
  }

  .header-actions {
    justify-content: center;
  }
}

.grid-item {
  width: 100%;
  height: fit-content;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(20px);
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  margin: 2rem auto;
  max-width: 600px;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.7;
}

.empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #6b46c1, #ec4899);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.empty-description {
  color: #6b7280;
  margin-bottom: 2rem;
}

.magic-btn {
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #a78bfa, #ec4899);
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 8px 30px rgba(167, 139, 250, 0.4);
}

.magic-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(167, 139, 250, 0.6);
}
</style>

