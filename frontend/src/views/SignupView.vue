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

<template>
  <div class="min-h-screen flex items-center justify-center bg-white py-12">
    <form
        @submit="onSubmit"
        class="w-full max-w-sm p-8 border border-gray-200 rounded-xl shadow-sm bg-white space-y-5"
    >
      <h1 class="text-3xl font-bold text-center">Create Account</h1>

      <!-- First Name -->
      <FormField v-slot="{ componentField }" name="firstname">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-1">First Name</FormLabel>
          <FormControl>
            <div class="relative">
              <User
                  class="absolute left-2 top-1/2 transform -translate-y-1/2 text-gray-500 h-4 w-4"
              />
              <Input
                  type="text"
                  placeholder="John"
                  class="w-full px-3 py-2 pl-8 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  v-bind="componentField"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500" />
        </FormItem>
      </FormField>

      <!-- Last Name -->
      <FormField v-slot="{ componentField }" name="lastname">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-1">Last Name</FormLabel>
          <FormControl>
            <div class="relative">
              <User
                  class="absolute left-2 top-1/2 transform -translate-y-1/2 text-gray-500 h-4 w-4"
              />
              <Input
                  type="text"
                  placeholder="Doe"
                  class="w-full px-3 py-2 pl-8 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  v-bind="componentField"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500" />
        </FormItem>
      </FormField>

      <!-- Username -->
      <FormField v-slot="{ componentField }" name="username">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-1">Username</FormLabel>
          <FormControl>
            <div class="relative">
              <User
                  class="absolute left-2 top-1/2 transform -translate-y-1/2 text-gray-500 h-4 w-4"
              />
              <Input
                  type="text"
                  placeholder="johndoe"
                  class="w-full px-3 py-2 pl-8 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  v-bind="componentField"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500" />
        </FormItem>
      </FormField>

      <!-- Email -->
      <FormField v-slot="{ componentField }" name="email">
        <FormItem>
          <FormLabel class="block text-sm font-medium text-gray-700 mb-1">Email</FormLabel>
          <FormControl>
            <div class="relative">
              <Mail
                  class="absolute left-2 top-1/2 transform -translate-y-1/2 text-gray-500 h-4 w-4"
              />
              <Input
                  type="email"
                  placeholder="name@example.com"
                  class="w-full px-3 py-2 pl-8 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  v-bind="componentField"
              />
            </div>
          </FormControl>
          <FormMessage class="text-sm text-red-500" />
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
        />
      </FormField>

      <!-- Accept Privacy Policy -->
      <FormField v-slot="{ value, handleChange }" name="acceptedPrivacyPolicy" type="checkbox">
        <FormItem>
          <div class="flex items-start space-x-2">
            <FormControl>
              <input
                  type="checkbox"
                  :checked="value"
                  @change="handleChange(($event.target as HTMLInputElement)?.checked ?? false)"
                  @keydown.enter.prevent="handleChange(!value)"
                  @keydown.space.prevent="handleChange(!value)"
                  id="acceptedPrivacyPolicy"
                  class="mt-1 h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                  tabindex="0"
                  role="checkbox"
                  :aria-checked="value"
                  aria-label="Accept privacy policy"
              />
            </FormControl>
            <label for="acceptedPrivacyPolicy" class="text-sm text-gray-700 cursor-pointer select-none">
              I accept the
              <router-link
                  to="/privacy"
                  class="text-blue-600 hover:underline focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 rounded-sm px-1"
                  tabindex="0"
                  aria-label="Open privacy policy"
              >
                privacy policy
              </router-link>
            </label>
          </div>
          <FormMessage class="text-sm text-red-500" />
        </FormItem>
      </FormField>

      <!-- Submit button -->
      <Button
          type="submit"
          :disabled="!meta.valid || isLoading"
          class="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed text-white py-2 rounded-md text-sm font-medium"
      >
        <template v-if="isLoading">Creating account...</template>
        <template v-else>Create Account</template>
      </Button>

      <!-- Conditional CTAs below -->
      <div class="text-sm text-center space-y-2">
        <div>
          <span class="text-gray-600">Already have an account?</span>
          <router-link to="/login" class="ml-1 text-blue-600 hover:underline">Sign in</router-link>
        </div>
      </div>
    </form>
  </div>
</template>
