import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '../types'
import { setAuthToken } from '../api/cartApi'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const isLoggedIn = ref<boolean>(false)

  // 用户登录
  const login = (userData: User, jwtToken: string) => {
    user.value = userData
    token.value = jwtToken
    isLoggedIn.value = true
    // 保存token到localStorage
    localStorage.setItem('token', jwtToken)
    // 设置axios默认header
    setAuthToken(jwtToken)
  }

  // 用户登出
  const logout = () => {
    user.value = null
    token.value = null
    isLoggedIn.value = false
    // 清除localStorage中的token
    localStorage.removeItem('token')
    // 清除axios默认header
    setAuthToken(null)
  }

  // 检查是否有保存的token
  const checkSavedToken = () => {
    const savedToken = localStorage.getItem('token')
    if (savedToken) {
      token.value = savedToken
      isLoggedIn.value = true
      setAuthToken(savedToken)
    }
  }
  
  // 更新用户信息
  const updateUser = (userData: Partial<User>) => {
    if (user.value) {
      user.value = { ...user.value, ...userData }
    }
  }

  return { user, token, isLoggedIn, login, logout, checkSavedToken, updateUser }
})