<!-- <template>
  <div class="container mt-5">
    <h1>{{ title }}</h1>
    <p>Đây là project Vue đầu tiên của tôi</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      title: 'Hello Vue 3 🚀'
    }
  }
}
</script>

<style>
h1 {
  color: blue;
}
</style> -->


<!-- nut bam -->
<!-- <template>
  <div class="container mt-5">
    <h1>{{ title }}</h1>

    <button class="btn btn-primary" @click="changeTitle">
      Đổi tiêu đề
    </button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      title: 'Hello Vue 3 🚀'
    }
  },
  methods: {
    // changeTitle() {
    //   this.title = 'ASM 1 VueJS 🎯'
    // }

//  
    changeTitle() {
  this.title = this.title === 'Hello Vue 3 🚀'
    ? 'ASM 1 VueJS 🎯'
    : 'Hello Vue 3 🚀'
}
  }
}
</script> -->



<!-- vong lap -->
 <!-- <template>
  <div class="container mt-5">
    <h2>Danh sách sản phẩm</h2>

    <ul class="list-group">
      <li
        class="list-group-item"
        v-for="item in products"
        :key="item.id"
      >
        {{ item.name }} - {{ item.price }} đ
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      products: [
        { id: 1, name: 'Áo thun', price: 150000 },
        { id: 2, name: 'Quần jean', price: 350000 },
        { id: 3, name: 'Giày sneaker', price: 800000 }
      ]
    }
  }
}
</script> -->


<!-- CRUD -->

<template>
  <router-view />
</template>
