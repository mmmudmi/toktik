<template>
  <nav>
    <v-toolbar class="Navbar">
      <v-toolbar-title>
        <v-row align="start">
          <v-col class="shrink">
            <v-img src="https://scalable.ap-south-1.linodeobjects.com/toktik_wide.png" style="cursor: pointer;margin-left:1pc" width="100" @click="navigateToHomePage"></v-img>
          </v-col>
          <v-col align="end">
            <v-btn class="reg-btn" style="width: 6.2pc" @click="navigateToMyVideosPage"> My videos </v-btn>
            <v-btn class="reg-btn" style="width: 6.2pc" @click="navigateToUploadPage">
              <i class="fa fa-plus" style="font-size:12px; padding-right: 5px"></i>
              Upload
            </v-btn>
            <v-btn class="red-btn" style="width: 6.2pc" @click="logout"> Log out</v-btn>
          </v-col>>
        </v-row>
      </v-toolbar-title>
    </v-toolbar>
  </nav>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
    };
  },
  methods: {
    navigateToUploadPage(){ this.$router.push('upload'); },
    navigateToHomePage(){ this.$router.push('home'); },
    navigateToMyVideosPage(){ this.$router.push('myVideos'); },
    logout(){
      console.log('exit')
      axios.get("http://localhost:8080/api/auth/logout")
        .then((res) => {
          let data = res.data
          if (data.success) {
            this.$router.push({ name: 'welcome' })
            alert(data.message)
          } else {
            alert(data.message)
          }
        }).catch(
        err => {
          console.log(err)
        })
    },
  }
}
</script>

<style scoped>
@import '@/styles/btn-style.css';

.Navbar{
  background-color: #ffffff;
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2);
}
</style>
