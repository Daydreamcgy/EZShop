import axios from 'axios'
import type { User } from '../types'
import { setAuthToken } from './cartApi'

const API_BASE_URL = 'http://localhost:8081/api/users'

// 添加日志功能
const log = (message: string, data?: any) => {
  console.log(`[User API] ${message}`, data || '')
}

const logError = (message: string, error?: any) => {
  console.error(`[User API Error] ${message}`, error || '')
}

// 设置JWT token
export const setUserAuthToken = setAuthToken

// 获取当前用户信息
export const getCurrentUser = async (): Promise<User> => {
  try {
    log('Fetching current user information')
    const response = await axios.get<User>(`${API_BASE_URL}/me`)
    log('Current user information fetched successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to fetch current user information', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取用户信息失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取用户信息失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 更新当前用户信息
export const updateCurrentUser = async (userData: { email: string; password?: string }): Promise<User> => {
  try {
    log('Updating current user information', userData)
    const response = await axios.put<User>(`${API_BASE_URL}/me`, userData)
    log('Current user information updated successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to update current user information', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '更新用户信息失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`更新用户信息失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}