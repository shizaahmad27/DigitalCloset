import { ref } from 'vue'

export function useDragDrop() {
  const isDragging = ref(false)
  const draggedItem = ref(null)

  const startDrag = (item) => {
    isDragging.value = true
    draggedItem.value = item
  }

  const endDrag = () => {
    isDragging.value = false
    draggedItem.value = null
  }

  const handleDrop = (target) => {
    if (draggedItem.value) {
      // TODO: Implement drop logic
      endDrag()
    }
  }

  return {
    isDragging,
    draggedItem,
    startDrag,
    endDrag,
    handleDrop
  }
} 