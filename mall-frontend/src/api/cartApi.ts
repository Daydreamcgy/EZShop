import axios from 'axios'
import type { CartItem } from '../types'

const API_BASE_URL = 'http://localhost:8081/api/cart'

// 添加日志功能
const log = (message: string, data?: any) => {
  console.log(`[Cart API] ${message}`, data || '')
}

const logError = (message: string, error?: any) => {
  console.error(`[Cart API Error] ${message}`, error || '')
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

// 获取购物车内容
export const getCartItems = async (): Promise<CartItem[]> => {
  try {
    log('Fetching cart items')
    const response = await axios.get(API_BASE_URL)
    log('Cart items fetched successfully', response.data)
    // 确保返回的是数组
    const data = Array.isArray(response.data) ? response.data : []
    return data
  } catch (error: any) {
    logError('Failed to fetch cart items', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取购物车内容失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取购物车内容失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 添加商品到购物车
export const addToCart = async (productId: number, quantity: number): Promise<CartItem> => {
  try {
    log(`Adding product to cart - productId: ${productId}, quantity: ${quantity}`)
    const response = await axios.post(`${API_BASE_URL}/add`, { productId, quantity })
    log('Product added to cart successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to add product to cart', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '添加商品到购物车失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`添加商品到购物车失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 更新购物车项数量
export const updateCartItem = async (id: number, quantity: number): Promise<CartItem> => {
  try {
    log(`Updating cart item - id: ${id}, quantity: ${quantity}`)
    const response = await axios.put(`${API_BASE_URL}/update/${id}`, { quantity })
    log('Cart item updated successfully', response.data)
    return response.data
  } catch (error: any) {
    logError(`Failed to update cart item with id: ${id}`, error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '更新购物车项失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`更新购物车项失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 从购物车删除商品项
export const removeCartItem = async (id: number): Promise<void> => {
  try {
    log(`Removing cart item with id: ${id}`)
    await axios.delete(`${API_BASE_URL}/${id}`)
    log('Cart item removed successfully')
  } catch (error: any) {
    logError(`Failed to remove cart item with id: ${id}`, error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '删除购物车项失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`删除购物车项失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 清空购物车
export const clearCart = async (): Promise<void> => {
  try {
    log('Clearing cart')
    await axios.delete(`${API_BASE_URL}/clear`)
    log('Cart cleared successfully')
  } catch (error: any) {
    logError('Failed to clear cart', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '清空购物车失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`清空购物车失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}