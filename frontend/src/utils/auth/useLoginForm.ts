import { ref } from 'vue'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'
import { useAuth } from '@/composables/useAuth'
import { useRouter } from 'vue-router'

export const useLoginForm = (redirectPath: string) => {
  const router = useRouter()
  const { login } = useAuth()
  const isLoading = ref(false)
  const hasAttemptedLogin = ref(false)
  const formError = ref<string | null>(null)

  // Schema for the login form
  const schema = toTypedSchema(
    z.object({
      email: z.string({ required_error: 'Email is required' })
        .email('Invalid email address'),
      password: z.string({ required_error: 'Password is required' })
        .min(1, 'Password is required')
    })
  )

  const { handleSubmit, meta } = useForm({
    validationSchema: schema
  })

  const onSubmit = handleSubmit(async (values) => {
    if (isLoading.value) return

    isLoading.value = true
    hasAttemptedLogin.value = true
    formError.value = null

    try {
      await login({
        email: values.email,
        password: values.password
      })
      await router.push(redirectPath)
    } catch (error: any) {
      console.error('Login error:', error)
      if (error.response?.status === 403) {
        formError.value = 'Invalid email or password'
      } else if (error.response?.status === 429) {
        formError.value = 'Too many attempts. Please try again later.'
      } else {
        formError.value = error?.response?.data?.message || 'Could not log in. Please try again.'
      }
    } finally {
      isLoading.value = false
    }
  })

  return {
    isLoading,
    hasAttemptedLogin,
    formError,
    meta,
    onSubmit
  }
} 