export interface Product {
  id: number
  name: string
  description: string
  price: number
  stock: number
  imageUrl: string | null
  brand: string
  category: string
}

export interface CartItem {
  id: number
  userId: number
  productId: number
  quantity: number
  product: Product
}

export interface AddToCartRequest {
  productId: number
  quantity: number
}

export interface UpdateQuantityRequest {
  quantity: number
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
}

export interface JwtResponse {
  token: string
  type: string
  username: string
  email: string
  roles: string[]
}

export interface User {
  id: number
  username: string
  email: string
}