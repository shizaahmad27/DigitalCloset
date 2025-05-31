import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useLogin, useSignup } from '../api/generated/auth-controller/auth-controller'
import type { LoginRequest, SignupRequest, AuthResponse } from '../api/generated/model'
import { useToast } from '@/components/ui/toast/use-toast'

export const useAuth = () => {
  const router = useRouter()
  const { toast } = useToast()
  const user = ref<AuthResponse | null>(null)
  const error = ref<string | null>(null)
  const isLoading = ref(false)

  const loginMutation = useLogin({
    mutation: {
      onSuccess: (data) => {
        user.value = data
        localStorage.setItem('token', data.token)
        error.value = null
        toast({
          title: 'Success',
          description: 'You have been logged in successfully',
          variant: 'default',
        })
        router.push('/dashboard')
      },
      onError: (err: any) => {
        const message = err.response?.data?.message || 'Login failed. Please check your credentials.'
        error.value = message
        toast({
          title: 'Error',
          description: message,
          variant: 'destructive',
        })
        console.error('Login error:', err)
      }
    }
  })

  const signupMutation = useSignup({
    mutation: {
      onSuccess: (data) => {
        user.value = data
        localStorage.setItem('token', data.token)
        error.value = null
        toast({
          title: 'Success',
          description: 'Your account has been created successfully',
          variant: 'default',
        })
        router.push('/dashboard')
      },
      onError: (err: any) => {
        console.error('Signup error details:', {
          status: err.response?.status,
          statusText: err.response?.statusText,
          data: err.response?.data,
          headers: err.response?.headers,
          config: {
            url: err.config?.url,
            method: err.config?.method,
            headers: err.config?.headers,
            data: err.config?.data
          }
        })

        let message = 'Signup failed. Please try again.'
        
        if (err.response?.status === 409) {
          message = err.response.data?.message || 'This username or email is already taken.'
        } else if (err.response?.status === 400) {
          message = 'Please check your input and try again.'
        } else if (err.response?.status === 403) {
          message = 'Access denied. Please try again.'
        }

        error.value = message
        toast({
          title: 'Error',
          description: message,
          variant: 'destructive',
        })
        console.error('Signup error:', err)
      }
    }
  })

  const login = async (credentials: LoginRequest) => {
    isLoading.value = true
    try {
      await loginMutation.mutateAsync({ data: credentials })
    } finally {
      isLoading.value = false
    }
  }

  const signup = async (userData: SignupRequest) => {
    isLoading.value = true
    try {
      console.log('Sending signup request with data:', userData)
      await signupMutation.mutateAsync({ data: userData })
    } finally {
      isLoading.value = false
    }
  }

  const logout = () => {
    user.value = null
    localStorage.removeItem('token')
    toast({
      title: 'Success',
      description: 'You have been logged out successfully',
      variant: 'default',
    })
    router.push('/login')
  }

  const isAuthenticated = () => {
    return !!localStorage.getItem('token')
  }

  return {
    user,
    error,
    isLoading,
    login,
    signup,
    logout,
    isAuthenticated
  }
} 