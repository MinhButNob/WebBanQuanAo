<template>
  <div class="container py-5">
    <h2 class="mb-4 fw-bold">📦 ĐƠN HÀNG CỦA TÔI</h2>

    <!-- Kiểm tra đăng nhập -->
    <div v-if="!isLoggedIn" class="text-center py-5">
      <div style="font-size: 80px; color: #ccc;">🔒</div>
      <p class="fs-5 mt-3">Vui lòng đăng nhập để xem đơn hàng</p>
      <router-link to="/login" class="btn btn-dark mt-2">
        Đăng nhập ngay
      </router-link>
    </div>

    <!-- Có đăng nhập -->
    <div v-else>
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-dark"></div>
      </div>

      <div v-else-if="orders.length > 0">
        <!-- Bộ lọc -->
        <div class="mb-4">
          <div class="btn-group flex-wrap">
            <button 
              v-for="status in statusFilters" 
              :key="status.value"
              :class="['btn', currentFilter === status.value ? 'btn-dark' : 'btn-outline-dark']"
              @click="filterStatus(status.value)"
            >
              {{ status.label }}
            </button>
          </div>
        </div>

        <!-- Danh sách đơn hàng -->
        <div class="orders-list">
          <div 
            v-for="order in filteredOrders" 
            :key="order.id" 
            class="card mb-3 order-card"
          >
            <div class="card-body">
              <div class="row align-items-center">
                <div class="col-md-2">
                  <small class="text-muted">Mã đơn</small>
                  <div class="fw-bold">#{{ order.id }}</div>
                </div>
                <div class="col-md-2">
                  <small class="text-muted">Ngày đặt</small>
                  <div>{{ formatDate(order.orderDate) }}</div>
                </div>
                <div class="col-md-2">
                  <small class="text-muted">Tổng tiền</small>
                  <div class="fw-bold text-danger">{{ formatPrice(order.total) }}</div>
                </div>
                <div class="col-md-3">
                  <small class="text-muted">Trạng thái</small>
                  <div>
                    <span :class="['badge', getStatusClass(order.status)]">
                      {{ getStatusText(order.status) }}
                    </span>
                  </div>
                </div>
                <div class="col-md-3 text-end">
                  <router-link 
                    :to="`/order-detail/${order.id}`" 
                    class="btn btn-outline-dark btn-sm"
                  >
                    Xem chi tiết →
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Không có đơn hàng -->
      <div v-else class="text-center py-5">
        <div style="font-size: 80px; color: #ccc;">📦</div>
        <p class="fs-5 mt-3">Bạn chưa có đơn hàng nào</p>
        <router-link to="/" class="btn btn-dark mt-2">
          Mua sắm ngay
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ClientOrders',  // ĐỔI TÊN COMPONENT

  data() {
    return {
      orders: [],
      loading: false,
      currentFilter: 'ALL',
      statusFilters: [
        { value: 'ALL', label: 'Tất cả' },
        { value: 'PENDING', label: 'Chờ xác nhận' },
        { value: 'CONFIRMED', label: 'Đã xác nhận' },
        { value: 'SHIPPING', label: 'Đang giao' },
        { value: 'COMPLETED', label: 'Hoàn thành' },
        { value: 'CANCELLED', label: 'Đã hủy' }
      ]
    };
  },

  computed: {
    isLoggedIn() {
      return localStorage.getItem('user') !== null;
    },

    filteredOrders() {
      if (this.currentFilter === 'ALL') {
        return this.orders;
      }
      return this.orders.filter(order => order.status === this.currentFilter);
    }
  },

  mounted() {
    if (this.isLoggedIn) {
      this.loadOrders();
    }
  },

  methods: {
    async loadOrders() {
      this.loading = true;
      try {
        const user = JSON.parse(localStorage.getItem('user'));
        const response = await axios.get(`http://localhost:8080/api/orders/user/${user.maUser}`);
        this.orders = response.data;
        console.log('Client orders:', this.orders);
      } catch (error) {
        console.error('Lỗi tải đơn hàng:', error);
        if (error.response && error.response.status === 404) {
          this.orders = [];
        } else {
          alert('Không thể tải danh sách đơn hàng!');
        }
      } finally {
        this.loading = false;
      }
    },

    filterStatus(status) {
      this.currentFilter = status;
    },

    formatPrice(price) {
      if (!price) return '0 ₫';
      return price.toLocaleString('vi-VN') + ' ₫';
    },

    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    },

    getStatusText(status) {
      const map = {
        'PENDING': 'Chờ xác nhận',
        'CONFIRMED': 'Đã xác nhận',
        'SHIPPING': 'Đang giao hàng',
        'COMPLETED': 'Hoàn thành',
        'CANCELLED': 'Đã hủy'
      };
      return map[status] || status;
    },

    getStatusClass(status) {
      const map = {
        'PENDING': 'bg-warning text-dark',
        'CONFIRMED': 'bg-info text-dark',
        'SHIPPING': 'bg-primary text-white',
        'COMPLETED': 'bg-success text-white',
        'CANCELLED': 'bg-danger text-white'
      };
      return map[status];
    }
  }
};
</script>

<style scoped>
.order-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #eee;
}

.order-card:hover {
  transform: translateX(5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
  border-color: #000;
}

.badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.btn-group .btn {
  border-radius: 20px;
  margin: 0 5px;
  padding: 8px 20px;
}

@media (max-width: 768px) {
  .btn-group {
    flex-wrap: wrap;
    gap: 5px;
  }
  
  .btn-group .btn {
    font-size: 12px;
    padding: 5px 12px;
  }
}
</style>