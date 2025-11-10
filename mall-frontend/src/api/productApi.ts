import axios from 'axios'
import type { Product } from '../types'

interface ProductPageResponse {
  content: Product[]
  totalElements: number
  totalPages: number
  pageNumber: number
  pageSize: number
}

const API_BASE_URL = 'http://localhost:8081/api/products'

// 添加日志功能
const log = (message: string, data?: any) => {
  console.log(`[Product API] ${message}`, data || '')
}

const logError = (message: string, error?: any) => {
  console.error(`[Product API Error] ${message}`, error || '')
}

// 获取商品列表
export const getProducts = async (name?: string, brand?: string, category?: string): Promise<Product[]> => {
  try {
    log(`Fetching products - name: ${name}, brand: ${brand}, category: ${category}`)
    const response = await axios.get<Product[]>(API_BASE_URL, {
      params: { name, brand, category }
    })
    log('Products fetched successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to fetch products', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取商品列表失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取商品列表失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 获取所有品牌
export const getAllBrands = async (): Promise<string[]> => {
  try {
    log('Fetching all brands')
    const response = await axios.get(`${API_BASE_URL}/brands`)
    log('Brands fetched successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to fetch brands', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取品牌列表失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取品牌列表失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 获取所有分类
export const getAllCategories = async (): Promise<string[]> => {
  try {
    log('Fetching all categories')
    const response = await axios.get(`${API_BASE_URL}/categories`)
    log('Categories fetched successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to fetch categories', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取分类列表失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取分类列表失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 根据ID获取商品详情
export const getProductById = async (id: number): Promise<Product> => {
  try {
    log(`Fetching product with id: ${id}`)
    const response = await axios.get(`${API_BASE_URL}/${id}`)
    log('Product fetched successfully', response.data)
    return response.data
  } catch (error: any) {
    logError(`Failed to fetch product with id: ${id}`, error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '获取商品详情失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`获取商品详情失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 创建商品
export const createProduct = async (product: Product): Promise<Product> => {
  try {
    log('Creating new product', product)
    const response = await axios.post(API_BASE_URL, product)
    log('Product created successfully', response.data)
    return response.data
  } catch (error: any) {
    logError('Failed to create product', error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '创建商品失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`创建商品失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 更新商品
export const updateProduct = async (id: number, product: Product): Promise<Product> => {
  try {
    log(`Updating product with id: ${id}`, product)
    const response = await axios.put(`${API_BASE_URL}/${id}`, product)
    log('Product updated successfully', response.data)
    return response.data
  } catch (error: any) {
    logError(`Failed to update product with id: ${id}`, error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '更新商品失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`更新商品失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}

// 删除商品
export const deleteProduct = async (id: number): Promise<void> => {
  try {
    log(`Deleting product with id: ${id}`)
    await axios.delete(`${API_BASE_URL}/${id}`)
    log('Product deleted successfully')
  } catch (error: any) {
    logError(`Failed to delete product with id: ${id}`, error)
    
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.response.statusText || '删除商品失败'
      logError(`Server responded with status ${status}`, error.response.data)
      throw new Error(`删除商品失败 (${status}): ${message}`)
    } else if (error.request) {
      logError('No response received from server', error.request)
      throw new Error('无法连接到服务器，请检查网络连接')
    } else {
      logError('Error setting up request', error.message)
      throw new Error(`请求配置错误: ${error.message}`)
    }
  }
}