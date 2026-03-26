<template>
  <div>
    <h3 class="mb-3">Quản lý loại sản phẩm</h3>

    <!-- FORM -->
    <div class="card p-3 mb-4">
      <div class="row">
        <div class="col-md-4">
          <label class="form-label text-secondary">Tên loại</label>
          <input
            v-model="category.name"
            class="form-control"
            placeholder="Tên loại"
          />
        </div>

        <div class="col-md-4">
          <label class="form-label text-secondary">Mô tả</label>
          <input
            v-model="category.description"
            class="form-control"
            placeholder="Mô tả"
          />
        </div>

        <div class="col-md-4">
          <label class="form-label text-secondary">Trạng thái</label>
          <select v-model="category.status" class="form-select">
            <option value="ACTIVE">Hoạt động</option>
            <option value="INACTIVE">Ngừng bán</option>
          </select>
        </div>

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
          <td>{{ c.status }}</td>
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
      errors: {} // 👈 thêm
    };
  },

  mounted() {
    this.loadData();
  },

  methods: {
    validate() {
      this.errors = {};

      if (!this.category.name) {
        this.errors.name = "Tên loại không được để trống";
      } else if (this.category.name.length < 3) {
        this.errors.name = "Tên phải >= 3 ký tự";
      }

      if (!this.category.description) {
        this.errors.description = "Mô tả không được để trống";
      }

      return Object.keys(this.errors).length === 0;
    },

    async addCategory() {
      if (!this.validate()) return;

      await axios.post("http://localhost:8080/api/categories", this.category);
      this.reset();
      this.loadData();
    },

    async updateCategory() {
      if (!this.validate()) return;

      await axios.put(
        `http://localhost:8080/api/categories/${this.category.id}`,
        this.category
      );

      this.reset();
      this.loadData();
    },

    async loadData() {
      const res = await axios.get("http://localhost:8080/api/categories");
      this.categories = res.data;
    },

    edit(c) {
      this.category = { ...c };
      this.errors = {};
    },

    async remove(id) {
      if (confirm("Xóa loại này?")) {
        await axios.delete(`http://localhost:8080/api/categories/${id}`);
        this.loadData();
      }
    },

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
