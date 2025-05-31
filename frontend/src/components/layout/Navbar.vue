<template>
  <nav class="glass-nav sticky top-0 z-50 backdrop-blur-md bg-white/20 border-b border-white/30 w-full">
    <div class="flex items-center justify-between h-20 px-8 sm:px-12 lg:px-16">
      <div class="flex items-center space-x-4">
        <router-link to="/" class="text-2xl font-bold bg-gradient-to-r from-pink-500 to-purple-600 bg-clip-text text-transparent">
          ✨ Digital Closet
        </router-link>
      </div>
      
      <!-- Mobile Menu Button -->
      <button @click="toggleMobileMenu" class="lg:hidden p-2 rounded-lg hover:bg-white/20">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path v-if="!isMobileMenuOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>

      <!-- Desktop Navigation Links -->
      <div v-if="isAuthenticated" class="hidden lg:flex space-x-8">
        <router-link v-for="link in navLinks" :key="link.name"
                     :to="link.path"
                     class="nav-link text-gray-700 hover:text-pink-600 transition-colors duration-300">
          {{ link.name }}
        </router-link>
        <button @click="handleLogout" 
                class="nav-link text-gray-700 hover:text-pink-600 transition-colors duration-300">
          👋 Logout
        </button>
      </div>

      <!-- Desktop Auth Links -->
      <div v-else class="hidden lg:flex space-x-4">
        <router-link to="/login" 
                    class="nav-link text-gray-700 hover:text-pink-600 transition-colors duration-300">
          🔑 Login
        </router-link>
        <router-link to="/signup" 
                    class="nav-link text-gray-700 hover:text-pink-600 transition-colors duration-300">
          ✨ Sign Up
        </router-link>
      </div>
    </div>

    <!-- Mobile Menu -->
    <div v-if="isMobileMenuOpen" class="lg:hidden">
      <div class="px-4 pt-2 pb-4 space-y-2 bg-white/20 backdrop-blur-md">
        <template v-if="isAuthenticated">
          <router-link v-for="link in navLinks" :key="link.name"
                       :to="link.path"
                       class="mobile-nav-link block px-4 py-3 text-gray-700 hover:text-pink-600 hover:bg-white/20 rounded-lg transition-colors duration-300">
            {{ link.name }}
          </router-link>
          <button @click="handleLogout" 
                  class="mobile-nav-link w-full text-left px-4 py-3 text-gray-700 hover:text-pink-600 hover:bg-white/20 rounded-lg transition-colors duration-300">
            👋 Logout
          </button>
        </template>
        <template v-else>
          <router-link to="/login" 
                      class="mobile-nav-link block px-4 py-3 text-gray-700 hover:text-pink-600 hover:bg-white/20 rounded-lg transition-colors duration-300">
            🔑 Login
          </router-link>
          <router-link to="/signup" 
                      class="mobile-nav-link block px-4 py-3 text-gray-700 hover:text-pink-600 hover:bg-white/20 rounded-lg transition-colors duration-300">
            ✨ Sign Up
          </router-link>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAuth } from '../../composables/useAuth'

const { isAuthenticated, logout } = useAuth()
const isMobileMenuOpen = ref(false)

const navLinks = ref([
  { name: '🏠 Dashboard', path: '/dashboard' },
  { name: '👗 Wardrobe', path: '/wardrobe' },
  { name: '✨ Create Outfit', path: '/create-outfit' },
  { name: '💖 Profile', path: '/profile' },
  { name: '🎯 Recommendations', path: '/recommendations' }
])

const handleLogout = () => {
  logout()
  isMobileMenuOpen.value = false
}

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}
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

.mobile-nav-link {
  font-size: 1.1rem;
  font-weight: 500;
}
</style>
