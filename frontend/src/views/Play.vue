<template>
  <Navbar />
  <div class="toktik-page">
    <div class="navigation-buttons">
      <i class="fa fa-chevron-left"></i>
      <i class="fa fa-chevron-right"></i>
    </div>
    <div class="vid-container">
      <v-row class="element-boxes">
        <v-col class="box">
          <div class="line">@username</div>
<!--          <div class="line">Title</div>-->
          <div class="line">Description Description Description Description Description</div>
        </v-col>
        <!--        likes, comments, icons -->
        <v-col class="box">
          <v-col>
            <i id="like-btn" @click="clickLike" class="fa fa-heart" style="font-size:36px; color: white;">
              <p style="color: white; font-size: 11px;text-align: right;font-family: Roboto;text-align: center">likes</p>
            </i>
          </v-col>
          <v-col>
            <i class="fa fa-comment" style="font-size:36px; color: white;">
              <p style="color: white; font-size: 10px;text-align: right;font-family: Roboto;text-align: center">comments</p>
            </i>
          </v-col>

        </v-col>
      </v-row>
      <video :src="this.video" controls autoplay loop></video>
    </div>

  </div>
</template>

<script >
import axios from 'axios'
import Navbar from '@/components/Navbar.vue'
import { isJwtExpired } from 'jwt-check-expiration';
export default {
  components: {Navbar},
  data(){
    return{
      videos: {"id1":{"title":"hello","views":2,"user":"mmmummmudmimmmudmimmmudmimmmudmidmi"},
        "id2":{"title":"hi","views":10,"user":"mimi"},
        "id3":{"title":"hohoho","views":8,"user":"may"},
        "id4":{"title":"heyyy","views":2,"user":"mild"},
        "id5":{"title":"good job","views":13,"user":"mmmudmi"},
        "id6":{"title":"niceeeeee","views":23,"user":"mmmudmi"},
        "id7":{"title":"su su","views":20,"user":"mimi"}
      },
      current: null,
      id: null,
      video: null,
      like: false,
    }
  },
  methods:{
    goToPreviousVideo() {
      if (this.videoIndex > 0) {
        this.videoIndex--;
      }
    },
    getVideo() {
      const form = new FormData();
      form.append('filename','Snaptik.app_7278649121777421576.mp4')
      axios.post('http://localhost:8080/api/s3/access-request',form)
        .then((res) => {
          let data = res.data
          this.video = data.message
          console.log(data.message)
        })
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
  },
  mounted() {
    this.id = this.$route.params.id
    this.current = this.videos[this.id]
    this.getVideo()
  },
  beforeMount() {
    let jwtToken = localStorage.getItem('token')
    if (jwtToken && !isJwtExpired(jwtToken)) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('token')}`;
      const form = new FormData;
      form.append("username", localStorage.getItem("username"))
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
  //background-color: black;
}
.navigation-buttons{
  z-index: 1;
  display: flex;
  position: absolute;
  justify-content: space-between; /* Add this line to position icons at the ends */
  width: 67vh;
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
  background: black;
  position: relative;
}
.vid-container video{
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
  content: '';
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
