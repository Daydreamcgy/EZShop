<template>
  <div class="profile-container">
    <div class="container">
      <div class="breadcrumb">
        <a href="/">首页</a>
        <span class="separator">/</span>
        <span>用户信息</span>
      </div>
      
      <h2 class="mi-title">用户信息</h2>
      
      <div class="profile-card">
        <form class="profile-form" @submit.prevent="handleUpdateProfile">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              id="username"
              v-model="profileForm.username" 
              type="text" 
              disabled
            >
            <div class="form-hint">用户名不可修改</div>
          </div>
          
          <div class="form-group">
            <label for="email">邮箱</label>
            <input 
              id="email"
              v-model="profileForm.email" 
              type="email" 
              placeholder="请输入邮箱"
              required
            >
          </div>
          
          <div class="form-group">
            <label for="password">新密码</label>
            <input 
              id="password"
              v-model="profileForm.password" 
              type="password" 
              placeholder="留空则不修改密码"
            >
            <div class="form-hint">如需修改密码，请输入新密码（至少6位）</div>
          </div>
          
          <div class="form-group">
            <label for="confirmPassword">确认新密码</label>
            <input 
              id="confirmPassword"
              v-model="profileForm.confirmPassword" 
              type="password" 
              placeholder="再次输入新密码"
            >
          </div>
          
          <button 
            class="btn btn-large" 
            type="submit" 
            :disabled="loading"
          >
            {{ loading ? '保存中...' : '保存信息' }}
          </button>
        </form>
      </div>
    </div>
    
    <!-- 全局消息提示组件 -->
    <Message ref="messageRef" />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import Message from '../components/Message.vue'
import { getCurrentUser, updateCurrentUser } from '../api/userApi'

const authStore = useAuthStore()
const messageRef = ref()

const profileForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const loading = ref(false)

// 获取当前用户信息
const loadCurrentUser = async () => {
  try {
    console.log('[UserProfile] Loading current user information')
    const user = await getCurrentUser()
    profileForm.username = user.username
    profileForm.email = user.email
    console.log('[UserProfile] Current user information loaded successfully', user)
  } catch (error) {
    console.error('[UserProfile] Failed to load current user information', error)
    messageRef.value?.error('加载用户信息失败')
  }
}

// 更新用户信息
const handleUpdateProfile = async () => {
  if (!validateForm()) return
  
  loading.value = true
  try {
    console.log('[UserProfile] Updating user information')
    
    const updateData: { email: string; password?: string } = {
      email: profileForm.email
    }
    
    // 只有当用户输入了新密码时才更新密码
    if (profileForm.password) {
      updateData.password = profileForm.password
    }
    
    const updatedUser = await updateCurrentUser(updateData)
    
    // 更新 store 中的用户信息
    authStore.updateUser({
      id: updatedUser.id,
      username: updatedUser.username,
      email: updatedUser.email
    })
    
    console.info('[UserProfile] User information updated successfully', updatedUser)
    messageRef.value?.success('用户信息更新成功')
  } catch (error) {
    console.error('[UserProfile] Failed to update user information', error)
    const errorMessage = (error as Error).message || '更新用户信息失败'
    messageRef.value?.error('更新失败: ' + errorMessage)
  } finally {
    loading.value = false
  }
}

// 表单验证
const validateForm = () => {
  console.log('[UserProfile] Validating profile form')
  
  if (!profileForm.email) {
    console.warn('[UserProfile] Validation failed: Email is required')
    messageRef.value?.error('请输入邮箱')
    return false
  }
  
  // 简单的邮箱格式验证
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(profileForm.email)) {
    console.warn('[UserProfile] Validation failed: Invalid email format')
    messageRef.value?.error('请输入有效的邮箱地址')
    return false
  }
  
  // 如果输入了密码，验证密码长度
  if (profileForm.password && profileForm.password.length < 6) {
    console.warn('[UserProfile] Validation failed: Password too short')
    messageRef.value?.error('密码长度至少6位')
    return false
  }
  
  // 如果输入了密码，验证两次密码是否一致
  if (profileForm.password && profileForm.password !== profileForm.confirmPassword) {
    console.warn('[UserProfile] Validation failed: Passwords do not match')
    messageRef.value?.error('两次输入的密码不一致')
    return false
  }
  
  console.log('[UserProfile] Form validation passed')
  return true
}

onMounted(() => {
  console.log('[UserProfile] Component mounted')
  loadCurrentUser()
})
</script>

<style scoped>
.profile-container {
  background-color: var(--mi-bg-color);
  min-height: calc(100vh - 120px);
  padding: 20px 0;
}

.profile-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
  max-width: 500px;
  margin: 30px auto 0 auto; /* 增加上边距，使其与标题分割线有足够间距 */
}

.profile-form .form-group {
  margin-bottom: 20px;
}

.profile-form label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.profile-form input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box;
}

.profile-form input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.form-hint {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.btn-large {
  width: 100%;
  padding: 12px;
  font-size: 16px;
}
</style>