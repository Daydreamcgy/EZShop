<template>
  <div class="product-detail-container">
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <a href="/">首页</a>
        <span class="separator">/</span>
        <a href="/products">商品列表</a>
        <span class="separator">/</span>
        <span>{{ product?.name }}</span>
      </div>
      
      <div class="product-detail mi-card" v-if="product">
        <div class="product-content">
          <div class="product-gallery">
            <div class="product-image">
              <img 
                v-if="product?.imageUrl" 
                :src="product?.imageUrl" 
                :alt="product?.name"
              >
              <div 
                v-else
                class="image-placeholder"
                :style="{ width: '400px', height: '400px', backgroundColor: '#f0f0f0', display: 'flex', alignItems: 'center', justifyContent: 'center', border: '1px dashed #ddd' }"
              >
                <span style="color: #999;">No Image</span>
              </div>
            </div>
          </div>
          
          <div class="product-info">
            <h1 class="product-name">{{ product?.name }}</h1>
            <div class="product-price">¥{{ product?.price.toFixed(2) }}</div>
            <div class="product-stock">库存: {{ product?.stock }}件</div>
            <div class="product-description">{{ product?.description }}</div>
            
            <div class="product-actions">
              <div class="quantity-control">
                <button @click="updateQuantity(quantity - 1)" class="quantity-btn">-</button>
                <input 
                  type="number" 
                  v-model="quantity" 
                  min="1" 
                  :max="product?.stock"
                  class="quantity-input"
                >
                <button @click="updateQuantity(quantity + 1)" class="quantity-btn">+</button>
              </div>
              
              <button 
                class="btn btn-large"
                @click="addToCart"
                :disabled="!authStore.isLoggedIn"
              >
                {{ authStore.isLoggedIn ? '加入购物车' : '请先登录' }}
              </button>
            </div>
          </div>
        </div>
        
        <div class="product-tabs">
          <div class="tab-headers">
            <div 
              v-for="(tab, index) in tabs" 
              :key="index"
              :class="{ active: activeTab === index }"
              @click="activeTab = index"
              class="tab-header"
            >
              {{ tab.title }}
            </div>
          </div>
          
          <div class="tab-content">
            <div v-if="activeTab === 0" class="tab-pane">
              <h3>商品详情</h3>
              <p>{{ product.description }}</p>
              <p>商品规格参数等详细信息...</p>
            </div>
            <div v-if="activeTab === 1" class="tab-pane">
              <h3>规格参数</h3>
              <p>商品规格参数列表...</p>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="loading">
        <p>加载中...</p>
      </div>
    </div>
    
    <!-- 侧边消息提示 -->
    <Message ref="messageRef" :message="messageText" :type="messageType" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { Product } from '../types'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'
import { getProductById } from '../api/productApi'
import { addToCart as apiAddToCart } from '../api/cartApi'
import Message from '../components/Message.vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const product = ref<Product | null>(null)
const quantity = ref(1)
const activeTab = ref(0)

// 消息提示相关
const messageRef = ref<InstanceType<typeof Message> | null>(null)
const messageText = ref('')
const messageType = ref<'success' | 'error' | 'warning' | 'info'>('success')

// 标签页配置
const tabs = [
  { title: '商品详情' },
  { title: '规格参数' }
]

const loadProduct = async (id: number) => {
  try {
    console.log(`[ProductDetail] Loading product with id: ${id}`)
    const data = await getProductById(id)
    product.value = data
    console.log('[ProductDetail] Product loaded successfully', data)
  } catch (error) {
    console.error('[ProductDetail] Failed to load product', error)
    ElMessage.error('加载商品详情失败: ' + (error as Error).message)
  }
}

const updateQuantity = (newQuantity: number) => {
  if (product.value) {
    quantity.value = Math.max(1, Math.min(newQuantity, product.value.stock))
  }
}

const showSideMessage = (text: string, type: 'success' | 'error' | 'warning' | 'info' = 'success') => {
  messageText.value = text
  messageType.value = type
  messageRef.value?.show()
}

const addToCart = async () => {
  if (!authStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (product.value) {
    try {
      console.log(`[ProductDetail] Adding ${quantity.value} of ${product.value.name} to cart`)
      await apiAddToCart(product.value.id, quantity.value)
      showSideMessage(`${product.value.name} 已添加到购物车`)
    } catch (error) {
      console.error('[ProductDetail] Failed to add product to cart', error)
      showSideMessage('添加到购物车失败: ' + (error as Error).message, 'error')
    }
  }
}

onMounted(() => {
  const productId = Number(route.params.id)
  loadProduct(productId)
})
</script>

<style scoped>
.product-detail-container {
  background-color: var(--mi-bg-color);
  padding: 20px 0;
  min-height: calc(100vh - 120px);
}

.product-detail {
  background-color: var(--mi-white);
  border-radius: 8px;
  overflow: hidden;
}

.product-content {
  display: flex;
  padding: 40px;
}

.product-gallery {
  flex: 1;
  padding-right: 40px;
}

.product-image {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.product-image img {
  max-width: 100%;
  max-height: 500px;
  object-fit: contain;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 24px;
  height:30px;
  margin: 0 0 0 0;
  font-weight: normal;
  word-wrap: break-word; /* 确保长名称能正确换行 */
  white-space: normal;   /* 允许换行 */
}

.product-price {
  font-size: 32px;
  color: var(--mi-primary-color);
  font-weight: bold;
  margin: 10px 0;
}

.product-stock {
  font-size: 16px;
  color: #999;
  margin-bottom: 20px;
}

.product-description {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 30px;
  color: var(--mi-text-secondary);
  word-wrap: break-word; /* 确保长描述能正确换行 */
  white-space: normal;   /* 允许换行 */
}

.product-actions {
  display: flex;
  gap: 20px;
  align-items: center;
}

.quantity-control {
  display: flex;
  align-items: center;
}

.quantity-btn {
  width: 40px;
  height: 40px;
  background-color: #f5f5f5;
  border: 1px solid #e0e0e0;
  font-size: 18px;
  cursor: pointer;
}

.quantity-input {
  width: 60px;
  height: 40px;
  border: 1px solid #e0e0e0;
  text-align: center;
  font-size: 16px;
}

.product-tabs {
  border-top: 1px solid var(--mi-border-color);
}

.tab-headers {
  display: flex;
  border-bottom: 1px solid var(--mi-border-color);
}

.tab-header {
  padding: 15px 30px;
  cursor: pointer;
  font-size: 16px;
  color: var(--mi-text-secondary);
}

.tab-header.active {
  color: var(--mi-primary-color);
  border-bottom: 2px solid var(--mi-primary-color);
  margin-bottom: -1px;
}

.tab-content {
  padding: 30px;
}

.tab-pane h3 {
  font-size: 20px;
  margin-bottom: 20px;
  font-weight: normal;
}

.loading {
  text-align: center;
  padding: 50px;
  background-color: var(--mi-white);
}

@media (max-width: 768px) {
  .product-content {
    flex-direction: column;
    padding: 20px;
  }
  
  .product-gallery {
    padding-right: 0;
    margin-bottom: 30px;
  }
  
  .product-tabs {
    margin: 0 -20px;
  }
  
  .tab-content {
    padding: 20px;
  }
  
  .product-name {
    font-size: 20px;
  }
}
</style>