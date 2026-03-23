<template>
  <div class="admin-layout">
    <!-- HEADER -->
    <nav class="navbar navbar-dark bg-dark px-3">
      <span class="navbar-brand">DOSHISHA </span>
    </nav>

    <div class="d-flex">
      <!-- SIDEBAR -->
      <aside class="sidebar bg-light border-end">
        <ul class="nav flex-column p-3">
          <li class="nav-item">
            <router-link
              class="nav-link"
              to="/admin"
              exact-active-class="router-link-exact-active"
            >
              Dashboard
            </router-link>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/admin/products">
              Quản lý sản phẩm
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/admin/orders">
              Quản lý đơn hàng
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/admin/employees">
              Quản lý nhân viên
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/admin/customers">
              Quản lý khách hàng
            </RouterLink>
          </li>

          <li class="nav-item mt-4">
            <button class="btn btn-danger w-100" @click="logout">
              Đăng xuất
            </button>
          </li>
          <!-- <li class="nav-item">
            <RouterLink class="nav-link" to="/admin/sale">
              Bán hàng
            </RouterLink>
          </li> -->
        </ul>
      </aside>

      <!-- CONTENT -->
      <main class="content p-4 flex-fill">
        <router-view />
      </main>
    </div>

    <!-- FOOTER -->
    <footer class="bg-dark text-white text-center py-2">
      © 2026 FASHION SHOP
    </footer>
  </div>
</template>

<script>


export default {
  name: "AdminLayout",

methods: {
  logout() {
    localStorage.removeItem("user");
    localStorage.removeItem("token");

    this.$router.push("/login");
  }
}
};


</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}

/* Trong phần <style scoped> của AdminLayout */
.sidebar {
  width: 240px;
  min-width: 240px; /* Thêm dòng này để khóa cứng độ rộng tối thiểu */
  flex-shrink: 0;   /* Thêm dòng này để ngăn Flexbox bóp nghẹt sidebar */
  min-height: calc(100vh - 56px - 40px);
}

.nav-link {
  color: #000;
}

.nav-link.router-link-exact-active {
  font-weight: bold;
  background-color: #000;
  color: #fff;
  border-radius: 4px;
}
</style>

