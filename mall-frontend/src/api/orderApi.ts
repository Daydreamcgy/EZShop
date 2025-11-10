import axios from 'axios'
import type { Order } from '../types'

const API_BASE_URL = 'http://localhost:8081/api/orders'

// 添加日志功能
const log = (message: string, data?: any) => {
  console.log(`[Order API] ${message}`, data || '')
}

const logError = (message: string, error?: any) => {
  console.error(`[Order API Error] ${message}`, error || '')
}

// 设置JWT token
export const setAuthToken = (token: string | null) => {
  if (token) {
    log('Setting auth token')
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
  } else {
    log('Removing auth token')
    delete axios.defaults.headers.common['Authorization']
  }
}

// 确保JWT token已设置
const ensureAuth = () => {
  const token = localStorage.getItem('token')
  if (token) {
    setAuthToken(token)
  }
}

// 创建订单
export const createOrder = async (): Promise<Order> => {
  try {
    // 确保JWT token已设置
    ensureAuth()
    
    log('Creating new order')
    const response = await axios.post(API_BASE_URL)
    log('Order created successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to create order', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '创建订单失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`创建订单失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 获取订单详情
export const getOrderById = async (id: number): Promise<Order> => {
  try {
    // 确保JWT token已设置
    ensureAuth()
    
    log(`Fetching order with id: ${id}`)
    const response = await axios.get(`${API_BASE_URL}/${id}`)
    log('Order fetched successfully', response.data)
    return response.data
  } catch (error: any) {
    logError(`Failed to fetch order with id: ${id}`, error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取订单详情失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取订单详情失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 获取用户订单列表
export const getOrders = async (): Promise<Order[]> => {
  try {
    // 确保JWT token已设置
    ensureAuth()
    
    log('Fetching user orders')
    const response = await axios.get(API_BASE_URL)
    log('User orders fetched successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to fetch user orders', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取订单列表失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取订单列表失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 更新订单状态
export const updateOrderStatus = async (id: number, status: string): Promise<Order> => {
  try {
    // 确保JWT token已设置
    ensureAuth()
    
    log(`Updating order status - orderId: ${id}, status: ${status}`)
    const response = await axios.put(`${API_BASE_URL}/${id}/status`, null, {
      params: { status }
    })
    log('Order status updated successfully', response.data)
    return response.data
  } catch (error: any) {
    logError(`Failed to update order status for order ${id}`, error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '更新订单状态失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`更新订单状态失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}