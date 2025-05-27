<template>
  <div
    class="drag-drop-area"
    :class="{ 'is-dragging': isDragging }"
    @dragenter.prevent="handleDragEnter"
    @dragleave.prevent="handleDragLeave"
    @dragover.prevent
    @drop.prevent="handleDrop"
  >
    <slot
      :is-dragging="isDragging"
      :handle-drag-enter="handleDragEnter"
      :handle-drag-leave="handleDragLeave"
      :handle-drop="handleDrop"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  accept: {
    type: String,
    default: '*'
  }
})

const emit = defineEmits(['drop'])

const isDragging = ref(false)
const dragCounter = ref(0)

const handleDragEnter = (event) => {
  dragCounter.value++
  isDragging.value = true
}

const handleDragLeave = (event) => {
  dragCounter.value--
  if (dragCounter.value === 0) {
    isDragging.value = false
  }
}

const handleDrop = (event) => {
  dragCounter.value = 0
  isDragging.value = false
  
  const items = event.dataTransfer.items
  if (!items) return

  const droppedItems = []
  
  for (let i = 0; i < items.length; i++) {
    const item = items[i]
    if (item.kind === 'file') {
      const file = item.getAsFile()
      if (file) {
        // Check if the file type matches the accept prop
        if (props.accept === '*' || file.type.match(props.accept)) {
          droppedItems.push(file)
        }
      }
    }
  }

  if (droppedItems.length > 0) {
    emit('drop', droppedItems)
  }
}
</script>

<style scoped>
.drag-drop-area {
  position: relative;
  width: 100%;
  height: 100%;
  transition: all 0.3s ease;
}

.drag-drop-area.is-dragging {
  background: rgba(139, 92, 246, 0.1);
  border: 2px dashed #8b5cf6;
  border-radius: 20px;
}

.drag-drop-area.is-dragging::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(139, 92, 246, 0.05);
  border-radius: 20px;
  pointer-events: none;
}
</style> 