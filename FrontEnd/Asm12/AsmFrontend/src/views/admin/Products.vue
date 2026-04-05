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

        <!-- ✅ SỬA: Size từ API -->
        <div class="col-md-6">
          <label>Size</label>
          <div class="d-flex gap-2 flex-wrap">
            <label v-for="s in sizeOptions" :key="s.id" class="border px-2">
              <input 
                type="checkbox" 
                :value="s.id" 
                v-model="form.sizeIds" 
              />
              {{ s.sizeName }}
            </label>
          </div>
        </div>

        <!-- ✅ SỬA: Màu từ API -->
        <div class="col-md-6">
          <label>Màu</label>
          <div class="d-flex gap-2 flex-wrap">
            <label v-for="c in colorOptions" :key="c.id" class="border px-2">
              <input 
                type="checkbox" 
                :value="c.id" 
                v-model="form.colorIds" 
              />
              {{ c.colorName }}
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
          <div v-if="form.image" class="mt-2">
            <img :src="form.image" width="80" class="border" />
          </div>
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
            <!-- ✅ SỬA: Hiển thị sizeName và colorName -->
            <td>{{ p.sizeName || 'Chưa chọn' }}</td>
            <td>{{ p.colorName || 'Chưa chọn' }}</td>
            <td>{{ p.categoryName }}</td>
            <td><img :src="p.image" width="50" height="50" style="object-fit: cover" /></td>
            <td>
              <span
                :class="p.status === 'ACTIVE' ? 'text-success' : 'text-danger'"
              >
                {{ p.status === "ACTIVE" ? "Hoạt động" : "Ngừng" }}
              </span>
            </td>
            <td>
              <button class="btn btn-warning btn-sm me-1" @click="editProduct(p)">
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
import axios from "axios";

const API_BASE = "http://localhost:8080/api";

export default {
  data() {
    return {
      tab: "form",
      keyword: "",
      selectedFile: null,

      // ✅ Lấy từ API
      sizeOptions: [],
      colorOptions: [],

      form: {
        id: null,
        name: "",
        price: "",
        categoryId: "",
        sizeIds: [],      // ✅ Mảng chứa ID của size
        colorIds: [],     // ✅ Mảng chứa ID của color
        stock: 0,
        status: "ACTIVE",
        description: "",
        image: "",
      },

      products: [],
      categories: [],
    };
  },

  computed: {
    filteredProducts() {
      return this.products.filter((p) =>
        (p.name || "").toLowerCase().includes(this.keyword.toLowerCase())
      );
    },
  },

  mounted() {
    this.loadProducts();
    this.loadCategories();
    this.loadSizes();    // ✅ Load size từ API
    this.loadColors();   // ✅ Load color từ API
  },

  methods: {
    validateForm() {
      if (!this.form.name.trim()) return alert("Tên không được trống"), false;
      if (!this.form.price || this.form.price <= 0)
        return alert("Giá phải > 0"), false;
      if (!this.form.categoryId) return alert("Chọn loại"), false;
      if (!this.form.sizeIds.length) return alert("Chọn size"), false;
      if (!this.form.colorIds.length) return alert("Chọn màu"), false;
      if (this.form.stock < 0) return alert("Tồn kho lỗi"), false;
      if (!this.form.id && !this.selectedFile)
        return alert("Chọn ảnh"), false;
      return true;
    },

    async loadProducts() {
      try {
        const res = await axios.get(`${API_BASE}/products`);
        this.products = res.data;
      } catch (e) {
        console.error("Lỗi load products:", e);
      }
    },

    async loadCategories() {
      try {
        const res = await axios.get(`${API_BASE}/categories`);
        this.categories = res.data;
      } catch (e) {
        console.error("Lỗi load categories:", e);
      }
    },

    // ✅ Load danh sách size
    async loadSizes() {
      try {
        const res = await axios.get(`${API_BASE}/sizes`);
        this.sizeOptions = res.data.filter(s => s.status === "ACTIVE");
      } catch (e) {
        console.error("Lỗi load sizes:", e);
      }
    },

    // ✅ Load danh sách màu
    async loadColors() {
      try {
        const res = await axios.get(`${API_BASE}/colors`);
        this.colorOptions = res.data.filter(c => c.status === "ACTIVE");
      } catch (e) {
        console.error("Lỗi load colors:", e);
      }
    },

    async addProduct() {
      if (!this.validateForm()) return;

      const formData = new FormData();

      formData.append("name", this.form.name);
      formData.append("price", Number(this.form.price));
      formData.append("categoryId", this.form.categoryId);
      formData.append("sizeId", this.form.sizeIds[0]);      // ⚠️ Tạm thời lấy cái đầu tiên
      formData.append("colorId", this.form.colorIds[0]);    // ⚠️ Tạm thời lấy cái đầu tiên
      formData.append("stock", this.form.stock);
      formData.append("status", this.form.status);
      formData.append("description", this.form.description);

      if (this.selectedFile) {
        formData.append("file", this.selectedFile);
      }

      try {
        await axios.post(`${API_BASE}/products/upload`, formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        this.afterSubmit();
      } catch (e) {
        console.error("Lỗi thêm:", e);
        alert("Thêm sản phẩm thất bại");
      }
    },

    async updateProduct() {
      if (!this.validateForm()) return;

      const data = {
        id: this.form.id,
        name: this.form.name,
        price: Number(this.form.price),
        stock: Number(this.form.stock),
        categoryId: Number(this.form.categoryId),
        sizeId: this.form.sizeIds[0],      // ⚠️ Tạm thời lấy cái đầu tiên
        colorId: this.form.colorIds[0],    // ⚠️ Tạm thời lấy cái đầu tiên
        status: this.form.status,
        description: this.form.description,
        image: this.form.image,
      };

      try {
        await axios.put(`${API_BASE}/products/${this.form.id}`, data);
        this.afterSubmit();
      } catch (e) {
        console.error("Lỗi cập nhật:", e);
        alert("Cập nhật thất bại");
      }
    },

    async deleteProduct(id) {
      if (confirm("Xóa sản phẩm?")) {
        try {
          await axios.delete(`${API_BASE}/products/${id}`);
          this.loadProducts();
        } catch (e) {
          console.error("Lỗi xóa:", e);
          alert("Xóa thất bại");
        }
      }
    },

    editProduct(p) {
      this.form = {
        id: p.id,
        name: p.name,
        price: p.price,
        categoryId: p.categoryId,
        sizeIds: p.sizeId ? [p.sizeId] : [],      // ✅ Chuyển thành mảng
        colorIds: p.colorId ? [p.colorId] : [],   // ✅ Chuyển thành mảng
        stock: p.stock,
        status: p.status,
        description: p.description,
        image: p.image,
      };
      this.selectedFile = null;
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
        sizeIds: [],
        colorIds: [],
        stock: 0,
        status: "ACTIVE",
        description: "",
        image: "",
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