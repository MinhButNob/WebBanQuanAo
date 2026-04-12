<template>
  <div>
    <h3 class="mb-3">Quản lý loại sản phẩm</h3>

    <!-- FORM -->
    <div class="card p-3 mb-4">
      <div class="row">
        <!-- NAME -->
        <div class="col-md-4">
          <label class="form-label text-secondary">Tên loại</label>
          <input
            v-model="category.name"
            class="form-control"
            :class="{ 'is-invalid': errors.name }"
            placeholder="Tên loại"
          />
          <div class="invalid-feedback">
            {{ errors.name }}
          </div>
        </div>

        <!-- DESCRIPTION -->
        <div class="col-md-4">
          <label class="form-label text-secondary">Mô tả</label>
          <input
            v-model="category.description"
            class="form-control"
            :class="{ 'is-invalid': errors.description }"
            placeholder="Mô tả"
          />
          <div class="invalid-feedback">
            {{ errors.description }}
          </div>
        </div>

        <!-- STATUS -->
        <div class="col-md-4">
          <label class="form-label text-secondary">Trạng thái</label>
          <select v-model="category.status" class="form-select">
            <option value="ACTIVE">Hoạt động</option>
            <option value="INACTIVE">Ngừng bán</option>
          </select>
        </div>

        <!-- BUTTON -->
        <div class="col-md-4 mt-3">
          <button
            class="btn btn-primary me-2"
            @click="addCategory"
            :disabled="category.id !== null"
          >
            Thêm
          </button>

          <button
            class="btn btn-warning me-2"
            @click="updateCategory"
            :disabled="category.id === null"
          >
            Cập nhật
          </button>

          <button class="btn btn-secondary" @click="reset">Làm mới</button>
        </div>
      </div>
    </div>

    <!-- TABLE -->
    <table class="table table-bordered">
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên loại</th>
          <th>Mô tả</th>
          <th>Trạng thái</th>
          <th>Hành động</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="c in categories" :key="c.id">
          <td>{{ c.id }}</td>
          <td>{{ c.name }}</td>
          <td>{{ c.description }}</td>
          <td>
            <span
              class="badge"
              :class="c.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'"
            >
              {{ c.status }}
            </span>
          </td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="edit(c)">
              Sửa
            </button>
            <button class="btn btn-danger btn-sm" @click="remove(c.id)">
              Xóa
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      category: {
        id: null,
        name: "",
        description: "",
        status: "ACTIVE",
      },
      categories: [],
      errors: {},
    };
  },

  mounted() {
    this.loadData();
  },

  methods: {
    // ✅ VALIDATE CHUẨN
    validate() {
      this.errors = {};

      if (!this.category.name || !this.category.name.trim()) {
        this.errors.name = "Tên loại không được để trống";
      } else if (this.category.name.trim().length < 3) {
        this.errors.name = "Tên phải >= 3 ký tự";
      }

      if (!this.category.description || !this.category.description.trim()) {
        this.errors.description = "Mô tả không được để trống";
      }

      return Object.keys(this.errors).length === 0;
    },

    // ✅ ADD
    async addCategory() {
      if (!this.validate()) return;

      try {
        await axios.post(
          "http://localhost:8080/api/categories",
          this.category
        );
        this.reset();
        this.loadData();
      } catch (e) {
        console.error("Add lỗi:", e);
        alert("Lỗi thêm!");
      }
    },

    // ✅ UPDATE
    async updateCategory() {
      if (!this.validate()) return;

      try {
        await axios.put(
          `http://localhost:8080/api/categories/${this.category.id}`,
          this.category
        );
        this.reset();
        this.loadData();
      } catch (e) {
        console.error("Update lỗi:", e);
        alert("Lỗi cập nhật!");
      }
    },

    // ✅ LOAD
    async loadData() {
      try {
        const res = await axios.get(
          "http://localhost:8080/api/categories"
        );
        this.categories = res.data;
      } catch (e) {
        console.error("Load lỗi:", e);
      }
    },

    // ✅ EDIT
    edit(c) {
      this.category = { ...c };
      this.errors = {};
    },

    // ✅ DELETE
    async remove(id) {
      if (!confirm("Xóa loại này?")) return;

      try {
        await axios.delete(
          `http://localhost:8080/api/categories/${id}`
        );
        this.loadData();
      } catch (e) {
        console.error("Delete lỗi:", e);
      }
    },

    // ✅ RESET
    reset() {
      this.category = {
        id: null,
        name: "",
        description: "",
        status: "ACTIVE",
      };
      this.errors = {};
    },
  },
};
</script>