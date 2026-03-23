<template>
  <div class="d-flex flex-column min-vh-100">
    <!-- HEADER -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
      <div class="container">
        <router-link class="navbar-brand fw-bold text-dark" to="/">
          DOSHISHA
        </router-link>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#clientMenu"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="clientMenu">
          <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link class="nav-link" to="/">Sản phẩm mới</router-link>
            </li>

            <li class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle"
                href="#"
                data-bs-toggle="dropdown"
              >
                Áo nam
              </a>
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#">Áo thun</a></li>
                <li><a class="dropdown-item" href="#">Áo sơ mi</a></li>
              </ul>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="#">Quần nam</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" href="#">Phụ kiện</a>
            </li>
          </ul>

          <div class="d-flex align-items-center gap-3">
            <i class="bi bi-search"></i>

            <!-- USER -->
            <div v-if="user">
              <span class="me-2">Xin chào, {{ user.username }}</span>
              <button class="btn btn-sm btn-outline-dark" @click="logout">
                Đăng xuất
              </button>
            </div>

            <i class="bi bi-person"></i>

            <router-link to="/cart" class="position-relative text-dark">
              <i class="bi bi-bag"></i>
              <span
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              >
                0
              </span>
            </router-link>
          </div>
        </div>
      </div>
    </nav>

    <!-- CONTENT -->
    <main class="container my-4 flex-grow-1">
      <router-view />
    </main>

    <!-- FOOTER -->
    <footer class="bg-dark text-white pt-4">
      <div class="container">
        <div class="row">
          <!-- SHOP INFO -->
          <div class="col-md-4 mb-3">
            <h5 class="fw-bold">FASHION SHOP</h5>
            <p class="small">
              Chuyên thời trang nam cao cấp. Phong cách – Đơn giản – Hiện đại.
            </p>
          </div>

          <!-- MENU -->
          <div class="col-md-4 mb-3">
            <h6 class="fw-bold">Danh mục</h6>
            <ul class="list-unstyled">
              <li>
                <a href="#" class="text-white text-decoration-none">Áo nam</a>
              </li>
              <li>
                <a href="#" class="text-white text-decoration-none">Quần nam</a>
              </li>
              <li>
                <a href="#" class="text-white text-decoration-none">Phụ kiện</a>
              </li>
            </ul>
          </div>

          <!-- CONTACT -->
          <div class="col-md-4 mb-3">
            <h6 class="fw-bold">Liên hệ</h6>
            <p class="small mb-1">📍 Hà Nội</p>
            <p class="small mb-1">📞 0123 456 789</p>
            <p class="small">✉ fashion@gmail.com</p>
          </div>
        </div>

        <hr class="border-secondary" />

        <div class="text-center small pb-3">
          © 2026 Fashion Shop. All rights reserved.
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem("user")),
    };
  },

  methods: {
    logout() {
      localStorage.removeItem("user");
      localStorage.removeItem("token");

      this.$router.push("/login");
    },
  },
};
</script>