<!-- Wardrobe.vue - Fixed main wardrobe view -->
<template>
  <div class="wardrobe-view">
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
            <label>Image</label>
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
            <label class="form-label">Description</label>
            <textarea
                v-model="newItem.description"
                class="form-input"
                placeholder="Enter item description"
                rows="3"
            ></textarea>
          </div>

          <div class="form-group">
            <label class="form-label">Category</label>
            <select
                v-model="newItem.category"
                class="form-select"
                required
            >
              <option value="">Select a category</option>
              <option value="TOP">Top</option>
              <option value="BOTTOM">Bottom</option>
              <option value="DRESS">Dress</option>
              <option value="OUTERWEAR">Outerwear</option>
              <option value="SHOES">Shoes</option>
              <option value="ACCESSORY">Accessory</option>
              <option value="OUTFIT">Outfit</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">Brand</label>
            <input
                v-model="newItem.brand"
                type="text"
                class="form-input"
                placeholder="Enter brand name"
            />
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
            <label class="form-label">Event Types</label>
            <div class="tags-input">
              <span
                  v-for="eventType in newItem.eventTypes"
                  :key="eventType"
                  class="tag"
              >
                #{{ formatEventType(eventType) }}
                <button
                    class="remove-tag"
                    @click="removeEventType(eventType)"
                >
                  ×
                </button>
              </span>
              <select
                  v-model="selectedEventType"
                  class="tag-select"
                  @change="addEventType"
              >
                <option value="">Add event type...</option>
                <option value="DAY">Day</option>
                <option value="NIGHT">Night</option>
                <option value="DATE">Date</option>
                <option value="CASUAL">Casual</option>
                <option value="BEACH">Beach</option>
                <option value="SCHOOL">School</option>
                <option value="FORMAL">Formal</option>
                <option value="SPORTS">Sports</option>
                <option value="WORK">Work</option>
              </select>
            </div>
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
                    v-for="tag in selectedItem.eventTypes"
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
import { ref, computed, onMounted } from 'vue'
import WardrobeGrid from '../components/WardrobeGrid.vue'
import ImageUpload from '@/components/ImageUpload.vue'
import { useGetAll1, useCreate1, useUpdate1, useDelete1, useToggleFavorite } from '../api/generated/clothing-item-controller/clothing-item-controller'
import { uploadImage } from '../services/imageService'

// State
const showAddItemModal = ref(false)
const showViewItemModal = ref(false)
const selectedItem = ref(null)
const newItem = ref({
  name: '',
  category: undefined,
  season: '',
  eventTypes: [],
  tags: [],
  imageUrl: null,
  description: null,
  brand: null
})
const newTag = ref('')
const selectedEventType = ref('')
const page = ref(0)
const size = ref(20)

// API Hooks
const { data: clothingItemsData, refetch: refetchItems } = useGetAll1({
  page: page.value,
  size: size.value,
  sortBy: 'createdAt',
  direction: 'DESC'
})

const createItem = useCreate1({
  mutation: {
    onSuccess: () => {
      refetchItems()
      showAddItemModal.value = false
      resetForm()
    }
  }
})

const updateItem = useUpdate1({
  mutation: {
    onSuccess: () => {
      refetchItems()
      showViewItemModal.value = false
    }
  }
})

const deleteItemMutation = useDelete1({
  mutation: {
    onSuccess: () => {
      refetchItems()
      showViewItemModal.value = false
    }
  }
})

const toggleFavoriteMutation = useToggleFavorite({
  mutation: {
    onSuccess: () => {
      refetchItems()
    }
  }
})

// Computed
const clothingItems = computed(() => clothingItemsData.value?.content || [])
const canSubmit = computed(() => {
  const item = newItem.value
  return item.name.trim() !== '' &&
      item.category &&
      item.season &&
      item.imageUrl &&
      item.eventTypes.length > 0
})

// Methods
const handleImageUpload = async (file) => {
  if (!file) {
    newItem.value.imageUrl = null
    return
  }

  try {
    const imageUrl = await uploadImage(file)
    if (!imageUrl) {
      throw new Error('No image URL returned from server')
    }
    newItem.value.imageUrl = imageUrl
  } catch (error) {
    console.error('Failed to upload image:', error)
    alert(`Failed to upload image: ${error.message || 'Unknown error'}`)
  }
}

const handleUploadError = (error) => {
  alert(error.message)
}

const addEventType = () => {
  if (selectedEventType.value && !newItem.value.eventTypes.includes(selectedEventType.value)) {
    newItem.value.eventTypes.push(selectedEventType.value.toUpperCase())
  }
  selectedEventType.value = ''
}

const removeEventType = (eventType) => {
  newItem.value.eventTypes = newItem.value.eventTypes.filter(t => t !== eventType)
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

const resetForm = () => {
  newItem.value = {
    name: '',
    category: undefined,
    season: '',
    eventTypes: [],
    tags: [],
    imageUrl: null,
    description: null,
    brand: null
  }
  newTag.value = ''
  selectedEventType.value = ''
}

const addItem = () => {
  createItem.mutate({ data: newItem.value })
}

const viewItem = (item) => {
  selectedItem.value = item
  showViewItemModal.value = true
}

const toggleFavorite = (item) => {
  if (item.id) {
    toggleFavoriteMutation.mutate({ id: item.id })
  }
}

const editItem = (item) => {
  selectedItem.value = item
  showAddItemModal.value = true
  newItem.value = { ...item }
}

const deleteItem = (item) => {
  if (confirm('Are you sure you want to delete this item?') && item.id) {
    deleteItemMutation.mutate({ id: item.id })
  }
}

const handleDragStart = (item) => {
  // Implement drag and drop functionality
  console.log('Drag started:', item)
}

const getCategoryEmoji = (category) => {
  const emojis = {
    TOP: '👚',
    BOTTOM: '👖',
    SHOES: '👠',
    ACCESSORY: '💍',
    OUTFIT: '👗'
  }
  return emojis[category] || '👕'
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

const formatEventType = (eventType) => {
  const formatMap = {
    'DAY': 'Day',
    'NIGHT': 'Night',
    'DATE': 'Date',
    'CASUAL': 'Casual',
    'BEACH': 'Beach',
    'SCHOOL': 'School',
    'FORMAL': 'Formal',
    'SPORTS': 'Sports',
    'WORK': 'Work'
  }
  return formatMap[eventType] || eventType
}

// Load initial data
onMounted(() => {
  refetchItems()
})
</script>

<style scoped>
.wardrobe-view {
  width: 100%;
  min-height: calc(100vh - 80px);
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  padding: 2rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
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
  background: linear-gradient(135deg, #6b46c1, #ec4899);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.close-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: none;
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  transform: scale(1.1);
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
  font-weight: 600;
  color: #374151;
}

.form-input,
.form-select {
  padding: 0.75rem 1rem;
  border: 2px solid rgba(139, 92, 246, 0.2);
  border-radius: 15px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
}

.form-input:focus,
.form-select:focus {
  outline: none;
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.2);
  background: rgba(255, 255, 255, 0.9);
}

.tags-input {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 0.75rem;
  border: 2px solid rgba(139, 92, 246, 0.2);
  border-radius: 15px;
  min-height: 50px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
}

.tag {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.75rem;
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.1), rgba(236, 72, 153, 0.1));
  color: #8b5cf6;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
  border: 1px solid rgba(139, 92, 246, 0.2);
}

.remove-tag {
  width: 18px;
  height: 18px;
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
  transform: scale(1.1);
}

.tag-input {
  border: none;
  background: transparent;
  padding: 0.25rem;
  font-size: 0.9rem;
  min-width: 100px;
  flex: 1;
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
  border-radius: 20px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.cancel-btn {
  background: rgba(107, 114, 128, 0.1);
  color: #6b7280;
}

.cancel-btn:hover {
  background: rgba(107, 114, 128, 0.2);
  transform: translateY(-1px);
}

.submit-btn {
  background: linear-gradient(135deg, #a78bfa, #ec4899);
  color: white;
  box-shadow: 0 4px 15px rgba(139, 92, 246, 0.3);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(139, 92, 246, 0.4);
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* Item Details Styles */
.item-details {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-bottom: 2rem;
}

.item-image {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
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
  font-weight: 600;
}

.info-value {
  font-size: 1.1rem;
  color: #1f2937;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 500;
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
  padding-top: 1rem;
  border-top: 1px solid rgba(139, 92, 246, 0.1);
}

.action-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  border: none;
}

.action-btn.edit {
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
  border: 1px solid rgba(139, 92, 246, 0.2);
}

.action-btn.edit:hover {
  background: rgba(139, 92, 246, 0.2);
  transform: translateY(-1px);
}

.action-btn.delete {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  border: 1px solid rgba(239, 68, 68, 0.2);
}

.action-btn.delete:hover {
  background: rgba(239, 68, 68, 0.2);
  transform: translateY(-1px);
}

@media (max-width: 768px) {
  .item-details {
    grid-template-columns: 1fr;
  }

  .modal-actions {
    flex-direction: column;
  }
}

.tag-select {
  border: none;
  background: transparent;
  padding: 0.25rem;
  font-size: 0.9rem;
  min-width: 150px;
  color: #6b7280;
}

.tag-select:focus {
  outline: none;
}

textarea.form-input {
  resize: vertical;
  min-height: 80px;
}
</style>
