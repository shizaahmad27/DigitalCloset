<template>
  <div class="wardrobe-view">
    <div class="wardrobe-header">
      <h1 class="wardrobe-title">Your Wardrobe</h1>
      <button
        class="add-item-btn"
        @click="showAddItemModal = true"
      >
        <span class="btn-icon">➕</span>
        Add New Item
      </button>
    </div>

    <WardrobeGrid
      :items="clothingItems"
      @add-item="showAddItemModal = true"
      @item-click="viewItem"
      @favorite="toggleFavorite"
      @edit="editItem"
      @delete="deleteItem"
      @dragstart="handleDragStart"
    />

    <!-- Add Item Modal -->
    <div
      v-if="showAddItemModal"
      class="modal-overlay"
      @click="showAddItemModal = false"
    >
      <div
        class="modal-content"
        @click.stop
      >
        <div class="modal-header">
          <h2 class="modal-title">Add New Item</h2>
          <button
            class="close-btn"
            @click="showAddItemModal = false"
          >
            ✕
          </button>
        </div>

        <form
          class="add-item-form"
          @submit.prevent="addItem"
        >
          <div class="form-group">
            <label class="form-label">Item Image</label>
            <ImageUpload
              @upload="handleImageUpload"
              @error="handleUploadError"
            />
          </div>

          <div class="form-group">
            <label class="form-label">Item Name</label>
            <input
              v-model="newItem.name"
              type="text"
              class="form-input"
              placeholder="Enter item name"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Category</label>
            <select
              v-model="newItem.category"
              class="form-select"
              required
            >
              <option value="">Select a category</option>
              <option value="top">Top</option>
              <option value="bottom">Bottom</option>
              <option value="shoes">Shoes</option>
              <option value="accessory">Accessory</option>
              <option value="full_outfit">Full Outfit</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">Season</label>
            <select
              v-model="newItem.season"
              class="form-select"
              required
            >
              <option value="">Select a season</option>
              <option value="spring">Spring</option>
              <option value="summer">Summer</option>
              <option value="fall">Fall</option>
              <option value="winter">Winter</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">Tags</label>
            <div class="tags-input">
              <span
                v-for="tag in newItem.tags"
                :key="tag"
                class="tag"
              >
                #{{ tag }}
                <button
                  class="remove-tag"
                  @click="removeTag(tag)"
                >
                  ×
                </button>
              </span>
              <input
                v-model="newTag"
                type="text"
                class="tag-input"
                placeholder="Add tags..."
                @keydown.enter.prevent="addTag"
                @blur="addTag"
              />
            </div>
          </div>

          <div class="form-actions">
            <button
              type="button"
              class="cancel-btn"
              @click="showAddItemModal = false"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="submit-btn"
              :disabled="!canSubmit"
            >
              Add Item
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- View Item Modal -->
    <div
      v-if="showViewItemModal"
      class="modal-overlay"
      @click="showViewItemModal = false"
    >
      <div
        class="modal-content"
        @click.stop
      >
        <div class="modal-header">
          <h2 class="modal-title">{{ selectedItem.name }}</h2>
          <button
            class="close-btn"
            @click="showViewItemModal = false"
          >
            ✕
          </button>
        </div>

        <div class="item-details">
          <div class="item-image">
            <img
              :src="selectedItem.imageUrl"
              :alt="selectedItem.name"
            />
          </div>

          <div class="item-info">
            <div class="info-group">
              <span class="info-label">Category</span>
              <span class="info-value">
                {{ getCategoryEmoji(selectedItem.category) }}
                {{ formatCategory(selectedItem.category) }}
              </span>
            </div>

            <div class="info-group">
              <span class="info-label">Season</span>
              <span class="info-value">
                {{ getSeasonEmoji(selectedItem.season) }}
                {{ formatSeason(selectedItem.season) }}
              </span>
            </div>

            <div class="info-group">
              <span class="info-label">Tags</span>
              <div class="item-tags">
                <span
                  v-for="tag in selectedItem.tags"
                  :key="tag"
                  class="tag"
                >
                  #{{ tag }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-actions">
          <button
            class="action-btn edit"
            @click="editItem(selectedItem)"
          >
            ✏️ Edit
          </button>
          <button
            class="action-btn delete"
            @click="deleteItem(selectedItem)"
          >
            🗑️ Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import WardrobeGrid from '../components/WardrobeGrid.vue'
import ImageUpload from '../components/ImageUpload.vue'

// State
const showAddItemModal = ref(false)
const showViewItemModal = ref(false)
const selectedItem = ref(null)
const newItem = ref({
  name: '',
  category: '',
  season: '',
  tags: [],
  imageUrl: null
})
const newTag = ref('')
const clothingItems = ref([
  // Mock data - replace with actual data from your store/API
  {
    id: 1,
    name: 'White T-shirt',
    category: 'top',
    season: 'summer',
    tags: ['casual', 'basic'],
    imageUrl: '/images/tshirt.jpg',
    isFavorite: false
  },
  {
    id: 2,
    name: 'Blue Jeans',
    category: 'bottom',
    season: 'all',
    tags: ['casual', 'denim'],
    imageUrl: '/images/jeans.jpg',
    isFavorite: true
  }
])

// Computed
const canSubmit = computed(() => {
  return newItem.value.name &&
         newItem.value.category &&
         newItem.value.season &&
         newItem.value.imageUrl
})

// Methods
const handleImageUpload = (file) => {
  // In a real app, you would upload the file to your server
  // and get back a URL. For now, we'll use a local URL
  const reader = new FileReader()
  reader.onload = (e) => {
    newItem.value.imageUrl = e.target.result
  }
  reader.readAsDataURL(file)
}

const handleUploadError = (error) => {
  console.error('Upload error:', error)
  // Handle error (show notification, etc.)
}

const addTag = () => {
  const tag = newTag.value.trim()
  if (tag && !newItem.value.tags.includes(tag)) {
    newItem.value.tags.push(tag)
  }
  newTag.value = ''
}

const removeTag = (tag) => {
  newItem.value.tags = newItem.value.tags.filter(t => t !== tag)
}

const addItem = () => {
  // In a real app, you would send this to your API
  const item = {
    id: Date.now(), // Temporary ID
    ...newItem.value,
    isFavorite: false
  }
  clothingItems.value.push(item)
  
  // Reset form
  newItem.value = {
    name: '',
    category: '',
    season: '',
    tags: [],
    imageUrl: null
  }
  showAddItemModal.value = false
}

const viewItem = (item) => {
  selectedItem.value = item
  showViewItemModal.value = true
}

const toggleFavorite = (item) => {
  item.isFavorite = !item.isFavorite
  // In a real app, you would update this in your API
}

const editItem = (item) => {
  // Implement edit functionality
  console.log('Edit item:', item)
}

const deleteItem = (item) => {
  if (confirm('Are you sure you want to delete this item?')) {
    clothingItems.value = clothingItems.value.filter(i => i.id !== item.id)
    showViewItemModal.value = false
    // In a real app, you would delete this from your API
  }
}

const handleDragStart = (item) => {
  // Implement drag and drop functionality
  console.log('Drag started:', item)
}

const getCategoryEmoji = (category) => {
  const emojis = {
    top: '👚',
    bottom: '👖',
    shoes: '👠',
    accessory: '💍',
    full_outfit: '👗'
  }
  return emojis[category.toLowerCase()] || '👕'
}

const formatCategory = (category) => {
  return category
    .split('_')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ')
}

const getSeasonEmoji = (season) => {
  const emojis = {
    spring: '🌸',
    summer: '☀️',
    fall: '🍂',
    winter: '❄️'
  }
  return emojis[season.toLowerCase()] || '🌤️'
}

const formatSeason = (season) => {
  return season.charAt(0).toUpperCase() + season.slice(1)
}
</script>

<style scoped>
.wardrobe-view {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

.wardrobe-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.wardrobe-title {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ec4899, #8b5cf6);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.add-item-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: linear-gradient(135deg, #a78bfa, #ec4899);
  color: white;
  border: none;
  border-radius: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-item-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(139, 92, 246, 0.4);
}

.btn-icon {
  font-size: 1.2rem;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  padding: 2rem;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
}

.close-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: none;
  background: #f3f4f6;
  color: #6b7280;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background: #e5e7eb;
  color: #1f2937;
}

/* Form Styles */
.add-item-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #4b5563;
}

.form-input,
.form-select {
  padding: 0.75rem 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-input:focus,
.form-select:focus {
  outline: none;
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.2);
}

.tags-input {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 0.5rem;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  min-height: 42px;
}

.tag {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.5rem;
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
  border-radius: 8px;
  font-size: 0.9rem;
}

.remove-tag {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(139, 92, 246, 0.2);
  color: #8b5cf6;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  transition: all 0.2s ease;
}

.remove-tag:hover {
  background: rgba(139, 92, 246, 0.3);
}

.tag-input {
  border: none;
  padding: 0.25rem;
  font-size: 0.9rem;
  min-width: 100px;
}

.tag-input:focus {
  outline: none;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

.cancel-btn,
.submit-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 10px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn {
  background: #f3f4f6;
  color: #4b5563;
  border: none;
}

.cancel-btn:hover {
  background: #e5e7eb;
}

.submit-btn {
  background: linear-gradient(135deg, #a78bfa, #ec4899);
  color: white;
  border: none;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(139, 92, 246, 0.3);
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Item Details Styles */
.item-details {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.item-image {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 15px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.info-label {
  font-size: 0.9rem;
  color: #6b7280;
}

.info-value {
  font-size: 1.1rem;
  color: #1f2937;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.item-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.action-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 10px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.action-btn.edit {
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
  border: none;
}

.action-btn.edit:hover {
  background: rgba(139, 92, 246, 0.2);
}

.action-btn.delete {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  border: none;
}

.action-btn.delete:hover {
  background: rgba(239, 68, 68, 0.2);
}

@media (max-width: 768px) {
  .item-details {
    grid-template-columns: 1fr;
  }
}
</style> 