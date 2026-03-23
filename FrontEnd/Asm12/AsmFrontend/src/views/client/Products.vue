<template>
  <div class="container-fluid px-0">
    <!-- BANNER -->
    <div class="banner mb-5">
      <img
        src="https://cdn-media.sforum.vn/storage/app/media/anh-dep-82.jpg"
        alt="Banner"
        class="w-100"
      />
      <div class="banner-text">
        <h1>Fall Winter 2025</h1>
        <p>New Collection</p>
      </div>
    </div>

    <!-- SECTION 1 -->
    <div class="container mb-5">
      <h3 class="section-title">SẢN PHẨM MỚI</h3>
      <div class="row g-4">
        <div class="col-md-3" v-for="p in products" :key="p.id">
          <div class="product-card">
            <span class="sale-badge">SALE</span>

            <router-link :to="`/product/${p.id}`">
              <img :src="p.image" class="img-fluid" />
            </router-link>

            <h6 class="mt-2">
              <router-link
                :to="`/product/${p.id}`"
                class="text-dark text-decoration-none"
              >
                {{ p.name }}
              </router-link>
            </h6>

            <p class="price">
              <span class="old">{{ p.oldPrice }}đ</span>
              <span class="new">{{ p.price }}đ</span>
            </p>
            <router-link :to="`/cart`">
              <button class="btn btn-dark w-100 btn-sm">Thêm vào giỏ</button>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- BANNER GIỮA -->
    <div class="mid-banner mb-5">
      <img
        src="https://cdn-media.sforum.vn/storage/app/media/anh-dep-82.jpg"
        class="w-100"
      />
    </div>

    <!-- SECTION 2 -->
    <div class="container mb-5">
      <h3 class="section-title">SẢN PHẨM NỔI BẬT</h3>
      <div class="row g-4">
        <div class="col-md-3" v-for="p in products" :key="'hot' + p.id">
          <div class="product-card highlight">
            <span class="sale-badge">-10%</span>
            <img :src="p.image" class="img-fluid" />
            <h6 class="mt-2">{{ p.name }}</h6>
            <p class="price">
              <span class="old">{{ p.oldPrice }}đ</span>
              <span class="new">{{ p.price }}đ</span>
            </p>
            <button class="btn btn-outline-dark w-100 btn-sm">
              Xem chi tiết
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- SECTION 3 -->
    <div class="container mb-5">
      <h3 class="section-title">ÁO KHOÁC NAM</h3>
      <div class="row g-4">
        <div class="col-md-3" v-for="p in products" :key="'jacket' + p.id">
          <div class="product-card simple">
            <img :src="p.image" class="img-fluid" />
            <h6 class="mt-2">{{ p.name }}</h6>
            <p class="price">
              <span class="new">{{ p.price }}đ</span>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductList",
  data() {
    return {
      products: [
        {
          id: 1,
          name: "Áo khoác nam basic",
          price: "650.000",
          oldPrice: "800.000",
          image:
            "https://cdn.hstatic.net/products/200000690725/_o_web__2__ba4e66e0c86d428aa9562cece6ef5a40_master.png",
        },
        {
          id: 2,
          name: "Áo khoác form rộng",
          price: "600.000",
          oldPrice: "750.000",
          image:
            "https://cdn.hstatic.net/products/200000690725/_o_web__2__ba4e66e0c86d428aa9562cece6ef5a40_master.png",
        },
        {
          id: 3,
          name: "Áo khoác dạ",
          price: "1.250.000",
          oldPrice: "1.390.000",
          image:
            "https://cdn.hstatic.net/products/200000690725/_o_web__2__ba4e66e0c86d428aa9562cece6ef5a40_master.png",
        },
        {
          id: 4,
          name: "Áo khoác hoodie",
          price: "550.000",
          oldPrice: "690.000",
          image:
            "https://cdn.hstatic.net/products/200000690725/_o_web__2__ba4e66e0c86d428aa9562cece6ef5a40_master.png",
        },
      ],
    };
  },
};
</script>

<style scoped>
.banner {
  position: relative;
}

.banner-text {
  position: absolute;
  bottom: 30%;
  left: 10%;
  color: white;
}

.banner-text h1 {
  font-size: 48px;
  font-weight: bold;
}

.section-title {
  font-weight: bold;
  margin-bottom: 20px;
}

.product-card {
  border: 1px solid #eee;
  padding: 15px;
  position: relative;
  background: #fff;
}

.product-card img {
  width: 100%;
}

.sale-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: red;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
}

.price .old {
  text-decoration: line-through;
  color: #999;
  margin-right: 8px;
}

.price .new {
  color: red;
  font-weight: bold;
}

.mid-banner img {
  height: 400px;
  object-fit: cover;
}
</style>
