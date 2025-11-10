import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import pinia, { initStores } from './stores'
import axios from 'axios'
import Message from './components/Message.vue'

// 设置axios默认基础URL
axios.defaults.baseURL = 'http://localhost:8081'

console.log('Creating Vue app...')
const app = createApp(App)

// 注册全局消息组件
app.component('Message', Message)

console.log('Installing router...')
app.use(router)

console.log('Installing Pinia...')
app.use(pinia)

// 初始化stores
initStores().then(() => {
  console.log('Stores initialized')
})

console.log('Mounting app to #app...')
app.mount('#app')

console.log('App mounted successfully!')