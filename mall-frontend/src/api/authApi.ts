import axios from 'axios'

const API_BASE_URL = 'http://localhost:8081/auth'

// 添加日志功能
const log = (message: string, data?: any) => {
  console.log(`[Auth API] ${message}`, data || '')
}

const logError = (message: string, error?: any) => {
  console.error(`[Auth API Error] ${message}`, error || '')
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
}

export interface AuthResponse {
  token: string;
}

// 用户登录
export const login = async (loginData: LoginRequest): Promise<AuthResponse> => {
  try {
    log('Attempting login for user:', loginData.username)
    const response = await axios.post(`${API_BASE_URL}/login`, loginData)
    log('Login successful for user:', loginData.username)
    return response.data
  } catch (error: any) {
    logError('Login failed for user:', loginData.username)
    logError('Error details:', error)
    
    // 提供更详细的错误信息
    if (error.response) {
      // 服务器响应了错误状态码
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '登录失败'
      logError(`Server responded with status ${status}:`, error.response.data)
      throw new Error(`登录失败 (${status}): ${message}`)
    } else if (error.request) {
      // 请求已发出但没有收到响应
      logError('No response received from server:', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      // 其他错误
      logError('Error setting up request:', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 用户注册
export const register = async (registerData: RegisterRequest): Promise<string> => {
  try {
    log('Attempting registration for user:', registerData.username)
    const response = await axios.post(`${API_BASE_URL}/register`, registerData)
    log('Registration successful for user:', registerData.username)
    return response.data
  } catch (error: any) {
    logError('Registration failed for user:', registerData.username)
    logError('Error details:', error)
    
    // 提供更详细的错误信息
    if (error.response) {
      // 服务器响应了错误状态码
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '注册失败'
      logError(`Server responded with status ${status}:`, error.response.data)
      throw new Error(`注册失败 (${status}): ${message}`)
    } else if (error.request) {
      // 请求已发出但没有收到响应
      logError('No response received from server:', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      // 其他错误
      logError('Error setting up request:', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}