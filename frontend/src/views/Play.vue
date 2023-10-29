<template>
  <Navbar />
  <div class="toktik-page">
    
    <v-row no-gutters>
      <v-col cols="5" class="left-side">
        <div class="vid-container">
          <video-player  ref="player" :options="videoOptions" ></video-player>
        </div>
      </v-col>

      <v-col cols="7"  class="right-side">
        <div class="user-container">
            <div class="sub-user-container">
              <div class="circle">
              <p class="initial">{{ this.username[0] || 'U' }}</p>
            </div>
            <p class="line" style="font-weight:600;position: relative;padding: 1pc 1pc 0 1pc; " >@{{ this.username || 'Unknown User' }}</p>
          </div>
        </div>

        <div  class="comments-container">
          <hr style="width: 100%; margin: 0;border: 0.5px solid #ececec;"> 

        </div >
        <div  class="comment-container">
          <hr style="width: 100%; margin: 0;border: 0.5px solid #ececec; margin-bottom: 10px;"> 

          <div style="display: flex; flex-direction: row;align-items: center;margin-bottom: 10px;">
            <i id="like-btn" @click="clickLike" class="fa fa-heart" style="font-size:30px; color: rgb(229, 229, 229); ">
            </i>
            <p style="color: rgb(0, 0, 0);margin-left: 8px; font-size: 15px;font-family: Roboto;">
              {{ this.likes }} {{ this.likes <= 1 ? 'like' : 'likes' }}
            </p>
          </div>

          <div style="display: flex;">
            <v-text-field
            v-model="comment"
            variant="outlined"
            label="Add comment"
            single-line
            :rules="[formRequired]"
          ></v-text-field>
          <v-btn class="blue-btn" style="height: 55px;background-color: #c9c9c9;"> <i class="fa fa-send"></i> </v-btn>
          </div>
          
        </div >
      
      </v-col>
    </v-row>
  </div>
</template>

<script >
import axios from 'axios'
import Navbar from '@/components/Navbar.vue'
import { isJwtExpired } from 'jwt-check-expiration';
import VideoPlayer from '@/components/VideoPlayer.vue';

export default {
  components: {Navbar,VideoPlayer},
  data(){
    return{
      prev: null,
      next: null,
      id: localStorage.getItem("id"),
      player: null,
      video: localStorage.getItem("filename"),
      caption: "",
      username: "",
      views: 0,
      likes: 2,
      type: localStorage.getItem("type"),
      like: false,
      videoOptions: {
        autoplay: true,
        controls: true,
        loop: true,
        sources: [
          {
            src: "http://localhost:8080/api/video/playlist/"+localStorage.getItem("filename"),
            type: 'application/x-mpegURL'
          }
        ]
      },
    }
  },
  computed: {
    player () {
      return this.$refs.player.player
    }
    
  },
  methods:{
    async fetchData(){
      axios.get("http://localhost:8080/api/"+localStorage.getItem("type"),{
          params: {page: this.id,size: 1},
        })
          .then((res) => {
            if (res.data != null) {
              res.data.forEach(item => {
                this.caption = item.caption;
                this.views = item.views;
                this.username = item.username;
              });
            } else {  
            }
          })
    }, 
    clickLike() {
      var likeBtn = document.getElementById('like-btn');
      if (this.like) {
        likeBtn.style.color = 'rgb(229, 229, 229)';
        this.like = false;
      } else {
        likeBtn.style.color = '#EE3457';
        this.like = true;
      }
    },
  },
  mounted(){

  },
  beforeMount() {
    let jwtToken = localStorage.getItem('token')
    if (jwtToken && !isJwtExpired(jwtToken)) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('token')}`;
      const form = new FormData;
      form.append("username", localStorage.getItem("username"))
      this.fetchData();
    } else {
      localStorage.removeItem('token')
      axios.defaults.headers.common['Authorization'] = null;
      this.$router.push({ name: 'welcome'})
    }
  },
  
}
</script>

<style scoped>
@import '@/styles/btn-style.css';

.toktik-page {
  /* margin: 0.2pc; */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.left-side{
  height: 100%;
  width: 100vw;
  place-items: center;
  overflow: hidden;
  background: rgb(255, 255, 255);
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}
.line {
  font-size: 15px;
  color: rgb(0, 0, 0);
  font-weight: 500;
  white-space: nowrap;
  overflow: scroll;
  direction: ltr;
  line-height: 1;
  pointer-events: auto;
}
.line::-webkit-scrollbar {
  width: 0; /* Hide scrollbar in Webkit browsers */
}

.vid-container{
  height: 100vh;
  /* width: 24.5pc; */
  place-items: center;
  overflow: hidden;
  background: rgb(255, 255, 255);
  position: relative;
}
.right-side{
  padding: 3pc;
  flex-direction: column;
  display: flex;
  position: relative;
}
.user-container{
  padding: 14px;

}
.circle {
  width: 50px;
  height: 50px;
  background-color: #000000;
  border-radius: 100pc;
}
.initial{
  color: white;
  font-weight: bolder;
  font-size: 2pc;
  text-align: center;
}
.comments-container{
  /* box-shadow: inset 0px 0px 4px rgba(0, 0, 0, 0.15); */
  height: 77.1vh;
  overflow-x: auto;
  height: 100%;
  overflow-y: auto;
  position: relative;

}
.comment-container{
  width: 100%;
  height: 9pc;
  padding: 16px 7px 0 16px;
  justify-content: center;
}
.sub-user-container{
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: row;
  justify-content: flex-start;
}


</style>