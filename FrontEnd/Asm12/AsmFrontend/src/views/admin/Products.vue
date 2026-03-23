<template>
  <div>
    <h2 class="mb-4">Quản lý sản phẩm</h2>
<div class="card mb-4 shadow-sm">
  <div class="card-header bg-light py-3">
    <h6 class="mb-0 fw-bold">Cập nhật sản phẩm</h6>
  </div>

  <div class="card-body">
    <div class="row g-3">
      
      <div class="col-md-6">
        <label class="form-label text-secondary">Tên sản phẩm</label>
        <input v-model="form.name" type="text" class="form-control" placeholder="Nhập tên sản phẩm" />
      </div>
      <div class="col-md-6">
        <label class="form-label text-secondary">Giá</label>
        <input v-model.number="form.price" type="number" class="form-control" placeholder="Nhập giá" />
      </div>

      <div class="col-md-6">
        <label class="form-label text-secondary">Loại</label>
        <select v-model="form.category" class="form-select">
          <option value="">-- Chọn loại --</option>
          <option>Áo</option>
          <option>Quần</option>
          <option>Phụ kiện</option>
        </select>
      </div>
      <div class="col-md-6">
        <label class="form-label text-secondary">Size</label>
        <select v-model="form.size" class="form-select">
          <option value="">-- Chọn size --</option>
          <option>S</option>
          <option>M</option>
          <option>L</option>
          <option>XL</option>
        </select>
      </div>

      <div class="col-md-6">
        <label class="form-label text-secondary">Màu</label>
        <select v-model="form.color" class="form-select">
          <option value="">-- Chọn màu --</option>
          <option>Đen</option>
          <option>Trắng</option>
          <option>Xanh</option>
          <option>Đỏ</option>
        </select>
      </div>
      <div class="col-md-6">
        <label class="form-label text-secondary">Số lượng tồn kho</label>
        <input type="number" min="0" v-model="form.stock" class="form-control" placeholder="0" />
      </div>

      <div class="col-md-6">
        <label class="form-label text-secondary">Link hình ảnh</label>
        <input v-model="form.image" type="text" class="form-control" placeholder="http://..." />
      </div>
      <div class="col-md-6">
        <label class="form-label text-secondary">Trạng thái</label>
        <select v-model="form.status" class="form-select">
          <option value="ACTIVE">Hoạt động</option>
          <option value="INACTIVE">Ngừng bán</option>
        </select>
      </div>

      <div class="col-12 d-flex justify-content-end gap-2 mt-3">
        <button class="btn btn-secondary px-4" @click="resetForm">Làm mới</button>
        <button class="btn btn-primary px-4" @click="addProduct">Lưu thông tin</button>
        <button class="btn btn-warning px-4 fw-bold" @click="updateProduct">Cập nhật</button>
      </div>

    </div>
  </div>
</div>

    <!-- TABLE -->
    <table class="table table-bordered align-middle">
      <thead class="table-dark">
        <tr>
          <th>#</th>
          <th>Tên</th>
          <th>Giá</th>
          <th>Loại</th>
          <th>Size</th>
          <th>Màu</th>
          <th>Tồn kho</th>
          <th>Trạng thái</th>
          <th>Hình</th>
          <th width="220">Hành động</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="(product, index) in products" :key="product.id">
          <td>{{ index + 1 }}</td>
          <td>{{ product.name }}</td>
          <td>{{ product.price.toLocaleString() }} đ</td>
          <td>{{ product.category }}</td>
          <td>{{ product.size }}</td>
          <td>{{ product.color }}</td>

          <td>
            <span :class="product.stock > 0 ? 'text-success' : 'text-danger'">
              {{ product.stock }}
            </span>
          </td>

          <!-- STATUS -->
          <td>
            <span :class="product.status === 'ACTIVE' ? 'text-success' : 'text-danger'">
              {{ product.status === 'ACTIVE' ? 'Hoạt động' : 'Ngừng bán' }}
            </span>
          </td>

          <td>
            <img :src="product.image" width="50" />
          </td>

          <td>
            <button class="btn btn-sm btn-warning me-1" @click="editProduct(index)">
              Sửa
            </button>

            <button class="btn btn-sm btn-danger" @click="deleteProduct(product.id)">
              Xóa
            </button>
          </td>
        </tr>

        <tr v-if="products.length === 0">
          <td colspan="10" class="text-center">Chưa có sản phẩm</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import {
  getProducts,
  createProduct,
  updateProductAPI,
  deleteProductAPI,
} from "@/api/product";

export default {
  name: "AdminProducts",

  data() {
    return {
      form: {
        id: null,
        name: "",
        price: "",
        category: "",
        size: "",
        color: "",
        stock: 0,
        image: "",
        status: "ACTIVE",
      },

      products: [],
      editingIndex: -1,
    };
  },

  mounted() {
    this.loadProducts();
  },

  methods: {
    async loadProducts() {
      const res = await getProducts();
      this.products = res.data;
    },

    async addProduct() {
      if (!this.form.name || !this.form.price) {
        alert("Vui lòng nhập đầy đủ thông tin");
        return;
      }

      await createProduct(this.form);
      await this.loadProducts();
      this.resetForm();
    },

    editProduct(index) {
      this.form = { ...this.products[index] };
      this.editingIndex = index;
    },

    async updateProduct() {
      if (this.editingIndex === -1) {
        alert("Chọn sản phẩm cần sửa");
        return;
      }

      await updateProductAPI(this.form.id, this.form);
      await this.loadProducts();
      this.resetForm();
    },

    async deleteProduct(id) {
      if (confirm("Bạn có chắc muốn xóa?")) {
        await deleteProductAPI(id);
        await this.loadProducts();
      }
    },

    resetForm() {
      this.form = {
        id: null,
        name: "",
        price: "",
        category: "",
        size: "",
        color: "",
        stock: 0,
        image: "",
        status: "ACTIVE",
      };
      this.editingIndex = -1;
    },
  },
};
</script>