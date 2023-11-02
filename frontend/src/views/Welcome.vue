<template>
  <div style="display: flex; justify-content: center;align-items: center;height: 100vh;">
    <div class="container">
        <v-img src="https://scalable.ap-south-1.linodeobjects.com/toktik.png" style="scale: 0.9;"></v-img>
      <v-row class="lower">

        <v-btn class="black-btn" style="margin: 0 1pc 0 1pc;width: 7pc;" @click="navigateToLogin">login</v-btn>
        <v-btn class="black-btn" style="margin: 0 1pc 0 1pc;width: 7pc;" @click="navigateToRegister">register</v-btn>
      </v-row>
    </div>
  </div>
</template>

<script >
import axios from 'axios'
export default {
  name: "Welcome",
  data(){
    return{
    }
  },
  methods:{
    navigateToRegister(){ this.$router.push('signUp'); },
    navigateToLogin(){ this.$router.push('login'); },
  },
  mounted() {
  },
  beforeMount() {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    axios.get("/api/auth/logout")
    axios.defaults.headers.common['Authorization'] = null;
  }
}
</script>

<style scoped>
@import '@/styles/btn-style.css';
.container{
  width: 27pc;
  height: 27pc;
  display: flex;
  flex-direction: column;
  position: relative;
  top: -3pc;
}
.upper{
  flex: 2;
  object-fit: contain;
}
.lower{
  flex: 1;
  width: 100%;
  justify-content: center;
  align-items: center;
}
</style>
