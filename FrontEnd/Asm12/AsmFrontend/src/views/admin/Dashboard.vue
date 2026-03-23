<template>
  <div>
    <h2 class="mb-4">Dashboard</h2>

    <!-- Thống kê -->
    <div class="row mb-4">
      <div class="col-md-3" v-for="item in stats" :key="item.title">
        <div class="card text-center">
          <div class="card-body">
            <h6 class="text-muted">{{ item.title }}</h6>
            <h3 class="fw-bold">{{ item.value }}</h3>
          </div>
        </div>
      </div>
    </div>

    <!-- Đơn hàng gần đây -->
    <div class="card">
      <div class="card-header fw-bold">Đơn hàng gần đây</div>
      <div class="card-body p-0">
        <table class="table table-striped mb-0">
          <thead class="table-light">
            <tr>
              <th>#</th>
              <th>Khách hàng</th>
              <th>Ngày</th>
              <th>Tổng tiền</th>
              <th>Trạng thái</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in orders" :key="order.id">
              <td>{{ order.id }}</td>
              <td>{{ order.customer }}</td>
              <td>{{ order.date }}</td>
              <td>{{ order.total }} đ</td>
              <td>
                <span
                  class="badge"
                  :class="
                    order.status === 'Hoàn thành' ? 'bg-success' : 'bg-warning'
                  "
                >
                  {{ order.status }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminDashboard",
  data() {
    return {
      stats: [
        { title: "Sản phẩm", value: 120 },
        { title: "Đơn hàng", value: 45 },
        { title: "Khách hàng", value: 30 },
        { title: "Doanh thu", value: "25.000.000" },
      ],
      orders: [
        {
          id: 1,
          customer: "Nguyễn Văn A",
          date: "10/10/2025",
          total: "1.250.000",
          status: "Hoàn thành",
        },
        {
          id: 2,
          customer: "Trần Thị B",
          date: "11/10/2025",
          total: "850.000",
          status: "Đang xử lý",
        },
        {
          id: 3,
          customer: "Lê Văn C",
          date: "12/10/2025",
          total: "2.100.000",
          status: "Hoàn thành",
        },
      ],
    };
  },
};
</script>

<style scoped>
.card {
  border-radius: 0;
}
</style>
