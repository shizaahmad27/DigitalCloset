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

<script setup lang="ts">
interface Category {
  value: string
  label: string
  count: number
}

interface Props {
  categories: Category[]
  selectedCategory: string
}

const props = withDefaults(defineProps<Props>(), {
  selectedCategory: 'all'
})

const emit = defineEmits<{
  'category-change': [category: string]
}>()

const selectCategory = (category: string) => {
  emit('category-change', category)
}
</script>

<style scoped>
.category-filter {
  @apply flex items-center;
}

.filter-tabs {
  @apply flex gap-2 bg-white/20 backdrop-blur-xl rounded-full p-2 border border-white/30;
}

.filter-tab {
  @apply flex items-center gap-2 px-4 py-2 border-none rounded-full bg-transparent text-gray-500 cursor-pointer transition-all duration-300 font-medium whitespace-nowrap;
}

.filter-tab:hover {
  @apply bg-white/30 text-gray-700 -translate-y-0.5;
}

.filter-tab.active {
  @apply bg-gradient-to-r from-pink-400 to-pink-500 text-white shadow-lg shadow-pink-400/40;
}

.tab-label {
  @apply text-sm;
}

.tab-count {
  @apply bg-white/30 px-2 py-0.5 rounded-full text-xs font-semibold;
}

.filter-tab.active .tab-count {
  @apply bg-white/40;
}
</style>