<template>
  <div>
    <h2 class="mb-4">Dashboard</h2>

    <!-- TABS -->
    <ul class="nav nav-tabs mb-4">
      <li class="nav-item">
        <button 
          class="nav-link" 
          :class="{ active: activeTab === 'overview' }"
          @click="activeTab = 'overview'"
        >
          📊 Tổng quan
        </button>
      </li>
      <li class="nav-item">
        <button 
          class="nav-link" 
          :class="{ active: activeTab === 'revenue' }"
          @click="activeTab = 'revenue'; loadRevenueData()"
        >
          💰 Doanh thu
        </button>
      </li>
    </ul>

    <!-- TAB TỔNG QUAN -->
    <div v-if="activeTab === 'overview'">
      <!-- Loading -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-dark"></div>
      </div>

      <div v-else>
        <!-- Thống kê -->
        <div class="row mb-4">
          <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
              <div class="card-body">
                <div class="stat-icon mb-2">📦</div>
                <h6 class="text-muted">Sản phẩm</h6>
                <h3 class="fw-bold">{{ stats.products }}</h3>
              </div>
            </div>
          </div>
          <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
              <div class="card-body">
                <div class="stat-icon mb-2">📋</div>
                <h6 class="text-muted">Đơn hàng</h6>
                <h3 class="fw-bold">{{ stats.orders }}</h3>
              </div>
            </div>
          </div>
          <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
              <div class="card-body">
                <div class="stat-icon mb-2">👥</div>
                <h6 class="text-muted">Khách hàng</h6>
                <h3 class="fw-bold">{{ stats.customers }}</h3>
              </div>
            </div>
          </div>
          <div class="col-md-3 mb-3">
            <div class="card text-center shadow-sm">
              <div class="card-body">
                <div class="stat-icon mb-2">💰</div>
                <h6 class="text-muted">Doanh thu</h6>
                <h3 class="fw-bold">{{ formatPrice(stats.revenue) }}</h3>
              </div>
            </div>
          </div>
        </div>

        <!-- Đơn hàng gần đây -->
        <div class="card shadow-sm">
          <div class="card-header fw-bold bg-dark text-white">
            📦 Đơn hàng gần đây
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-striped mb-0">
                <thead class="table-light">
                  <tr>
                    <th>Mã đơn</th>
                    <th>Khách hàng</th>
                    <th>SĐT</th>
                    <th>Ngày đặt</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="order in recentOrders" :key="order.id">
                    <td>#{{ order.id }}</td>
                    <td>{{ order.customerName }}</td>
                    <td>{{ order.phone }}</td>
                    <td>{{ formatDate(order.orderDate) }}</td>
                    <td class="text-danger fw-bold">{{ formatPrice(order.total) }}</td>
                    <td>
                      <span :class="['badge', getStatusClass(order.status)]">
                        {{ getStatusText(order.status) }}
                      </span>
                    </td>
                  </tr>
                  <tr v-if="recentOrders.length === 0">
                    <td colspan="6" class="text-center py-4">
                      📦 Chưa có đơn hàng nào
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- TAB DOANH THU -->
    <div v-if="activeTab === 'revenue'">
      <div class="card shadow-sm">
        <div class="card-header bg-dark text-white">
          <h5 class="mb-0">💰 Thống kê doanh thu</h5>
        </div>
        <div class="card-body">
          <!-- Bộ lọc thời gian -->
          <div class="row mb-4">
            <div class="col-md-4">
              <label class="form-label fw-bold">Khoảng thời gian</label>
              <select v-model="timeRange" class="form-select" @change="loadRevenueData">
                <option value="today">Hôm nay</option>
                <option value="week">7 ngày qua</option>
                <option value="month">30 ngày qua</option>
                <option value="quarter">3 tháng qua</option>
                <option value="year">12 tháng qua</option>
              </select>
            </div>
            <div class="col-md-8">
              <label class="form-label fw-bold">Tùy chỉnh</label>
              <div class="row">
                <div class="col-md-5">
                  <input type="date" v-model="customStartDate" class="form-control" />
                </div>
                <div class="col-md-5">
                  <input type="date" v-model="customEndDate" class="form-control" />
                </div>
                <div class="col-md-2">
                  <button class="btn btn-dark w-100" @click="loadRevenueData">Lọc</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Tổng doanh thu theo bộ lọc -->
          <div class="row mb-4">
            <div class="col-md-4 mb-3">
              <div class="card text-center bg-success text-white">
                <div class="card-body">
                  <h6 class="mb-1">Tổng doanh thu</h6>
                  <h3 class="fw-bold mb-0">{{ formatPrice(revenueStats.totalRevenue) }}</h3>
                </div>
              </div>
            </div>
            <div class="col-md-4 mb-3">
              <div class="card text-center bg-info text-white">
                <div class="card-body">
                  <h6 class="mb-1">Số đơn hàng</h6>
                  <h3 class="fw-bold mb-0">{{ revenueStats.totalOrders }}</h3>
                </div>
              </div>
            </div>
            <div class="col-md-4 mb-3">
              <div class="card text-center bg-warning text-dark">
                <div class="card-body">
                  <h6 class="mb-1">Giá trị trung bình/đơn</h6>
                  <h3 class="fw-bold mb-0">{{ formatPrice(revenueStats.averageOrderValue) }}</h3>
                </div>
              </div>
            </div>
          </div>

          <!-- Biểu đồ doanh thu -->
          <div class="mb-4">
            <h6 class="fw-bold mb-3">📈 Biểu đồ doanh thu theo ngày</h6>
            <canvas ref="revenueChart" id="revenueChart" style="max-height: 400px; width: 100%;"></canvas>
          </div>

          <!-- Bảng chi tiết doanh thu theo ngày -->
          <div>
            <h6 class="fw-bold mb-3">📋 Chi tiết doanh thu theo ngày</h6>
            <div class="table-responsive">
              <table class="table table-bordered">
                <thead class="table-dark">
                  <tr>
                    <th>Ngày</th>
                    <th>Số đơn hàng</th>
                    <th>Doanh thu</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in revenueStats.dailyData" :key="item.date">
                    <td>{{ formatDateShort(item.date) }}</td>
                    <td>{{ item.count }}</td>
                    <td class="text-danger fw-bold">{{ formatPrice(item.revenue) }}</td>
                  </tr>
                  <tr v-if="revenueStats.dailyData.length === 0">
                    <td colspan="3" class="text-center py-4">
                      📊 Không có dữ liệu doanh thu
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export default {
  name: "AdminDashboard",

  data() {
    return {
      activeTab: 'overview',
      loading: false,
      revenueLoading: false,
      stats: {
        products: 0,
        orders: 0,
        customers: 0,
        revenue: 0
      },
      recentOrders: [],
      // Doanh thu
      timeRange: 'month',
      customStartDate: '',
      customEndDate: '',
      revenueStats: {
        totalRevenue: 0,
        totalOrders: 0,
        averageOrderValue: 0,
        dailyData: []
      },
      revenueChart: null
    };
  },

  mounted() {
    this.loadDashboard();
  },

  methods: {
    async loadDashboard() {
      this.loading = true;
      try {
        const [productsRes, ordersRes] = await Promise.all([
          axios.get('http://localhost:8080/api/products/active'),
          axios.get('http://localhost:8080/api/admin/orders')
        ]);

        const products = productsRes.data || [];
        const orders = ordersRes.data || [];

        this.stats.products = products.length;
        this.stats.orders = orders.length;
        
        const totalRevenue = orders
          .filter(order => order.status === 'COMPLETED')
          .reduce((sum, order) => sum + (order.total || 0), 0);
        this.stats.revenue = totalRevenue;

        this.recentOrders = [...orders]
          .sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))
          .slice(0, 5);

        try {
          const customersRes = await axios.get('http://localhost:8080/api/customers');
          this.stats.customers = customersRes.data.length;
        } catch (err) {
          this.stats.customers = 0;
        }

      } catch (error) {
        console.error('Lỗi tải dashboard:', error);
        this.stats = { products: 0, orders: 0, customers: 0, revenue: 0 };
        this.recentOrders = [];
      } finally {
        this.loading = false;
      }
    },

    async loadRevenueData() {
      this.revenueLoading = true;
      try {
        let startDate, endDate;
        
        // Tính ngày bắt đầu dựa trên timeRange
        const today = new Date();
        endDate = this.formatDateForAPI(today);
        
        switch(this.timeRange) {
          case 'today':
            startDate = endDate;
            break;
          case 'week':
            const weekAgo = new Date(today);
            weekAgo.setDate(today.getDate() - 7);
            startDate = this.formatDateForAPI(weekAgo);
            break;
          case 'month':
            const monthAgo = new Date(today);
            monthAgo.setMonth(today.getMonth() - 1);
            startDate = this.formatDateForAPI(monthAgo);
            break;
          case 'quarter':
            const quarterAgo = new Date(today);
            quarterAgo.setMonth(today.getMonth() - 3);
            startDate = this.formatDateForAPI(quarterAgo);
            break;
          case 'year':
            const yearAgo = new Date(today);
            yearAgo.setFullYear(today.getFullYear() - 1);
            startDate = this.formatDateForAPI(yearAgo);
            break;
          default:
            startDate = this.formatDateForAPI(new Date(today.setMonth(today.getMonth() - 1)));
        }
        
        // Nếu có custom date thì ghi đè
        if (this.customStartDate && this.customEndDate) {
          startDate = this.customStartDate;
          endDate = this.customEndDate;
        }
        
        // Gọi API lấy đơn hàng
        const response = await axios.get('http://localhost:8080/api/admin/orders');
        const orders = response.data || [];
        
        // Lọc đơn hàng theo ngày và chỉ lấy đơn hoàn thành
        const start = new Date(startDate);
        const end = new Date(endDate);
        end.setHours(23, 59, 59);
        
        const filteredOrders = orders.filter(order => {
          const orderDate = new Date(order.orderDate);
          return order.status === 'COMPLETED' && orderDate >= start && orderDate <= end;
        });
        
        // Tính tổng doanh thu
        const totalRevenue = filteredOrders.reduce((sum, order) => sum + (order.total || 0), 0);
        const totalOrders = filteredOrders.length;
        const averageOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;
        
        // Nhóm dữ liệu theo ngày
        const dailyMap = new Map();
        
        filteredOrders.forEach(order => {
          const dateKey = this.formatDateShort(order.orderDate);
          if (!dailyMap.has(dateKey)) {
            dailyMap.set(dateKey, { revenue: 0, count: 0 });
          }
          const dayData = dailyMap.get(dateKey);
          dayData.revenue += order.total || 0;
          dayData.count += 1;
        });
        
        // Chuyển đổi thành mảng và sắp xếp theo ngày
        const dailyData = Array.from(dailyMap.entries())
          .map(([date, data]) => ({ date, revenue: data.revenue, count: data.count }))
          .sort((a, b) => new Date(a.date) - new Date(b.date));
        
        this.revenueStats = {
          totalRevenue,
          totalOrders,
          averageOrderValue,
          dailyData
        };
        
        // Vẽ biểu đồ
        this.renderChart();
        
      } catch (error) {
        console.error('Lỗi tải doanh thu:', error);
        alert('Không thể tải dữ liệu doanh thu!');
      } finally {
        this.revenueLoading = false;
      }
    },
    
    renderChart() {
      const canvas = this.$refs.revenueChart;
      if (!canvas) return;
      
      const ctx = canvas.getContext('2d');
      
      // Destroy chart cũ nếu có
      if (this.revenueChart) {
        this.revenueChart.destroy();
      }
      
      const labels = this.revenueStats.dailyData.map(item => item.date);
      const revenues = this.revenueStats.dailyData.map(item => item.revenue);
      
      this.revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: 'Doanh thu (₫)',
            data: revenues,
            borderColor: '#198754',
            backgroundColor: 'rgba(25, 135, 84, 0.1)',
            borderWidth: 2,
            fill: true,
            tension: 0.4,
            pointBackgroundColor: '#198754',
            pointBorderColor: '#fff',
            pointBorderWidth: 2,
            pointRadius: 4,
            pointHoverRadius: 6
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: true,
          plugins: {
            legend: {
              position: 'top',
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  let label = context.dataset.label || '';
                  if (label) {
                    label += ': ';
                  }
                  label += new Intl.NumberFormat('vi-VN').format(context.raw) + ' ₫';
                  return label;
                }
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                callback: function(value) {
                  return new Intl.NumberFormat('vi-VN').format(value) + ' ₫';
                }
              }
            }
          }
        }
      });
    },
    
    formatDateForAPI(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    
    formatDateShort(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    },

    getStatusText(status) {
      const map = {
        'PENDING': 'Chờ xử lý',
        'CONFIRMED': 'Đã xác nhận',
        'SHIPPING': 'Đang giao',
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
      return map[status] || 'bg-secondary';
    },

    formatPrice(price) {
      if (!price) return '0 ₫';
      return price.toLocaleString('vi-VN') + ' ₫';
    },

    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`;
    }
  }
};
</script>

<style scoped>
.card {
  border-radius: 8px;
  transition: transform 0.2s;
  cursor: pointer;
}

.card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.stat-icon {
  font-size: 2rem;
}

.table th, .table td {
  vertical-align: middle;
}

.badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.card-header {
  border-bottom: none;
}

.nav-tabs .nav-link {
  color: #000;
  font-weight: 500;
}

.nav-tabs .nav-link.active {
  background-color: #000;
  color: white;
  border-color: #000;
}

.nav-tabs .nav-link:hover:not(.active) {
  background-color: #f8f9fa;
  border-color: #dee2e6 #dee2e6 #000;
}
</style>