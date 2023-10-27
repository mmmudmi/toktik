<template>
  <Navbar />
  <div class="toktik-page">
    <div class="navigation-buttons">
      <i class="fa fa-chevron-left" @click="goToPreviousVideo" id="goToPreviousVideo"></i>
      <i class="fa fa-chevron-right" @click="goToNextVideo" id="goToNextVideo"></i>
    </div>
    <div class="vid-container">
      <v-row class="element-boxes">
  

        <v-col class="box">
          <div class="line">@{{ this.username }}</div>
<!--          <div class="line">Title</div>-->
          <div class="line">{{ this.caption }}</div>
        </v-col>
        <!--        likes, comments, icons -->
        <v-col class="box">
          <!-- <v-col>
            <i id="like-btn" @click="clickLike" class="fa fa-heart" style="font-size:36px; color: white;">
              <p style="color: white; font-size: 11px;text-align: right;font-family: Roboto;text-align: center">likes</p>
            </i>
          </v-col>
          <v-col>
            <i class="fa fa-comment" style="font-size:36px; color: white;">
              <p style="color: white; font-size: 10px;text-align: right;font-family: Roboto;text-align: center">comments</p>
            </i>
          </v-col> -->

        </v-col>
        
      </v-row>
      <div class="vid">
          <video-player  ref="player" :options="videoOptions" ></video-player>
        </div>
      
    </div>

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
      // localStorage.setItem('type', 'views')
      //   localStorage.setItem('filename', Filename)
      //   localStorage.setItem('id', Id)

      prev: null,
      next: null,
      id: localStorage.getItem("id"),
      player: null,
      video: localStorage.getItem("filename"),
      caption: "",
      username: "",
      // views: 0,
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
    onPlayerPlay({ event, player }) {
      console.log(event.type);
      player.setPlaying(true);
    },
    onPlayerPause({ event, player }) {
      console.log(event.type);
      player.setPlaying(false);
    },
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
              this.fetchNext();
              this.fetchPrev();
            } else {  //handle only right click
              this.id -=1;
            }
          })
    }, 
    async fetchPrev() {
      if (this.id > 0){
        axios.get("http://localhost:8080/api/"+localStorage.getItem("type"),{
          params: {page: this.id-1,size: 1},
        })
          .then((res) => {
            const prev = []
            res.data.forEach(item => prev.push(item));
            this.prev = prev;

          })
          
      } 
    },
    async fetchNext() {
        axios.get("http://localhost:8080/api/"+localStorage.getItem("type"),{
          params: {page: this.id+1,size: 1},
        })
          .then((res) => {
            const next = []
            res.data.forEach(item => next.push(item));
            
            if (next != []){
              this.next = next
            }
          })
          
    },
    goToPreviousVideo() {
      console.log("this.prev: ",this.prev)
      if (this.prev.length == 1) {
        console.log("this.prev: ",this.prev)
        // localStorage.setItem('filename', this.prev.video)
        // localStorage.setItem('id', this.id--)
        // window.location.reload()

      } 
    },
    goToNextVideo() {
      console.log("this.next: ",this.next)
      if (this.next.length == 1) {
        console.log("this.next: ",this.next)
        // localStorage.setItem('filename', this.next.video)
        // localStorage.setItem('id', this.id++)
        // window.location.reload()
      } 
    },
    clickLike() {
      var likeBtn = document.getElementById('like-btn');
      if (this.like) {
        likeBtn.style.color = 'white';
        this.like = false;
      } else {
        likeBtn.style.color = '#EE3457';
        this.like = true;
      }
    },
    togglePlay(){
      this.player.setPlaying(false);
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
  margin: 0pc 2pc 0pc 2pc;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 95vh;
  /* background-color: black; */
}
.navigation-buttons{
  z-index: 1;
  display: flex;
  position: absolute;
  justify-content: space-between; /* Add this line to position icons at the ends */
  width: 70vh;
  color: #000000;
  font-size:36px
}
.fa {
  font-size: 24px; /* Adjust the font size as needed */
  cursor: pointer;
}
.vid-container{
  height: 100%;
  width: 28pc;
  place-items: center;
  overflow: hidden;
  background: rgb(0, 0, 0);
  position: relative;
  
}
/* .vid-container video{
  max-width: 100%;
  max-height: 100%;
  width: 100%;
  height: auto;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
} */
/* video-player {
  width: 100%; 
  height: auto; 
} */
.vid{
  max-width: 100%;
  max-height: 100%;
  width: 100%;
  height: auto;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.vid-container::before {
  position: absolute;
  width: 100%;
  height: 30%;
  bottom: 0;
  left: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.15), rgba(0, 0, 0, 0)); /* Fade from black to transparent */
  z-index: 2;
  pointer-events: none;
}
.element-boxes{
  display: flex;
  justify-content: space-between;
  position: absolute;
  left: 1pc;
  right: 0.4pc;
  bottom: 0pc;
}
.box{
  position: relative;
  z-index: 3;
  width: 75%;
  overflow: hidden;
}
.box:nth-child(1){
  pointer-events: none;
}
.box:nth-child(2){
  width: 20%;
  height: 20%;
  text-align: right;
  display: flex;
  flex-direction: column;
  top: -120px;
}
.line {
  font-size: 14px;
  color: white;
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
</style>