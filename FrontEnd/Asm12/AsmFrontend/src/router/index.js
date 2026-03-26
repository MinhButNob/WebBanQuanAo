import { createRouter, createWebHistory } from "vue-router";

// layouts
import AdminLayout from "../layouts/AdminLayout.vue";
import ClientLayout from "../layouts/ClientLayout.vue";

// admin pages
import AdminProducts from "../views/admin/Products.vue";
import AdminOrders from "../views/admin/Orders.vue";
import AdminCustomers from "../views/admin/Customers.vue";
import AdminSale from "../views/admin/Sale.vue";
import AdminDashboard from "../views/admin/Dashboard.vue";
import EmployeeManager from "@/views/admin/EmployeeManager.vue";
import Category from "@/views/admin/Category.vue";

import Login from "../views/auth/Login.vue";
import Register from "../views/auth/Register.vue";

// client pages
import Products from "../views/client/Products.vue";
import ProductDetail from "../views/client/ProductDetail.vue";
import Cart from "../views/client/Cart.vue";
import Checkout from "../views/client/Checkout.vue";



// IMPORT BOOTSTRAP
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";


const routes = [
  {
    path: "/",
    component: ClientLayout,
    children: [
      { path: "", component: Products },
      { path: "product/:id", component: ProductDetail },
      { path: "cart", component: Cart },
      { path: "checkout", component: Checkout },
    ],
  },

  { path: "/login", component: Login },
  { path: "/register", component: Register },
  {
    path: "/admin",
    component: AdminLayout,
    children: [
      { path: "", component: AdminDashboard },
      { path: "products", component: AdminProducts },
      { path: "employees", component: EmployeeManager },
      { path: "orders", component: AdminOrders },
      { path: "customers", component: AdminCustomers },
      { path: "sale", component: AdminSale },
      { path: "category", component: Category },
    ],
  },
  {
    path: "/checkout",
    component: Checkout,
    meta: { requiresAuth: true },
  },
];


const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem("user"));

 
  if (to.meta.requiresAuth && !user) {
    next("/login");
    return;
  }

  // cần quyền admin
  if (to.meta.role === "admin" && user?.role !== "admin") {
    next("/");
    return;
  }

  next();
});

export default router;         