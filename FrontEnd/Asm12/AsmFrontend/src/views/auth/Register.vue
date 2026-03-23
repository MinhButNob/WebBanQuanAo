<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card shadow">
          <div class="card-header text-center fw-bold fs-4">
            Tạo tài khoản
          </div>

          <div class="card-body">
            <form @submit.prevent="register">
              <div class="mb-3">
                <label class="form-label">Họ tên</label>
                <input
                  type="text"
                  class="form-control"
                  v-model="name"
                  required
                />
              </div>

              <div class="mb-3">
                <label class="form-label">Email</label>
                <input
                  type="email"
                  class="form-control"
                  v-model="email"
                  required
                />
              </div>

              <div class="mb-3">
                <label class="form-label">Mật khẩu</label>
                <input
                  type="password"
                  class="form-control"
                  v-model="password"
                  required
                />
              </div>

              <button class="btn btn-success w-100">
                Đăng ký
              </button>
            </form>

            <p class="text-center mt-3 mb-0">
              Đã có tài khoản?
              <router-link to="/login">Đăng nhập</router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { registerAPI } from "@/api/auth";

const router = useRouter();

const name = ref("");
const email = ref("");
const password = ref("");

const register = async () => {
  try {
    await registerAPI({
      ten: name.value,
      email: email.value,
      matKhau: password.value,
    });

    alert("Đăng ký thành công");
    router.push("/login");
  } catch (err) {
    alert(err.response?.data || "Email đã tồn tại");
  }
};
</script>