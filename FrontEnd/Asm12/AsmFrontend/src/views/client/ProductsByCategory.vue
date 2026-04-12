<template>
  <div class="container py-4">
    <nav aria-label="breadcrumb" class="mb-4">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><router-link to="/">Trang chủ</router-link></li>
        <li class="breadcrumb-item active">{{ categoryName }}</li>
      </ol>
    </nav>

    <div class="row">
      <div class="col-lg-12">
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-dark"></div>
          <p>Đang tải sản phẩm...</p>
        </div>

        <div v-else-if="products.length === 0" class="text-center py-5">
          <div style="font-size: 80px; color: #ccc;">📦</div>
          <p class="fs-5 mt-3">Chưa có sản phẩm nào trong danh mục {{ categoryName }}</p>
          <router-link to="/" class="btn btn-dark mt-2">
            Tiếp tục mua sắm
          </router-link>
        </div>

        <div v-else class="row">
          <div v-for="product in products" :key="product.id" class="col-md-3 col-sm-6 mb-4">
            <div class="product-card" @click="goToProduct(product.id)">
              <img :src="product.image" class="product-img" :alt="product.name" />
              <div class="product-body">
                <h6 class="product-title">{{ product.name }}</h6>
                <div class="product-price">{{ formatPrice(product.price) }}₫</div>
                <button class="btn btn-dark btn-sm w-100 mt-2" @click.stop="addToCart(product)">
                  <i class="bi bi-cart-plus"></i> Thêm giỏ
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ProductsByCategory',

  data() {
    return {
      products: [],
      categoryName: '',
      categoryId: null,
      loading: false
    };
  },

  mounted() {
    this.categoryId = this.$route.params.id;
    console.log('Category ID:', this.categoryId);
    this.loadCategoryInfo();
    this.loadProducts();
  },

  watch: {
    '$route.params.id'() {
      this.categoryId = this.$route.params.id;
      console.log('Category ID changed:', this.categoryId);
      this.loadCategoryInfo();
      this.loadProducts();
    }
  },

  methods: {
    async loadCategoryInfo() {
      try {
        const response = await axios.get(`http://localhost:8080/api/categories/${this.categoryId}`);
        this.categoryName = response.data.name;
        console.log('Category name:', this.categoryName);
      } catch (error) {
        console.error('Lỗi tải danh mục:', error);
        this.categoryName = 'Sản phẩm';
      }
    },

    async loadProducts() {
      this.loading = true;
      try {
        const response = await axios.get(`http://localhost:8080/api/products/category/${this.categoryId}`);
        console.log('API response:', response.data);
        this.products = response.data;
        console.log('Products loaded:', this.products.length);
      } catch (error) {
        console.error('Lỗi chi tiết:', error);
        if (error.response) {
          console.log('Error response:', error.response.data);
          alert(`Lỗi: ${error.response.data.message || 'Không thể tải sản phẩm'}`);
        } else if (error.request) {
          console.log('No response from server');
          alert('Không thể kết nối đến server!');
        } else {
          alert('Có lỗi xảy ra!');
        }
        this.products = [];
      } finally {
        this.loading = false;
      }
    },

    goToProduct(productId) {
      this.$router.push(`/product/${productId}`);
    },

    addToCart(product) {
      const cart = JSON.parse(localStorage.getItem('cart') || '[]');
      const existingItem = cart.find(item => item.id === product.id);
      
      if (existingItem) {
        existingItem.quantity += 1;
      } else {
        cart.push({
          id: product.id,
          name: product.name,
          price: product.price,
          quantity: 1,
          image: product.image,
          sizeName: '',
          colorName: ''
        });
      }
      
      localStorage.setItem('cart', JSON.stringify(cart));
      window.dispatchEvent(new Event('cart-updated'));
      alert('Đã thêm sản phẩm vào giỏ hàng!');
    },

    formatPrice(price) {
      if (!price) return '0';
      return price.toLocaleString('vi-VN');
    }
  }
};
</script>

<style scoped>
.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(0,0,0,0.15);
}

.product-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-body {
  padding: 12px;
}

.product-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 40px;
}

.product-price {
  color: red;
  font-weight: bold;
  font-size: 15px;
  margin-bottom: 8px;
}

.breadcrumb-item a {
  text-decoration: none;
  color: #666;
}

.breadcrumb-item.active {
  color: #000;
  font-weight: 500;
}
</style>