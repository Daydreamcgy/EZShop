<template>
  <div class="message-container">
    <transition name="slide">
      <div v-if="visible" class="message-panel" :class="type">
        <div class="message-content">
          <div class="message-icon" v-if="type === 'success'">✓</div>
          <div class="message-text">{{ message }}</div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  message: string
  type: 'success' | 'error' | 'warning' | 'info'
  duration?: number
}>()

const visible = ref(false)

const show = () => {
  visible.value = true
  
  // 自动隐藏
  const duration = props.duration || 3000
  setTimeout(() => {
    visible.value = false
  }, duration)
}

// 监听消息变化，自动显示
watch(() => props.message, () => {
  if (props.message) {
    show()
  }
}, { immediate: true })

defineExpose({
  show
})
</script>

<style scoped>
.message-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
}

.message-panel {
  padding: 16px 24px;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  min-width: 200px;
  transform-origin: right top;
}

.message-panel.success {
  background-color: #f6ffed;
  border: 1px solid #b7eb8f;
  color: #52c41a;
}

.message-panel.error {
  background-color: #fff2f0;
  border: 1px solid #ffccc7;
  color: #ff4d4f;
}

.message-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.message-icon {
  font-size: 20px;
  font-weight: bold;
}

.message-text {
  font-size: 14px;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}
</style>