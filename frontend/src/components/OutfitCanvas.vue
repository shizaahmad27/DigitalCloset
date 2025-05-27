<template>
  <div
    class="outfit-canvas"
    @dragover.prevent
    @drop="handleDrop"
  >
    <div class="canvas-header">
      <h2 class="canvas-title">Create Your Outfit ✨</h2>
      <div class="canvas-actions">
        <button
          class="action-btn save"
          :disabled="!canSave"
          @click="$emit('save')"
        >
          💾 Save Outfit
        </button>
        <button
          class="action-btn clear"
          :disabled="!hasItems"
          @click="$emit('clear')"
        >
          🗑️ Clear Canvas
        </button>
      </div>
    </div>

    <div class="canvas-content">
      <div
        v-if="!hasItems"
        class="empty-canvas"
      >
        <div class="empty-icon">👗</div>
        <h3>Drag & Drop Your Items</h3>
        <p>Start building your outfit by dragging items from your wardrobe</p>
      </div>

      <div
        v-else
        class="items-container"
      >
        <div
          v-for="(item, index) in items"
          :key="item.id"
          class="canvas-item"
          :style="getItemStyle(item)"
          @click="$emit('item-click', item)"
        >
          <img :src="item.imageUrl" :alt="item.name" />
          <div class="item-overlay">
            <button
              class="remove-btn"
              @click.stop="$emit('remove-item', index)"
            >
              ✕
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="canvas-footer">
      <div class="outfit-info">
        <input
          v-model="outfitName"
          type="text"
          placeholder="Name your outfit..."
          class="outfit-name-input"
          @input="$emit('name-change', outfitName)"
        />
        <div class="outfit-tags">
          <span
            v-for="tag in outfitTags"
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
            placeholder="Add tags..."
            class="tag-input"
            @keydown.enter="addTag"
            @blur="addTag"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  items: {
    type: Array,
    default: () => []
  },
  initialName: {
    type: String,
    default: ''
  },
  initialTags: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits([
  'save',
  'clear',
  'item-click',
  'remove-item',
  'name-change',
  'tags-change'
])

const outfitName = ref(props.initialName)
const outfitTags = ref([...props.initialTags])
const newTag = ref('')

const hasItems = computed(() => props.items.length > 0)
const canSave = computed(() => hasItems.value && outfitName.value.trim().length > 0)

const handleDrop = (event) => {
  try {
    const item = JSON.parse(event.dataTransfer.getData('text/plain'))
    emit('drop', item)
  } catch (error) {
    console.error('Error handling dropped item:', error)
  }
}

const getItemStyle = (item) => {
  return {
    transform: `rotate(${item.rotation || 0}deg) scale(${item.scale || 1})`,
    zIndex: item.zIndex || 1
  }
}

const addTag = () => {
  const tag = newTag.value.trim()
  if (tag && !outfitTags.value.includes(tag)) {
    outfitTags.value.push(tag)
    emit('tags-change', outfitTags.value)
  }
  newTag.value = ''
}

const removeTag = (tag) => {
  outfitTags.value = outfitTags.value.filter(t => t !== tag)
  emit('tags-change', outfitTags.value)
}
</script>

<style scoped>
.outfit-canvas {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 30px;
  padding: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  min-height: 600px;
}

.canvas-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.canvas-title {
  font-size: 2rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ec4899, #8b5cf6);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.canvas-actions {
  display: flex;
  gap: 1rem;
}

.action-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.action-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.action-btn.save {
  background: linear-gradient(135deg, #a78bfa, #ec4899);
  color: white;
  box-shadow: 0 4px 15px rgba(139, 92, 246, 0.3);
}

.action-btn.save:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(139, 92, 246, 0.4);
}

.action-btn.clear {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.action-btn.clear:hover:not(:disabled) {
  background: rgba(239, 68, 68, 0.2);
}

.canvas-content {
  flex: 1;
  min-height: 400px;
  border: 2px dashed rgba(139, 92, 246, 0.3);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.empty-canvas {
  text-align: center;
  color: #6b7280;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.7;
}

.items-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  padding: 2rem;
}

.canvas-item {
  position: absolute;
  cursor: move;
  transition: transform 0.3s ease;
}

.canvas-item img {
  max-width: 200px;
  max-height: 200px;
  object-fit: contain;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.1));
}

.item-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.canvas-item:hover .item-overlay {
  opacity: 1;
}

.remove-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.9);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  transition: all 0.2s ease;
}

.remove-btn:hover {
  transform: scale(1.1);
  background: #ef4444;
}

.canvas-footer {
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  padding-top: 1.5rem;
}

.outfit-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.outfit-name-input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid rgba(139, 92, 246, 0.3);
  border-radius: 15px;
  font-size: 1.1rem;
  transition: all 0.3s ease;
}

.outfit-name-input:focus {
  outline: none;
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.2);
}

.outfit-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.5rem 0.75rem;
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
  border-radius: 12px;
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
  padding: 0.5rem 0.75rem;
  border: 2px solid transparent;
  border-radius: 12px;
  font-size: 0.9rem;
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
  min-width: 100px;
}

.tag-input:focus {
  outline: none;
  border-color: #8b5cf6;
}
</style> 