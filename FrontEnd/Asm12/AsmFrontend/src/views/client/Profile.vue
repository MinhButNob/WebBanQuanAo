<template>
  <div class="container py-5">
    <div class="row">
      <!-- Sidebar -->
      <div class="col-md-3 mb-4">
        <div class="card">
          <div class="card-body text-center">
            <div class="avatar-circle mb-3">
              <span class="avatar-text">{{ getInitials() }}</span>
            </div>
            <h5 class="mb-1">{{ user?.ten || user?.fullName || 'Khách hàng' }}</h5>
            <p class="text-muted small">{{ user?.email }}</p>
            <hr />
            <div class="list-group list-group-flush">
              <router-link to="/profile" class="list-group-item list-group-item-action active">
                <i class="bi bi-person me-2"></i> Thông tin tài khoản
              </router-link>
              <router-link to="/orders" class="list-group-item list-group-item-action">
                <i class="bi bi-bag-check me-2"></i> Đơn hàng của tôi
              </router-link>
              <!-- <router-link to="/change-password" class="list-group-item list-group-item-action">
                <i class="bi bi-key me-2"></i> Đổi mật khẩu
              </router-link> -->
            </div>
          </div>
        </div>
      </div>

      <!-- Nội dung chính -->
      <div class="col-md-9">
        <div class="card">
          <div class="card-header bg-dark text-white">
            <h5 class="mb-0">Thông tin tài khoản</h5>
          </div>
          <div class="card-body">
            <div v-if="loading" class="text-center py-5">
              <div class="spinner-border text-dark"></div>
            </div>

            <div v-else>
              <form @submit.prevent="updateProfile">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label class="form-label">Họ và tên</label>
                    <input 
                      type="text" 
                      class="form-control" 
                      v-model="formData.ten"
                      required
                    />
                  </div>

                  <div class="col-md-6 mb-3">
                    <label class="form-label">Email</label>
                    <input 
                      type="email" 
                      class="form-control" 
                      v-model="formData.email"
                      readonly
                      disabled
                    />
                    <small class="text-muted">Email không thể thay đổi</small>
                  </div>

                  <div class="col-md-6 mb-3">
                    <label class="form-label">Số điện thoại</label>
                    <input 
                      type="tel" 
                      class="form-control" 
                      v-model="formData.soDienThoai"
                      pattern="[0-9]{10,11}"
                    />
                  </div>

                  <div class="col-md-6 mb-3">
                    <label class="form-label">Vai trò</label>
                    <input 
                      type="text" 
                      class="form-control" 
                      :value="formData.vaiTro === 'admin' ? 'Quản trị viên' : 'Khách hàng'"
                      readonly
                      disabled
                    />
                  </div>

                  <div class="col-12 mb-3">
                    <label class="form-label">Địa chỉ</label>
                    <textarea 
                      class="form-control" 
                      v-model="formData.diaChi"
                      rows="2"
                      placeholder="Nhập địa chỉ của bạn"
                    ></textarea>
                  </div>
                </div>

                <div class="d-flex justify-content-end gap-2">
                  <button type="button" class="btn btn-secondary" @click="resetForm">
                    Hủy
                  </button>
                  <button type="submit" class="btn btn-dark" :disabled="saving">
                    <span v-if="saving" class="spinner-border spinner-border-sm me-1"></span>
                    {{ saving ? 'Đang lưu...' : 'Cập nhật thông tin' }}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Profile',

  data() {
    return {
      user: null,
      formData: {
        ten: '',
        email: '',
        soDienThoai: '',
        diaChi: '',
        vaiTro: ''
      },
      loading: false,
      saving: false
    };
  },

  computed: {
    isLoggedIn() {
      return localStorage.getItem('user') !== null;
    }
  },

  mounted() {
    if (!this.isLoggedIn) {
      this.$router.push('/login');
      return;
    }
    this.loadUserInfo();
  },

  methods: {
    getInitials() {
      const name = this.user?.ten || this.user?.fullName || 'K';
      return name.charAt(0).toUpperCase();
    },

    async loadUserInfo() {
      this.loading = true;
      try {
        const userStr = localStorage.getItem('user');
        this.user = JSON.parse(userStr);
        
        // Gán dữ liệu vào form
        this.formData.ten = this.user?.ten || '';
        this.formData.email = this.user?.email || '';
        this.formData.soDienThoai = this.user?.soDienThoai || '';
        this.formData.diaChi = this.user?.diaChi || '';
        this.formData.vaiTro = this.user?.vaiTro || 'user';
        
      } catch (error) {
        console.error('Lỗi load user:', error);
        alert('Không thể tải thông tin người dùng!');
      } finally {
        this.loading = false;
      }
    },

    async updateProfile() {
      if (!this.formData.ten.trim()) {
        alert('Vui lòng nhập họ tên!');
        return;
      }

      this.saving = true;
      try {
        const response = await axios.put(
          `http://localhost:8080/api/users/${this.user.maUser}`,
          {
            ten: this.formData.ten,
            soDienThoai: this.formData.soDienThoai,
            diaChi: this.formData.diaChi
          }
        );

        if (response.data.success) {
          // Cập nhật lại localStorage
          const updatedUser = { ...this.user, ...this.formData };
          localStorage.setItem('user', JSON.stringify(updatedUser));
          this.user = updatedUser;
          
          alert('Cập nhật thông tin thành công!');
          
          // Phát sự kiện cập nhật user
          window.dispatchEvent(new Event('user-logged-in'));
        }
      } catch (error) {
        console.error('Lỗi cập nhật:', error);
        alert('Không thể cập nhật thông tin. Vui lòng thử lại!');
      } finally {
        this.saving = false;
      }
    },

    resetForm() {
      this.formData.ten = this.user?.ten || '';
      this.formData.soDienThoai = this.user?.soDienThoai || '';
      this.formData.diaChi = this.user?.diaChi || '';
    }
  }
};
</script>

<style scoped>
.avatar-circle {
  width: 80px;
  height: 80px;
  background-color: #000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.avatar-text {
  font-size: 32px;
  font-weight: bold;
  color: white;
}

.list-group-item.active {
  background-color: #000;
  border-color: #000;
}

.list-group-item-action:hover {
  background-color: #f8f9fa;
}

.card {
  border: none;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.08);
  border-radius: 12px;
}

.form-control:focus {
  border-color: #000;
  box-shadow: 0 0 0 0.2rem rgba(0, 0, 0, 0.1);
}
</style>