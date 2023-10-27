<template>
  <Navbar />
  <div style="margin: 2pc">
    <div id="upload-progress-container" style="display:none">
      <progress id="upload-progress" value="0" max="100"></progress>
    </div>

    <v-row class="container">
      <div class="circle">
        <p class="initial">{{ this.username[0] || 'U' }}</p>
      </div>
      <p class="name">@{{ this.username || 'Unknown User' }}</p>
      <hr class="line-below-name">
    </v-row>
    <v-row>
      <v-col v-for="(video,id) in list" :key="id" cols="12" sm="6" md="3">
        <v-card class="card-container" @click="redirect(video.video,id)">
          <div class="vid">
            <v-btn @click="deleteVideo(video.video)" id="remove" density="compact" icon="mdi-close" style="color: #fff; background-color:transparent"></v-btn>
            <img :src="video.preview" style="width: 100%;height: 100%;" class="preview" >
            <v-row style="position: relative; left: 1.5pc; bottom: 1.5pc;z-index: 2;">
              <i class="fa fa-play" style="color: white; margin-right: 10px;"></i>
              <p class="txt-card" style="font-size: 15px;position: absolute;left: 18px;bottom: -3px">{{ video.views }} views</p>
            </v-row>
          </div>
          <div class="description" style="z-index: 2;">
            <div class="line">{{ video.caption }}</div>
          </div>
        </v-card>
      </v-col>
    </v-row>

  </div>
</template>

<script >
import axios from 'axios';
import { isJwtExpired } from 'jwt-check-expiration';
import Navbar from '@/components/Navbar.vue'

export default {
  components: {Navbar},
  data(){
    return{
      username: localStorage.getItem('username'),
      list: [],
      page:-1,
      size:12,  
    }
  },
  methods:{
    redirect(Filename,Id){
        // type = "views" "profile"     path: '/play:filename:type:id:',
        localStorage.setItem('type', 'profile')
        localStorage.setItem('id', Id)
        localStorage.setItem('filename', Filename)
        this.$router.push({ name: 'play'})
      },
    deleteVideo(filename){
      axios.get("http://localhost:8080/api/video/delete/"+filename)
        .then((res) => {
          alert(res.data.message)
          this.getList();
        })
    },
    getList() {
      // VideoSimpleRecord(String video, String preview, String caption, Integer views, String username) 

      axios.get("http://localhost:8080/api/video/profile",{
        params: {page: this.page,size:12},
      })
        .then((res) => {
          this.list = res.data
          console.log(this.list);
        })
    },
    fetchData(){
        this.page += 1;
        axios.get("http://localhost:8080/api/video/views",{
          params: {page: this.page,size: this.size},
        })
          .then((res) => {
            if (res.data.length >= 1) {
              res.data.forEach(item => this.list.push(item));
            } else {
              this.page -=1;
            }
          })
      }, 
  },
  mounted() {
    window.addEventListener("scroll",() => {
        let scrollTop=document.documentElement.scrollTop;
        let scrollHeight=document.documentElement.scrollHeight;
        let clientHeight=document.documentElement.clientHeight;
        if(scrollTop+clientHeight>=scrollHeight-10) {
          this.fetchData();
        }
      })
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
    this.fetchData();
  }
}
</script>

<style scoped>
@import '@/styles/btn-style.css';

.circle {
  width: 80px;
  height: 80px;
  background-color: #000000;
  border-radius: 50%;
  /* border: 2px solid black; */
}
.initial{
  color: white;
  font-weight: bolder;
  font-size: 3pc;
  text-align: center;
}
.name{
  color: black;
  font-weight: bold;
  font-size: 1pc;
}
.container{
  display: flex;
  flex-direction: column;
  align-items: center;
}
.line-below-name{
  width: 90%;
  margin: 10px 0;
  border: 1px solid #dcdcdc;
}.card-container{
   width: 100%;
   overflow: hidden;
 }
.vid{
  height: 25pc;
  width: 100%;
  overflow: hidden;
  background-color: #000000;
}
.description{
  background-color: #ffffff;
  padding: 1pc 1pc 0pc 1pc ;
}
.txt-card{
  color: white;
}
.line {
  color: black;
  font-size: 14px;
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
.vid::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 25%;
  bottom: 2.9pc;
  left: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.12), rgba(0, 0, 0, 0)); /* Fade from black to transparent */
  z-index: 1;
  pointer-events: none;
}
#remove{
  position: absolute;
  top: 7px;
  right: 7px;
}
.preview{
  height: 25pc;
  width: 100%;
  object-fit: cover;
}

</style>
