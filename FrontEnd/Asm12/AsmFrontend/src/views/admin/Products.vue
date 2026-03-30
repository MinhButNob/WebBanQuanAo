<template>
  <div>
    <h2 class="mb-4">Quản lý sản phẩm</h2>

    <!-- TAB -->
    <ul class="nav nav-tabs mb-3">
      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: tab === 'form' }"
          @click="tab = 'form'"
        >
          Thêm / Sửa
        </button>
      </li>
      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: tab === 'list' }"
          @click="tab = 'list'"
        >
          Danh sách
        </button>
      </li>
    </ul>

    <!-- FORM -->
    <div v-if="tab === 'form'" class="card p-3">
      <div class="row g-3">
        <div class="col-md-6">
          <label>Tên</label>
          <input v-model="form.name" class="form-control" />
        </div>

        <div class="col-md-6">
          <label>Giá</label>
          <input
            v-model.number="form.price"
            type="number"
            class="form-control"
          />
        </div>

        <div class="col-md-6">
          <label>Loại</label>
          <select v-model="form.categoryId" class="form-select">
            <option disabled value="">-- Chọn loại --</option>
            <option v-for="c in categories" :key="c.id" :value="c.id">
              {{ c.name }}
            </option>
          </select>
        </div>

        <div class="col-md-6">
          <label>Size</label>
          <div class="d-flex gap-2 flex-wrap">
            <label v-for="s in sizes" :key="s" class="border px-2">
              <input type="checkbox" :value="s" v-model="form.size" />
              {{ s }}
            </label>
          </div>
        </div>

        <div class="col-md-6">
          <label>Màu</label>
          <div class="d-flex gap-2 flex-wrap">
            <label v-for="c in colors" :key="c" class="border px-2">
              <input type="checkbox" :value="c" v-model="form.color" />
              {{ c }}
            </label>
          </div>
        </div>

        <div class="col-md-6">
          <label>Tồn kho</label>
          <input
            v-model.number="form.stock"
            type="number"
            class="form-control"
          />
        </div>

        <div class="col-md-6">
          <label>Ảnh</label>
          <input type="file" class="form-control" @change="handleFileUpload" />
        </div>

        <div class="col-md-6">
          <label>Trạng thái</label>
          <select v-model="form.status" class="form-select">
            <option value="ACTIVE">Hoạt động</option>
            <option value="INACTIVE">Ngừng</option>
          </select>
        </div>

        <div class="col-12">
          <label>Mô tả</label>
          <textarea v-model="form.description" class="form-control"></textarea>
        </div>

        <div class="col-12 text-end">
          <button class="btn btn-secondary me-2" @click="resetForm">
            Reset
          </button>

          <button v-if="!form.id" class="btn btn-primary" @click="addProduct">
            Thêm
          </button>

          <button v-if="form.id" class="btn btn-warning" @click="updateProduct">
            Cập nhật
          </button>
        </div>
      </div>
    </div>

    <!-- LIST -->
    <div v-if="tab === 'list'">
      <input
        v-model="keyword"
        class="form-control mb-3"
        placeholder="Tìm theo tên..."
      />

      <table class="table table-bordered">
        <thead>
          <tr>
            <th>#</th>
            <th>Tên</th>
            <th>Giá</th>
            <th>Size</th>
            <th>Màu</th>
            <th>Loại</th>
            <th>Hình</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="(p, i) in filteredProducts" :key="p.id">
            <td>{{ i + 1 }}</td>
            <td>{{ p.name }}</td>
            <td>{{ formatPrice(p.price) }}</td>
            <td>{{ p.size }}</td>
            <td>{{ p.color }}</td>
            <td>{{ p.categoryName }}</td>
            <td><img :src="p.image" width="50" /></td>
            <td>
              <span
                :class="p.status === 'ACTIVE' ? 'text-success' : 'text-danger'"
              >
                {{ p.status === "ACTIVE" ? "Hoạt động" : "Ngừng" }}
              </span>
            </td>
            <td>
              <button class="btn btn-warning btn-sm" @click="editProduct(p)">
                Sửa
              </button>
              <button
                class="btn btn-danger btn-sm"
                @click="deleteProduct(p.id)"
              >
                Xóa
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { getProducts, updateProductAPI, deleteProductAPI } from "@/api/product";
import { getCategories } from "@/api/category";

export default {
  data() {
    return {
      tab: "form",
      keyword: "",
      selectedFile: null,

      sizes: ["S", "M", "L", "XL"],
      colors: ["Đen", "Trắng", "Xanh", "Đỏ"],

      form: {
        id: null,
        name: "",
        price: "",
        categoryId: "",
        size: [],
        color: [],
        stock: 0,
        status: "ACTIVE",
        description: "",
      },

      products: [],
      categories: [],
    };
  },

  computed: {
    filteredProducts() {
      return this.products.filter((p) =>
        (p.name || "").toLowerCase().includes(this.keyword.toLowerCase()),
      );
    },
  },

  mounted() {
    this.loadProducts();
    this.loadCategories();
  },

  methods: {
    validateForm() {
      if (!this.form.name.trim()) return (alert("Tên không được trống"), false);
      if (!this.form.price || this.form.price <= 0)
        return (alert("Giá phải > 0"), false);
      if (!this.form.categoryId) return (alert("Chọn loại"), false);
      if (!this.form.size.length) return (alert("Chọn size"), false);
      if (!this.form.color.length) return (alert("Chọn màu"), false);
      if (this.form.stock < 0) return (alert("Tồn kho lỗi"), false);
      if (!this.form.id && !this.selectedFile)
        return (alert("Chọn ảnh"), false);
      return true;
    },

    async loadProducts() {
      const res = await getProducts();
      this.products = res.data;
    },

    async loadCategories() {
      const res = await getCategories();
      this.categories = res.data;
    },

    async addProduct() {
      if (!this.validateForm()) return;

      const formData = new FormData();

      Object.keys(this.form).forEach((key) => {
        if (key === "size" || key === "color") {
          formData.append(key, this.form[key].join(","));
        } else {
          formData.append(key, this.form[key]);
        }
      });

      formData.append("price", Number(this.form.price));

      if (this.selectedFile) {
        formData.append("file", this.selectedFile);
      }

      await fetch("http://localhost:8080/api/products/upload", {
        method: "POST",
        body: formData,
      });

      this.afterSubmit();
    },

    async updateProduct() {
      if (!this.validateForm()) return;

      const data = {
        ...this.form,
        price: Number(this.form.price),
        stock: Number(this.form.stock),
        categoryId: Number(this.form.categoryId),
        size: this.form.size.join(","),
        color: this.form.color.join(","),
      };

      await updateProductAPI(this.form.id, data);
      this.afterSubmit();
    },

    async deleteProduct(id) {
      if (confirm("Xóa sản phẩm?")) {
        await deleteProductAPI(id);
        this.loadProducts();
      }
    },

    editProduct(p) {
      this.form = {
        ...p,
        size: p.size ? p.size.split(",") : [],
        color: p.color ? p.color.split(",") : [],
      };
      this.tab = "form";
    },

    handleFileUpload(e) {
      this.selectedFile = e.target.files[0];
    },

    resetForm() {
      this.form = {
        id: null,
        name: "",
        price: "",
        categoryId: "",
        size: [],
        color: [],
        stock: 0,
        status: "ACTIVE",
        description: "",
      };
      this.selectedFile = null;
    },

    afterSubmit() {
      this.loadProducts();
      this.resetForm();
      this.tab = "list";
    },

    formatPrice(price) {
      return price ? price.toLocaleString() + " đ" : "0 đ";
    },
  },
};
</script>
