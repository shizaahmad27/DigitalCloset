import { ref } from 'vue'

export function useOutfits() {
  const outfits = ref([])
  const loading = ref(false)
  const error = ref(null)

  const fetchOutfits = async () => {
    loading.value = true
    try {
      // TODO: Implement API call to fetch outfits
      loading.value = false
    } catch (err) {
      error.value = err.message
      loading.value = false
    }
  }

  const createOutfit = async (outfit) => {
    try {
      // TODO: Implement API call to create outfit
      outfits.value.push(outfit)
    } catch (err) {
      error.value = err.message
    }
  }

  return {
    outfits,
    loading,
    error,
    fetchOutfits,
    createOutfit
  }
} 