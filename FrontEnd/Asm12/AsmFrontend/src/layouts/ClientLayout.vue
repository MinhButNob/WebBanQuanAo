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
              <router-link class="nav-link" to="/">Sản phẩm mới</router-link>
            </li>

            <li class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle"
                href="#"
                data-bs-toggle="dropdown"
              >
                Áo nam
              </a>
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#">Áo thun</a></li>
                <li><a class="dropdown-item" href="#">Áo sơ mi</a></li>
              </ul>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="#">Quần nam</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="#">Phụ kiện</a>
            </li>
          </ul>

          <div class="d-flex align-items-center gap-3">
            <!-- ✅ THANH TÌM KIẾM -->
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
              <!-- Kết quả gợi ý -->
              <div v-if="searchResults.length > 0 && searchKeyword" class="search-results">
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

            <!-- ✅ USER DROPDOWN - Hiển thị tên khách hàng -->
            <div v-if="user" class="dropdown">
              <button
                class="btn btn-link text-dark text-decoration-none dropdown-toggle"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                <i class="bi bi-person-circle me-1"></i>
                {{ displayName }}
              </button>
              <ul class="dropdown-menu dropdown-menu-end">
                <li>
                  <router-link class="dropdown-item" to="/account/profile">
                    <i class="bi bi-person me-2"></i> Thông tin tài khoản
                  </router-link>
                </li>
                <li>
                  <router-link class="dropdown-item" to="/account/orders">
                    <i class="bi bi-bag-check me-2"></i> Đơn hàng của tôi
                  </router-link>
                </li>
                <li>
                  <router-link class="dropdown-item" to="/account/change-password">
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

            <!-- ❌ Nếu chưa đăng nhập thì hiển thị nút đăng nhập -->
            <div v-else>
              <router-link to="/login" class="btn btn-outline-dark btn-sm">
                <i class="bi bi-person"></i> Đăng nhập
              </router-link>
            </div>

            <!-- ✅ CART BUTTON với số lượng thực tế -->
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

    <!-- CONTENT -->
    <main class="container my-4 flex-grow-1">
      <router-view @update-cart="updateCartCount" @user-logged-in="loadUser" />
    </main>

    <!-- FOOTER -->
    <footer class="bg-dark text-white pt-4">
      <div class="container">
        <div class="row">
          <div class="col-md-4 mb-3">
            <h5 class="fw-bold">FASHION SHOP</h5>
            <p class="small">
              Chuyên thời trang nam cao cấp. Phong cách – Đơn giản – Hiện đại.
            </p>
          </div>

          <div class="col-md-4 mb-3">
            <h6 class="fw-bold">Danh mục</h6>
            <ul class="list-unstyled">
              <li><a href="#" class="text-white text-decoration-none">Áo nam</a></li>
              <li><a href="#" class="text-white text-decoration-none">Quần nam</a></li>
              <li><a href="#" class="text-white text-decoration-none">Phụ kiện</a></li>
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
          © 2026 Fashion Shop. All rights reserved.
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      user: null,
      cartCount: 0,
      searchKeyword: '',      // Từ khóa tìm kiếm
      searchResults: [],      // Kết quả tìm kiếm
      allProducts: [],        // Lưu tất cả sản phẩm để tìm kiếm
    };
  },

  computed: {
    // Hiển thị tên đẹp: ưu tiên fullName > username > email
    displayName() {
      if (!this.user) return "";
      return this.user.fullName || this.user.username || this.user.email || "Khách hàng";
    },
  },

  mounted() {
    this.loadUser();
    this.loadCartCount();
    this.loadAllProducts(); // Load sản phẩm cho tìm kiếm
    
    // Lắng nghe sự kiện thay đổi giỏ hàng
    window.addEventListener('cart-updated', this.loadCartCount);
    
    // Lắng nghe sự kiện đăng nhập (từ component login)
    window.addEventListener('user-logged-in', this.loadUser);
  },

  beforeUnmount() {
    window.removeEventListener('cart-updated', this.loadCartCount);
    window.removeEventListener('user-logged-in', this.loadUser);
  },

  methods: {
    loadUser() {
      const userData = localStorage.getItem("user");
      if (userData) {
        this.user = JSON.parse(userData);
        console.log("Đã load user:", this.user);
      } else {
        this.user = null;
      }
    },

    loadCartCount() {
      const cart = JSON.parse(localStorage.getItem("cart") || "[]");
      this.cartCount = cart.reduce((total, item) => total + (item.quantity || 1), 0);
    },

    updateCartCount() {
      this.loadCartCount();
    },

    // Load tất cả sản phẩm cho tìm kiếm
    async loadAllProducts() {
      try {
        const response = await axios.get('http://localhost:8080/api/products/active');
        this.allProducts = response.data;
      } catch (error) {
        console.error("Lỗi tải sản phẩm:", error);
      }
    },

    // Xử lý tìm kiếm
    handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.searchResults = [];
        return;
      }
      
      const keyword = this.searchKeyword.toLowerCase().trim();
      this.searchResults = this.allProducts
        .filter(product => 
          product.name.toLowerCase().includes(keyword) ||
          (product.description && product.description.toLowerCase().includes(keyword))
        )
        .slice(0, 5); // Giới hạn 5 kết quả
    },

    // Đi đến trang chi tiết sản phẩm
    goToProduct(productId) {
      this.searchKeyword = '';
      this.searchResults = [];
      this.$router.push(`/product/${productId}`);
    },

    formatPrice(price) {
      if (!price) return '0';
      return price.toLocaleString('vi-VN');
    },

    logout() {
      localStorage.removeItem("user");
      localStorage.removeItem("token");
      this.user = null;
      
      // Phát sự kiện cập nhật giỏ hàng (nếu cần)
      window.dispatchEvent(new Event("cart-updated"));
      
      this.$router.push("/login");
    },
  },
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

/* ✅ Style cho thanh tìm kiếm */
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
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
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
}
</style>