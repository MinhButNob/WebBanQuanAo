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

    <!-- SẢN PHẨM MỚI -->
    <div class="container mb-5">
      <h3 class="section-title">🆕 SẢN PHẨM MỚI</h3>
      <div class="slider-container">
        <button class="slider-nav prev" @click="prevProduct('new')">
          ❮
        </button>
        
        <div class="slider-wrapper">
          <div class="row g-4 slider-track" :style="{ transform: `translateX(-${newCurrentIndex * 25}%)` }">
            <div class="col-md-3" v-for="p in newProducts" :key="p.id">
              <div class="product-card">
                <span class="sale-badge">SALE</span>
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
                  <button class="btn btn-dark w-100 btn-sm">Xem chi tiết</button>
                </router-link>
              </div>
            </div>
          </div>
        </div>
        
        <button class="slider-nav next" @click="nextProduct('new')">
          ❯
        </button>
      </div>
      
      <!-- Dots indicator -->
      <div class="dots-container">
        <span 
          v-for="(_, index) in newProducts" 
          :key="index" 
          class="dot" 
          :class="{ active: newCurrentIndex === index }"
          @click="goToProduct('new', index)"
        ></span>
      </div>
    </div>

    <div class="mid-banner mb-5">
      <img
        src="https://cdn-media.sforum.vn/storage/app/media/anh-dep-82.jpg"
        class="w-100"
      />
    </div>

    <!-- SẢN PHẨM NỔI BẬT -->
    <div class="container mb-5">
      <h3 class="section-title">🔥 SẢN PHẨM NỔI BẬT</h3>
      <div class="slider-container">
        <button class="slider-nav prev" @click="prevProduct('hot')">
          ❮
        </button>
        
        <div class="slider-wrapper">
          <div class="row g-4 slider-track" :style="{ transform: `translateX(-${hotCurrentIndex * 25}%)` }">
            <div class="col-md-3" v-for="p in hotProducts" :key="'hot' + p.id">
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
                  <button class="btn btn-outline-dark w-100 btn-sm">Xem chi tiết</button>
                </router-link>
              </div>
            </div>
          </div>
        </div>
        
        <button class="slider-nav next" @click="nextProduct('hot')">
          ❯
        </button>
      </div>
      
      <!-- Dots indicator -->
      <div class="dots-container">
        <span 
          v-for="(_, index) in hotProducts" 
          :key="index" 
          class="dot" 
          :class="{ active: hotCurrentIndex === index }"
          @click="goToProduct('hot', index)"
        ></span>
      </div>
    </div>

    <!-- ÁO THUN NAM -->
    <div class="container mb-5">
      <h3 class="section-title">👕 ÁO THUN NAM</h3>
      <div class="slider-container">
        <button class="slider-nav prev" @click="prevProduct('tshirt')">
          ❮
        </button>
        
        <div class="slider-wrapper">
          <div class="row g-4 slider-track" :style="{ transform: `translateX(-${tshirtCurrentIndex * 25}%)` }">
            <div class="col-md-3" v-for="p in tshirtProducts" :key="'tshirt' + p.id">
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
                  <button class="btn btn-outline-dark w-100 btn-sm">Xem chi tiết</button>
                </router-link>
              </div>
            </div>
          </div>
        </div>
        
        <button class="slider-nav next" @click="nextProduct('tshirt')">
          ❯
        </button>
      </div>
      
      <div class="dots-container">
        <span 
          v-for="(_, index) in tshirtProducts" 
          :key="index" 
          class="dot" 
          :class="{ active: tshirtCurrentIndex === index }"
          @click="goToProduct('tshirt', index)"
        ></span>
      </div>
    </div>

    <!-- QUẦN NAM -->
    <div class="container mb-5">
      <h3 class="section-title">👖 QUẦN NAM</h3>
      <div class="slider-container">
        <button class="slider-nav prev" @click="prevProduct('pants')">
          ❮
        </button>
        
        <div class="slider-wrapper">
          <div class="row g-4 slider-track" :style="{ transform: `translateX(-${pantsCurrentIndex * 25}%)` }">
            <div class="col-md-3" v-for="p in pantsProducts" :key="'pants' + p.id">
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
                  <button class="btn btn-outline-dark w-100 btn-sm">Xem chi tiết</button>
                </router-link>
              </div>
            </div>
          </div>
        </div>
        
        <button class="slider-nav next" @click="nextProduct('pants')">
          ❯
        </button>
      </div>
      
      <div class="dots-container">
        <span 
          v-for="(_, index) in pantsProducts" 
          :key="index" 
          class="dot" 
          :class="{ active: pantsCurrentIndex === index }"
          @click="goToProduct('pants', index)"
        ></span>
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
      itemsPerPage: 4,
      autoSlideInterval: null,
      // Slide cho sản phẩm mới (index từng sản phẩm)
      newCurrentIndex: 0,
      // Slide cho sản phẩm nổi bật
      hotCurrentIndex: 0,
      // Slide cho áo thun
      tshirtCurrentIndex: 0,
      // Slide cho quần
      pantsCurrentIndex: 0
    };
  },

  computed: {
    // Sản phẩm mới (tất cả)
    newProducts() {
      return this.products;
    },

    // Sản phẩm nổi bật
    hotProducts() {
      return this.products;
    },

    // Áo thun
    tshirtProducts() {
      return this.products;
    },

    // Quần nam
    pantsProducts() {
      return this.products;
    }
  },

  mounted() {
    this.fetchProducts();
    this.startAutoSlide();
  },

  beforeUnmount() {
    this.stopAutoSlide();
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

    // Next 1 sản phẩm
    nextProduct(section) {
      const sections = {
        new: 'newCurrentIndex',
        hot: 'hotCurrentIndex',
        tshirt: 'tshirtCurrentIndex',
        pants: 'pantsCurrentIndex'
      };
      
      const products = {
        new: this.newProducts,
        hot: this.hotProducts,
        tshirt: this.tshirtProducts,
        pants: this.pantsProducts
      };
      
      const currentIndex = sections[section];
      const total = products[section].length;
      
      if (total > 0) {
        // Vòng lặp: nếu hết thì quay lại đầu
        if (this[currentIndex] < total - 1) {
          this[currentIndex]++;
        } else {
          this[currentIndex] = 0;
        }
        this.resetAutoSlide();
      }
    },

    // Prev 1 sản phẩm
    prevProduct(section) {
      const sections = {
        new: 'newCurrentIndex',
        hot: 'hotCurrentIndex',
        tshirt: 'tshirtCurrentIndex',
        pants: 'pantsCurrentIndex'
      };
      
      const products = {
        new: this.newProducts,
        hot: this.hotProducts,
        tshirt: this.tshirtProducts,
        pants: this.pantsProducts
      };
      
      const currentIndex = sections[section];
      const total = products[section].length;
      
      if (total > 0) {
        // Vòng lặp: nếu về đầu thì qua cuối
        if (this[currentIndex] > 0) {
          this[currentIndex]--;
        } else {
          this[currentIndex] = total - 1;
        }
        this.resetAutoSlide();
      }
    },

    goToProduct(section, index) {
      const sections = {
        new: 'newCurrentIndex',
        hot: 'hotCurrentIndex',
        tshirt: 'tshirtCurrentIndex',
        pants: 'pantsCurrentIndex'
      };
      
      const currentIndex = sections[section];
      this[currentIndex] = index;
      this.resetAutoSlide();
    },

    startAutoSlide() {
      // Tự động chuyển từng sản phẩm cho sản phẩm nổi bật
      this.autoSlideInterval = setInterval(() => {
        if (this.hotCurrentIndex < this.hotProducts.length - 1) {
          this.hotCurrentIndex++;
        } else {
          this.hotCurrentIndex = 0; // Vòng lặp về đầu
        }
      }, 5000);
    },

    stopAutoSlide() {
      if (this.autoSlideInterval) {
        clearInterval(this.autoSlideInterval);
        this.autoSlideInterval = null;
      }
    },

    resetAutoSlide() {
      this.stopAutoSlide();
      this.startAutoSlide();
    },

    formatPrice(price) {
      if (!price) return '0';
      return price.toLocaleString('vi-VN');
    }
  }
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

/* Slider styles */
.slider-container {
  position: relative;
  display: flex;
  align-items: center;
  gap: 15px;
}

.slider-wrapper {
  flex: 1;
  overflow: hidden;
  border-radius: 8px;
}

.slider-track {
  display: flex;
  flex-wrap: nowrap;
  transition: transform 0.5s ease-in-out;
  margin: 0;
}

.slider-track .col-md-3 {
  flex: 0 0 25%;
  max-width: 25%;
}

.slider-nav {
  width: 40px;
  height: 40px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 20px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.slider-nav:hover {
  background: rgba(0, 0, 0, 0.9);
  transform: scale(1.1);
}

/* Dots indicator */
.dots-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
  flex-wrap: wrap;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #ccc;
  cursor: pointer;
  transition: all 0.3s;
}

.dot.active {
  width: 25px;
  border-radius: 5px;
  background: #000;
}

.dot:hover {
  background: #666;
}

/* Product card */
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

/* Responsive */
@media (max-width: 768px) {
  .slider-track .col-md-3 {
    flex: 0 0 100%;
    max-width: 100%;
  }
  
  .slider-nav {
    width: 30px;
    height: 30px;
    font-size: 16px;
  }
  
  .product-card img {
    height: 180px;
  }
  
  .dots-container {
    gap: 5px;
  }
  
  .dot.active {
    width: 15px;
  }
}
</style>