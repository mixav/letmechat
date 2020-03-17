<template>
  <div>
    <b-icon-circle :style="{color:online?green:red}"></b-icon-circle>
    <span>{{ name }}</span>
  </div>
</template>

<script>
import Vue from 'vue'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'

Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)

export default {
  name: "User",
  props: {
    name: String,
    id: Number,
    online: Boolean
  }
};
</script>