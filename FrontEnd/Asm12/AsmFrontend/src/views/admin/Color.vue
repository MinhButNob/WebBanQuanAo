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
            v-model="color.colorName"
            class="form-control"
            :class="{ 'is-invalid': errors.colorName }"
            placeholder="Tên loại"
          />
          <div class="invalid-feedback">
            {{ errors.colorName }}
          </div>
        </div>

        <!-- STATUS -->
        <div class="col-md-4">
          <label class="form-label text-secondary">Trạng thái</label>
          <select v-model="color.status" class="form-select">
            <option value="ACTIVE">Hoạt động</option>
            <option value="INACTIVE">Ngừng bán</option>
          </select>
        </div>

        <!-- BUTTON -->
        <div class="col-md-4 mt-3">
          <button
            class="btn btn-primary me-2"
            @click="addColor"
            :disabled="color.id !== null"
          >
            Thêm
          </button>

          <button
            class="btn btn-warning me-2"
            @click="updateColor"
            :disabled="color.id === null"
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
        <tr v-for="c in colors" :key="c.id">
          <td>{{ c.id }}</td>
          <td>{{ c.colorName }}</td>
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
            <button class="btn btn-danger btn-sm" @click="deleteColor(c.id)">
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
      color: {
        id: null,
        colorName: "",
        status: "ACTIVE",
      },
      errors: {},
      colors: [],
    };
  },

  mounted() {
    this.loadData();
  },

  methods: {
    async loadData() {
      try {
        const res = await axios.get("http://localhost:8080/api/colors");
        this.colors = res.data;
      } catch (e) {
        console.errors("load loi", e);
      }
    },

    async addColor(){
      if (!this.validate())return;
      try{
        await axios.post(
        "http://localhost:8080/api/colors",
        this.color
      );
      this.reset();
      this.loadData();
      }
      catch(e){
        console.error("loi ",e);
        alert("them that bai");
      }

      
    },

    edit(c){
      this.color = {...c};
      this.errors = {};
    },

    async updateColor(){
      if (!this.validate())return;
      try{
        await axios.put(
        `http://localhost:8080/api/colors/${this.color.id}`,
        this.color
      );
      this.reset();
      this.loadData();
      }
      catch(e){
        console.error("loi ",e);
        alert("cap nhat that bai");
      }
    },

     async deleteColor(id){
      if (!confirm("Xoa mau nay ?"))return;
      try{
        await axios.delete(
        `http://localhost:8080/api/colors/${id}`
      );
      this.loadData();
      }
      catch(e){
        console.error("loi ",e);
        alert("xoa that bai");
      }
    },

    reset() {
      this.color = {
        id: null,
        colorName: "",
        status: "ACTIVE",
      };
    },

    validate() {
      this.errors = {};

      if (!this.color.colorName || !this.color.colorName.trim()) {
        this.errors.colorName = "Tên loại không được để trống";
      } else if (this.color.colorName.trim().length < 2) {
        this.errors.colorName = "Tên phải >= 2 ký tự";
      }

      return Object.keys(this.errors).length === 0;
    },
  },
};
</script>
