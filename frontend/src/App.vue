<!-- App.vue - Main Application -->
<template>
  <div id="app" class="min-h-screen bg-gradient-to-br from-pink-50 via-purple-50 to-rose-100">
    <!-- Floating sparkles background -->
    <div class="sparkles-bg"></div>

    <!-- Navigation -->
   <navbar/>

    <!-- Main Content -->
    <router-view />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Navbar from './components/layout/Navbar.vue'

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
