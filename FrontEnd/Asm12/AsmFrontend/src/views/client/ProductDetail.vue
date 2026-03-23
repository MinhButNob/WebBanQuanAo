<template>
  <div class="container py-5">
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
          {{ product.price.toLocaleString() }}₫
        </p>

        <!-- Chọn size -->
        <div class="mb-3">
          <label class="fw-semibold mb-1">Kích cỡ</label>
          <select class="form-select" v-model="selectedSize">
            <option disabled value="">Chọn size</option>
            <option v-for="s in product.sizes" :key="s">{{ s }}</option>
          </select>
        </div>

        <!-- Chọn màu -->
        <div class="mb-3">
          <label class="fw-semibold mb-1">Màu sắc</label>
          <select class="form-select" v-model="selectedColor">
            <option disabled value="">Chọn màu</option>
            <option v-for="c in product.colors" :key="c">{{ c }}</option>
          </select>
        </div>

        <!-- Số lượng -->
        <div class="mb-4">
          <label class="fw-semibold mb-1">Số lượng</label>
          <input
            type="number"
            min="1"
            class="form-control w-25"
            v-model="quantity"
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      quantity: 1,
      selectedSize: "",
      selectedColor: "",
      product: {
        id: 1,
        name: "Áo khoác nam form rộng",
        price: 599000,
        image:
          "https://cdn.hstatic.net/products/200000690725/_o_web__2__ba4e66e0c86d428aa9562cece6ef5a40_master.png",
        sizes: ["S", "M", "L", "XL"],
        colors: ["Đen", "Xám", "Xanh rêu"],
        description:
          "Áo khoác nam phong cách hiện đại, chất liệu dày dặn, phù hợp mùa thu đông.",
      },
    };
  },
  methods: {
    addToCart() {
      if (!this.selectedSize || !this.selectedColor) {
        alert("Vui lòng chọn size và màu");
        return;
      }

      alert("Đã thêm vào giỏ hàng 🛒");
    },
  },
};
</script>

<style scoped>
img {
  max-height: 500px;
  object-fit: cover;
}
</style>
