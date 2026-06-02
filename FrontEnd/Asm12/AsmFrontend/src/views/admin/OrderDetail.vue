<template>
  <div>
    <div
      class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2"
    >
      <h2 class="mb-0">
        Chi tiết đơn hàng #{{ order?.id || $route.params.id }}
      </h2>
      <RouterLink to="/admin/orders" class="btn btn-outline-dark"
        >← Quay lại danh sách</RouterLink
      >
    </div>

    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-dark"></div>
    </div>

    <div v-else-if="order" class="row g-4">
      <div class="col-lg-8">
        <div class="card shadow-sm mb-4">
          <div class="card-header bg-dark text-white fw-bold">
            Thông tin đơn hàng
          </div>
          <div class="card-body">
            <div class="row g-3">
              <div class="col-md-6">
                <small class="text-muted d-block">Mã đơn</small>
                <strong>#{{ order.id }}</strong>
              </div>
              <div class="col-md-6">
                <small class="text-muted d-block">Ngày đặt</small>
                <strong>{{ formatDate(order.orderDate) }}</strong>
              </div>
              <div class="col-md-6">
                <small class="text-muted d-block">Khách hàng</small>
                <strong>{{ order.customerName || "---" }}</strong>
              </div>
              <div class="col-md-6">
                <small class="text-muted d-block">Số điện thoại</small>
                <strong>{{ order.phone || "---" }}</strong>
              </div>
              <div class="col-12">
                <small class="text-muted d-block">Địa chỉ giao hàng</small>
                <strong>{{ order.address || "---" }}</strong>
              </div>
              <div class="col-12" v-if="order.note">
                <small class="text-muted d-block">Ghi chú</small>
                <strong>{{ order.note }}</strong>
              </div>
            </div>
          </div>
        </div>

        <div class="card shadow-sm">
          <div class="card-header bg-dark text-white fw-bold">
            Sản phẩm trong đơn
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-bordered align-middle mb-0">
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
                  <tr
                    v-for="item in orderItems"
                    :key="
                      item.id || `${item.productId}-${item.size}-${item.color}`
                    "
                  >
                    <td>
                      <div class="d-flex align-items-center gap-2">
                        <img
                          v-if="item.image"
                          :src="item.image"
                          alt="product"
                          class="product-img"
                        />
                        <span>{{ item.productName || "Sản phẩm" }}</span>
                      </div>
                    </td>
                    <td>
                      <small>
                        Size: {{ item.size || "--" }}<br />
                        Màu: {{ item.color || "--" }}
                      </small>
                    </td>
                    <td>{{ formatPrice(item.price) }}</td>
                    <td>x{{ item.quantity }}</td>
                    <td class="fw-bold text-danger">
                      {{
                        formatPrice((item.price || 0) * (item.quantity || 0))
                      }}
                    </td>
                  </tr>
                  <tr v-if="orderItems.length === 0">
                    <td colspan="5" class="text-center py-4">
                      Không có sản phẩm trong đơn hàng
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div class="col-lg-4">
        <div class="card shadow-sm sticky-top" style="top: 20px">
          <div class="card-header bg-dark text-white fw-bold">
            Trạng thái và thanh toán
          </div>
          <div class="card-body">
            <div class="mb-3">
              <small class="text-muted d-block mb-1">Trạng thái hiện tại</small>
              <span :class="['badge', getStatusClass(order.status)]">{{
                getStatusText(order.status)
              }}</span>
            </div>

            <div class="mb-3">
              <label class="form-label">Cập nhật trạng thái</label>
              <select
                v-model="selectedStatus"
                class="form-select"
                :disabled="saving || isStatusLocked(order.status)"
              >
                <option
                  v-for="status in allowedStatusOptions"
                  :key="status.value"
                  :value="status.value"
                >
                  {{ status.label }}
                </option>
              </select>
            </div>

            <div v-if="selectedStatus === 'CANCELLED'" class="mb-3">
              <label class="form-label">Lý do hủy đơn</label>
              <textarea
                v-model.trim="cancellationReasonInput"
                class="form-control"
                rows="3"
                placeholder="Nhập lý do hủy đơn"
                :disabled="saving || isStatusLocked(order.status)"
              ></textarea>
            </div>

            <div
              v-if="order.status === 'CANCELLED' && order.cancellationReason"
              class="alert alert-warning py-2"
            >
              <strong>Lý do hủy:</strong> {{ order.cancellationReason }}
            </div>

            <button
              class="btn btn-primary w-100 mb-4"
              @click="saveStatus"
              :disabled="
                saving ||
                selectedStatus === order.status ||
                isStatusLocked(order.status)
              "
            >
              <span
                v-if="saving"
                class="spinner-border spinner-border-sm me-2"
              ></span>
              {{ saving ? "Đang lưu..." : "💾 Lưu trạng thái" }}
            </button>

            <hr />

            <div class="d-flex justify-content-between mb-2">
              <span>Tạm tính</span>
              <span>{{ formatPrice(order.subtotal ?? order.total) }}</span>
            </div>
            <div class="d-flex justify-content-between mb-2">
              <span>VAT</span>
              <span>{{ formatPrice(order.vat || 0) }}</span>
            </div>
            <div class="d-flex justify-content-between mb-2">
              <span>Phí vận chuyển</span>
              <span>{{ formatPrice(order.shippingFee || 0) }}</span>
            </div>
            <hr />
            <div class="d-flex justify-content-between">
              <strong>Tổng cộng</strong>
              <strong class="text-danger">{{
                formatPrice(order.total)
              }}</strong>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-5">
      <h5>Không tìm thấy đơn hàng</h5>
      <RouterLink to="/admin/orders" class="btn btn-dark mt-3"
        >Quay lại danh sách đơn</RouterLink
      >
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AdminOrderDetail",

  data() {
    return {
      order: null,
      loading: false,
      saving: false,
      selectedStatus: "PENDING",
      cancellationReasonInput: "",
    };
  },

  computed: {
    orderItems() {
      if (!this.order) return [];
      return this.order.items || this.order.orderDetails || [];
    },

    allowedStatusOptions() {
      if (!this.order) return [];
      return this.getAllowedStatusOptions(this.order.status);
    },
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
          `http://localhost:8080/api/admin/orders/${orderId}`,
        );
        this.order = response.data;
        this.selectedStatus = this.order?.status || "PENDING";
        this.cancellationReasonInput = this.order?.cancellationReason || "";
      } catch (error) {
        console.error("Lỗi tải chi tiết đơn hàng:", error);
        alert("Không thể tải chi tiết đơn hàng!");
      } finally {
        this.loading = false;
      }
    },

    async saveStatus() {
      if (!this.order) return;

      if (!this.isValidTransition(this.order.status, this.selectedStatus)) {
        alert(
          "Trạng thái chỉ được chuyển sang bước kế tiếp hoặc hủy khi chưa hoàn thành!",
        );
        this.selectedStatus = this.order.status;
        return;
      }

      const payload = {
        status: this.selectedStatus,
      };

      if (
        this.selectedStatus === "CANCELLED" &&
        this.order.status !== "CANCELLED"
      ) {
        const cancellationReason = this.cancellationReasonInput.trim();
        if (!cancellationReason) {
          alert("Vui lòng nhập lý do hủy đơn!");
          return;
        }
        payload.cancellationReason = cancellationReason;
      }

      this.saving = true;
      try {
        await axios.put(
          `http://localhost:8080/api/admin/orders/${this.order.id}/status`,
          payload,
        );
        this.order.status = this.selectedStatus;
        this.order.cancellationReason =
          this.selectedStatus === "CANCELLED"
            ? payload.cancellationReason || this.order.cancellationReason || ""
            : null;
        if (this.selectedStatus !== "CANCELLED") {
          this.cancellationReasonInput = "";
        }
        alert("Cập nhật trạng thái thành công!");
      } catch (error) {
        console.error("Lỗi cập nhật trạng thái:", error);
        const message =
          error?.response?.data?.message || "Không thể cập nhật trạng thái!";
        alert(message);
      } finally {
        this.saving = false;
      }
    },

    formatPrice(price) {
      if (!price) return "0 ₫";
      return Number(price).toLocaleString("vi-VN") + " ₫";
    },

    formatDate(dateString) {
      if (!dateString) return "";
      const date = new Date(dateString);
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} ${hours}:${minutes}`;
    },

    getStatusText(status) {
      const map = {
        PENDING: "⏳ Chờ xử lý",
        CONFIRMED: "✅ Đã xác nhận",
        SHIPPING: "🚚 Đang giao",
        COMPLETED: "🎉 Hoàn thành",
        CANCELLED: "❌ Đã hủy",
      };
      return map[status] || status;
    },

    isStatusLocked(status) {
      return status === "COMPLETED" || status === "CANCELLED";
    },

    isValidTransition(currentStatus, nextStatus) {
      if (currentStatus === nextStatus) return true;

      const flow = ["PENDING", "CONFIRMED", "SHIPPING", "COMPLETED"];
      const currentIndex = flow.indexOf(currentStatus);
      const nextIndex = flow.indexOf(nextStatus);

      if (currentIndex !== -1 && nextIndex === currentIndex + 1) {
        return true;
      }

      if (
        nextStatus === "CANCELLED" &&
        currentStatus !== "COMPLETED" &&
        currentStatus !== "CANCELLED"
      ) {
        return true;
      }

      return false;
    },

    getAllowedStatusOptions(currentStatus) {
      if (this.isStatusLocked(currentStatus)) {
        return [
          { value: currentStatus, label: this.getStatusText(currentStatus) },
        ];
      }

      const flow = ["PENDING", "CONFIRMED", "SHIPPING", "COMPLETED"];
      const currentIndex = flow.indexOf(currentStatus);
      const options = [currentStatus];

      if (currentIndex !== -1 && currentIndex < flow.length - 1) {
        options.push(flow[currentIndex + 1]);
      }

      if (currentStatus !== "COMPLETED" && currentStatus !== "CANCELLED") {
        options.push("CANCELLED");
      }

      return [...new Set(options)].map((status) => ({
        value: status,
        label: this.getStatusText(status),
      }));
    },

    getStatusClass(status) {
      const map = {
        PENDING: "bg-warning text-dark",
        CONFIRMED: "bg-info text-dark",
        SHIPPING: "bg-primary text-white",
        COMPLETED: "bg-success text-white",
        CANCELLED: "bg-danger text-white",
      };
      return map[status] || "bg-secondary";
    },
  },
};
</script>

<style scoped>
.product-img {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: 8px;
}

.badge {
  padding: 8px 10px;
  border-radius: 14px;
}

.table th,
.table td {
  vertical-align: middle;
}

@media (max-width: 768px) {
  .product-img {
    width: 40px;
    height: 40px;
  }
}
</style>
