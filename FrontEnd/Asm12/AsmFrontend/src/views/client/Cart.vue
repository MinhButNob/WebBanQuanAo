<template>
  <div class="container py-5">
    <h2 class="text-center mb-4 fw-bold">GIỎ HÀNG</h2>

    <div v-if="cart.length > 0" class="row">
      <!-- DANH SÁCH SẢN PHẨM -->
      <div class="col-lg-8">
        <table class="table align-middle">
          <thead class="table-dark">
            <tr>
              <th>Sản phẩm</th>
              <th>Size / Màu</th>
              <th>Giá</th>
              <th width="140">Số lượng</th>
              <th>Tạm tính</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in cart" :key="index">
              <td>
                <div class="d-flex align-items-center gap-3">
                  <img :src="item.image" class="cart-img" />
                  <span>{{ item.name }}</span>
                </div>
              </td>
              <td>
                <small class="text-muted">
                  {{ item.sizeName || 'Chưa chọn' }} / {{ item.colorName || 'Chưa chọn' }}
                </small>
              </td>
              <td>{{ formatPrice(item.price) }}</td>
              <td>
                <div class="d-flex align-items-center gap-2">
                  <button
                    class="btn btn-outline-dark btn-sm"
                    @click="decrease(item, index)"
                  >
                    -
                  </button>
                  <span class="mx-2">{{ item.quantity }}</span>
                  <button
                    class="btn btn-outline-dark btn-sm"
                    @click="increase(item, index)"
                  >
                    +
                  </button>
                </div>
              </td>
              <td>{{ formatPrice(item.price * item.quantity) }}</td>
              <td>
                <button
                  class="btn btn-sm btn-danger"
                  @click="removeItem(index)"
                >
                  Xóa
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- TỔNG TIỀN -->
      <div class="col-lg-4">
        <div class="card">
          <div class="card-body">
            <h5 class="mb-3">Tóm tắt đơn hàng</h5>

            <div class="d-flex justify-content-between mb-2">
              <span>Tạm tính</span>
              <strong>{{ formatPrice(total) }}</strong>
            </div>

            <div class="d-flex justify-content-between mb-3">
              <span>Phí vận chuyển</span>
              <strong>Miễn phí</strong>
            </div>

            <hr />

            <div class="d-flex justify-content-between mb-3">
              <strong>Tổng cộng</strong>
              <strong>{{ formatPrice(total) }}</strong>
            </div>

            <button class="btn btn-dark w-100" @click="checkout">
              Đặt hàng
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- GIỎ HÀNG TRỐNG -->
    <div v-else class="text-center py-5">
      <p class="fs-5">Giỏ hàng của bạn đang trống</p>
      <router-link to="/" class="btn btn-dark">
        Tiếp tục mua sắm
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "Cart",

  data() {
    return {
      cart: [],  // Sẽ được load từ localStorage
    };
  },

  computed: {
    total() {
      return this.cart.reduce(
        (sum, item) => sum + (item.price * (item.quantity || 1)),
        0
      );
    }
  },

  mounted() {
    this.loadCart();
    // Lắng nghe sự kiện cập nhật giỏ hàng từ các component khác
    window.addEventListener('cart-updated', this.loadCart);
  },

  beforeUnmount() {
    window.removeEventListener('cart-updated', this.loadCart);
  },

  methods: {
    // Load giỏ hàng từ localStorage
    loadCart() {
      const cartData = localStorage.getItem("cart");
      if (cartData) {
        this.cart = JSON.parse(cartData);
      } else {
        this.cart = [];
      }
    },

    // Lưu giỏ hàng vào localStorage
    saveCart() {
      localStorage.setItem("cart", JSON.stringify(this.cart));
      // Phát sự kiện để header cập nhật số lượng
      window.dispatchEvent(new Event("cart-updated"));
    },

    // Tăng số lượng
    increase(item, index) {
      if (!this.cart[index].quantity) {
        this.cart[index].quantity = 1;
      }
      this.cart[index].quantity++;
      this.saveCart();
    },

    // Giảm số lượng
    decrease(item, index) {
      if (this.cart[index].quantity > 1) {
        this.cart[index].quantity--;
        this.saveCart();
      } else {
        // Nếu quantity = 1 thì hỏi có muốn xóa không
        if (confirm("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?")) {
          this.removeItem(index);
        }
      }
    },

    // Xóa sản phẩm khỏi giỏ hàng
    removeItem(index) {
      if (confirm("Xóa sản phẩm này khỏi giỏ hàng?")) {
        this.cart.splice(index, 1);
        this.saveCart();
      }
    },

    // Xử lý đặt hàng
    checkout() {
      if (this.cart.length === 0) {
        alert("Giỏ hàng trống!");
        return;
      }
      
      // Kiểm tra đăng nhập
      const user = localStorage.getItem("user");
      if (!user) {
        alert("Vui lòng đăng nhập để đặt hàng!");
        this.$router.push("/login");
        return;
      }
      
      alert("Đặt hàng thành công! Cảm ơn bạn đã mua sắm.");
      
      // Xóa giỏ hàng sau khi đặt hàng thành công
      this.cart = [];
      this.saveCart();
      
      // Chuyển về trang chủ (hoặc trang đơn hàng)
      this.$router.push("/");
    },

    // Format giá tiền
    formatPrice(price) {
      if (!price && price !== 0) return "0 ₫";
      return price.toLocaleString("vi-VN") + " ₫";
    }
  }
};
</script>

<style scoped>
.cart-img {
  width: 70px;
  height: 70px;
  object-fit: cover;
}

table {
  vertical-align: middle;
}

.btn-outline-dark {
  transition: all 0.2s;
}

.btn-outline-dark:hover {
  background-color: #000;
  color: #fff;
}

.card {
  position: sticky;
  top: 20px;
}
</style>