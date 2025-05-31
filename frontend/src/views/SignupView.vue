<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-pink-50 via-purple-50 to-cyan-50 py-12 px-4">
    <form
        @submit="onSubmit"
        class="w-full max-w-md p-8 bg-white/30 backdrop-blur-xl border border-white/40 rounded-3xl shadow-xl space-y-6"
    >
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold bg-gradient-to-r from-pink-500 via-purple-600 to-cyan-500 bg-clip-text text-transparent mb-2">
          Create Account
        </h1>
        <p class="text-gray-600 text-sm">Join the fashion revolution ✨</p>
      </div>

      <!-- First Name -->
      <FormField v-slot="{ componentField }" name="firstname">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-2">First Name</FormLabel>
          <FormControl>
            <div class="relative">
              <User
                  class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-600 h-5 w-5"
              />
              <Input
                  type="text"
                  placeholder="John"
                  class="w-full px-3 py-3 pl-10 bg-white/50 border border-white/60 rounded-xl shadow-sm focus:outline-none focus:ring-2 focus:ring-pink-400 focus:border-transparent backdrop-blur-sm"
                  v-bind="componentField"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500 mt-1" />
        </FormItem>
      </FormField>

      <!-- Last Name -->
      <FormField v-slot="{ componentField }" name="lastname">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-2">Last Name</FormLabel>
          <FormControl>
            <div class="relative">
              <User
                  class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-600 h-5 w-5"
              />
              <Input
                  type="text"
                  placeholder="Doe"
                  class="w-full px-3 py-3 pl-10 bg-white/50 border border-white/60 rounded-xl shadow-sm focus:outline-none focus:ring-2 focus:ring-pink-400 focus:border-transparent backdrop-blur-sm"
                  v-bind="componentField"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500 mt-1" />
        </FormItem>
      </FormField>

      <!-- Username -->
      <FormField v-slot="{ componentField }" name="username">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-2">Username</FormLabel>
          <FormControl>
            <div class="relative">
              <User
                  class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-600 h-5 w-5"
              />
              <Input
                  type="text"
                  placeholder="johndoe"
                  class="w-full px-3 py-3 pl-10 bg-white/50 border border-white/60 rounded-xl shadow-sm focus:outline-none focus:ring-2 focus:ring-pink-400 focus:border-transparent backdrop-blur-sm"
                  v-bind="componentField"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500 mt-1" />
        </FormItem>
      </FormField>

      <!-- Email -->
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
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500 mt-1" />
        </FormItem>
      </FormField>

      <!-- Password field using component -->
      <FormField v-slot="{ componentField }" name="password">
        <PasswordInput
            name="password"
            label="Password"
            placeholder="********"
            :componentField="componentField"
            :showToggle="true"
            :showIcon="true"
            class="password-input-styled"
        />
      </FormField>

      <!-- Confirm Password using component -->
      <FormField v-slot="{ componentField }" name="confirmPassword">
        <PasswordInput
            name="confirmPassword"
            label="Confirm Password"
            placeholder="********"
            :componentField="componentField"
            :showToggle="true"
            :showIcon="true"
            class="password-input-styled"
        />
      </FormField>

      <!-- Accept Privacy Policy -->
      <FormField v-slot="{ value, handleChange }" name="acceptedPrivacyPolicy" type="checkbox">
        <FormItem>
          <div class="flex items-start space-x-3">
            <FormControl>
              <input
                  type="checkbox"
                  :checked="value"
                  @change="handleChange(($event.target as HTMLInputElement)?.checked ?? false)"
                  @keydown.enter.prevent="handleChange(!value)"
                  @keydown.space.prevent="handleChange(!value)"
                  id="acceptedPrivacyPolicy"
                  class="mt-1 h-4 w-4 rounded border-gray-300 text-pink-500 focus:ring-pink-400 focus:ring-2"
                  tabindex="0"
                  role="checkbox"
                  :aria-checked="value"
                  aria-label="Accept privacy policy"
              />
            </FormControl>
            <label for="acceptedPrivacyPolicy" class="text-sm text-gray-700 cursor-pointer select-none leading-relaxed">
              I accept the
              <router-link
                  to="/privacy"
                  class="text-pink-600 hover:text-pink-700 hover:underline focus:outline-none focus:ring-2 focus:ring-pink-400 focus:ring-offset-2 rounded-sm px-1 font-medium"
                  tabindex="0"
                  aria-label="Open privacy policy"
              >
                privacy policy
              </router-link>
            </label>
          </div>
          <FormMessage class="text-sm text-red-500 mt-1" />
        </FormItem>
      </FormField>

      <!-- Submit button -->
      <Button
          type="submit"
          :disabled="!meta.valid || isLoading"
          class="w-full bg-gradient-to-r from-pink-500 to-purple-600 hover:from-pink-600 hover:to-purple-700 disabled:from-gray-400 disabled:to-gray-500 disabled:cursor-not-allowed text-white py-3 rounded-xl text-sm font-semibold transition-all duration-300 transform hover:scale-[1.02] hover:shadow-lg disabled:hover:scale-100 disabled:hover:shadow-none"
      >
        <template v-if="isLoading">
          <span class="flex items-center justify-center">
            <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            Creating account...
          </span>
        </template>
        <template v-else>Create Account ✨</template>
      </Button>

      <!-- Conditional CTAs below -->
      <div class="text-sm text-center pt-4 border-t border-white/30">
        <span class="text-gray-600">Already have an account?</span>
        <router-link
            to="/login"
            class="ml-1 text-pink-600 hover:text-pink-700 font-medium hover:underline transition-colors duration-200"
        >
          Sign in
        </router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'
import { User, Mail } from 'lucide-vue-next'
import { useToast } from '@/components/ui/toast/use-toast'

import { Button } from '@/components/ui/button'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import PasswordInput from '@/components/auth/PasswordInput.vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { signup } = useAuth()

// Schema for the registration form
const schema = toTypedSchema(
    z.object({
      firstname: z.string({ required_error: 'First name is required' })
          .min(2, 'First name must be at least 2 characters'),
      lastname: z.string({ required_error: 'Last name is required' })
          .min(2, 'Last name must be at least 2 characters'),
      username: z.string({ required_error: 'Username is required' })
          .min(3, 'Username must be at least 3 characters'),
      email: z.string({ required_error: 'Email is required' })
          .email('Invalid email address'),
      password: z.string({ required_error: 'Password is required' })
          .min(8, 'Password must be at least 8 characters')
          .regex(/[A-Z]/, 'Must contain at least one uppercase letter')
          .regex(/[a-z]/, 'Must contain at least one lowercase letter')
          .regex(/[0-9]/, 'Must contain at least one number')
          .regex(/[^A-Za-z0-9]/, 'Must contain at least one special character'),
      confirmPassword: z.string({ required_error: 'Please confirm your password' }),
      acceptedPrivacyPolicy: z.literal(true, {
        errorMap: () => ({ message: 'You must accept the privacy policy' }),
      }),
    }).refine((data) => data.password === data.confirmPassword, {
      message: 'Passwords do not match',
      path: ['confirmPassword'],
    })
)

const { handleSubmit, meta } = useForm({
  validationSchema: schema,
})

// Loading state
const isLoading = ref(false)

const onSubmit = handleSubmit(async (values) => {
  if (isLoading.value) return

  isLoading.value = true
  try {
    const { confirmPassword, acceptedPrivacyPolicy, ...registrationData } = values
    await signup(registrationData)
  } catch (error: any) {
    console.error('Registration error:', error)
  } finally {
    isLoading.value = false
  }
})
</script>

<style scoped>
/* Additional styles for the PasswordInput component to match */
:deep(.password-input-styled input) {
  @apply bg-white/50 border-white/60 rounded-xl backdrop-blur-sm;
  @apply focus:ring-2 focus:ring-pink-400 focus:border-transparent;
}

:deep(.password-input-styled .form-label) {
  @apply text-gray-700 font-medium mb-2;
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
</style>
