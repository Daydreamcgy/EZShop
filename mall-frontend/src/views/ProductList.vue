<template>
  <div class="product-list-container">
    <div class="container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <a href="/">首页</a>
        <span class="separator">/</span>
        <span>商品列表</span>
      </div>
      
      <!-- 商品列表标题 -->
      <h2 class="mi-title">商品列表</h2>
      
      <!-- 品牌和分类筛选 -->
      <div class="filters">
        <div class="filter-group">
          <label>品牌：</label>
          <select v-model="selectedBrand" @change="filterProducts">
            <option value="">全部品牌</option>
            <option v-for="brand in brands" :key="brand" :value="brand">{{ brand }}</option>
          </select>
        </div>
        
        <div class="filter-group">
          <label>分类：</label>
          <select v-model="selectedCategory" @change="filterProducts">
            <option value="">全部分类</option>
            <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
          </select>
        </div>
        
        <div class="filter-group">
          <label>搜索：</label>
          <input 
            type="text" 
            v-model="searchKeyword" 
            placeholder="输入商品名称"
            @keyup.enter="searchProducts"
          />
          <button @click="searchProducts">搜索</button>
        </div>
      </div>
      
      <!-- 商品列表 -->
      <div class="product-grid">
        <div 
          v-for="product in products" 
          :key="product.id" 
          class="product-item"
          @click="goToProductDetail(product.id)"
        >
          <div class="product-image">
            <img 
              v-if="product.imageUrl" 
              :src="product.imageUrl" 
              :alt="product.name"
            >
            <div 
              v-else
              class="image-placeholder"
              :style="{ width: '200px', height: '200px', backgroundColor: '#f0f0f0', display: 'flex', alignItems: 'center', justifyContent: 'center', border: '1px dashed #ddd' }"
            >
              <span style="color: #999;">No Image</span>
            </div>
          </div>
          <div class="product-name">{{ product.name }}</div>
          <div class="product-desc">{{ product.description }}</div>
          <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
          <div class="product-brand">品牌：{{ product.brand }}</div>
          <div class="product-category">分类：{{ product.category }}</div>
        </div>
      </div>
      
      <!-- 移除分页控件 -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import type { Product } from '../types'
import { getProducts, getAllBrands, getAllCategories } from '../api/productApi'

const router = useRouter();

const products = ref<Product[]>([])
const brands = ref<string[]>([])
const categories = ref<string[]>([])
const selectedBrand = ref('')
const selectedCategory = ref('')
const searchKeyword = ref('')
// 移除分页相关状态
// const currentPage = ref(1)
// const pageSize = ref(8)
// const totalProducts = ref(0)

const loadProducts = async () => {
  try {
    console.log('[ProductList] Loading products')
    const data = await getProducts()
    products.value = data
    console.log('[ProductList] Products loaded successfully', data)
  } catch (error) {
    console.error('[ProductList] Failed to load products', error)
  }
}

const loadFilters = async () => {
  try {
    console.log('[ProductList] Loading filters')
    brands.value = await getAllBrands()
    categories.value = await getAllCategories()
    console.log('[ProductList] Filters loaded successfully', { brands: brands.value, categories: categories.value })
  } catch (error) {
    console.error('[ProductList] Failed to load filters', error)
  }
}

const filterProducts = async () => {
  try {
    console.log('[ProductList] Filtering products', { brand: selectedBrand.value, category: selectedCategory.value })
    
    if (selectedBrand.value && selectedCategory.value) {
      // 同时有品牌和分类筛选
      const data = await getProducts(undefined, selectedBrand.value, selectedCategory.value)
      products.value = data
    } else if (selectedBrand.value) {
      // 只有品牌筛选
      const data = await getProducts(undefined, selectedBrand.value)
      products.value = data
    } else if (selectedCategory.value) {
      // 只有分类筛选
      const data = await getProducts(undefined, undefined, selectedCategory.value)
      products.value = data
    } else {
      // 没有筛选条件
      const data = await getProducts()
      products.value = data
    }
    
    console.log('[ProductList] Products filtered successfully', products.value)
  } catch (error) {
    console.error('[ProductList] Failed to filter products', error)
  }
}

const searchProducts = async () => {
  try {
    console.log('[ProductList] Searching products', { keyword: searchKeyword.value })
    
    if (searchKeyword.value.trim()) {
      const data = await getProducts(searchKeyword.value)
      products.value = data
    } else {
      // 如果搜索关键词为空，则加载所有产品
      const data = await getProducts()
      products.value = data
    }
    
    console.log('[ProductList] Products searched successfully', products.value)
  } catch (error) {
    console.error('[ProductList] Failed to search products', error)
  }
}

// 移除分页相关代码
const handlePageChange = (page: number) => {
  // 不再需要处理分页
}

const goToProductDetail = (productId: number) => {
  router.push(`/products/${productId}`)
}

onMounted(() => {
  console.log('[ProductList] Component mounted')
  loadProducts()
  loadFilters()
})
</script>

<style scoped>
.product-list-container {
  background-color: var(--mi-bg-color);
  padding: 20px 0;
}

.filters {
  display: flex;
  gap: 20px;
  margin: 30px 0 20px 0; /* 增加上边距，使其与标题分割线有足够间距 */
  flex-wrap: wrap;
  align-items: center;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-group label {
  font-weight: bold;
}

.filter-group select,
.filter-group input {
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filter-group button {
  padding: 5px 15px;
  background-color: #ff6700;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.filter-group button:hover {
  background-color: #e55d00;
}

.product-item {
  cursor: pointer;
}

.product-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.product-brand,
.product-category {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}
</style>