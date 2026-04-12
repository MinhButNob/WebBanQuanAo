<template>
  <div class="container py-5" v-if="product">
    <div class="row g-5">
      <!-- Hình ảnh -->
      <div class="col-md-6">
        <img
          :src="product.image"
          class="img-fluid border"
          alt="product"
        />
      </div>

      <!-- Thông tin -->
      <div class="col-md-6">
        <h2 class="fw-bold">{{ product.name }}</h2>

        <p class="fs-4 fw-semibold text-danger">
          {{ formatPrice(product.price) }}₫
        </p>

        <!-- Chọn size -->
        <div class="mb-3">
          <label class="fw-semibold mb-1">Kích cỡ</label>
          <select class="form-select" v-model="selectedSizeId">
            <option disabled value="">Chọn size</option>
            <option v-for="s in sizeOptions" :key="s.id" :value="s.id">
              {{ s.sizeName }}
            </option>
          </select>
        </div>

        <!-- Chọn màu -->
        <div class="mb-3">
          <label class="fw-semibold mb-1">Màu sắc</label>
          <select class="form-select" v-model="selectedColorId">
            <option disabled value="">Chọn màu</option>
            <option v-for="c in colorOptions" :key="c.id" :value="c.id">
              {{ c.colorName }}
            </option>
          </select>
        </div>

        <!-- Số lượng -->
        <div class="mb-4">
          <label class="fw-semibold mb-1">Số lượng</label>
          <input
            type="number"
            min="1"
            class="form-control w-25"
            v-model.number="quantity"
          />
        </div>

        <!-- Button -->
        <button class="btn btn-dark px-4" @click="addToCart">
          Thêm vào giỏ hàng
        </button>
      </div>
    </div>

    <!-- Mô tả -->
    <div class="mt-5">
      <h5 class="fw-bold">Mô tả sản phẩm</h5>
      <p class="text-muted">
        {{ product.description }}
      </p>
    </div>

    <!-- SẢN PHẨM LIÊN QUAN -->
    <div class="mt-5">
      <h3 class="section-title">SẢN PHẨM LIÊN QUAN</h3>
      <div class="row g-4">
        <div class="col-md-3" v-for="p in relatedProducts" :key="p.id">
          <div class="product-card">
            <!-- SỬA LẠI ĐÂY - Dùng @click để chuyển trang -->
            <div @click="goToProduct(p.id)" style="cursor: pointer;">
              <img :src="p.image" class="img-fluid" />
              <h6 class="mt-2">
                <span class="text-dark text-decoration-none">
                  {{ p.name }}
                </span>
              </h6>
              <p class="price">
                <span class="new">{{ formatPrice(p.price) }}đ</span>
              </p>
              <button class="btn btn-outline-dark w-100 btn-sm">
                Xem chi tiết
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Loading -->
  <div v-else class="container py-5 text-center">
    <div class="spinner-border text-dark" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      product: null,
      relatedProducts: [],
      sizeOptions: [],
      colorOptions: [],
      selectedSizeId: "",
      selectedColorId: "",
      quantity: 1,
    };
  },

  async mounted() {
    await this.loadProductData();
  },

  // ========== THÊM WATCH ĐỂ THEO DÕI THAY ĐỔI ID ==========
  watch: {
    '$route.params.id': {
      immediate: true,
      handler() {
        this.loadProductData();
      }
    }
  },
  // ========================================================

  methods: {
    // Load toàn bộ dữ liệu sản phẩm
    async loadProductData() {
      const productId = this.$route.params.id;
      if (!productId) {
        alert("Không tìm thấy sản phẩm");
        this.$router.push("/");
        return;
      }
      
      await this.fetchProduct(productId);
      if (this.product) {
        await this.fetchSizes();
        await this.fetchColors();
        await this.fetchRelatedProducts();
      }
    },

    // Lấy chi tiết sản phẩm theo ID
    async fetchProduct(id) {
      try {
        const response = await axios.get(`http://localhost:8080/api/products/${id}`);
        this.product = response.data;
        console.log("Đã tải sản phẩm:", this.product);
      } catch (error) {
        console.error("Lỗi tải sản phẩm:", error);
        alert("Không thể tải thông tin sản phẩm");
        this.$router.push("/");
      }
    },

    // Lấy sản phẩm liên quan (cùng category, khác id)
    async fetchRelatedProducts() {
      if (!this.product || !this.product.categoryId) return;
      
      try {
        const response = await axios.get('http://localhost:8080/api/products/active');
        this.relatedProducts = response.data
          .filter(p => p.categoryId === this.product.categoryId && p.id !== this.product.id)
          .slice(0, 4);
        console.log('Sản phẩm liên quan:', this.relatedProducts.length);
      } catch (error) {
        console.error("Lỗi tải sản phẩm liên quan:", error);
      }
    },

    // Lấy danh sách size
    async fetchSizes() {
      try {
        const response = await axios.get("http://localhost:8080/api/sizes");
        this.sizeOptions = response.data.filter(s => s.status === "ACTIVE");
      } catch (error) {
        console.error("Lỗi tải size:", error);
      }
    },

    // Lấy danh sách màu
    async fetchColors() {
      try {
        const response = await axios.get("http://localhost:8080/api/colors");
        this.colorOptions = response.data.filter(c => c.status === "ACTIVE");
      } catch (error) {
        console.error("Lỗi tải màu:", error);
      }
    },

    // ========== THÊM METHOD NÀY ĐỂ CHUYỂN TRANG ==========
    goToProduct(productId) {
      // Chuyển sang trang chi tiết sản phẩm mới
      this.$router.push(`/product/${productId}`);
      // Cuộn lên đầu trang
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    // ====================================================

    // Thêm vào giỏ hàng
    addToCart() {
      if (!this.selectedSizeId) {
        alert("Vui lòng chọn kích cỡ");
        return;
      }
      if (!this.selectedColorId) {
        alert("Vui lòng chọn màu sắc");
        return;
      }
      if (this.quantity < 1) {
        alert("Số lượng phải lớn hơn 0");
        return;
      }

      const selectedSize = this.sizeOptions.find(s => s.id === this.selectedSizeId);
      const selectedColor = this.colorOptions.find(c => c.id === this.selectedColorId);

      const cartItem = {
        id: this.product.id,
        name: this.product.name,
        price: this.product.price,
        image: this.product.image,
        sizeId: this.selectedSizeId,
        sizeName: selectedSize ? selectedSize.sizeName : "",
        colorId: this.selectedColorId,
        colorName: selectedColor ? selectedColor.colorName : "",
        quantity: this.quantity,
      };

      let cart = JSON.parse(localStorage.getItem("cart") || "[]");
      const existingIndex = cart.findIndex(
        item => item.id === cartItem.id && 
                item.sizeId === cartItem.sizeId && 
                item.colorId === cartItem.colorId
      );

      if (existingIndex !== -1) {
        cart[existingIndex].quantity += cartItem.quantity;
      } else {
        cart.push(cartItem);
      }

      localStorage.setItem("cart", JSON.stringify(cart));
      window.dispatchEvent(new Event("cart-updated"));
      alert(`Đã thêm ${this.quantity} sản phẩm vào giỏ hàng 🛒`);
      this.quantity = 1;
    },

    formatPrice(price) {
      if (!price) return "0";
      return price.toLocaleString("vi-VN");
    },
  },
};
</script>

<style scoped>
img {
  max-height: 500px;
  object-fit: cover;
}

.spinner-border {
  width: 3rem;
  height: 3rem;
}

.section-title {
  font-weight: bold;
  margin-bottom: 20px;
  border-left: 4px solid #000;
  padding-left: 15px;
}

.product-card {
  border: 1px solid #eee;
  padding: 15px;
  position: relative;
  background: #fff;
  transition: transform 0.3s;
  border-radius: 8px;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.product-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.price .new {
  color: red;
  font-weight: bold;
  font-size: 1rem;
}

.btn-sm {
  font-size: 0.875rem;
  padding: 0.4rem 0.75rem;
}
</style>