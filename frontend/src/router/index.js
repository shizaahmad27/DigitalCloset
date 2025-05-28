import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import Wardrobe from '../views/Wardrobe.vue'
import OutfitCreator from '../views/OutfitCreator.vue'
import Profile from '../views/Profile.vue'
import Recommendations from '../views/Recommendations.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/wardrobe',
    name: 'Wardrobe',
    component: Wardrobe
  },
  {
    path: '/create-outfit',
    name: 'OutfitCreator',
    component: OutfitCreator
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile
  },
  {
    path: '/recommendations',
    name: 'Recommendations',
    component: Recommendations
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 