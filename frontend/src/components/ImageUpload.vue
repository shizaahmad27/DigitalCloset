
<!-- ImageUpload.vue - Drag & Drop Image Upload -->
<template>
  <div class="image-upload-container">
    <div 
      ref="dropZone"
      class="upload-zone"
      :class="{ 'drag-over': isDragOver, 'has-images': images.length > 0 }"
      @drop="onDrop"
      @dragover.prevent
      @dragenter="onDragEnter"
      @dragleave="onDragLeave"
      @click="triggerFileInput">
      
      <!-- Upload prompt -->
      <div v-if="images.length === 0" class="upload-prompt">
        <div class="upload-icon">📸</div>
        <h3 class="upload-title">Upload Your Fashion Items</h3>
        <p class="upload-description">
          Drag & drop images here or click to browse
        </p>
        <div class="upload-formats">
          Supports: JPG, PNG, WEBP (Max 10MB each)
        </div>
      </div>

      <!-- Image previews -->
      <div v-else class="image-previews">
        <div v-for="(image, index) in images" :key="index" class="image-preview">
          <img :src="image.preview" :alt="`Preview ${index + 1}`" class="preview-image">
          <div class="image-overlay">
            <button @click.stop="removeImage(index)" class="remove-image-btn">
              ✕
            </button>
          </div>
          <div class="image-info">
            <span class="image-name">{{ image.file.name }}</span>
            <span class="image-size">{{ formatFileSize(image.file.size) }}</span>
          </div>
        </div>
        
        <!-- Add more button -->
        <div class="add-more-btn" @click.stop="triggerFileInput">
          <span class="add-icon">➕</span>
          <span class="add-text">Add More</span>
        </div>
      </div>
    </div>

    <!-- Hidden file input -->
    <input 
      ref="fileInput"
      type="file"
      multiple
      accept="image/*"
      @change="onFileSelect"
      style="display: none;">

    <!-- Upload progress -->
    <div v-if="isUploading" class="upload-progress">
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: uploadProgress + '%' }"></div>
      </div>
      <span class="progress-text">Uploading... {{ uploadProgress }}%</span>
    </div>

    <!-- Action buttons -->
    <div v-if="images.length > 0" class="upload-actions">
      <button @click="clearAll" class="clear-btn">
        🗑️ Clear All
      </button>
      <button @click="uploadImages" class="upload-btn" :disabled="isUploading">
        ✨ Upload Images
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue'

const emit = defineEmits(['images-uploaded', 'upload-progress'])

const dropZone = ref(null)
const fileInput = ref(null)
const isDragOver = ref(false)
const images = ref([])
const isUploading = ref(false)
const uploadProgress = ref(0)

const maxFileSize = 10 * 1024 * 1024 // 10MB
const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp']

const onDragEnter = (event) => {
  event.preventDefault()
  isDragOver.value = true
}

const onDragLeave = (event) => {
  event.preventDefault()
  if (!dropZone.value.contains(event.relatedTarget)) {
    isDragOver.value = false
  }
}

const onDrop = (event) => {
  event.preventDefault()
  isDragOver.value = false
  
  const files = Array.from(event.dataTransfer.files)
  processFiles(files)
}

const onFileSelect = (event) => {
  const files = Array.from(event.target.files)
  processFiles(files)
}

const processFiles = (files) => {
  const validFiles = files.filter(file => {
    if (!allowedTypes.includes(file.type)) {
      alert(`${file.name} is not a supported image format`)
      return false
    }
    if (file.size > maxFileSize) {
      alert(`${file.name} is too large. Maximum size is 10MB`)
      return false
    }
    return true
  })

  validFiles.forEach(file => {
    const reader = new FileReader()
    reader.onload = (e) => {
      images.value.push({
        file,
        preview: e.target.result
      })
    }
    reader.readAsDataURL(file)
  })
}

const removeImage = (index) => {
  images.value.splice(index, 1)
}

const clearAll = () => {
  images.value = []
}

const triggerFileInput = () => {
  fileInput.value.click()
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const uploadImages = async () => {
  if (images.value.length === 0) return
  
  isUploading.value = true
  uploadProgress.value = 0
  
  try {
    const formData = new FormData()
    images.value.forEach((image, index) => {
      formData.append(`images`, image.file)
    })
    
    // Simulate upload progress
    const progressInterval = setInterval(() => {
      uploadProgress.value += 10
      if (uploadProgress.value >= 90) {
        clearInterval(progressInterval)
      }
    }, 200)
    
    // Replace with actual API call
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    uploadProgress.value = 100
    
    emit('images-uploaded', images.value.map(img => img.file))
    
    // Clear after successful upload
    setTimeout(() => {
      images.value = []
      isUploading.value = false
      uploadProgress.value = 0
    }, 1000)
    
  } catch (error) {
    console.error('Upload failed:', error)
    isUploading.value = false
    uploadProgress.value = 0
    alert('Upload failed. Please try again.')
  }
}
</script>

<style scoped>
.image-upload-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.upload-zone {
  border: 3px dashed rgba(236, 72, 153, 0.3);
  border-radius: 25px;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  position: relative;
  overflow: hidden;
}

.upload-zone::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.05), rgba(139, 92, 246, 0.05));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.upload-zone:hover::before,
.upload-zone.drag-over::before {
  opacity: 1;
}

.upload-zone.drag-over {
  border-color: #ec4899;
  background: rgba(236, 72, 153, 0.1);
  transform: scale(1.02);
  box-shadow: 0 15px 40px rgba(236, 72, 153, 0.3);
}

.upload-zone.has-images {
  border-style: solid;
  border-color: rgba(16, 185, 129, 0.5);
}

.upload-prompt {
  position: relative;
  z-index: 1;
}

.upload-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.7;
}

.upload-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #6b46c1, #ec4899);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.upload-description {
  color: #6b7280;
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

.upload-formats {
  font-size: 0.875rem;
  color: #9ca3af;
  background: rgba(255, 255, 255, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 15px;
  display: inline-block;
}

.image-previews {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
  position: relative;
  z-index: 1;
}

.image-preview {
  position: relative;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(15px);
  border-radius: 15px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  overflow: hidden;
  transition: all 0.3s ease;
}

.image-preview:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.preview-image {
  width: 100%;
  aspect-ratio: 3/4;
  object-fit: cover;
  border-radius: 15px 15px 0 0;
}

.image-overlay {
  position: absolute;
  top: 0;
  right: 0;
  padding: 0.5rem;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.remove-image-btn {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: rgba(239, 68, 68, 0.9);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  transition: all 0.3s ease;
}

.remove-image-btn:hover {
  background: #dc2626;
  transform: scale(1.1);
}

.image-info {
  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.image-name {
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
  truncate: true;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.image-size {
  font-size: 0.75rem;
  color: #6b7280;
}

.add-more-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  border: 2px dashed rgba(236, 72, 153, 0.4);
  border-radius: 15px;
  background: rgba(255, 255, 255, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  color: #6b7280;
}

.add-more-btn:hover {
  border-color: #ec4899;
  background: rgba(236, 72, 153, 0.1);
  color: #ec4899;
  transform: scale(1.02);
}

.add-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.add-text {
  font-weight: 500;
}

.upload-progress {
  margin-top: 1.5rem;
  text-align: center;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(135deg, #10b981, #06b6d4);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
}

.upload-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 1.5rem;
}

.clear-btn {
  padding: 0.75rem 1.5rem;
  background: rgba(239, 68, 68, 0.1);
  color: #dc2626;
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 25px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.clear-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  border-color: #dc2626;
}

.upload-btn {
  padding: 0.75rem 2rem;
  background: linear-gradient(135deg, #10b981, #06b6d4);
  color: white;
  border: none;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.4);
}

.upload-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.6);
}

.upload-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}
</style>
