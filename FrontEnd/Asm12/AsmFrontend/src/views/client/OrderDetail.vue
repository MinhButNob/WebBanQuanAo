<template>
  <div class="container py-5">
    <!-- Nút quay lại -->
    <div class="mb-4">
      <button @click="$router.back()" class="btn btn-outline-dark">
        ← Quay lại
      </button>
    </div>

    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-dark"></div>
    </div>

    <div v-else-if="order" class="row">
      <!-- Cột trái: Thông tin đơn hàng -->
      <div class="col-lg-8">
        <!-- Trạng thái đơn hàng -->
        <div class="card mb-4">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <span
                  :class="['badge', getStatusClass(order.status)]"
                  style="font-size: 14px"
                >
                  {{ getStatusText(order.status) }}
                </span>
                <div class="mt-2">
                  <small class="text-muted">Mã đơn hàng: #{{ order.id }}</small>
                </div>
              </div>
              <div class="text-end">
                <small class="text-muted">Ngày đặt</small>
                <div>{{ formatDate(order.orderDate) }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Thông tin giao hàng -->
        <div class="card mb-4">
          <div class="card-header bg-light">
            <h5 class="mb-0">📦 Thông tin giao hàng</h5>
          </div>
          <div class="card-body">
            <div class="row">
              <div class="col-md-6 mb-2">
                <strong>Người nhận:</strong> {{ order.customerName }}
              </div>
              <div class="col-md-6 mb-2">
                <strong>Số điện thoại:</strong> {{ order.phone }}
              </div>
              <div class="col-12 mb-2">
                <strong>Địa chỉ:</strong> {{ order.address }}
              </div>
              <div class="col-12" v-if="order.note">
                <strong>Ghi chú:</strong> {{ order.note }}
              </div>
            </div>

            <div
              v-if="order.status === 'CANCELLED' && order.cancellationReason"
              class="alert alert-warning mt-3 mb-0"
            >
              <strong>Lý do hủy đơn:</strong> {{ order.cancellationReason }}
            </div>
          </div>
        </div>

        <!-- Danh sách sản phẩm -->
        <div class="card">
          <div class="card-header bg-light">
            <h5 class="mb-0">🛍️ Sản phẩm đã đặt</h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table mb-0">
                <thead class="table-light">
                  <tr>
                    <th>Sản phẩm</th>
                    <th>Phân loại</th>
                    <th>Đơn giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in order.items" :key="item.id">
                    <td>
                      <div class="d-flex align-items-center gap-3">
                        <img :src="item.image" class="product-img" />
                        <span>{{ item.productName }}</span>
                      </div>
                    </td>
                    <td>
                      <small class="text-muted">
                        Size: {{ item.size || "--" }}<br />
                        Màu: {{ item.color || "--" }}
                      </small>
                    </td>
                    <td>{{ formatPrice(item.price) }}</td>
                    <td>x{{ item.quantity }}</td>
                    <td class="fw-bold text-primary">
                      {{ formatPrice(item.price * item.quantity) }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Cột phải: Tổng tiền -->
      <div class="col-lg-4">
        <div class="card sticky-top" style="top: 20px">
          <div class="card-header bg-light">
            <h5 class="mb-0">💰 Tổng tiền</h5>
          </div>
          <div class="card-body">
            <div class="d-flex justify-content-between mb-2">
              <span>Tạm tính</span>
              <span>{{ formatPrice(order.subtotal) }}</span>
            </div>
            <div class="d-flex justify-content-between mb-2">
              <span>Thuế VAT (10%)</span>
              <span>{{ formatPrice(order.vat) }}</span>
            </div>
            <div class="d-flex justify-content-between mb-2">
              <span>Phí vận chuyển</span>
              <span class="text-success">Miễn phí</span>
            </div>
            <hr />
            <div class="d-flex justify-content-between mb-3">
              <strong class="fs-5">Tổng cộng</strong>
              <strong class="fs-5 text-danger">{{
                formatPrice(order.total)
              }}</strong>
            </div>

            <div class="d-grid gap-2">
              <button
                v-if="order.status === 'PENDING'"
                class="btn btn-danger"
                @click="cancelOrder"
              >
                Hủy đơn hàng
              </button>
              <button
                v-if="order.status === 'COMPLETED'"
                class="btn btn-success"
                @click="buyAgain"
              >
                Mua lại
              </button>
            </div>

            <!-- Theo dõi đơn hàng -->
            <div class="mt-4">
              <div class="progress" style="height: 5px">
                <div
                  class="progress-bar bg-dark"
                  :style="{ width: getProgressWidth() }"
                ></div>
              </div>
              <div class="d-flex justify-content-between mt-2 small">
                <span class="text-success" v-if="order.status !== 'CANCELLED'"
                  >✅ Đặt hàng</span
                >
                <span
                  class="text-success"
                  v-if="
                    ['CONFIRMED', 'SHIPPING', 'COMPLETED'].includes(
                      order.status,
                    )
                  "
                  >✅ Xác nhận</span
                >
                <span
                  class="text-success"
                  v-if="['SHIPPING', 'COMPLETED'].includes(order.status)"
                  >✅ Giao hàng</span
                >
                <span class="text-success" v-if="order.status === 'COMPLETED'"
                  >✅ Hoàn thành</span
                >
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Không tìm thấy -->
    <div v-else class="text-center py-5">
      <h3>🔍 Không tìm thấy đơn hàng</h3>
      <router-link to="/orders" class="btn btn-dark mt-3">
        Xem danh sách đơn hàng
      </router-link>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "OrderDetail",

  data() {
    return {
      order: null,
      loading: false,
    };
  },

  mounted() {
    this.loadOrderDetail();
  },

  methods: {
    async loadOrderDetail() {
      this.loading = true;
      try {
        const orderId = this.$route.params.id;
        const response = await axios.get(
          `http://localhost:8080/api/orders/${orderId}`,
        );
        this.order = response.data;
      } catch (error) {
        console.error("Lỗi:", error);
        alert("Không thể tải chi tiết đơn hàng!");
      } finally {
        this.loading = false;
      }
    },

    async cancelOrder() {
      if (confirm("Bạn có chắc muốn hủy đơn hàng này?")) {
        try {
          // Gọi API hủy đơn hàng
          await axios.put(
            `http://localhost:8080/api/orders/${this.order.id}/cancel`,
          );
          alert("Đã hủy đơn hàng!");
          this.loadOrderDetail(); // Reload
        } catch (error) {
          alert("Hủy đơn hàng thất bại!");
        }
      }
    },

    buyAgain() {
      // Thêm lại sản phẩm vào giỏ hàng
      const cart = JSON.parse(localStorage.getItem("cart") || "[]");
      this.order.items.forEach((item) => {
        cart.push({
          id: item.productId,
          name: item.productName,
          price: item.price,
          quantity: item.quantity,
          image: item.image,
          sizeName: item.size,
          colorName: item.color,
        });
      });
      localStorage.setItem("cart", JSON.stringify(cart));
      window.dispatchEvent(new Event("cart-updated"));
      alert("Đã thêm sản phẩm vào giỏ hàng!");
      this.$router.push("/cart");
    },

    getProgressWidth() {
      const statusMap = {
        PENDING: 25,
        CONFIRMED: 50,
        SHIPPING: 75,
        COMPLETED: 100,
        CANCELLED: 0,
      };
      return statusMap[this.order?.status] + "%";
    },

    formatPrice(price) {
      if (!price) return "0 ₫";
      return price.toLocaleString("vi-VN") + " ₫";
    },

    formatDate(dateString) {
      if (!dateString) return "";
      const date = new Date(dateString);
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`;
    },

    getStatusText(status) {
      const map = {
        PENDING: "⏳ Chờ xác nhận",
        CONFIRMED: "✅ Đã xác nhận",
        SHIPPING: "🚚 Đang giao hàng",
        COMPLETED: "🎉 Hoàn thành",
        CANCELLED: "❌ Đã hủy",
      };
      return map[status] || status;
    },

    getStatusClass(status) {
      const map = {
        PENDING: "bg-warning text-dark",
        CONFIRMED: "bg-info text-dark",
        SHIPPING: "bg-primary text-white",
        COMPLETED: "bg-success text-white",
        CANCELLED: "bg-danger text-white",
      };
      return map[status];
    },
  },
};
</script>

<style scoped>
.product-img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
}

.card {
  border: 1px solid #eee;
  border-radius: 12px;
  overflow: hidden;
}

.card-header {
  font-weight: 500;
}

.table > :not(caption) > * > * {
  vertical-align: middle;
}

.badge {
  padding: 6px 12px;
  border-radius: 20px;
}

.sticky-top {
  position: sticky;
  top: 20px;
}

@media (max-width: 768px) {
  .product-img {
    width: 40px;
    height: 40px;
  }

  .table {
    font-size: 12px;
  }
}
</style>
