<!-- ClothingCard.vue - Pinterest-style clothing item card -->
<template>
  <div class="clothing-card group cursor-pointer" 
       :draggable="draggable"
       @dragstart="onDragStart"
       @click="$emit('click', item)">
    <!-- Main image container -->
    <div class="card-image-container">
      <img :src="getImageUrl(item.imageUrl)" 
           :alt="item.name" 
           class="card-image"
           @load="onImageLoad">
      
      <!-- Overlay with quick actions -->
      <div class="overlay">
        <div class="overlay-actions">
          <button class="action-btn" @click.stop="$emit('favorite', item)">
            <span class="text-xl">💖</span>
          </button>
          <button class="action-btn" @click.stop="$emit('edit', item)">
            <span class="text-xl">✏️</span>
          </button>
          <button class="action-btn" @click.stop="$emit('delete', item)">
            <span class="text-xl">🗑️</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Card content -->
    <div class="card-content">
      <h3 class="card-title">{{ item.name }}</h3>
      <div class="card-tags">
        <span class="tag category-tag">{{ item.category }}</span>
        <span v-for="eventType in item.eventTypes" :key="eventType" 
              class="tag event-tag">{{ eventType }}</span>
      </div>
      <div class="card-details">
        <span class="detail-item">{{ item.brand }}</span>
        <span class="detail-item">{{ item.season }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const props = defineProps({
  item: {
    type: Object,
    required: true
  },
  draggable: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['click', 'favorite', 'edit', 'delete', 'dragstart'])

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return `${API_URL}${url}`
}

const onDragStart = (event) => {
  event.dataTransfer.setData('text/plain', JSON.stringify(props.item))
  emit('dragstart', props.item)
}

const onImageLoad = (event) => {
  event.target.classList.add('loaded')
}
</script>

<style scoped>
.clothing-card {
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 
    0 8px 32px 0 rgba(31, 38, 135, 0.37),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  position: relative;
}

.clothing-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 
    0 20px 60px 0 rgba(31, 38, 135, 0.5),
    0 0 40px rgba(236, 72, 153, 0.3),
    inset 0 1px 0 rgba(255, 255, 255, 0.6);
}

.clothing-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0));
  border-radius: 20px;
  pointer-events: none;
}

.card-image-container {
  position: relative;
  width: 100%;
  aspect-ratio: 3/4;
  overflow: hidden;
  border-radius: 16px 16px 0 0;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease, filter 0.4s ease;
  opacity: 0;
}

.card-image.loaded {
  opacity: 1;
}

.clothing-card:hover .card-image {
  transform: scale(1.1);
  filter: brightness(1.1) saturate(1.2);
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0) 0%,
    rgba(0, 0, 0, 0.1) 50%,
    rgba(0, 0, 0, 0.3) 100%
  );
  opacity: 0;
  transition: opacity 0.3s ease;
  display: flex;
  align-items: flex-end;
  padding: 1rem;
}

.clothing-card:hover .overlay {
  opacity: 1;
}

.overlay-actions {
  display: flex;
  gap: 0.5rem;
  margin-left: auto;
}

.action-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.action-btn:hover {
  background: rgba(255, 255, 255, 1);
  transform: scale(1.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.card-content {
  padding: 1rem;
}

.card-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #6b46c1, #ec4899);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem;
  margin-bottom: 0.75rem;
}

.tag {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 500;
  backdrop-filter: blur(10px);
}

.category-tag {
  background: linear-gradient(135deg, #f472b6, #ec4899);
  color: white;
}

.event-tag {
  background: rgba(167, 139, 250, 0.8);
  color: white;
}

.card-details {
  display: flex;
  justify-content: space-between;
  font-size: 0.875rem;
  color: #6b7280;
}

.detail-item {
  background: rgba(255, 255, 255, 0.5);
  padding: 0.25rem 0.5rem;
  border-radius: 10px;
  backdrop-filter: blur(5px);
}
</style>