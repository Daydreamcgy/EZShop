<template>
  <div class="register-container">
    <div class="container">
      <div class="auth-container">
        <div class="auth-card">
          <h2>用户注册</h2>
          <form class="auth-form" @submit.prevent="handleRegister">
            <div class="form-group">
              <label for="username">用户名</label>
              <input 
                id="username"
                v-model="registerForm.username" 
                type="text" 
                placeholder="请输入用户名"
                required
              >
            </div>
            <div class="form-group">
              <label for="email">邮箱</label>
              <input 
                id="email"
                v-model="registerForm.email" 
                type="email" 
                placeholder="请输入邮箱"
                required
                @blur="validateEmail"
              >
              <div v-if="emailError" class="error-message">{{ emailError }}</div>
            </div>
            <div class="form-group">
              <label for="password">密码</label>
              <input 
                id="password"
                v-model="registerForm.password" 
                type="password" 
                placeholder="请输入密码"
                required
              >
            </div>
            <div class="form-group">
              <label for="confirmPassword">确认密码</label>
              <input 
                id="confirmPassword"
                v-model="registerForm.confirmPassword" 
                type="password" 
                placeholder="请再次输入密码"
                required
              >
            </div>
            <button 
              class="btn btn-large" 
              type="submit" 
              :disabled="loading"
            >
              {{ loading ? '注册中...' : '注册' }}
            </button>
          </form>
          <div class="auth-links">
            <router-link to="/login">已有账号？立即登录</router-link>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 全局消息提示组件 -->
    <Message ref="messageRef" />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/authApi'

const router = useRouter()
const messageRef = ref()

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const loading = ref(false)
const emailError = ref('')

const validateEmailFormat = (email: string) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

const validateEmail = () => {
  if (registerForm.email && !validateEmailFormat(registerForm.email)) {
    emailError.value = '邮箱格式不正确，请输入包含"@"和"."的有效邮箱地址'
  } else {
    emailError.value = ''
  }
}

const validateForm = () => {
  console.log('[Register] Validating registration form')
  
  if (!registerForm.username) {
    console.warn('[Register] Validation failed: Username is required')
    messageRef.value?.error('请输入用户名')
    return false
  }
  
  if (!registerForm.email) {
    console.warn('[Register] Validation failed: Email is required')
    messageRef.value?.error('请输入邮箱')
    return false
  }
  
  if (!validateEmailFormat(registerForm.email)) {
    console.warn('[Register] Validation failed: Invalid email format')
    messageRef.value?.error('邮箱格式不正确，请输入包含"@"和"."的有效邮箱地址')
    return false
  }
  
  if (!registerForm.password) {
    console.warn('[Register] Validation failed: Password is required')
    messageRef.value?.error('请输入密码')
    return false
  }
  
  if (registerForm.password.length < 6) {
    console.warn('[Register] Validation failed: Password too short')
    messageRef.value?.error('密码长度至少6位')
    return false
  }
  
  if (registerForm.password !== registerForm.confirmPassword) {
    console.warn('[Register] Validation failed: Passwords do not match')
    messageRef.value?.error('两次输入的密码不一致')
    return false
  }
  
  console.log('[Register] Form validation passed')
  return true
}

const handleRegister = async () => {
  if (!validateForm()) return
  
  loading.value = true
  try {
    console.log('[Register] Starting registration process for user:', registerForm.username)
    
    // 调用真实的注册API
    await register({
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    })
    
    console.info('[Register] Registration successful for user:', registerForm.username)
    messageRef.value?.success('注册成功！欢迎加入我们，即将跳转到登录页面。')
    
    // 注册成功后跳转到登录页
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error) {
    console.error('[Register] Registration failed for user:', registerForm.username, error)
    const errorMessage = (error as Error).message || '注册失败'
    messageRef.value?.error('注册失败: ' + errorMessage)
  } finally {
    loading.value = false
  }
}

// 监听密码确认字段变化
watch(() => registerForm.confirmPassword, (newVal) => {
  if (newVal && newVal !== registerForm.password) {
    // 只在用户输入时提示，避免初始状态的干扰
    if (newVal.length >= registerForm.password.length) {
      console.warn('[Register] Password confirmation does not match')
      messageRef.value?.error('两次输入的密码不一致')
    }
  }
})

// 监听邮箱字段变化，清除错误信息
watch(() => registerForm.email, () => {
  if (emailError.value) {
    emailError.value = ''
  }
})
</script>

<style scoped>
.register-container {
  background-color: var(--mi-bg-color);
  min-height: 100vh;
}

.error-message {
  color: red;
  font-size: 12px;
  margin-top: 4px;
}
</style>