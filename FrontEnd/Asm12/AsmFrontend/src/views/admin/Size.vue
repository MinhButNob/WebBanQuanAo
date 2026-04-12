<template>
  <div>
    <h3 class="mb-3">Quản lý loại size</h3>

    <!-- FORM -->
    <div class="card p-3 mb-4">
      <div class="row">
        <!-- NAME -->
        <div class="col-md-4">
          <label class="form-label text-secondary">Tên loại</label>
          <input
            v-model="size.sizeName"
            class="form-control"
            :class="{ 'is-invalid': errors.sizeName }"
            placeholder="Tên loại"
          />
          <div class="invalid-feedback">
            {{ errors.sizeName }}
          </div>
        </div>

        <!-- STATUS -->
        <div class="col-md-4">
          <label class="form-label text-secondary">Trạng thái</label>
          <select v-model="size.status" class="form-select">
            <option value="ACTIVE">Hoạt động</option>
            <option value="INACTIVE">Ngừng bán</option>
          </select>
        </div>

        <!-- BUTTON -->
        <div class="col-md-4 mt-3">
          <button
            class="btn btn-primary me-2"
            @click="add"
            :disabled="size.id !== null"
          >
            Thêm
          </button>

          <button
            class="btn btn-warning me-2"
            @click="update"
            :disabled="size.id === null"
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
      <th>Trạng thái</th>
      <th>Hành động</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="s in sizes" :key="s.id">
      <td>{{ s.id }}</td>         
      <td>{{ s.sizeName }}</td>     
      <td>
        <span class="badge" :class="s.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'">
          {{ s.status }}
        </span>
      </td>
      <td>
        <button class="btn btn-warning btn-sm me-2" @click="edit(s)">Sửa</button>
        <button class="btn btn-danger btn-sm" @click="deleteSize(s.id)">Xóa</button>
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
      size: {
        id: null,
        sizeName: "",
        status: "ACTIVE",
      },
      errors: {},
      sizes: [],
    };
  },

  mounted() {
    this.loadData();
  },

  methods: {
    async loadData() {
      try {
        const res = await axios.get("http://localhost:8080/api/sizes");
        this.sizes = res.data;
      } catch (e) {
        console.errors("load loi", e);
      }
    },

    async add() {
      if (!this.validate()) return;
      try {
        await axios.post("http://localhost:8080/api/sizes", this.size);
        this.reset();
        this.loadData();
      } catch (e) {
        console.error("loi ", e);
        alert("them that bai");
      }
    },

    edit(c) {
      this.size = { ...c };
      this.errors = {};
    },

    async update() {
      if (!this.validate()) return;
      try {
        await axios.put(
          `http://localhost:8080/api/sizes/${this.size.id}`,
          this.size,
        );
        this.reset();
        this.loadData();
      } catch (e) {
        console.error("loi ", e);
        alert("cap nhat that bai");
      }
    },

    async deleteSize(id) {
      if (!confirm("Xoa size nay ?")) return;
      try {
        await axios.delete(`http://localhost:8080/api/sizes/${id}`);
        this.loadData();
      } catch (e) {
        console.error("loi ", e);
        alert("xoa that bai");
      }
    },

    reset() {
      this.size = {
        id: null,
        sizeName: "",
        status: "ACTIVE",
      };
    },

    validate() {
      this.errors = {};

      if (!this.size.sizeName || !this.size.sizeName.trim()) {
        this.errors.sizeName = "Tên loại không được để trống";
      } else if (this.size.sizeName.trim().length < 1) {
        this.errors.sizeName = "Tên phải >= 2 ký tự";
      }

      return Object.keys(this.errors).length === 0;
    },
  },
};
</script>
