<template>
  <div id="app">
    <!-- 顶部导航 -->
    <div class="mi-header">
      <div class="container">
        <div class="header-nav">
          <div class="nav-left">
            <router-link to="/" class="logo">EZShop</router-link>
          </div>
          <div class="nav-center">
            <ul class="nav-menu">
              <li><router-link to="/" :class="{ active: $route.name === 'Home' }">首页</router-link></li>
              <li><router-link to="/products" :class="{ active: $route.name === 'ProductList' }">商品列表</router-link></li>
              <li><router-link to="/cart" :class="{ active: $route.name === 'Cart' }">购物车</router-link></li>
              <li v-if="authStore.isLoggedIn"><router-link to="/orders" :class="{ active: $route.name === 'OrderList' }">我的订单</router-link></li>
            </ul>
          </div>
          <div class="nav-right">
            <div class="user-actions" v-if="!authStore.isLoggedIn">
              <router-link to="/login">登录</router-link>
              <span class="separator">|</span>
              <router-link to="/register">注册</router-link>
            </div>
            <div class="user-actions" v-else>
              <router-link to="/profile">{{ authStore.user?.username }}</router-link>
              <span class="separator">|</span>
              <a href="#" @click="handleLogout">退出</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 主体内容 -->
    <div class="main-content">
      <router-view></router-view>
    </div>
    
    <!-- 底部 -->
    <div class="mi-footer">
      <div class="container">
        <p style="text-align: center; padding: 20px 0; color: #999;">
          &copy; 2025 EZShop 商城 - 模仿小米商城设计
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

// 监听路由变化
watch(route, () => {
  // 可以在这里添加路由变化时的逻辑
})

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding: 20px 0;
}

.header-nav {
  display: flex;
  align-items: center;
  height: 60px;
}

.nav-left .logo {
  font-size: 24px;
  font-weight: bold;
  color: var(--mi-primary-color);
  text-decoration: none;
}

.nav-center {
  flex: 1;
  margin: 0 20px;
}

.nav-menu {
  display: flex;
  list-style: none;
}

.nav-menu li {
  margin-right: 20px;
}

.nav-menu a {
  text-decoration: none;
  color: var(--mi-text-primary);
  font-size: 16px;
  padding: 5px 10px;
  border-radius: 4px;
  transition: color 0.3s;
}

.nav-menu a:hover,
.nav-menu a.active {
  color: var(--mi-primary-color);
}

.nav-right {
  display: flex;
  align-items: center;
}

.user-actions {
  display: flex;
  align-items: center;
}

.user-actions a {
  color: var(--mi-text-primary);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.user-actions a:hover {
  color: var(--mi-primary-color);
}

.separator {
  margin: 0 10px;
  color: #ccc;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .nav-center {
    display: none;
  }
  
  .nav-left .logo {
    font-size: 20px;
  }
  
  .user-actions span {
    font-size: 12px;
  }
}
</style>