<template>
  <div>
    <h2 class="mb-4">Quản lý đơn hàng</h2>

    <!-- TABLE -->
    <table class="table table-bordered align-middle">
      <thead class="table-dark">
        <tr>
          <th>#</th>
          <th>Mã đơn</th>
          <th>Khách hàng</th>
          <th>Ngày đặt</th>
          <th>Tổng tiền</th>
          <th>Trạng thái</th>
          <th width="220">Hành động</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="(order, index) in orders" :key="order.id">
          <td>{{ index + 1 }}</td>
          <td>#{{ order.id }}</td>
          <td>{{ order.customer }}</td>
          <td>{{ order.date }}</td>
          <td>{{ order.total.toLocaleString() }} đ</td>
          <td>
            <select
              v-model="order.status"
              class="form-select form-select-sm"
            >
              <option>Chờ xử lý</option>
              <option>Đã xác nhận</option>
              <option>Đang giao</option>
              <option>Hoàn thành</option>
              <option>Hủy</option>
            </select>
          </td>
          <td>
            <RouterLink
              class="btn btn-sm btn-outline-dark me-2"
              :to="`/admin/orders/${order.id}`"
            >
              Chi tiết
            </RouterLink>

            <button
              class="btn btn-sm btn-outline-danger"
              @click="deleteOrder(order.id)"
            >
              Xóa
            </button>
          </td>
        </tr>

        <tr v-if="orders.length === 0">
          <td colspan="7" class="text-center">
            Chưa có đơn hàng
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'AdminOrders',

  data() {
    return {
      orders: [
        {
          id: 1001,
          customer: 'Nguyễn Văn A',
          date: '2026-01-10',
          total: 598000,
          status: 'Chờ xử lý'
        },
        {
          id: 1002,
          customer: 'Trần Thị B',
          date: '2026-01-11',
          total: 399000,
          status: 'Đã xác nhận'
        }
      ]
    }
  },

  methods: {
    deleteOrder(id) {
      if (confirm('Bạn có chắc muốn xóa đơn hàng này?')) {
        this.orders = this.orders.filter(
          order => order.id !== id
        )
      }
    }
  }
}
</script>
