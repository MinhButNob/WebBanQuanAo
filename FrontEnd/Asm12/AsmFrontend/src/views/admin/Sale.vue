<template>
  <div class="container-fluid p-4">
    <h2 class="mb-4">Bán hàng tại quầy</h2>

    <div class="row">
      <!-- DANH SÁCH SẢN PHẨM -->
      <div class="col-md-7">
        <div class="card mb-4">
          <div class="card-body">
            <h5 class="mb-3">Danh sách sản phẩm</h5>

            <table class="table table-hover align-middle">
              <thead class="table-dark">
                <tr>
                  <th>Tên</th>
                  <th>Giá</th>
                  <th>Tồn kho</th>
                  <th width="120"></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="p in products" :key="p.id">
                  <td>{{ p.name }}</td>
                  <td>{{ formatPrice(p.price) }}</td>
                  <td>{{ p.stock }}</td>
                  <td>
                    <button
                      class="btn btn-sm btn-dark"
                      @click="addToCart(p)"
                      :disabled="p.stock === 0"
                    >
                      Thêm
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- ĐƠN HÀNG -->
      <div class="col-md-5">
        <div class="card">
          <div class="card-body">
            <h5 class="mb-3">Đơn hàng</h5>

            <table class="table align-middle">
              <thead>
                <tr>
                  <th>Sản phẩm</th>
                  <th width="120">SL</th>
                  <th width="120">Giá</th>
                  <th width="80"></th>
                </tr>
              </thead>

              <tbody>
                <tr v-for="item in cart" :key="item.id">
                  <td>{{ item.name }}</td>
                  <td>
                    <div class="d-flex align-items-center gap-1">
                      <button
                        class="btn btn-sm btn-outline-dark"
                        @click="decreaseQty(item)"
                      >
                        -
                      </button>
                      <span>{{ item.qty }}</span>
                      <button
                        class="btn btn-sm btn-outline-dark"
                        @click="increaseQty(item)"
                        :disabled="item.qty >= item.stock"
                      >
                        +
                      </button>
                    </div>
                  </td>
                  <td>{{ formatPrice(item.price * item.qty) }}</td>
                  <td>
                    <button
                      class="btn btn-sm btn-danger"
                      @click="removeItem(item.id)"
                    >
                      X
                    </button>
                  </td>
                </tr>

                <tr v-if="cart.length === 0">
                  <td colspan="4" class="text-center text-muted">
                    Chưa có sản phẩm
                  </td>
                </tr>
              </tbody>
            </table>

            <hr />

            <div class="d-flex justify-content-between mb-3">
              <strong>Tổng tiền:</strong>
              <strong>{{ formatPrice(total) }}</strong>
            </div>

            <button
              class="btn btn-success w-100"
              :disabled="cart.length === 0"
              @click="checkout"
            >
              Tạo đơn hàng
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminSale",

  data() {
    return {
      products: [
        { id: 1, name: "Áo thun", price: 150000, stock: 10 },
        { id: 2, name: "Quần jean", price: 350000, stock: 5 },
        { id: 3, name: "Áo khoác", price: 500000, stock: 3 }
      ],

      cart: []
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
    addToCart(product) {
      const found = this.cart.find(i => i.id === product.id);

      if (found) {
        if (found.qty < product.stock) {
          found.qty++;
        }
      } else {
        this.cart.push({
          ...product,
          qty: 1
        });
      }
    },

    increaseQty(item) {
      if (item.qty < item.stock) item.qty++;
    },

    decreaseQty(item) {
      if (item.qty > 1) item.qty--;
    },

    removeItem(id) {
      this.cart = this.cart.filter(i => i.id !== id);
    },

    checkout() {
      alert("Tạo đơn hàng thành công!");

      // reset
      this.cart = [];
    },

    formatPrice(price) {
      return price.toLocaleString("vi-VN") + " ₫";
    }
  }
};
</script>
