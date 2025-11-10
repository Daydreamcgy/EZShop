<template>
  <div class="login-container">
    <div class="container">
      <div class="auth-container">
        <div class="auth-card">
          <h2>用户登录</h2>
          <form class="auth-form" @submit.prevent="handleLogin">
            <div class="form-group">
              <label for="username">用户名</label>
              <input 
                id="username"
                v-model="loginForm.username" 
                type="text" 
                placeholder="请输入用户名"
                required
              >
            </div>
            <div class="form-group">
              <label for="password">密码</label>
              <input 
                id="password"
                v-model="loginForm.password" 
                type="password" 
                placeholder="请输入密码"
                required
              >
            </div>
            <button 
              class="btn btn-large" 
              type="submit" 
              :disabled="loading"
            >
              {{ loading ? '登录中...' : '登录' }}
            </button>
          </form>
          <div class="auth-links">
            <router-link to="/register">还没有账号？立即注册</router-link>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 全局消息提示组件 -->
    <Message ref="messageRef" />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { login } from '../api/authApi'
import Message from '../components/Message.vue'

const router = useRouter()
const authStore = useAuthStore()
const messageRef = ref()

const loginForm = reactive({
  username: '',
  password: ''
})

const loading = ref(false)

const validateForm = () => {
  console.log('[Login] Validating login form')
  
  if (!loginForm.username) {
    console.warn('[Login] Validation failed: Username is required')
    messageRef.value?.error('请输入用户名')
    return false
  }
  
  if (!loginForm.password) {
    console.warn('[Login] Validation failed: Password is required')
    messageRef.value?.error('请输入密码')
    return false
  }
  
  if (loginForm.password.length < 6) {
    console.warn('[Login] Validation failed: Password too short')
    messageRef.value?.error('密码长度至少6位')
    return false
  }
  
  console.log('[Login] Form validation passed')
  return true
}

const handleLogin = async () => {
  if (!validateForm()) return
  
  loading.value = true
  try {
    console.log('[Login] Starting login process for user:', loginForm.username)
    
    // 调用真实的登录API
    const response = await login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    // 保存token到store
    authStore.login({
      id: 1, // 实际项目中应该从后端返回的用户信息中获取
      username: loginForm.username,
      email: `${loginForm.username}@example.com` // 实际项目中应该从后端返回的用户信息中获取
    }, response.token)
    
    console.info('[Login] Login successful for user:', loginForm.username)
    messageRef.value?.success('登录成功')
    
    // 登录成功后跳转到首页
    setTimeout(() => {
      router.push('/')
    }, 1000)
  } catch (error) {
    console.error('[Login] Login failed for user:', loginForm.username, error)
    const errorMessage = (error as Error).message || '登录失败'
    messageRef.value?.error('登录失败: ' + errorMessage)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  background-color: var(--mi-bg-color);
  min-height: 100vh;
}
</style>