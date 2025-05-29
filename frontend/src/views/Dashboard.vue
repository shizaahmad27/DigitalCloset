<!-- Dashboard.vue - Main Dashboard with Weather Recommendations -->
<template>
  <div class="max-w-[1650px] mx-auto px-4 py-8">
    <!-- Welcome Section -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">
          Good {{ getTimeOfDay() }}, Fashion Lover! ✨
        </h1>
        <p class="welcome-subtitle">
          Ready to create some magical looks today?
        </p>
      </div>

      <!-- Updated weather widget with loading/error states -->
      <div class="weather-widget">
        <div class="weather-info">
          <!-- Loading state -->
          <div v-if="weather.loading" class="weather-loading">
            <div class="loading-spinner">🌀</div>
            <div class="weather-details">
              <span class="temperature">Loading...</span>
              <span class="condition">Getting weather</span>
            </div>
          </div>

          <!-- Error state -->
          <div v-else-if="weather.error" class="weather-error">
            <div class="weather-icon">🌤️</div>
            <div class="weather-details">
              <span class="temperature">--°C</span>
              <span class="condition">Weather unavailable</span>
              <button @click="initWeatherWithFallback" class="retry-btn">
                🔄 Retry
              </button>
            </div>
          </div>

          <!-- Success state -->
          <div v-else class="weather-success">
            <div class="weather-icon">{{ weather.icon }}</div>
            <div class="weather-details">
              <span class="temperature">{{ weather.temperature }}°C</span>
              <span class="condition">{{ weather.condition }}</span>
              <span v-if="weather.city" class="location">
                📍 {{ weather.city }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Today's Recommendations -->
    <div class="recommendations-section">
      <h2 class="section-title">
        Perfect for Today 🌟
      </h2>

      <div class="recommendation-cards">
        <div v-for="recommendation in todaysRecommendations"
             :key="recommendation.id"
             class="recommendation-card"
             @click="selectRecommendation(recommendation)">
          <div class="card-header">
            <span class="recommendation-badge">{{ recommendation.reason }}</span>
          </div>

          <div class="outfit-showcase">
            <div v-for="item in recommendation.items"
                 :key="item.id"
                 class="showcase-item">
              <img :src="item.imageUrl" :alt="item.name" class="showcase-image">
            </div>
          </div>

          <div class="card-content">
            <h3 class="outfit-name">{{ recommendation.name }}</h3>
            <div class="outfit-tags">
              <span class="tag">{{ recommendation.style }}</span>
              <span class="tag">{{ recommendation.occasion }}</span>
            </div>
          </div>

          <div class="card-actions">
            <button class="try-btn">✨ Try This Look</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Stats -->
    <div class="stats-section">
      <h2 class="section-title">Your Wardrobe Stats 📊</h2>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">👗</div>
          <div class="stat-number">{{ stats.totalItems }}</div>
          <div class="stat-label">Total Items</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">✨</div>
          <div class="stat-number">{{ stats.outfits }}</div>
          <div class="stat-label">Saved Outfits</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">💖</div>
          <div class="stat-number">{{ stats.favorites }}</div>
          <div class="stat-label">Favorites</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">🌟</div>
          <div class="stat-number">{{ stats.mostWorn }}</div>
          <div class="stat-label">Most Worn</div>
        </div>
      </div>
    </div>

    <!-- Recent Activity -->
    <div class="activity-section">
      <h2 class="section-title">Recent Activity 📱</h2>

      <div class="activity-feed">
        <div v-for="activity in recentActivity"
             :key="activity.id"
             class="activity-item">
          <div class="activity-icon">{{ activity.icon }}</div>
          <div class="activity-content">
            <span class="activity-text">{{ activity.text }}</span>
            <span class="activity-time">{{ activity.time }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Actions -->
    <div class="quick-actions">
      <router-link to="/wardrobe" class="action-btn primary">
        <span class="action-icon">👗</span>
        <span class="action-text">Browse Wardrobe</span>
      </router-link>

      <router-link to="/create-outfit" class="action-btn secondary">
        <span class="action-icon">✨</span>
        <span class="action-text">Create Outfit</span>
      </router-link>

      <button @click="openUploadModal" class="action-btn tertiary">
        <span class="action-icon">📸</span>
        <span class="action-text">Add Items</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Weather state
const weather = ref({
  temperature: null,
  condition: '',
  icon: '🌤️',
  loading: true,
  error: null
})

// Weather icon mapping
const weatherIcons = {
  'clear sky': '☀️',
  'few clouds': '🌤️',
  'scattered clouds': '⛅',
  'broken clouds': '☁️',
  'shower rain': '🌦️',
  'rain': '🌧️',
  'thunderstorm': '⛈️',
  'snow': '❄️',
  'mist': '🌫️'
}

// Get user's location
const getUserLocation = () => {
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      reject(new Error('Geolocation is not supported by this browser'))
      return
    }

    navigator.geolocation.getCurrentPosition(
        (position) => {
          resolve({
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
          })
        },
        (error) => {
          reject(new Error(`Location error: ${error.message}`))
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 300000 // 5 minutes
        }
    )
  })
}

// Fetch weather data from OpenWeatherMap
const fetchWeatherData = async (lat, lon) => {
  const API_KEY = 'YOUR_API_KEY_HERE' // Replace with your actual API key
  const url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`

  try {
    const response = await fetch(url)

    if (!response.ok) {
      throw new Error(`Weather API error: ${response.status}`)
    }

    const data = await response.json()

    return {
      temperature: Math.round(data.main.temp),
      condition: data.weather[0].description,
      icon: weatherIcons[data.weather[0].description] || '🌤️',
      humidity: data.main.humidity,
      windSpeed: data.wind.speed,
      city: data.name,
      country: data.sys.country
    }
  } catch (error) {
    throw new Error(`Failed to fetch weather: ${error.message}`)
  }
}

// Alternative: IP-based location weather (no user permission needed)
const fetchWeatherByIP = async () => {
  try {
    // First get location by IP
    const ipResponse = await fetch('https://ipapi.co/json/')
    const ipData = await ipResponse.json()

    // Then get weather for that location
    const weatherData = await fetchWeatherData(ipData.latitude, ipData.longitude)

    return weatherData
  } catch (error) {
    throw new Error(`IP-based weather failed: ${error.message}`)
  }
}

// Initialize weather with fallback
const initWeatherWithFallback = async () => {
  try {
    weather.value.loading = true

    try {
      // Try GPS first
      const location = await getUserLocation()
      const weatherData = await fetchWeatherData(location.latitude, location.longitude)
      weather.value = { ...weatherData, loading: false, error: null }
    } catch (gpsError) {
      console.log('GPS failed, trying IP location:', gpsError.message)

      // Fallback to IP-based location
      const weatherData = await fetchWeatherByIP()
      weather.value = { ...weatherData, loading: false, error: null }
    }

  } catch (error) {
    console.error('All weather methods failed:', error)
    weather.value = {
      temperature: '--',
      condition: 'Weather unavailable',
      icon: '🌤️',
      loading: false,
      error: error.message
    }
  }
}

// Optional: Refresh weather data periodically
const startWeatherRefresh = () => {
  // Refresh every 30 minutes
  setInterval(() => {
    initWeatherWithFallback()
  }, 30 * 60 * 1000)
}

// Sample data
const stats = ref({
  totalItems: 127,
  outfits: 23,
  favorites: 45,
  mostWorn: 8
})

const todaysRecommendations = ref([
  {
    id: 1,
    name: 'Casual Chic',
    reason: 'Perfect for today\'s weather',
    style: 'Casual',
    occasion: 'Daily',
    items: [
      { id: 1, name: 'White Tee', imageUrl: '/api/placeholder/100/120' },
      { id: 2, name: 'Blue Jeans', imageUrl: '/api/placeholder/100/120' },
      { id: 3, name: 'White Sneakers', imageUrl: '/api/placeholder/100/120' }
    ]
  },
  {
    id: 2,
    name: 'Date Night Glam',
    reason: 'You have dinner planned',
    style: 'Elegant',
    occasion: 'Date',
    items: [
      { id: 4, name: 'Black Dress', imageUrl: '/api/placeholder/100/120' },
      { id: 5, name: 'Heels', imageUrl: '/api/placeholder/100/120' },
      { id: 6, name: 'Statement Necklace', imageUrl: '/api/placeholder/100/120' }
    ]
  },
  {
    id: 3,
    name: 'Work Ready',
    reason: 'Professional & comfortable',
    style: 'Professional',
    occasion: 'Work',
    items: [
      { id: 7, name: 'Blazer', imageUrl: '/api/placeholder/100/120' },
      { id: 8, name: 'Trousers', imageUrl: '/api/placeholder/100/120' },
      { id: 9, name: 'Loafers', imageUrl: '/api/placeholder/100/120' }
    ]
  }
])

const recentActivity = ref([
  { id: 1, icon: '✨', text: 'Created "Summer Vibes" outfit', time: '2 hours ago' },
  { id: 2, icon: '📸', text: 'Added 3 new items to wardrobe', time: '5 hours ago' },
  { id: 3, icon: '💖', text: 'Favorited pink sundress', time: '1 day ago' },
  { id: 4, icon: '🌟', text: 'Wore "Office Chic" outfit', time: '2 days ago' }
])

// Utility functions
const getTimeOfDay = () => {
  const hour = new Date().getHours()
  if (hour < 12) return 'Morning'
  if (hour < 17) return 'Afternoon'
  return 'Evening'
}

const selectRecommendation = (recommendation) => {
  console.log('Selected recommendation:', recommendation)
}

const openUploadModal = () => {
  console.log('Opening upload modal')
}

// Initialize on component mount
onMounted(() => {
  initWeatherWithFallback()
  // Optionally start auto-refresh
  // startWeatherRefresh()
})
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 3rem;
}

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(30px);
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 2rem;
  position: relative;
  overflow: hidden;
}

.welcome-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.1), rgba(139, 92, 246, 0.1));
  pointer-events: none;
}

.welcome-content {
  position: relative;
  z-index: 1;
}

.welcome-title {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ec4899, #8b5cf6, #06b6d4);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 0.5rem;
}

.welcome-subtitle {
  font-size: 1.2rem;
  color: #6b7280;
  font-weight: 500;
}

.weather-widget {
  position: relative;
  z-index: 1;
}

.weather-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(15px);
  border-radius: 20px;
  padding: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.4);
}

.weather-icon {
  font-size: 3rem;
}

.weather-details {
  display: flex;
  flex-direction: column;
}

.temperature {
  font-size: 1.5rem;
  font-weight: 700;
  color: #374151;
}

.condition {
  font-size: 1rem;
  color: #6b7280;
  font-weight: 500;
}

/* Weather loading and error states */
.weather-loading {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.loading-spinner {
  font-size: 2rem;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.weather-error .retry-btn {
  background: rgba(239, 68, 68, 0.8);
  color: white;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 10px;
  font-size: 0.75rem;
  cursor: pointer;
  margin-top: 0.25rem;
  transition: all 0.2s ease;
}

.weather-error .retry-btn:hover {
  background: rgba(239, 68, 68, 1);
  transform: scale(1.05);
}

.location {
  font-size: 0.8rem;
  color: #9ca3af;
  display: block;
  margin-top: 0.25rem;
}

.weather-success {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  background: linear-gradient(135deg, #6b46c1, #ec4899);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 1.5rem;
}

.recommendation-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
}

.recommendation-card {
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  position: relative;
}

.recommendation-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0));
  pointer-events: none;
}

.recommendation-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.15);
}

.card-header {
  padding: 1.5rem 1.5rem 0;
  position: relative;
  z-index: 1;
}

.recommendation-badge {
  background: linear-gradient(135deg, #f472b6, #ec4899);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 600;
}

.outfit-showcase {
  display: flex;
  justify-content: center;
  gap: 1rem;
  padding: 1.5rem;
  position: relative;
  z-index: 1;
}

.showcase-item {
  flex: 1;
  max-width: 80px;
}

.showcase-image {
  width: 100%;
  aspect-ratio: 3/4;
  object-fit: cover;
  border-radius: 15px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.recommendation-card:hover .showcase-image {
  transform: scale(1.05);
}

.card-content {
  padding: 0 1.5rem 1rem;
  text-align: center;
  position: relative;
  z-index: 1;
}

.outfit-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.75rem;
}

.outfit-tags {
  display: flex;
  gap: 0.5rem;
  justify-content: center;
  margin-bottom: 1rem;
}

.tag {
  background: rgba(167, 139, 250, 0.8);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 15px;
  font-size: 0.75rem;
  font-weight: 500;
}

.card-actions {
  padding: 0 1.5rem 1.5rem;
  position: relative;
  z-index: 1;
}

.try-btn {
  width: 100%;
  padding: 0.75rem;
  background: linear-gradient(135deg, #a78bfa, #ec4899);
  color: white;
  border: none;
  border-radius: 20px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.try-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(167, 139, 250, 0.4);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

.stat-card {
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  padding: 2rem;
  text-align: center;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  background: linear-gradient(135deg, #ec4899, #8b5cf6);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #6b7280;
  font-weight: 500;
}

.activity-feed {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 1.5rem;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  background: rgba(236, 72, 153, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.activity-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.activity-text {
  color: #374151;
  font-weight: 500;
}

.activity-time {
  color: #9ca3af;
  font-size: 0.875rem;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  border-radius: 25px;
  text-decoration: none;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  border: none;
  cursor: pointer;
  font-family: inherit;
}

.action-btn.primary {
  background: linear-gradient(135deg, #ec4899, #f472b6);
  color: white;
  box-shadow: 0 8px 30px rgba(236, 72, 153, 0.4);
}

.action-btn.secondary {
  background: linear-gradient(135deg, #8b5cf6, #a78bfa);
  color: white;
  box-shadow: 0 8px 30px rgba(139, 92, 246, 0.4);
}

.action-btn.tertiary {
  background: linear-gradient(135deg, #06b6d4, #0891b2);
  color: white;
  box-shadow: 0 8px 30px rgba(6, 182, 212, 0.4);
}

.action-btn:hover {
  transform: translateY(-8px) scale(1.03);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.2);
}

.action-icon {
  font-size: 3rem;
}

.action-text {
  font-size: 1.1rem;
  font-weight: 600;
}

@media (max-width: 768px) {
  .welcome-section {
    flex-direction: column;
    gap: 1.5rem;
    text-align: center;
  }

  .recommendation-cards {
    grid-template-columns: 1fr;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }
}
</style>
