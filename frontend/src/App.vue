<!-- App.vue - Main Application -->
<template>
  <div id="app" class="min-h-screen bg-gradient-to-br from-pink-50 via-purple-50 to-rose-100">
    <!-- Floating sparkles background -->
    <div class="sparkles-bg"></div>
    
    <!-- Navigation -->
    <nav class="glass-nav sticky top-0 z-50 backdrop-blur-md bg-white/20 border-b border-white/30">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <div class="flex items-center space-x-4">
            <h1 class="text-2xl font-bold bg-gradient-to-r from-pink-500 to-purple-600 bg-clip-text text-transparent">
              ✨ Digital Closet
            </h1>
          </div>
          <div class="flex space-x-6">
            <router-link v-for="link in navLinks" :key="link.name" 
                        :to="link.path" 
                        class="nav-link text-gray-700 hover:text-pink-600 transition-colors duration-300">
              {{ link.name }}
            </router-link>
          </div>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <router-view />
  </div>
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

<style>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');

* {
  font-family: 'Poppins', sans-serif;
}

.sparkles-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;
  background-image: 
    radial-gradient(2px 2px at 20px 30px, #fbbf24, transparent),
    radial-gradient(2px 2px at 40px 70px, #f472b6, transparent),
    radial-gradient(1px 1px at 90px 40px, #a78bfa, transparent),
    radial-gradient(1px 1px at 130px 80px, #fb7185, transparent),
    radial-gradient(2px 2px at 160px 30px, #fbbf24, transparent);
  background-repeat: repeat;
  background-size: 200px 100px;
  animation: sparkle 20s linear infinite;
}

@keyframes sparkle {
  from { transform: translateY(0px); }
  to { transform: translateY(-100px); }
}

.glass-nav {
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
}

.nav-link {
  position: relative;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  transform: translateY(-2px);
}
</style>