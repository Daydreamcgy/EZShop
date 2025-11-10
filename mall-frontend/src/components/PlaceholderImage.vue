<template>
  <div class="placeholder-image" :style="style">
    <span class="placeholder-text">{{ text }}</span>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  width?: number | string
  height?: number | string
  text?: string
  backgroundColor?: string
  textColor?: string
}

const props = withDefaults(defineProps<Props>(), {
  width: 200,
  height: 200,
  text: 'No Image',
  backgroundColor: '#f0f0f0',
  textColor: '#999999'
})

const style = computed(() => ({
  width: typeof props.width === 'number' ? `${props.width}px` : props.width,
  height: typeof props.height === 'number' ? `${props.height}px` : props.height,
  backgroundColor: props.backgroundColor,
  color: props.textColor
}))
</script>

<style scoped>
.placeholder-image {
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px dashed #ddd;
  border-radius: 4px;
  font-family: Arial, sans-serif;
}

.placeholder-text {
  text-align: center;
  font-size: 14px;
  padding: 10px;
}
</style>