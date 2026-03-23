<template>
  <div class="container py-5">
    <h2 class="text-center mb-4 fw-bold">GIỎ HÀNG</h2>

    <div v-if="cart.length > 0" class="row">
      <!-- DANH SÁCH SẢN PHẨM -->
      <div class="col-lg-8">
        <table class="table align-middle">
          <thead class="table-dark">
            <tr>
              <th>Sản phẩm</th>
              <th>Giá</th>
              <th width="140">Số lượng</th>
              <th>Tạm tính</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in cart" :key="item.id">
              <td>
                <div class="d-flex align-items-center gap-3">
                  <img :src="item.image" class="cart-img" />
                  <span>{{ item.name }}</span>
                </div>
              </td>
              <td>{{ formatPrice(item.price) }}</td>
              <td>
                <div class="d-flex align-items-center gap-2">
                  <button
                    class="btn btn-outline-dark btn-sm"
                    @click="decrease(item)"
                  >
                    -
                  </button>
                  <span>{{ item.qty }}</span>
                  <button
                    class="btn btn-outline-dark btn-sm"
                    @click="increase(item)"
                  >
                    +
                  </button>
                </div>
              </td>
              <td>{{ formatPrice(item.price * item.qty) }}</td>
              <td>
                <button
                  class="btn btn-sm btn-danger"
                  @click="remove(item.id)"
                >
                  X
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- TỔNG TIỀN -->
      <div class="col-lg-4">
        <div class="card">
          <div class="card-body">
            <h5 class="mb-3">Tóm tắt đơn hàng</h5>

            <div class="d-flex justify-content-between mb-2">
              <span>Tạm tính</span>
              <strong>{{ formatPrice(total) }}</strong>
            </div>

            <div class="d-flex justify-content-between mb-3">
              <span>Phí vận chuyển</span>
              <strong>Miễn phí</strong>
            </div>

            <hr />

            <div class="d-flex justify-content-between mb-3">
              <strong>Tổng cộng</strong>
              <strong>{{ formatPrice(total) }}</strong>
            </div>

            <button class="btn btn-dark w-100" @click="checkout">
              Đặt hàng
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- GIỎ HÀNG TRỐNG -->
    <div v-else class="text-center py-5">
      <p class="fs-5">Giỏ hàng của bạn đang trống</p>
      <router-link to="/" class="btn btn-dark">
        Tiếp tục mua sắm
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "Cart",

  data() {
    return {
      cart: [
        {
          id: 1,
          name: "Áo thun basic",
          price: 199000,
          qty: 1,
          image:
            "https://product.hstatic.net/200000690725/product/2_1f24982f91b6417c8ca97b3e7c4da4ee_master.png"
        },
        {
          id: 2,
          name: "Áo thun form rộng",
          price: 219000,
          qty: 2,
          image:
            "https://product.hstatic.net/200000690725/product/2_1f24982f91b6417c8ca97b3e7c4da4ee_master.png"
        }
      ]
    };
  },

  computed: {
    total() {
      return this.cart.reduce(
        (sum, item) => sum + item.price * item.qty,
        0
      );
    }
  },

  methods: {
    increase(item) {
      item.qty++;
    },
    decrease(item) {
      if (item.qty > 1) item.qty--;
    },
    remove(id) {
      this.cart = this.cart.filter(item => item.id !== id);
    },
    checkout() {
      alert("Đặt hàng thành công!");
      this.cart = [];
    },
    formatPrice(price) {
      return price.toLocaleString("vi-VN") + " ₫";
    }
  }
};
</script>

<style scoped>
.cart-img {
  width: 70px;
  height: 70px;
  object-fit: cover;
}
</style>
