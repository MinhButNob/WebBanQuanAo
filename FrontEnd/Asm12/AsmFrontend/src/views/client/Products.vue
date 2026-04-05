<template>
  <div class="container-fluid px-0">
    <!-- BANNER -->
    <div class="banner mb-5">
      <img
        src="https://cdn-media.sforum.vn/storage/app/media/anh-dep-82.jpg"
        alt="Banner"
        class="w-100"
      />
      <div class="banner-text">
        <h1>Fall Winter 2025</h1>
        <p>New Collection</p>
      </div>
    </div>

    <div class="container mb-5">
      <h3 class="section-title">SẢN PHẨM MỚI</h3>
      <div class="row g-4">
        <div class="col-md-3" v-for="p in products.slice(0,4)" :key="p.id">
          <div class="product-card">
            <span class="sale-badge">SALE</span>

            <router-link :to="`/product/${p.id}`">
              <img :src="p.image" class="img-fluid" />
            </router-link>

            <h6 class="mt-2">
              <router-link
                :to="`/product/${p.id}`"
                class="text-dark text-decoration-none"
              >
                {{ p.name }}
              </router-link>
            </h6>

            <p class="price">
              <span class="new">{{ formatPrice(p.price) }}đ</span>
            </p>
            
            <router-link :to="`/product/${p.id}`">
              <button class="btn btn-dark w-100 btn-sm">
                Xem chi tiết
              </button>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <div class="mid-banner mb-5">
      <img
        src="https://cdn-media.sforum.vn/storage/app/media/anh-dep-82.jpg"
        class="w-100"
      />
    </div>

    <div class="container mb-5">
      <h3 class="section-title">SẢN PHẨM NỔI BẬT</h3>
      <div class="row g-4">
        <div class="col-md-3" v-for="p in products.slice(4,8)" :key="'hot' + p.id">
          <div class="product-card highlight">
            <span class="sale-badge">-10%</span>
            
            <router-link :to="`/product/${p.id}`">
              <img :src="p.image" class="img-fluid" />
            </router-link>
            
            <h6 class="mt-2">
              <router-link :to="`/product/${p.id}`" class="text-dark text-decoration-none">
                {{ p.name }}
              </router-link>
            </h6>
            
            <p class="price">
              <span class="new">{{ formatPrice(p.price) }}đ</span>
            </p>
            
            <router-link :to="`/product/${p.id}`">
              <button class="btn btn-outline-dark w-100 btn-sm">
                Xem chi tiết
              </button>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <div class="container mb-5">
      <h3 class="section-title">ÁO KHOÁC NAM</h3>
      <div class="row g-4">
        <div class="col-md-3" v-for="p in products.slice(8,12)" :key="'jacket' + p.id">
          <div class="product-card simple">
            <router-link :to="`/product/${p.id}`">
              <img :src="p.image" class="img-fluid" />
            </router-link>
            
            <h6 class="mt-2">
              <router-link :to="`/product/${p.id}`" class="text-dark text-decoration-none">
                {{ p.name }}
              </router-link>
            </h6>
            
            <p class="price">
              <span class="new">{{ formatPrice(p.price) }}đ</span>
            </p>
            
            <router-link :to="`/product/${p.id}`">
              <button class="btn btn-outline-dark w-100 btn-sm">
                Xem chi tiết
              </button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "ProductList",
  data() {
    return {
      products: [],      
    };
  },

  mounted() {
    this.fetchProducts(); 
  },

  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get('http://localhost:8080/api/products/active');
        this.products = response.data;
        console.log('Đã tải sản phẩm:', this.products.length);
      } catch (error) {
        console.error("Lỗi khi lấy sản phẩm:", error);
        alert("Không thể tải danh sách sản phẩm. Vui lòng thử lại sau.");
      }
    },
    formatPrice(price) {
      if (!price) return '0';
      return price.toLocaleString('vi-VN'); // Định dạng số 
    }
  },
};
</script>

<style scoped>
.banner {
  position: relative;
}

.banner-text {
  position: absolute;
  bottom: 30%;
  left: 10%;
  color: white;
}

.banner-text h1 {
  font-size: 48px;
  font-weight: bold;
}

.section-title {
  font-weight: bold;
  margin-bottom: 20px;
}

.product-card {
  border: 1px solid #eee;
  padding: 15px;
  position: relative;
  background: #fff;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.product-card img {
  width: 100%;
  height: 250px;
  object-fit: cover;
}

.sale-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: red;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  z-index: 1;
}

.price .old {
  text-decoration: line-through;
  color: #999;
  margin-right: 8px;
}

.price .new {
  color: red;
  font-weight: bold;
  font-size: 1.1rem;
}

.mid-banner img {
  height: 400px;
  object-fit: cover;
}

.btn-sm {
  font-size: 0.875rem;
  padding: 0.4rem 0.75rem;
}
</style>