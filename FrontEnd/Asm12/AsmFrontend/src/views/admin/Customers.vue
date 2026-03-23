<template>
  <div class="container-fluid p-4">
    <h2 class="mb-4">Quản lý khách hàng</h2>

    <!-- FORM -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="mb-3">Thêm / Cập nhật khách hàng</h5>

        <div class="row g-3">
          <div class="col-md-3">
            <input v-model="form.name" type="text" class="form-control" placeholder="Tên khách hàng" />
          </div>

          <div class="col-md-3">
            <input v-model="form.email" type="email" class="form-control" placeholder="Email" />
          </div>

          <div class="col-md-3">
            <input v-model="form.phone" type="text" class="form-control" placeholder="Số điện thoại" />
          </div>

          <div class="col-md-3">
            <input v-model="form.address" type="text" class="form-control" placeholder="Địa chỉ" />
          </div>

          <div class="col-md-12">
            <button @click="addCustomer" class="btn btn-dark me-2">
              Thêm khách hàng
            </button>

            <button @click="updateCustomer" class="btn btn-warning me-2">
              Cập nhật
            </button>

            <button @click="resetForm" class="btn btn-secondary">
              Làm mới
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- DANH SÁCH -->
    <div class="card">
      <div class="card-body">
        <h5 class="mb-3">Danh sách khách hàng</h5>

        <table class="table table-bordered table-hover align-middle">
          <thead class="table-dark">
            <tr>
              <th>#</th>
              <th>Tên</th>
              <th>Email</th>
              <th>SĐT</th>
              <th>Địa chỉ</th>
              <th width="250">Hành động</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="(c, index) in customers" :key="c.id">
              <td>{{ index + 1 }}</td>
              <td>{{ c.name }}</td>
              <td>{{ c.email }}</td>
              <td>{{ c.phone }}</td>
              <td>{{ c.address }}</td>
              <td>
                

                <button class="btn btn-sm btn-warning me-1" @click="editCustomer(index)">
                  Sửa
                </button>

                <button class="btn btn-sm btn-danger" @click="removeCustomer(c.id)">
                  Xóa
                </button>
              </td>
            </tr>

            <tr v-if="customers.length === 0">
              <td colspan="6" class="text-center text-muted">
                Chưa có khách hàng
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
  name: "AdminCustomers",

  data() {
    return {
      form: {
        name: "",
        email: "",
        phone: "",
        address: ""
      },

      customers: [
        {
          id: 1,
          name: "Nguyễn Văn A",
          email: "a@gmail.com",
          phone: "0901234567",
          address: "Hà Nội"
        },
        {
          id: 2,
          name: "Trần Thị B",
          email: "b@gmail.com",
          phone: "0912345678",
          address: "TP.HCM"
        }
      ],

      editingIndex: -1,
      selectedCustomer: null
    };
  },

  methods: {
    addCustomer() {
      if (!this.form.name || !this.form.phone) {
        alert("Vui lòng nhập tên và số điện thoại");
        return;
      }

      this.customers.push({
        id: Date.now(),
        ...this.form
      });

      this.resetForm();
    },

    editCustomer(index) {
      this.form = { ...this.customers[index] };
      this.editingIndex = index;
    },

    updateCustomer() {
      if (this.editingIndex === -1) {
        alert("Vui lòng chọn khách hàng cần sửa");
        return;
      }

      this.customers[this.editingIndex] = {
        ...this.customers[this.editingIndex],
        ...this.form
      };

      this.resetForm();
    },

    viewDetail(customer) {
      this.selectedCustomer = customer;
    },

    removeCustomer(id) {
      if (confirm("Bạn có chắc muốn xóa khách hàng này?")) {
        this.customers = this.customers.filter(c => c.id !== id);
      }
    },

    resetForm() {
      this.form = {
        name: "",
        email: "",
        phone: "",
        address: ""
      };
      this.editingIndex = -1;
    }
  }
};
</script>