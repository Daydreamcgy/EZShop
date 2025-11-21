<template>
  <div class="order-list-container">
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <a href="/">首页</a>
        <span class="separator">/</span>
        <span>我的订单</span>
      </div>
      
      <h2 class="mi-title">我的订单</h2>
      
      <!-- 标题栏和操作区分离 -->
      <div class="page-actions">
        <button 
          @click="exportOrders" 
          class="export-btn mi-btn"
          :disabled="exporting || loading"
        >
          {{ exporting ? '导出中...' : '导出订单' }}
        </button>
      </div>
      
      <div v-if="loading" class="loading">
        <div class="skeleton-loader">
          <div class="skeleton-line"></div>
          <div class="skeleton-line"></div>
          <div class="skeleton-line"></div>
        </div>
      </div>
      
      <div v-else-if="orders.length === 0" class="empty-orders">
        <div style="text-align: center; padding: 40px 0;">
          <p style="font-size: 18px; color: #999; margin-bottom: 20px;">暂无订单</p>
          <router-link to="/products">
            <button class="btn">去逛逛</button>
          </router-link>
        </div>
      </div>
      
      <div v-else class="orders-list">
        <div 
          v-for="order in orders" 
          :key="order.id" 
          class="order-card mi-card"
        >
          <div class="order-header">
            <span class="order-id">订单号: {{ order.id }}</span>
            <span :class="['order-status', getOrderStatusType(order.status)]">
              {{ getOrderStatusText(order.status) }}
            </span>
          </div>
          
          <div class="order-details">
            <div 
              v-for="item in order.orderItems" 
              :key="item.id" 
              class="order-item"
            >
              <div class="item-info">
                <div class="item-name">{{ item.product.name }}</div>
                <div class="item-quantity">数量: {{ item.quantity }}</div>
              </div>
              <div class="item-price">¥{{ item.price.toFixed(2) }}</div>
            </div>
          </div>
          
          <div class="order-footer">
            <span class="total-amount">总金额: ¥{{ order.totalAmount.toFixed(2) }}</span>
            <div class="order-actions">
              <span class="order-date">{{ formatDate(order.createdAt) }}</span>
              <button 
                v-if="order.status === 'PENDING_PAYMENT'" 
                @click="payOrder(order.id)"
                class="btn-pay"
                :disabled="processingOrderId === order.id"
              >
                {{ processingOrderId === order.id ? '处理中...' : '付款' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 侧边消息提示 -->
    <Message ref="messageRef" :message="messageText" :type="messageType" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { Order } from '../types'
import { getOrders, updateOrderStatus } from '../api/orderApi'
import Message from '../components/Message.vue'

const orders = ref<Order[]>([])
const loading = ref(true)
const processingOrderId = ref<number | null>(null)
const exporting = ref(false)

// 消息提示相关
const messageRef = ref<InstanceType<typeof Message> | null>(null)
const messageText = ref('')
const messageType = ref<'success' | 'error' | 'warning' | 'info'>('success')

// 获取订单状态的显示文本
const getOrderStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'PENDING_PAYMENT': '待付款',
    'PAID': '已付款',
    'SHIPPED': '已发货',
    'DELIVERED': '已送达',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || status
}

// 获取订单状态的样式类型
const getOrderStatusType = (status: string) => {
  const typeMap: Record<string, string> = {
    'PENDING_PAYMENT': 'status-warning',
    'PAID': 'status-info',
    'SHIPPED': 'status-info',
    'DELIVERED': 'status-info',
    'COMPLETED': 'status-success',
    'CANCELLED': 'status-danger'
  }
  return typeMap[status] || 'status-info'
}

// 格式化日期
const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN')
}

// 获取用户订单
const fetchOrders = async () => {
  try {
    console.log('[OrderList] Fetching user orders')
    loading.value = true
    
    const fetchedOrders = await getOrders()
    orders.value = fetchedOrders
    console.log('[OrderList] Orders fetched successfully', fetchedOrders)
  } catch (error) {
    console.error('[OrderList] Failed to fetch orders', error)
    ElMessage.error('获取订单列表失败: ' + (error as Error).message)
  } finally {
    loading.value = false
  }
}

const showSideMessage = (text: string, type: 'success' | 'error' | 'warning' | 'info' = 'success') => {
  messageText.value = text
  messageType.value = type
  messageRef.value?.show()
}

// 付款订单
const payOrder = async (orderId: number) => {
  try {
    processingOrderId.value = orderId
    await updateOrderStatus(orderId, 'PAID')
    showSideMessage('付款成功')
    
    // 直接更新本地订单状态，避免刷新整个列表
    const order = orders.value.find(order => order.id === orderId)
    if (order) {
      order.status = 'PAID'
    }
    
    processingOrderId.value = null
  } catch (error) {
    console.error('[OrderList] Failed to pay order', error)
    showSideMessage('付款失败: ' + (error as Error).message, 'error')
    processingOrderId.value = null
  }
}

// 导出订单为Excel文件
const exportOrders = async () => {
  try {
    exporting.value = true
    console.log('[OrderList] Exporting orders to Excel')
    
    // 调用导出API
    const response = await fetch('http://localhost:8081/api/orders/export/excel', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    })
    
    if (!response.ok) {
      throw new Error('导出失败: ' + response.statusText)
    }
    
    // 处理响应
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    
    // 创建下载链接并触发下载
    const link = document.createElement('a')
    link.href = url
    
    // 从响应头获取文件名，如果没有则使用默认名称
    const contentDisposition = response.headers.get('content-disposition')
    let filename = '订单记录.xlsx'
    if (contentDisposition) {
      const match = contentDisposition.match(/filename="([^"]+)"/)
      if (match && match[1]) {
        filename = match[1]
      }
    }
    
    link.download = filename
    document.body.appendChild(link)
    link.click()
    
    // 清理
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    showSideMessage('订单导出成功')
    
  } catch (error) {
    console.error('[OrderList] Failed to export orders', error)
    showSideMessage('导出失败: ' + (error as Error).message, 'error')
  } finally {
    exporting.value = false
  }
}

fetchOrders()
</script>

<style scoped>
.page-actions {
    display: flex;
    justify-content: flex-end;
    margin: 15px 0 20px;
}

.export-btn.mi-btn {
    background-color: #ff9500; /* 小米风格橙色 */
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 2px; /* 小米风格小圆角 */
    cursor: pointer;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.3s;
}

.export-btn.mi-btn:hover:not(:disabled) {
    background-color: #ffad33;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(255, 149, 0, 0.3);
}

.export-btn.mi-btn:active:not(:disabled) {
    transform: translateY(0);
    box-shadow: none;
}

.export-btn.mi-btn:disabled {
    background-color: #d9d9d9;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
}

.order-list-container {
  background-color: var(--mi-bg-color);
  padding: 20px 0;
  min-height: calc(100vh - 120px);
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

.order-card {
  margin-bottom: 20px;
  padding: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--mi-border-color);
}

.order-id {
  font-weight: bold;
}

.order-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.status-warning {
  background-color: #fff6e5;
  color: #ff9500;
}

.status-info {
  background-color: #e5f2ff;
  color: #007aff;
}

.status-success {
  background-color: #e5ffe5;
  color: #34c759;
}

.status-danger {
  background-color: #ffe5e5;
  color: #ff3b30;
}

.order-details {
  margin-bottom: 15px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.order-item:last-child {
  border-bottom: none;
}

.item-info {
  flex: 1;
}

.item-name {
  font-weight: 500;
  margin-bottom: 5px;
}

.item-quantity {
  font-size: 12px;
  color: #999;
}

.item-price {
  font-weight: bold;
  color: var(--mi-primary-color);
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.total-amount {
  color: var(--mi-primary-color);
  font-size: 18px;
}

.order-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.order-date {
  color: #909399;
  font-size: 14px;
  font-weight: normal;
}

.btn-pay {
  background-color: #ff9500;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-pay:hover:not(:disabled) {
  background-color: #ffad33;
}

.btn-pay:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}
</style>