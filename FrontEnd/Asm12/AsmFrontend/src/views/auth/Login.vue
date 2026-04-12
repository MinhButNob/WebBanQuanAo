<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-5">
        <div class="card shadow">
          <div class="card-header text-center fw-bold fs-4">
            Đăng nhập
          </div>

          <div class="card-body">
            <form @submit.prevent="login">
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

              <button class="btn btn-dark w-100">
                Đăng nhập
              </button>
            </form>

            <p class="text-center mt-3 mb-0">
              Chưa có tài khoản?
              <router-link to="/register">Đăng ký</router-link>
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
import { loginAPI } from "@/api/auth";

const router = useRouter();

const email = ref("");
const password = ref("");
const loading = ref(false);

const login = async () => {
  loading.value = true;
  try {
    const res = await loginAPI({
      email: email.value,
      matKhau: password.value,
    });

    const user = res.data;

    localStorage.setItem("user", JSON.stringify(user));

    if (user.vaiTro === "ADMIN") {
      router.push("/admin");
    } else {
      router.push("/");
    }
  } catch (err) {
    alert(err.response?.data || "Sai tài khoản hoặc mật khẩu");
  } finally {
    loading.value = false;
  }
};
</script>