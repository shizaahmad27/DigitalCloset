<!-- ImageUpload.vue - Drag & Drop Image Upload -->
<template>
  <div class="image-upload">
    <div
      v-if="!previewUrl"
      class="upload-placeholder"
      @click="triggerFileInput"
      @dragover.prevent
      @drop.prevent="handleDrop"
    >
      <div class="upload-icon">📁</div>
      <p class="upload-text">Click or drag an image here</p>
      <input
        ref="fileInput"
        type="file"
        accept="image/*"
        class="hidden"
        @change="handleFileSelect"
      />
    </div>
    <div
      v-else
      class="image-preview"
    >
      <img
        :src="previewUrl"
        alt="Preview"
        class="preview-image"
      />
      <button
        class="remove-image"
        @click="removeImage"
      >
        ✕
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['upload', 'error'])
const fileInput = ref(null)
const previewUrl = ref(null)

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleFileSelect = (event) => {
  const file = event.target.files?.[0]
  if (file) {
    validateAndProcessFile(file)
  }
}

const handleDrop = (event) => {
  const file = event.dataTransfer.files?.[0]
  if (file) {
    validateAndProcessFile(file)
  }
}

const validateAndProcessFile = (file) => {
  // Validate file type
  if (!file.type.startsWith('image/')) {
    emit('error', new Error('Please select an image file'))
    return
  }

  // Validate file size (max 5MB)
  if (file.size > 5 * 1024 * 1024) {
    emit('error', new Error('Image size should be less than 5MB'))
    return
  }

  // Create preview
  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target.result
  }
  reader.readAsDataURL(file)

  // Emit the file for upload
  emit('upload', file)
}

const removeImage = () => {
  previewUrl.value = null
  if (fileInput.value) {
    fileInput.value.value = ''
  }
  emit('upload', null)
}
</script>

<style scoped>
.image-upload {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 15px;
  overflow: hidden;
}

.upload-placeholder {
  width: 100%;
  height: 100%;
  border: 2px dashed rgba(139, 92, 246, 0.3);
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.5);
}

.upload-placeholder:hover {
  border-color: #8b5cf6;
  background: rgba(139, 92, 246, 0.05);
}

.upload-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.upload-text {
  color: #6b7280;
  font-size: 0.9rem;
}

.hidden {
  display: none;
}

.image-preview {
  width: 100%;
  height: 100%;
  position: relative;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 15px;
}

.remove-image {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.9);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  transition: all 0.2s ease;
}

.remove-image:hover {
  background: rgba(239, 68, 68, 1);
  transform: scale(1.1);
}
</style>
