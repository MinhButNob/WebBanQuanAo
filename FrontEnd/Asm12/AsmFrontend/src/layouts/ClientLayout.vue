<template>
  <div class="d-flex flex-column min-vh-100">
    <!-- HEADER -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
      <div class="container">
        <router-link class="navbar-brand fw-bold text-dark" to="/">
          DOSHISHA
        </router-link>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#clientMenu"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="clientMenu">
          <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link class="nav-link" to="/">Trang chủ</router-link>
            </li>

            <!-- 3 danh mục bán chạy nhất - CHỈ LÀ LINK THƯỜNG, KHÔNG DROPDOWN -->
            <li 
              v-for="category in topCategories" 
              :key="category.id" 
              class="nav-item"
            >
              <router-link class="nav-link" :to="`/category/${category.id}`">
                🔥 {{ category.name }}
              </router-link>
            </li>

            <!-- Danh sách tất cả loại sản phẩm - CÓ DROPDOWN MENU -->
            <li class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle"
                href="#"
                data-bs-toggle="dropdown"
              >
                📋 Danh sách loại
              </a>
              <ul class="dropdown-menu dropdown-menu-end" style="max-height: 400px; overflow-y: auto;">
                <li v-for="category in allCategories" :key="category.id">
                  <router-link 
                    class="dropdown-item" 
                    :to="`/category/${category.id}`"
                  >
                    {{ category.name }}
                    <!-- ĐÃ XÓA PHẦN HIỂN THỊ SỐ LƯỢNG SẢN PHẨM -->
                  </router-link>
                </li>
              </ul>
            </li>
          </ul>

          <div class="d-flex align-items-center gap-3">
            <!-- Thanh tìm kiếm -->
            <div class="search-wrapper">
              <form @submit.prevent="handleSearch" class="d-flex">
                <input
                  type="text"
                  v-model="searchKeyword"
                  placeholder="Tìm kiếm sản phẩm..."
                  class="form-control form-control-sm search-input"
                />
                <button type="submit" class="btn btn-dark btn-sm ms-2">
                  <i class="bi bi-search"></i>
                </button>
              </form>
              <div
                v-if="searchResults.length > 0 && searchKeyword"
                class="search-results"
              >
                <div
                  v-for="result in searchResults"
                  :key="result.id"
                  class="search-result-item"
                  @click="goToProduct(result.id)"
                >
                  <img :src="result.image" :alt="result.name" class="result-img" />
                  <div class="result-info">
                    <div class="result-name">{{ result.name }}</div>
                    <div class="result-price">{{ formatPrice(result.price) }}₫</div>
                  </div>
                </div>
              </div>
            </div>

            <!-- User dropdown -->
            <div v-if="user" class="dropdown">
              <button
                class="btn btn-link text-dark text-decoration-none dropdown-toggle"
                data-bs-toggle="dropdown"
              >
                <i class="bi bi-person-circle me-1"></i>
                {{ displayName }}
              </button>
              <ul class="dropdown-menu dropdown-menu-end">
                <li>
                  <router-link class="dropdown-item" to="/profile">
                    <i class="bi bi-person me-2"></i> Thông tin tài khoản
                  </router-link>
                </li>
                <li>
                  <router-link class="dropdown-item" to="/orders">
                    <i class="bi bi-bag-check me-2"></i> Đơn hàng của tôi
                  </router-link>
                </li>
                <li>
                  <router-link class="dropdown-item" to="/change-password">
                    <i class="bi bi-key me-2"></i> Đổi mật khẩu
                  </router-link>
                </li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                  <a class="dropdown-item text-danger" @click="logout" href="#">
                    <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
                  </a>
                </li>
              </ul>
            </div>

            <div v-else>
              <router-link to="/login" class="btn btn-outline-dark btn-sm">
                <i class="bi bi-person"></i> Đăng nhập
              </router-link>
            </div>

            <!-- Cart button -->
            <router-link to="/cart" class="btn btn-outline-dark position-relative">
              <i class="bi bi-bag">Giỏ hàng</i>
              <span
                v-if="cartCount > 0"
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              >
                {{ cartCount }}
              </span>
            </router-link>
          </div>
        </div>
      </div>
    </nav>

    <main class="container my-4 flex-grow-1">
      <router-view @update-cart="updateCartCount" @user-logged-in="loadUser" />
    </main>

    <!-- FOOTER -->
    <footer class="bg-dark text-white pt-4">
      <div class="container">
        <div class="row">
          <div class="col-md-4 mb-3">
            <h5 class="fw-bold">DOSHISHA</h5>
            <p class="small">
              Chuyên thời trang nam cao cấp. Phong cách – Đơn giản – Hiện đại.
            </p>
          </div>

          <div class="col-md-4 mb-3">
            <h6 class="fw-bold">Danh mục</h6>
            <ul class="list-unstyled">
              <li v-for="cat in allCategories.slice(0, 5)" :key="cat.id">
                <router-link :to="`/category/${cat.id}`" class="text-white text-decoration-none small">
                  {{ cat.name }}
                </router-link>
              </li>
            </ul>
          </div>

          <div class="col-md-4 mb-3">
            <h6 class="fw-bold">Liên hệ</h6>
            <p class="small mb-1">📍 Hà Nội</p>
            <p class="small mb-1">📞 0123 456 789</p>
            <p class="small">✉ fashion@gmail.com</p>
          </div>
        </div>

        <hr class="border-secondary" />
        <div class="text-center small pb-3">
          © 2026 DOSHISHA. All rights reserved.
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      user: null,
      cartCount: 0,
      searchKeyword: "",
      searchResults: [],
      allProducts: [],
      allCategories: [],     // Tất cả danh mục
      topCategories: []      // 3 danh mục bán chạy nhất
    };
  },

  computed: {
    displayName() {
      if (!this.user) return "";
      return this.user.ten || this.user.email || "Khách hàng";
    }
  },

  mounted() {
    this.loadUser();
    this.loadCartCount();
    this.loadAllProducts();
    this.loadCategories();

    window.addEventListener("cart-updated", this.loadCartCount);
    window.addEventListener("user-logged-in", this.loadUser);
  },

  beforeUnmount() {
    window.removeEventListener("cart-updated", this.loadCartCount);
    window.removeEventListener("user-logged-in", this.loadUser);
  },

  methods: {
    loadUser() {
      const userData = localStorage.getItem("user");
      this.user = userData ? JSON.parse(userData) : null;
    },

    loadCartCount() {
      const cart = JSON.parse(localStorage.getItem("cart") || "[]");
      this.cartCount = cart.reduce((total, item) => total + (item.quantity || 1), 0);
    },

    updateCartCount() {
      this.loadCartCount();
    },

    async loadAllProducts() {
      try {
        const response = await axios.get("http://localhost:8080/api/products/active");
        this.allProducts = response.data;
        console.log("Đã load sản phẩm:", this.allProducts.length);
      } catch (error) {
        console.error("Lỗi tải sản phẩm:", error);
      }
    },

    async loadCategories() {
      try {
        const response = await axios.get("http://localhost:8080/api/categories");
        this.allCategories = response.data;
        
        // Lấy 3 danh mục đầu tiên làm danh mục bán chạy
        this.topCategories = this.allCategories.slice(0, 3);
        
        console.log("Tất cả danh mục:", this.allCategories);
        console.log("3 danh mục bán chạy:", this.topCategories);
      } catch (error) {
        console.error("Lỗi tải danh mục:", error);
      }
    },

    handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.searchResults = [];
        return;
      }
      const keyword = this.searchKeyword.toLowerCase().trim();
      this.searchResults = this.allProducts
        .filter(product => product.name.toLowerCase().includes(keyword))
        .slice(0, 5);
    },

    goToProduct(productId) {
      this.searchKeyword = "";
      this.searchResults = [];
      this.$router.push(`/product/${productId}`);
    },

    formatPrice(price) {
      if (!price) return "0";
      return price.toLocaleString("vi-VN");
    },

    logout() {
      localStorage.removeItem("user");
      localStorage.removeItem("token");
      this.user = null;
      window.dispatchEvent(new Event("cart-updated"));
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.dropdown-toggle::after {
  vertical-align: middle;
}

.btn-outline-dark {
  transition: all 0.2s;
}

.btn-outline-dark:hover {
  background-color: #000;
  color: #fff;
}

.badge {
  font-size: 0.7rem;
  padding: 0.25rem 0.5rem;
}

.nav-link {
  font-weight: 500;
}

.dropdown-item {
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

/* Style cho thanh tìm kiếm */
.search-wrapper {
  position: relative;
  width: 250px;
}

.search-input {
  border-radius: 20px;
  padding: 0.375rem 0.75rem;
  font-size: 0.875rem;
  border: 1px solid #ddd;
  transition: all 0.3s;
}

.search-input:focus {
  border-color: #000;
  box-shadow: none;
  outline: none;
}

.search-results {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  max-height: 400px;
  overflow-y: auto;
  margin-top: 5px;
}

.search-result-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  cursor: pointer;
  transition: background 0.2s;
  border-bottom: 1px solid #eee;
}

.search-result-item:hover {
  background-color: #f8f9fa;
}

.result-img {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.result-info {
  flex: 1;
}

.result-name {
  font-size: 0.875rem;
  font-weight: 500;
  color: #333;
}

.result-price {
  font-size: 0.75rem;
  color: red;
  font-weight: bold;
}

/* Dropdown menu cho danh sách loại */
.dropdown-menu-end {
  right: 0;
  left: auto;
}

@media (max-width: 992px) {
  .search-wrapper {
    width: 100%;
    margin: 10px 0;
  }

  .search-results {
    position: fixed;
    left: 10px;
    right: 10px;
    top: auto;
  }
  
  .dropdown-menu-end {
    right: auto;
    left: 0;
  }
}
</style>