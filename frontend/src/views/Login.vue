<template>
  <div class="background">
    <div class="container">
      <v-form>
        <p style="font-size: 15px; margin: 0 0 0 1pc">Username</p>
        <v-text-field
          required
          variant="outlined"
          v-model="username"
          type="text"
          label="username"
          single-line
          :rules="[formRequired]"
        ></v-text-field>
        <p style="font-size: 15px; margin: 0 0 0 1pc">Password</p>
        <v-text-field
          variant="outlined"
          v-model="password"
          label="password"
          single-line
          :rules="[formRequired]"
          :append-icon="eye ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="() => (eye = !eye)"
          :type="eye ? 'password' : 'text'"
        ></v-text-field>
        <div style="display: flex; justify-content: center; align-items: center;">
          <v-btn class="black-btn" style="width: 7pc;margin: 0.5pc" @click="login">Log in</v-btn>
        </div>
        <div class="txt-below">
          <p style="font-size: 13px">Don't have an account?</p>
          <u style="font-size: 13px;
          font-weight: bold;
          margin-left: 0.2pc;
          color: #EE3457;
          cursor: pointer;"
             @click="navigateToRegister"
          >Sign up</u>
<!--          <p style="font-size: 13px">or</p>-->
<!--          <u style="font-size: 13px;-->
<!--          font-weight: bold;-->
<!--          margin-left: 0.2pc;-->
<!--          color: #EE3457;-->
<!--          cursor: pointer;"-->
<!--          @click="navigateToRegister"-->
<!--          >forget password</u>-->

        </div>
      </v-form>
    </div>
    <v-img src="https://scalable.ap-south-1.linodeobjects.com/toktik.png" class="background-image"></v-img>
  </div>
</template>

<script>
import axios from 'axios';
import { isJwtExpired } from 'jwt-check-expiration';

export default {
  data() {
    return {
      username: null,
      password: null,
      eye: String,
      formRequired: value => !!value || 'Field is required',
    };
  },
  methods: {
    navigateToRegister(){ this.$router.push('signUp'); },
    login(){
      const form = new FormData();
      form.append('username', this.username)
      form.append('password', this.password)
      axios.post("http://localhost:8080/api/auth/login", form)
        .then((res) => {
          let data = res.data
          if (data.success) {
            localStorage.setItem('token', data.message)
            localStorage.setItem('username', this.username)
            this.$router.push({ name: 'home' })
          } else {
            alert(data.message)
            // window.location.reload()
          }
        }).catch(
            err => {
              console.log(form)
              console.log(err)
            })
    }
  },
  mounted() {},
  beforeMount() {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    axios.get("http://localhost:8080/api/auth/logout")
    axios.defaults.headers.common['Authorization'] = null;
  }
};
</script>

<style scoped>
@import '@/styles/btn-style.css';
.container {
  background-color: #ffffff;
  width: 30pc;
  height: 21pc;
  border-radius: 10px;
  z-index: 1;
  position: relative;
  box-shadow: inset 1px 1px 5px rgba(0, 0, 0, 0.3);
  padding: 2pc;
}

.background {
  background-color: #ffffff;
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative; /* Set the background to a relative position */
}

.background-image {
  position: absolute;
  top: 3.5pc;
  left: 50%;
  transform: translateX(-50%);
  width: 8pc;
  height: auto;
  z-index: 0;
}
.txt-below{
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0.4pc;
}

</style>
