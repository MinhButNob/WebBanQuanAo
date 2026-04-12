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
import Color from "@/views/admin/Color.vue";

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
import Size from "@/views/admin/Size.vue";
import OrderSuccess from "@/views/client/OrderSuccess.vue";
import Orders from "../views/admin/Orders.vue";
import OrderDetail from "@/views/client/OrderDetail.vue";
import ClientOrders from "@/views/client/ClientOrders.vue";
import Profile from "@/views/client/Profile.vue";
import ProductsByCategory from "@/views/client/ProductsByCategory.vue";

const routes = [
  {
    path: "/",
    component: ClientLayout,
    children: [
      { path: "", component: Products },
      { path: "product/:id", component: ProductDetail },
      { path: "cart", component: Cart },
      { path: "checkout", component: Checkout },
      { path: "orders", name: "ClientOrders", component: ClientOrders },
      { path: "order-detail/:id", name: "OrderDetail", component: OrderDetail },
      {
        path: "profile",
        name: "Profile",
        component: Profile,
        meta: { requiresAuth: true },
      },
       { path: "category/:id", name: "ProductsByCategory", component: ProductsByCategory },
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
      { path: "colors", component: Color },
      { path: "sizes", component: Size },
    ],
  },
  {
    path: "/checkout",
    component: Checkout,
    meta: { requiresAuth: true },
  },
  {
    path: "/checkout",
    name: "Checkout",
    component: Checkout,
    meta: { requiresAuth: true },
  },

  {
    path: "/checkout",
    name: "Checkout",
    component: Checkout,
  },
  {
    path: "/order-success",
    name: "OrderSuccess",
    component: OrderSuccess,
  },

  {
    path: "/order-detail/:id", // :id là mã đơn hàng
    name: "OrderDetail",
    component: OrderDetail,
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
