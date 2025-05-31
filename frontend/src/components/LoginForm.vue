<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-pink-50 via-purple-50 to-cyan-50 px-4">
    <form
        @submit="onSubmit"
        class="w-full max-w-md p-8 bg-white/30 backdrop-blur-xl border border-white/40 rounded-3xl shadow-xl space-y-6"
    >
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold bg-gradient-to-r from-pink-500 via-purple-600 to-cyan-500 bg-clip-text text-transparent mb-2">
          Welcome Back
        </h1>
        <p class="text-gray-600 text-sm">Sign in to your style sanctuary ✨</p>
      </div>

      <!-- Form-level error alert -->
      <Alert v-if="formError" variant="destructive" class="bg-red-50/80 backdrop-blur-sm border-red-300 text-red-700 rounded-xl">
        <AlertCircle class="h-4 w-4" />
        <AlertDescription class="ml-2">{{ formError }}</AlertDescription>
      </Alert>

      <!-- Email Field -->
      <FormField v-slot="{ componentField }" name="email">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-2">Email</FormLabel>
          <FormControl>
            <div class="relative">
              <Mail
                  class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-600 h-5 w-5"
              />
              <Input
                  type="email"
                  placeholder="name@example.com"
                  class="w-full px-3 py-3 pl-10 bg-white/50 border border-white/60 rounded-xl shadow-sm focus:outline-none focus:ring-2 focus:ring-pink-400 focus:border-transparent backdrop-blur-sm"
                  v-bind="componentField"
                  autocomplete="username"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500 mt-1" />
        </FormItem>
      </FormField>

      <!-- Password Field using component -->
      <FormField v-slot="{ componentField }" name="password">
        <PasswordInput
            name="password"
            label="Password"
            placeholder="********"
            :componentField="componentField"
            :showToggle="true"
            :showIcon="true"
            autocomplete="current-password"
            class="password-input-styled"
        />
      </FormField>

      <!-- Submit Button -->
      <Button
          type="submit"
          :disabled="((!meta.valid || !meta.dirty) && !hasAttemptedLogin) || isLoading"
          class="w-full bg-gradient-to-r from-pink-500 to-purple-600 hover:from-pink-600 hover:to-purple-700 disabled:from-gray-400 disabled:to-gray-500 disabled:cursor-not-allowed text-white py-3 rounded-xl text-sm font-semibold transition-all duration-300 transform hover:scale-[1.02] hover:shadow-lg disabled:hover:scale-100 disabled:hover:shadow-none"
      >
        <template v-if="isLoading">
          <span class="flex items-center justify-center">
            <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            Signing in...
          </span>
        </template>
        <template v-else>Sign In ✨</template>
      </Button>

      <!-- Bottom links -->
      <div class="text-sm text-center space-y-3 pt-4 border-t border-white/30">
        <div>
          <span class="text-gray-600">Don't have an account?</span>
          <router-link
              to="/signup"
              class="ml-1 text-pink-600 hover:text-pink-700 font-medium hover:underline transition-colors duration-200"
          >
            Sign up
          </router-link>
        </div>

        <router-link
            to="/forgot-password"
            class="block text-purple-600 hover:text-purple-700 hover:underline transition-colors duration-200 font-medium"
        >
          Forgot your password?
        </router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Mail, AlertCircle } from 'lucide-vue-next'
import { useRoute } from 'vue-router'

import { Button } from '@/components/ui/button'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import PasswordInput from '@/components/auth/PasswordInput.vue'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { useLoginForm } from '@/utils/auth/useLoginForm'

const route = useRoute()
const redirectPath = computed(() => (route.query.redirect as string) || '/dashboard')
const { isLoading, hasAttemptedLogin, formError, meta, onSubmit } = useLoginForm(redirectPath.value)
</script>

<style scoped>
/* Additional styles for the PasswordInput component to match */
:deep(.password-input-styled input) {
  @apply bg-white/50 border-white/60 rounded-xl backdrop-blur-sm py-3;
  @apply focus:ring-2 focus:ring-pink-400 focus:border-transparent;
}

:deep(.password-input-styled .form-label) {
  @apply text-gray-700 font-medium mb-2;
}

/* Make password input icons more visible */
:deep(.password-input-styled svg) {
  color: #4b5563 !important; /* gray-600 */
  width: 1.25rem !important; /* h-5 w-5 */
  height: 1.25rem !important;
}

/* Fix for label color issues - force labels to stay gray */
label,
:deep(label),
:deep(.form-label),
.form-label {
  color: #374151 !important;
  font-weight: 500 !important;
}

/* Ensure labels stay visible in all states */
:deep(.form-item label),
:deep(.form-field label) {
  color: #374151 !important;
}

/* Fix specific form validation states */
:deep(.form-item[data-valid] label),
:deep(.form-item[data-invalid] label),
:deep(.form-field[data-valid] label),
:deep(.form-field[data-invalid] label) {
  color: #374151 !important;
}

/* Custom focus styles for better visual consistency */
input:focus,
button:focus {
  outline: none;
}

/* Loading spinner animation */
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.animate-spin {
  animation: spin 1s linear infinite;
}

/* Enhanced glassmorphism effect */
.backdrop-blur-xl {
  backdrop-filter: blur(20px);
}

/* Smooth transitions for all interactive elements */
a, button {
  transition: all 0.2s ease-in-out;
}
</style>
