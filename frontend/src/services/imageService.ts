import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

interface ImageUploadResponse {
  url: string
}

export const uploadImage = async (file: File): Promise<string> => {
  const formData = new FormData()
  formData.append('file', file)

  try {
    const response = await axios.post<ImageUploadResponse>(`${API_URL}/api/images/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return `${API_URL}${response.data.url}`
  } catch (error) {
    if (axios.isAxiosError(error) && error.response?.data?.error) {
      throw new Error(error.response.data.error)
    }
    throw new Error('Failed to upload image. Please try again.')
  }
} 