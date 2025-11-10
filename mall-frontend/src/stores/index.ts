import { createPinia } from 'pinia'

const pinia = createPinia()

export default pinia

// 在应用启动后初始化时检查保存的token
export async function initStores() {
  const { useAuthStore } = await import('./auth')
  const authStore = useAuthStore()
  authStore.checkSavedToken()
}