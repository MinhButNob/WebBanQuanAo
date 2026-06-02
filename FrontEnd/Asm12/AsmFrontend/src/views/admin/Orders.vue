<template>
  <div>
    <h2 class="mb-4">Quản lý đơn hàng</h2>

    <!-- Bộ lọc -->
    <div class="d-flex gap-3 mb-4 flex-wrap justify-content-between">
      <div class="d-flex gap-3 flex-wrap">
        <select
          v-model="filterStatus"
          class="form-select w-auto"
          @change="resetPageAndFilter"
        >
          <option value="ALL">Tất cả</option>
          <option value="PENDING">⏳ Chờ xử lý</option>
          <option value="CONFIRMED">✅ Đã xác nhận</option>
          <option value="SHIPPING">🚚 Đang giao</option>
          <option value="COMPLETED">🎉 Hoàn thành</option>
          <option value="CANCELLED">❌ Đã hủy</option>
        </select>

        <input
          type="text"
          v-model="searchKeyword"
          class="form-control"
          placeholder="Tìm theo mã, tên KH, SĐT..."
          style="width: 250px"
          @input="resetPageAndFilter"
        />
      </div>

      <select
        v-model="pageSize"
        class="form-select w-auto"
        style="width: 120px"
        @change="currentPage = 1"
      >
        <option :value="5">5 / trang</option>
        <option :value="10">10 / trang</option>
        <option :value="20">20 / trang</option>
        <option :value="50">50 / trang</option>
      </select>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-dark"></div>
    </div>

    <!-- TABLE -->
    <div v-else>
      <div class="table-responsive">
        <table class="table table-bordered align-middle">
          <thead class="table-dark">
            <tr>
              <th>#</th>
              <th>Mã đơn</th>
              <th>Khách hàng</th>
              <th>SĐT</th>
              <th>Ngày đặt</th>
              <th>Tổng tiền</th>
              <th>Trạng thái</th>
              <th width="150">Hành động</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="(order, index) in paginatedOrders" :key="order.id">
              <td>{{ (currentPage - 1) * pageSize + index + 1 }}</td>
              <td>#{{ order.id }}</td>
              <td>{{ order.customerName }}</td>
              <td>{{ order.phone }}</td>
              <td>{{ formatDate(order.orderDate) }}</td>
              <td class="text-danger fw-bold">
                {{ formatPrice(order.total) }}
              </td>
              <td>
                <select
                  v-model="order.status"
                  class="form-select form-select-sm"
                  :disabled="
                    saving === order.id ||
                    isStatusLocked(getRuleStatus(order.id, order.status))
                  "
                >
                  <option
                    v-for="status in getAllowedStatusOptions(
                      getRuleStatus(order.id, order.status),
                    )"
                    :key="status.value"
                    :value="status.value"
                  >
                    {{ status.label }}
                  </option>
                </select>

                <div v-if="order.status === 'CANCELLED'" class="mt-2">
                  <textarea
                    v-model.trim="cancelReasons[order.id]"
                    class="form-control form-control-sm"
                    rows="2"
                    placeholder="Nhập lý do hủy đơn"
                    :disabled="
                      saving === order.id ||
                      getRuleStatus(order.id, order.status) === 'CANCELLED'
                    "
                  ></textarea>
                </div>
              </td>
              <td>
                <button
                  class="btn btn-sm btn-primary me-2"
                  @click="saveStatus(order.id, order.status)"
                  :disabled="
                    saving === order.id ||
                    order.status === getRuleStatus(order.id, order.status)
                  "
                >
                  <span
                    v-if="saving === order.id"
                    class="spinner-border spinner-border-sm"
                  ></span>
                  <span v-else>💾 Lưu</span>
                </button>

                <RouterLink
                  class="btn btn-sm btn-outline-dark"
                  :to="`/admin/orders/${order.id}`"
                >
                  📋 Chi tiết
                </RouterLink>
              </td>
            </tr>

            <tr v-if="paginatedOrders.length === 0">
              <td colspan="8" class="text-center py-4">
                📦 Không tìm thấy đơn hàng nào
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- PHÂN TRANG -->
      <div
        v-if="filteredOrders.length > 0"
        class="d-flex justify-content-between align-items-center mt-3"
      >
        <small class="text-muted">
          Hiển thị {{ (currentPage - 1) * pageSize + 1 }} -
          {{ Math.min(currentPage * pageSize, filteredOrders.length) }}
          / {{ filteredOrders.length }} đơn hàng
        </small>

        <nav>
          <ul class="pagination mb-0">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <button
                class="page-link"
                @click="currentPage--"
                :disabled="currentPage === 1"
              >
                «
              </button>
            </li>

            <li
              v-for="page in displayPages"
              :key="page"
              class="page-item"
              :class="{ active: currentPage === page }"
            >
              <button class="page-link" @click="currentPage = page">
                {{ page }}
              </button>
            </li>

            <li
              class="page-item"
              :class="{ disabled: currentPage === totalPages }"
            >
              <button
                class="page-link"
                @click="currentPage++"
                :disabled="currentPage === totalPages"
              >
                »
              </button>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AdminOrders",

  data() {
    return {
      orders: [],
      originalOrders: [],
      loading: false,
      saving: null,
      filterStatus: "ALL",
      searchKeyword: "",
      cancelReasons: {},
      // Phân trang
      currentPage: 1,
      pageSize: 10,
    };
  },

  computed: {
    filteredOrders() {
      let filtered = [...this.orders];

      // Lọc theo trạng thái
      if (this.filterStatus !== "ALL") {
        filtered = filtered.filter(
          (order) => order.status === this.filterStatus,
        );
      }

      // Tìm kiếm
      if (this.searchKeyword) {
        const keyword = this.searchKeyword.toLowerCase();
        filtered = filtered.filter(
          (order) =>
            order.id.toString().includes(keyword) ||
            order.customerName.toLowerCase().includes(keyword) ||
            order.phone.includes(keyword),
        );
      }

      return filtered;
    },

    totalPages() {
      return Math.ceil(this.filteredOrders.length / this.pageSize);
    },

    paginatedOrders() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredOrders.slice(start, end);
    },

    displayPages() {
      const pages = [];
      const maxVisible = 5;
      let startPage = Math.max(
        1,
        this.currentPage - Math.floor(maxVisible / 2),
      );
      let endPage = Math.min(this.totalPages, startPage + maxVisible - 1);

      if (endPage - startPage + 1 < maxVisible) {
        startPage = Math.max(1, endPage - maxVisible + 1);
      }

      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
      return pages;
    },
  },

  mounted() {
    this.loadOrders();
  },

  methods: {
    getStatusLabel(status) {
      const map = {
        PENDING: "⏳ Chờ xử lý",
        CONFIRMED: "✅ Đã xác nhận",
        SHIPPING: "🚚 Đang giao",
        COMPLETED: "🎉 Hoàn thành",
        CANCELLED: "❌ Đã hủy",
      };
      return map[status] || status;
    },

    getRuleStatus(orderId, fallbackStatus) {
      const original = this.originalOrders.find((o) => o.id === orderId);
      return original ? original.status : fallbackStatus;
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
          { value: currentStatus, label: this.getStatusLabel(currentStatus) },
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
        label: this.getStatusLabel(status),
      }));
    },

    async loadOrders() {
      this.loading = true;
      try {
        const response = await axios.get(
          "http://localhost:8080/api/admin/orders",
        );
        // Sắp xếp đơn mới nhất lên đầu
        this.orders = response.data.sort((a, b) => {
          return new Date(b.orderDate) - new Date(a.orderDate);
        });
        // Lưu bản sao trạng thái gốc
        this.originalOrders = JSON.parse(JSON.stringify(this.orders));
        this.cancelReasons = this.orders.reduce((acc, order) => {
          acc[order.id] = order.cancellationReason || "";
          return acc;
        }, {});
        console.log("Loaded orders:", this.orders.length);
      } catch (error) {
        console.error("Lỗi tải đơn hàng:", error);
        alert("Không thể tải danh sách đơn hàng!");
      } finally {
        this.loading = false;
      }
    },

    resetPageAndFilter() {
      this.currentPage = 1;
    },

    async saveStatus(orderId, newStatus) {
      const currentStatus = this.getRuleStatus(orderId, newStatus);

      if (!this.isValidTransition(currentStatus, newStatus)) {
        alert(
          "Trạng thái chỉ được chuyển sang bước kế tiếp hoặc hủy khi chưa hoàn thành!",
        );
        const order = this.orders.find((o) => o.id === orderId);
        if (order) {
          order.status = currentStatus;
        }
        return;
      }

      const payload = {
        status: newStatus,
      };

      if (newStatus === "CANCELLED" && currentStatus !== "CANCELLED") {
        const cancellationReason = (this.cancelReasons[orderId] || "").trim();
        if (!cancellationReason) {
          alert("Vui lòng nhập lý do hủy đơn!");
          return;
        }
        payload.cancellationReason = cancellationReason;
      }

      this.saving = orderId;
      try {
        await axios.put(
          `http://localhost:8080/api/admin/orders/${orderId}/status`,
          payload,
        );
        alert("Cập nhật trạng thái thành công!");

        // Cập nhật lại bản sao gốc
        const index = this.originalOrders.findIndex((o) => o.id === orderId);
        if (index !== -1) {
          this.originalOrders[index].status = newStatus;
          this.originalOrders[index].cancellationReason =
            newStatus === "CANCELLED"
              ? payload.cancellationReason || this.cancelReasons[orderId] || ""
              : null;
        }

        const order = this.orders.find((o) => o.id === orderId);
        if (order) {
          order.cancellationReason =
            newStatus === "CANCELLED"
              ? payload.cancellationReason || this.cancelReasons[orderId] || ""
              : null;
        }
      } catch (error) {
        console.error("Lỗi cập nhật:", error);
        const message =
          error?.response?.data?.message || "Không thể cập nhật trạng thái!";
        alert(message);

        // Khôi phục trạng thái cũ
        const original = this.originalOrders.find((o) => o.id === orderId);
        if (original) {
          const order = this.orders.find((o) => o.id === orderId);
          if (order) order.status = original.status;
        }
      } finally {
        this.saving = null;
      }
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
  },
};
</script>

<style scoped>
.table th,
.table td {
  vertical-align: middle;
}

.form-select-sm {
  width: 140px;
}

.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
}

.btn-primary:hover {
  background-color: #0056b3;
  border-color: #0056b3;
}

.pagination {
  margin-bottom: 0;
}

.page-link {
  cursor: pointer;
  color: #000;
}

.page-item.active .page-link {
  background-color: #000;
  border-color: #000;
  color: white;
}

.page-link:hover {
  background-color: #f8f9fa;
  color: #000;
}
</style>
