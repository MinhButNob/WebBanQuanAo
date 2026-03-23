<template>
  <div class="employee-page">
    <h2 class="mb-4">Quản lý nhân viên</h2>

    <!-- FORM -->
    <div class="card mb-4">
      <div class="card-header fw-bold">
        {{ updateIndex === -1 ? "Thêm nhân viên" : "Cập nhật nhân viên" }}
      </div>

      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Tên nhân viên</label>
            <input v-model="form.name" type="text" class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Email</label>
            <input v-model="form.email" type="email" class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Số điện thoại</label>
            <input v-model="form.phone" type="text" class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Chức vụ</label>
            <select v-model="form.role" class="form-select">
              <option disabled value="">-- Chọn chức vụ --</option>
              <option>Admin</option>
              <option>Nhân viên bán hàng</option>
              <option>Quản lý kho</option>
            </select>
          </div>

          <div class="col-md-6">
            <label class="form-label">Mật khẩu</label>
            <input
              v-model="form.password"
              type="password"
              class="form-control"
            />
          </div>

          <div class="col-md-6">
            <label class="form-label">Trạng thái</label>
            <select v-model="form.status" class="form-select">
              <option>Đang làm việc</option>
              <option>Nghỉ việc</option>
            </select>
          </div>
        </div>

        <div class="mt-4 text-end">
          <button @click="resetForm" class="btn btn-secondary me-2">
            Làm mới
          </button>

          <button @click="addEmployee" class="btn btn-primary me-2">
            Lưu thông tin
          </button>

          <button @click="updateEmployee" class="btn btn-warning">
            Cập nhật
          </button>
        </div>
      </div>
    </div>

    <!-- TABLE -->
    <div class="card">
      <div class="card-header fw-bold">Danh sách nhân viên</div>

      <div class="card-body p-0">
        <table class="table table-striped mb-0">
          <thead class="table-dark">
            <tr>
              <th>#</th>
              <th>Tên</th>
              <th>Email</th>
              <th>Chức vụ</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="(emp, index) in employees" :key="emp.id">
              <td>{{ index + 1 }}</td>
              <td>{{ emp.name }}</td>
              <td>{{ emp.email }}</td>
              <td>{{ emp.role }}</td>
              <td>{{ emp.status }}</td>
              <td>
                <button
                  class="btn btn-sm btn-warning me-2"
                  @click="editEmployee(index)"
                >
                  Sửa
                </button>

                <button
                  class="btn btn-sm btn-danger"
                  @click="deleteEmployee(index)"
                >
                  Xoá
                </button>
              </td>
            </tr>

            <tr v-if="employees.length === 0">
              <td colspan="6" class="text-center">Chưa có nhân viên</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from "vue";

export default {
  setup() {
    const employees = ref([
      {
        id: 1,
        name: "Nguyễn Văn A",
        email: "a@shop.com",
        phone: "0901234567",
        role: "Admin",
        password: "123",
        status: "Đang làm việc",
      },
    ]);

    const selectedIndex = ref(-1);

    const form = reactive({
      name: "",
      email: "",
      phone: "",
      role: "",
      password: "",
      status: "Đang làm việc",
    });

    function addEmployee() {
      employees.value.push({
        id: Date.now(),
        ...form,
      });
      resetForm();
    }

    function editEmployee(index) {
      Object.assign(form, employees.value[index]);
      selectedIndex.value = index;
    }

    function updateEmployee() {
      if (selectedIndex.value !== -1) {
        employees.value[selectedIndex.value] = {
          ...employees.value[selectedIndex.value],
          ...form,
        };
        resetForm();
      }
    }

    function deleteEmployee(index) {
      employees.value.splice(index, 1);
    }

    function resetForm() {
      Object.assign(form, {
        name: "",
        email: "",
        phone: "",
        role: "",
        password: "",
        status: "Đang làm việc",
      });
      selectedIndex.value = -1;
    }

    return {
      employees,
      form,
      addEmployee,
      editEmployee,
      updateEmployee,
      deleteEmployee,
      resetForm,
    };
  },
};
</script>

<style scoped>
.employee-page {
  max-width: 1100px;
}
</style>
