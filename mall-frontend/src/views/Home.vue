<template>
  <div class="home-container">
    <div class="container">
      <!-- 轮播图 -->
      <div class="banner">
        <button class="banner-nav banner-nav-prev" @click="prevSlide">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 18L9 12L15 6" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
        <button class="banner-nav banner-nav-next" @click="nextSlide">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M9 18L15 12L9 6" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
        
        <div class="banner-wrapper" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
          <div class="banner-slide" v-for="(slide, index) in slides" :key="index">
            <div class="banner-placeholder" :style="{ backgroundColor: getBackgroundColor(index) }">
              <div class="banner-placeholder-content" :style="{ width: '100%', height: '400px', display: 'flex', alignItems: 'center', justifyContent: 'center' }">
                <span style="font-size: 24px; color: '#666'">{{ slide.title }}</span>
              </div>
            </div>
            <div class="banner-content">
              <h2>{{ slide.title }}</h2>
              <p>{{ slide.description }}</p>
              <button class="btn btn-large" @click="goToProducts">立即选购</button>
            </div>
          </div>
        </div>
        <div class="banner-indicators">
          <span 
            v-for="(slide, index) in slides" 
            :key="index" 
            :class="{ active: currentSlide === index }"
            @click="currentSlide = index"
          ></span>
        </div>
      </div>

      <!-- 热门商品推荐 -->
      <div class="section">
        <h2 class="section-title">热门推荐</h2>
        <div class="product-grid">
          <div 
            v-for="product in featuredProducts" 
            :key="product.id" 
            class="product-item"
          >
            <div class="product-image">
              <img 
                v-if="product.imageUrl" 
                :src="product.imageUrl" 
                :alt="product.name"
              >
              <div 
                v-else
                class="image-placeholder"
                :style="{ width: '200px', height: '200px', backgroundColor: '#f0f0f0', display: 'flex', alignItems: 'center', justifyContent: 'center', border: '1px dashed #ddd' }"
              >
                <span style="color: #999;">No Image</span>
              </div>
            </div>
            <div class="product-name">{{ product.name }}</div>
            <div class="product-desc">{{ product.description }}</div>
            <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
            <button 
              class="btn"
              @click="addToCart(product)"
              :disabled="!authStore.isLoggedIn"
            >
              加入购物车
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'
import type { Product } from '../types'

const router = useRouter()
const authStore = useAuthStore()

// 轮播图数据
const slides = ref([
  {
    title: '新品上市',
    description: '最新科技产品，为您带来极致体验'
  },
  {
    title: '限时优惠',
    description: '精选商品限时折扣，不容错过'
  },
  {
    title: '品质保证',
    description: '正品保障，售后无忧'
  }
])

const currentSlide = ref(0)

// 热门推荐商品
const featuredProducts = ref<Product[]>([
  {
    id: 1,
    name: 'iPhone 15',
    description: '最新款苹果手机，拥有超凡的性能和出色的摄影能力',
    price: 9999.00,
    stock: 50,
    imageUrl: ''
  },
  {
    id: 2,
    name: 'MacBook Pro',
    description: '专业级笔记本电脑，搭载M3芯片，提供无与伦比的性能',
    price: 19999.00,
    stock: 20,
    imageUrl: ''
  },
  {
    id: 3,
    name: 'iPad Air',
    description: '轻薄平板电脑，配备M2芯片，性能强劲',
    price: 5999.00,
    stock: 30,
    imageUrl: ''
  },
  {
    id: 4,
    name: 'Apple Watch',
    description: '智能手表，具备健康监测、运动追踪等功能',
    price: 3999.00,
    stock: 100,
    imageUrl: ''
  }
])

// 根据索引获取背景颜色，实现渐变效果
const getBackgroundColor = (index: number) => {
  // 基础蓝色调，随着索引增加逐渐加深
  const baseColor = 233 - (index * 20)
  return `rgb(${baseColor}, ${baseColor + 10}, 255)`
}

// 轮播图自动播放
let slideInterval: number | null = null

const startSlideShow = () => {
  slideInterval = window.setInterval(() => {
    currentSlide.value = (currentSlide.value + 1) % slides.value.length
  }, 5000)
}

// 停止自动播放
const stopSlideShow = () => {
  if (slideInterval) {
    clearInterval(slideInterval)
    slideInterval = null
  }
}

// 切换到下一张幻灯片
const nextSlide = () => {
  stopSlideShow()
  currentSlide.value = (currentSlide.value + 1) % slides.value.length
  startSlideShow()
}

// 切换到上一张幻灯片
const prevSlide = () => {
  stopSlideShow()
  currentSlide.value = (currentSlide.value - 1 + slides.value.length) % slides.value.length
  startSlideShow()
}

const goToProducts = () => {
  router.push('/products')
}

const addToCart = (product: Product) => {
  if (!authStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  // 模拟添加到购物车
  console.log('Adding to cart:', product)
  ElMessage.success(`${product.name} 已添加到购物车`)
}

onMounted(() => {
  startSlideShow()
})

// 清理定时器
onUnmounted(() => {
  if (slideInterval) {
    clearInterval(slideInterval)
  }
})
</script>

<style scoped>
.home-container {
  background-color: var(--mi-bg-color);
}

.banner {
  position: relative;
  height: 400px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 40px;
}

.banner-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.3);
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: background-color 0.3s;
}

.banner-nav:hover {
  background-color: rgba(0, 0, 0, 0.5);
}

.banner-nav-prev {
  left: 20px;
}

.banner-nav-next {
  right: 20px;
}

.banner-wrapper {
  display: flex;
  transition: transform 0.5s ease-in-out;
  height: 100%;
}

.banner-slide {
  position: relative;
  min-width: 100%;
  height: 100%;
}

.banner-placeholder {
  width: 100%;
  height: 100%;
}

.banner-content {
  position: absolute;
  top: 50%;
  left: 10%;
  transform: translateY(-50%);
  color: white;
  max-width: 500px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.5);
}

.banner-content h2 {
  font-size: 36px;
  margin-bottom: 15px;
}

.banner-content p {
  font-size: 18px;
  margin-bottom: 25px;
}

.banner-indicators {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
}

.banner-indicators span {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: rgba(255,255,255,0.5);
  cursor: pointer;
  transition: background-color 0.3s;
}

.banner-indicators span.active {
  background-color: white;
}

.section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 24px;
  font-weight: normal;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--mi-border-color);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.product-item {
  background-color: var(--mi-white);
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.product-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
}

.product-image {
  margin-bottom: 15px;
}

.product-name {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 10px;
}

.product-desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
  height: 40px;
  overflow: hidden;
}

.product-price {
  font-size: 20px;
  font-weight: bold;
  color: var(--mi-primary-color);
  margin-bottom: 15px;
}

@media (max-width: 768px) {
  .banner {
    height: 250px;
  }
  
  .banner-nav {
    width: 30px;
    height: 30px;
  }
  
  .banner-nav-prev {
    left: 10px;
  }
  
  .banner-nav-next {
    right: 10px;
  }
  
  .banner-content h2 {
    font-size: 24px;
  }
  
  .banner-content p {
    font-size: 14px;
  }
  
  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
}
</style>