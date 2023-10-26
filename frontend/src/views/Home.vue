<template>
  <Navbar />
  <div style="margin: 2pc;">
    <v-row>
      <v-col v-for="(video,id) in list" :key="id" cols="12" sm="6" md="3">
        <v-card class="card-container" @click="redirect(video.video,id)">
          <div class="vid" >
            <img :src="video.preview" style="width: 100%;height: 100%;" class="preview" >
            <v-row style="position: relative; left: 1.5pc; bottom: 1.5pc;z-index: 2;">
              <i class="fa fa-play" style="color: white; margin-right: 10px;"></i>
              <p class="txt-card" style="font-size: 15px; position: absolute; left: 18px; bottom: -3px">
                 {{ video.views }} {{ video.views <= 1 ? 'view' : 'views' }}
              </p>            
            </v-row>
          </div>
          <div class="description" style="z-index: 2;">
            <div class="line">{{ video.caption }}</div>
            <div class="line">@{{ video.username }}</div>
          </div>
        </v-card>

      </v-col>
    </v-row>
  </div>
</template>

<script>
  import Navbar from '@/components/Navbar.vue'
  import axios from 'axios';
  import { isJwtExpired } from 'jwt-check-expiration';
  export default {
    computed: {
    },
    components: {Navbar},
    data(){
      return{
        list: [],
        page:-1,
        size:12,
      }
    },
    methods:{
      redirect(Filename,Id){
        // type = "views" "latest"     path: '/play:filename:type:id:',
        localStorage.setItem('type', 'views')
        localStorage.setItem('id', Id)
        localStorage.setItem('filename', Filename)
        this.$router.push({ name: 'play'})
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
            console.log("fetch: " + this.list);
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
        this.fetchData();
        form.append("username", localStorage.getItem("username"))
      } else {
        
        localStorage.removeItem('token')
        axios.defaults.headers.common['Authorization'] = null;
        this.$router.push({ name: 'welcome'})
      }
      // VideoSimpleRecord(String video, String preview, String caption, Integer views, String username) 
      
    },
  }
</script>

<style scoped>
@import '@/styles/btn-style.css';

.card-container{
  width: 100%;
  /* height: 100pc; */
  overflow: hidden;
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
.vid{
  height: 25pc;
  width: 100%;
  overflow: hidden;
  background-color: #000000;
}
.vid::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 25%;
  bottom: 4.75pc;
  left: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.12), rgba(0, 0, 0, 0)); /* Fade from black to transparent */
  z-index: 1;
  pointer-events: none;
}
.preview{
  height: 25pc;
  width: 100%;
  object-fit: cover;
}

</style>
