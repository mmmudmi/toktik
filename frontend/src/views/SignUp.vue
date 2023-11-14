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
        <p style="font-size: 15px; margin: 0 0 0 1pc">Email</p>
        <v-text-field
          required
          variant="outlined"
          v-model="email"
          type="email"
          label="email"
          single-line
          :rules="[formRequired]"
        ></v-text-field>
        <p style="font-size: 15px; margin: 0 0 0 1pc">Password</p>
        <v-text-field
          variant="outlined"
          v-model="password1"
          label="password"
          single-line
          :rules="[formRequired]"
          type="password"
        ></v-text-field>
        <p style="font-size: 15px; margin: 0 0 0 1pc">Confirmed password</p>
        <v-text-field
          variant="outlined"
          v-model="password2"
          label="password"
          single-line
          :rules="[formRequired]"
          type="password"
        ></v-text-field>
        <div style="display: flex; justify-content: center; align-items: center;">
          <v-btn class="black-btn" style="width: 7pc;margin: 0.5pc" @click="signUp" >Sign up</v-btn>
        </div>
      </v-form>
      <div class="txt-below">
        <p style="font-size: 13px">Already have an account?</p>
        <u style="font-size: 13px;
          font-weight: bold;
          margin-left: 0.2pc;
          color: #EE3457;
          cursor: pointer;"
           @click="navigateToLogin"
        >Log in</u>
      </div>
    </div>
    <v-img src="https://scalable.ap-south-1.linodeobjects.com/toktik.png" class="background-image"></v-img>
    
    <v-dialog v-model="successDialog" :persistent="true">
      <v-card style="
          background-color: #ffffff;
          width: 30pc;
          height: 33pc;
          border-radius: 10px;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          box-shadow: inset 1px 1px 5px rgba(0, 0, 0, 0.3);
          padding: 2pc;
        ">
        <v-card-text class="success-container">
          <i class="fa fa-check-circle" style="font-size:120px;color: #87D571FF;"></i>
          <p class="success-txt">Registration successful!</p>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import { isJwtExpired } from 'jwt-check-expiration';

export default {
  name: "Register",
  data() {
    return {
      username: null,
      email: null,
      password1: null,
      password2: null,
      formRequired: value => !!value || 'Field is required',
      successDialog: false,
    };
  },
  methods: {
    navigateToLogin(){ this.$router.push('login'); },
    signUp(){
      const form = new FormData();
      form.append('username', this.username)
      form.append('email', this.email)
      form.append('password1', this.password1)
      form.append('password2', this.password2)
      axios.post("http://127.0.0.1:8080/api/auth/signup", form)
        .then((res) => {
          let data = res.data
          if (data.code == 0) {
            this.successDialog = true;
            setTimeout(() => {
              this.successDialog = false; // Hide the dialog after 5 seconds
              this.navigateToLogin();
            }, 2000);
            // this.$router.push({ naame: 'home' })
          } else {
            alert(data.message)
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
    axios.get("http://127.0.0.1:8080/api/auth/logout")
    axios.defaults.headers.common['Authorization'] = null;
  },
};
</script>

<style scoped>
@import '@/styles/btn-style.css';
.container {
  background-color: #ffffff;
  width: 30pc;
  height: 33pc;
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
  top: 1pc;
  left: 50%;
  transform: translateX(-50%);
  width: 5pc;
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
.success-container{
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}
.success-txt{
  color: #000000;
  font-size: 2pc;
  font-weight: bold;
  text-align: center;
}
</style>
