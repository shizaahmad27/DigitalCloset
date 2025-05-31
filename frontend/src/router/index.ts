import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import { useAuth } from '../composables/useAuth'
import Dashboard from '../views/Dashboard.vue'
import Wardrobe from '../views/Wardrobe.vue'
import OutfitCreator from '../views/OutfitCreator.vue'
import Profile from '../views/Profile.vue'
import Recommendations from '../views/Recommendations.vue'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiresAuth: false }
  },
  {
    path: '/signup',
    name: 'signup',
    component: SignupView,
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/wardrobe',
    name: 'Wardrobe',
    component: Wardrobe,
    meta: { requiresAuth: true }
  },
  {
    path: '/create-outfit',
    name: 'OutfitCreator',
    component: OutfitCreator,
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '/recommendations',
    name: 'Recommendations',
    component: Recommendations,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const { isAuthenticated } = useAuth()
  
  if (to.meta.requiresAuth && !isAuthenticated()) {
    next('/login')
  } else if (!to.meta.requiresAuth && isAuthenticated()) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router 