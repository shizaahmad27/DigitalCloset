<!-- CategoryFilter.vue - Glowy filter component -->
<template>
  <div class="category-filter">
    <div class="filter-tabs">
      <button
        v-for="category in categories"
        :key="category.value"
        :class="['filter-tab', { active: selectedCategory === category.value }]"
        @click="selectCategory(category.value)">
        <span class="tab-label">{{ category.label }}</span>
        <span class="tab-count">{{ category.count }}</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  categories: {
    type: Array,
    required: true
  },
  selectedCategory: {
    type: String,
    default: 'all'
  }
})

const emit = defineEmits(['category-change'])

const selectCategory = (category) => {
  emit('category-change', category)
}
</script>

<style scoped>
.category-filter {
  display: flex;
  align-items: center;
}

.filter-tabs {
  display: flex;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.filter-tab {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 20px;
  background: transparent;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  white-space: nowrap;
}

.filter-tab:hover {
  background: rgba(255, 255, 255, 0.3);
  color: #374151;
  transform: translateY(-1px);
}

.filter-tab.active {
  background: linear-gradient(135deg, #f472b6, #ec4899);
  color: white;
  box-shadow: 0 4px 15px rgba(244, 114, 182, 0.4);
}

.tab-label {
  font-size: 0.875rem;
}

.tab-count {
  background: rgba(255, 255, 255, 0.3);
  padding: 0.125rem 0.5rem;
  border-radius: 10px;
  font-size: 0.75rem;
  font-weight: 600;
}

.filter-tab.active .tab-count {
  background: rgba(255, 255, 255, 0.4);
}
</style>