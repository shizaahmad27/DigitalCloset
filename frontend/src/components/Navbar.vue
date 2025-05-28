<template>
  <nav class="glass-nav sticky top-0 z-50 backdrop-blur-md bg-white/20 border-b border-white/30 w-full">
    <div class="flex items-center justify-between h-20 px-8 sm:px-12 lg:px-16">
      <div class="flex items-center space-x-4">
        <h1 class="text-2xl font-bold bg-gradient-to-r from-pink-500 to-purple-600 bg-clip-text text-transparent">
          ✨ Digital Closet
        </h1>
      </div>
      <div class="flex space-x-8">
        <router-link v-for="link in navLinks" :key="link.name"
                     :to="link.path"
                     class="nav-link text-gray-700 hover:text-pink-600 transition-colors duration-300">
          {{ link.name }}
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'

const navLinks = ref([
  { name: '🏠 Dashboard', path: '/' },
  { name: '👗 Wardrobe', path: '/wardrobe' },
  { name: '✨ Create Outfit', path: '/create-outfit' },
  { name: '💖 Profile', path: '/profile' },
  { name: '🎯 Recommendations', path: '/recommendations' }
])
</script>

<style scoped>
.glass-nav {
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
}

.nav-link {
  position: relative;
  padding: 0.75rem 1.5rem;
  border-radius: 20px;
  transition: all 0.3s ease;
  font-size: 1.15rem;
  font-weight: 500;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  transform: translateY(-2px);
}
</style>
