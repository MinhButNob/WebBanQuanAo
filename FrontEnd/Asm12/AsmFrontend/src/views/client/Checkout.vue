<template>
  <div class="container py-5">
    <h2 class="text-center mb-4 fw-bold">THANH TOÁN</h2>

    <div v-if="cart.length > 0" class="row">
      <!-- Cột trái: Thông tin giao hàng -->
      <div class="col-lg-7">
        <!-- Thông tin nhận hàng -->
        <div class="card mb-4">
          <div class="card-body">
            <h5 class="card-title mb-3">
              <i class="bi bi-person-bounding-box"></i> Thông tin giao hàng
            </h5>

            <form @submit.prevent="submitOrder">
              <div class="mb-3">
                <label class="form-label">Họ và tên <span class="text-danger">*</span></label>
                <input
                  type="text"
                  class="form-control"
                  v-model="orderInfo.customerName"
                  required
                  placeholder="Nguyễn Văn A"
                />
              </div>

              <div class="mb-3">
                <label class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                <input
                  type="tel"
                  class="form-control"
                  v-model="orderInfo.phone"
                  required
                  placeholder="0987654321"
                />
              </div>

              <div class="mb-3">
                <label class="form-label">Địa chỉ <span class="text-danger">*</span></label>
                <textarea
                  class="form-control"
                  v-model="orderInfo.address"
                  required
                  rows="3"
                  placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố"
                ></textarea>
              </div>

              <div class="mb-3">
                <label class="form-label">Ghi chú (tùy chọn)</label>
                <textarea
                  class="form-control"
                  v-model="orderInfo.note"
                  rows="2"
                  placeholder="Ghi chú về đơn hàng, thời gian giao hàng..."
                ></textarea>
              </div>

              <div class="form-check mb-3">
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="orderInfo.saveInfo"
                  id="saveInfo"
                />
                <label class="form-check-label" for="saveInfo">
                  Lưu thông tin cho lần sau
                </label>
              </div>
            </form>
          </div>
        </div>

        <!-- Phương thức thanh toán -->
        <div class="card mb-4">
          <div class="card-body">
            <h5 class="card-title mb-3">
              <i class="bi bi-credit-card"></i> Phương thức thanh toán
            </h5>

            <div class="form-check mb-2">
              <input
                class="form-check-input"
                type="radio"
                name="paymentMethod"
                id="cod"
                value="COD"
                v-model="orderInfo.paymentMethod"
                checked
              />
              <label class="form-check-label" for="cod">
                <strong>Thanh toán khi nhận hàng (COD)</strong>
                <div class="small text-muted">Bạn chỉ thanh toán khi nhận được hàng</div>
              </label>
            </div>

            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="paymentMethod"
                id="banking"
                value="BANKING"
                v-model="orderInfo.paymentMethod"
                disabled
              />
              <label class="form-check-label" for="banking">
                <strong>Chuyển khoản ngân hàng</strong>
                <div class="small text-muted">(Đang phát triển)</div>
              </label>
            </div>
          </div>
        </div>
      </div>

      <!-- Cột phải: Tóm tắt đơn hàng -->
      <div class="col-lg-5">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title mb-3">Đơn hàng của bạn</h5>

            <!-- Danh sách sản phẩm -->
            <div class="order-items mb-3" style="max-height: 400px; overflow-y: auto">
              <div
                v-for="(item, index) in cart"
                :key="index"
                class="d-flex gap-3 mb-3 pb-3 border-bottom"
              >
                <img :src="item.image" class="checkout-img" :alt="item.name" />
                <div class="flex-grow-1">
                  <div class="fw-bold">{{ item.name }}</div>
                  <div class="small text-muted">
                    Size: {{ item.sizeName || 'Chưa chọn' }} / Màu: {{ item.colorName || 'Chưa chọn' }}
                  </div>
                  <div class="d-flex justify-content-between mt-2">
                    <span>Số lượng: x{{ item.quantity }}</span>
                    <span class="text-primary">{{ formatPrice(item.price) }}</span>
                  </div>
                </div>
              </div>
            </div>

            <hr />

            <!-- Tổng tiền -->
            <div class="d-flex justify-content-between mb-2">
              <span>Tạm tính</span>
              <strong>{{ formatPrice(subtotal) }}</strong>
            </div>

            <div class="d-flex justify-content-between mb-2">
              <span>Thuế VAT (10%)</span>
              <strong>{{ formatPrice(vat) }}</strong>
            </div>

            <div class="d-flex justify-content-between mb-3">
              <span>Phí vận chuyển</span>
              <strong class="text-success">Miễn phí</strong>
            </div>

            <hr />

            <div class="d-flex justify-content-between mb-4">
              <strong class="fs-5">Tổng cộng</strong>
              <strong class="fs-5 text-danger">{{ formatPrice(total) }}</strong>
            </div>

            <!-- Nút đặt hàng -->
            <button
              class="btn btn-dark w-100 py-2"
              @click="submitOrder"
              :disabled="loading || cart.length === 0"
            >
              <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
              {{ loading ? 'Đang xử lý...' : 'ĐẶT HÀNG NGAY' }}
            </button>

            <div class="text-center mt-3">
              <router-link to="/cart" class="text-decoration-none">
                ← Quay lại giỏ hàng
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Giỏ hàng trống -->
    <div v-else class="text-center py-5">
      <div style="font-size: 80px; color: #ccc;">🛒</div>
      <p class="fs-5 mt-3">Giỏ hàng của bạn đang trống</p>
      <router-link to="/" class="btn btn-dark mt-2">
        Tiếp tục mua sắm
      </router-link>
    </div>
  </div>
</template>

<script>
import axios from '@/config/axios';

export default {
  name: "Checkout",

  data() {
    return {
      cart: [],
      loading: false,
      orderInfo: {
        customerName: '',
        phone: '',
        address: '',
        note: '',
        paymentMethod: 'COD',
        saveInfo: false
      }
    };
  },

  computed: {
    subtotal() {
      return this.cart.reduce(
        (sum, item) => sum + (item.price * (item.quantity || 1)),
        0
      );
    },
    vat() {
      return this.subtotal * 0.1; // VAT 10%
    },
    total() {
      return this.subtotal + this.vat;
    }
  },

  mounted() {
    this.loadCart();
    this.loadSavedInfo();

    // Nếu giỏ hàng trống, quay về trang chủ
    if (this.cart.length === 0) {
      alert('Giỏ hàng của bạn đang trống!');
      this.$router.push('/');
    }
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

    // Load thông tin đã lưu
    loadSavedInfo() {
      const savedInfo = localStorage.getItem('shipping_info');
      if (savedInfo) {
        const info = JSON.parse(savedInfo);
        this.orderInfo.customerName = info.customerName || '';
        this.orderInfo.phone = info.phone || '';
        this.orderInfo.address = info.address || '';
      }

      // Load user info nếu đã đăng nhập
      const user = localStorage.getItem('user');
      if (user) {
        const userData = JSON.parse(user);
        if (!this.orderInfo.customerName) this.orderInfo.customerName = userData.ten || '';
        if (!this.orderInfo.phone) this.orderInfo.phone = userData.soDienThoai || '';
      }
    },

    // Lưu thông tin giao hàng
    saveShippingInfo() {
      if (this.orderInfo.saveInfo) {
        const infoToSave = {
          customerName: this.orderInfo.customerName,
          phone: this.orderInfo.phone,
          address: this.orderInfo.address
        };
        localStorage.setItem('shipping_info', JSON.stringify(infoToSave));
      }
    },

    // Format giá tiền
    formatPrice(price) {
      if (!price && price !== 0) return "0 ₫";
      return price.toLocaleString("vi-VN") + " ₫";
    },

    // Kiểm tra thông tin
    validateOrder() {
      if (!this.orderInfo.customerName.trim()) {
        alert('Vui lòng nhập họ tên!');
        return false;
      }
      if (!this.orderInfo.phone.trim()) {
        alert('Vui lòng nhập số điện thoại!');
        return false;
      }
      if (!/^[0-9]{10,11}$/.test(this.orderInfo.phone.trim())) {
        alert('Số điện thoại không hợp lệ (10-11 số)!');
        return false;
      }
      if (!this.orderInfo.address.trim()) {
        alert('Vui lòng nhập địa chỉ!');
        return false;
      }
      return true;
    },

    // Gửi đơn hàng
    async submitOrder() {
      if (!this.validateOrder()) return;
      if (this.cart.length === 0) {
        alert('Giỏ hàng trống!');
        return;
      }

      // Kiểm tra đăng nhập (tùy chọn, có thể bỏ nếu cho phép mua không cần đăng nhập)
      const user = localStorage.getItem('user');
      if (!user) {
        const confirm = window.confirm('Bạn cần đăng nhập để đặt hàng. Đến trang đăng nhập?');
        if (confirm) {
          // Lưu lại trang hiện tại để sau khi đăng nhập quay lại
          localStorage.setItem('redirect_after_login', '/checkout');
          this.$router.push('/login');
        }
        return;
      }

      this.loading = true;

      try {
        const userData = JSON.parse(user);

        // Chuẩn bị dữ liệu gửi lên server
        const orderData = {
          userId: userData.maUser,
          customerName: this.orderInfo.customerName,
          phone: this.orderInfo.phone,
          address: this.orderInfo.address,
          note: this.orderInfo.note,
          paymentMethod: this.orderInfo.paymentMethod,
          subtotal: this.subtotal,
          vat: this.vat,
          totalAmount: this.total,
          items: this.cart.map(item => ({
            productId: item.id,
            productName: item.name,
            size: item.sizeName || '',
            color: item.colorName || '',
            quantity: item.quantity,
            price: item.price,
            image: item.image
          }))
        };

        console.log('Đang gửi đơn hàng:', orderData); // Debug

        // Gọi API tạo đơn hàng
        const response = await axios.post('http://localhost:8080/api/orders/create', orderData);

        if (response.status === 200 || response.status === 201) {
          // Lưu thông tin giao hàng nếu được chọn
          this.saveShippingInfo();

          // Xóa giỏ hàng
          localStorage.removeItem('cart');

          // Cập nhật số lượng giỏ hàng trên header
          window.dispatchEvent(new Event('cart-updated'));

          // Chuyển đến trang thành công
          this.$router.push({
            path: '/order-success',
            query: { orderId: response.data.id }
          });
        }
      } catch (error) {
        console.error('Lỗi đặt hàng:', error);
        if (error.response) {
          alert(`Lỗi: ${error.response.data.message || 'Không thể đặt hàng, vui lòng thử lại!'}`);
        } else if (error.request) {
          alert('Không thể kết nối đến server. Vui lòng kiểm tra backend đã chạy chưa!');
        } else {
          alert('Có lỗi xảy ra, vui lòng thử lại!');
        }
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.checkout-img {
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 8px;
}

.card {
  border: none;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.08);
  border-radius: 12px;
}

.order-items {
  scrollbar-width: thin;
}

.order-items::-webkit-scrollbar {
  width: 5px;
}

.order-items::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.order-items::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

.form-control:focus,
.form-check-input:focus {
  border-color: #000;
  box-shadow: 0 0 0 0.2rem rgba(0, 0, 0, 0.1);
}

.btn-dark {
  background-color: #000;
  transition: all 0.3s;
}

.btn-dark:hover:not(:disabled) {
  background-color: #333;
  transform: translateY(-2px);
}

.btn-dark:active {
  transform: translateY(0);
}

@media (max-width: 768px) {
  .checkout-img {
    width: 50px;
    height: 50px;
  }
}
</style>