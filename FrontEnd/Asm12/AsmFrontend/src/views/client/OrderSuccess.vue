<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-lg-6">
        <div class="card text-center">
          <div class="card-body p-5">
            <!-- Icon thành công -->
            <div class="mb-4">
              <div class="success-icon mx-auto">
                ✓
              </div>
            </div>

            <h2 class="mb-3 text-success">ĐẶT HÀNG THÀNH CÔNG!</h2>

            <p class="text-muted mb-4">
              Cảm ơn bạn đã mua sắm tại cửa hàng của chúng tôi.
            </p>

            <div v-if="orderId" class="alert alert-info">
              Mã đơn hàng: <strong>#{{ orderId }}</strong>
            </div>

            <p class="small text-muted mb-4">
              Chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất để xác nhận đơn hàng.
            </p>

            <div class="d-flex gap-3 justify-content-center">
              <router-link to="/" class="btn btn-dark">
                <i class="bi bi-house"></i> Tiếp tục mua sắm
              </router-link>
              <!-- SỬA LẠI DÒNG NÀY - dùng :to bind động -->
              <router-link :to="'/order-detail/' + orderId" class="btn btn-outline-dark">
                <i class="bi bi-list-check"></i> Xem đơn hàng
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "OrderSuccess",

  data() {
    return {
      orderId: null
    };
  },

  mounted() {
    this.orderId = this.$route.query.orderId;
    console.log('Order ID:', this.orderId); // Debug
  }
};
</script>

<style scoped>
.success-icon {
  width: 80px;
  height: 80px;
  background-color: #4caf50;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50px;
  font-weight: bold;
  animation: scaleUp 0.5s ease;
}

@keyframes scaleUp {
  0% {
    transform: scale(0);
  }
  70% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.card {
  border: none;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
}
</style>