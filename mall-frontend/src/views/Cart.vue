<template>
  <div class="cart-container">
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <a href="/">首页</a>
        <span class="separator">/</span>
        <span>购物车</span>
      </div>
      
      <div v-if="!authStore.isLoggedIn" class="login-prompt">
        <el-empty description="请先登录查看购物车">
          <router-link to="/login">
            <button class="btn">立即登录</button>
          </router-link>
        </el-empty>
      </div>
      
      <div v-else>
        <h2 class="mi-title">我的购物车</h2>
        
        <div v-if="loading" class="loading">
          <div class="skeleton-loader">
            <div class="skeleton-line"></div>
            <div class="skeleton-line"></div>
            <div class="skeleton-line"></div>
          </div>
        </div>
        
        <div v-else-if="cartItems.length === 0" class="empty-cart">
          <el-empty description="购物车为空" />
          <router-link to="/products">
            <button class="btn">去逛逛</button>
          </router-link>
        </div>
        
        <div v-else>
          <table class="cart-table">
            <thead>
              <tr>
                <th width="40%">商品信息</th>
                <th>单价</th>
                <th>数量</th>
                <th>小计</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in cartItems" :key="item.id">
                <td>
                  <div style="display: flex; align-items: center;">
                    <div class="cart-item-image">
                      <img 
                        v-if="item.productImage" 
                        :src="item.productImage" 
                        :alt="item.productName"
                      >
                      <div 
                        v-else
                        class="image-placeholder"
                        :style="{ width: '80px', height: '80px', backgroundColor: '#f0f0f0', display: 'flex', alignItems: 'center', justifyContent: 'center', border: '1px dashed #ddd' }"
                      >
                        <span style="color: #999; font-size: 12px;">No Image</span>
                      </div>
                    </div>
                    <div class="cart-item-name">{{ item.productName }}</div>
                  </div>
                </td>
                <td class="cart-item-price">¥{{ item.productPrice?.toFixed(2) }}</td>
                <td>
                  <div class="quantity-control">
                    <button @click="updateQuantity(item, item.quantity - 1)">-</button>
                    <input 
                      type="number" 
                      :value="item.quantity" 
                      min="1" 
                      :max="item.productStock"
                      @change="(event) => updateQuantity(item, parseInt((event.target as HTMLInputElement).value))"
                    >
                    <button @click="updateQuantity(item, item.quantity + 1)">+</button>
                  </div>
                </td>
                <td class="cart-item-total">¥{{ (item.productPrice || 0) * item.quantity.toFixed(2) }}</td>
                <td>
                  <el-button 
                    type="danger" 
                    size="small" 
                    @click="removeItem(item)"
                  >
                    删除
                  </el-button>
                </td>
              </tr>
            </tbody>
          </table>
          
          <div class="cart-summary">
            <div style="margin-bottom: 20px;">
              <span>总计（不含运费）：</span>
              <span class="cart-total">¥{{ totalPrice.toFixed(2) }}</span>
            </div>
            <button class="btn btn-large" @click="placeOrder">下单</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 侧边消息提示 -->
    <Message ref="messageRef" :message="messageText" :type="messageType" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElDialog } from 'element-plus'
import type { CartItem } from '../types'
import { useAuthStore } from '../stores/auth'
import { getCartItems, updateCartItem, removeCartItem, clearCart } from '../api/cartApi'
import { createOrder } from '../api/orderApi'
import Message from '../components/Message.vue'

const router = useRouter()
const authStore = useAuthStore()

const cartItems = ref<CartItem[]>([])
const loading = ref(true)

// 消息提示相关
const messageRef = ref<InstanceType<typeof Message> | null>(null)
const messageText = ref('')
const messageType = ref<'success' | 'error' | 'warning' | 'info'>('success')

const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => {
    return total + ((item.productPrice || 0) * item.quantity)
  }, 0)
})

const showSideMessage = (text: string, type: 'success' | 'error' | 'warning' | 'info' = 'success') => {
  messageText.value = text
  messageType.value = type
  messageRef.value?.show()
}

const fetchCartItems = async () => {
  try {
    console.log('[Cart] Fetching cart items')
    loading.value = true
    const items = await getCartItems()
    console.log('[Cart] Raw cart items data:', items)
    cartItems.value = items
    console.log('[Cart] Cart items fetched successfully', items)
  } catch (error) {
    console.error('[Cart] Failed to fetch cart items', error)
    ElMessage.error('获取购物车数据失败: ' + (error as Error).message)
  } finally {
    loading.value = false
  }
}

const updateQuantity = async (item: CartItem, quantity: number) => {
  // 确保数量在合理范围内
  const newQuantity = Math.max(1, Math.min(quantity, item.productStock || 0))
  
  try {
    console.log(`[Cart] Updating item ${item.id} quantity to ${newQuantity}`)
    const updatedItem = await updateCartItem(item.id, newQuantity)
    const index = cartItems.value.findIndex(cartItem => cartItem.id === item.id)
    if (index !== -1) {
      cartItems.value[index] = updatedItem
    }
    ElMessage.success('数量已更新')
  } catch (error) {
    console.error('[Cart] Failed to update cart item quantity', error)
    ElMessage.error('更新数量失败: ' + (error as Error).message)
  }
}

const removeItem = async (item: CartItem) => {
  try {
    console.log(`[Cart] Removing item ${item.id}`)
    await removeCartItem(item.id)
    cartItems.value = cartItems.value.filter(cartItem => cartItem.id !== item.id)
    ElMessage.success('已从购物车删除')
  } catch (error) {
    console.error('[Cart] Failed to remove cart item', error)
    ElMessage.error('删除商品失败: ' + (error as Error).message)
  }
}

const placeOrder = async () => {
  try {
    // 打印调试信息
    console.log('[Cart] Placing order, token:', localStorage.getItem('token'))
    
    // 直接创建订单，无需确认弹窗
    const order = await createOrder()
    console.log('[Cart] Order created:', order)
    
    // 显示成功消息
    showSideMessage('订单创建成功')
    
    // 清空购物车
    try {
      await clearCart()
    } catch (clearError) {
      console.warn('[Cart] Failed to clear cart, but order was created', clearError)
      // 即使清空购物车失败，也继续执行后续操作，因为订单已经创建成功
    }
    
    // 更新本地购物车状态
    cartItems.value = []
    
    // 跳转到订单列表页面
    setTimeout(() => {
      router.push('/orders')
    }, 1500)
  } catch (error) {
    console.error('[Cart] Failed to create order', error)
    showSideMessage('订单创建失败: ' + (error as Error).message, 'error')
  }
}

// 检查用户是否已登录，未登录则跳转到登录页
const checkAuthAndFetch = () => {
  if (authStore.isLoggedIn) {
    fetchCartItems()
  } else {
    loading.value = false
    ElMessage.warning('请先登录')
  }
}

onMounted(() => {
  checkAuthAndFetch()
})

// 监听登录状态变化
watch(() => authStore.isLoggedIn, (newVal) => {
  if (newVal) {
    fetchCartItems()
  } else {
    loading.value = false
  }
})
</script>

<style scoped>
.cart-container {
  background-color: var(--mi-bg-color);
  padding: 20px 0;
  min-height: calc(100vh - 120px);
}

.empty-cart, .login-prompt {
  text-align: center;
  padding: 40px 0;
  background-color: var(--mi-white);
}

.loading {
  padding: 20px;
  background-color: var(--mi-white);
}

.skeleton-loader .skeleton-line {
  height: 20px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: loading 1.5s infinite;
  margin-bottom: 10px;
  border-radius: 4px;
}

@keyframes loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
  background-color: var(--mi-white);
  margin-bottom: 20px;
}

.cart-table th,
.cart-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid var(--mi-border-color);
}

.cart-table th {
  background-color: #f8f9fa;
  font-weight: 500;
}

.cart-item-image {
  width: 80px;
  height: 80px;
  margin-right: 15px;
}

.cart-item-name {
  font-weight: 500;
}

.cart-item-price,
.cart-item-total {
  font-weight: bold;
  color: var(--mi-primary-color);
}

.quantity-control {
  display: flex;
  align-items: center;
}

.quantity-control input {
  width: 50px;
  text-align: center;
  margin: 0 5px;
  padding: 5px;
  border: 1px solid var(--mi-border-color);
  border-radius: 4px;
}

.quantity-control button {
  width: 30px;
  height: 30px;
  background-color: #f8f9fa;
  border: 1px solid var(--mi-border-color);
  border-radius: 4px;
  cursor: pointer;
}

.quantity-control button:hover {
  background-color: #e9ecef;
}

.cart-summary {
  background-color: var(--mi-white);
  padding: 20px;
  text-align: right;
}

.cart-total {
  font-size: 24px;
  font-weight: bold;
  color: var(--mi-primary-color);
  margin-left: 10px;
}
</style>