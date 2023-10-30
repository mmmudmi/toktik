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
            <div>
              <p class="line" style="font-weight:600;position: relative;padding: 0 0 0 1pc; " >@{{ this.username || 'Unknown User' }}</p>
              <p class="line" style="font-weight:400;position: relative;padding: 0 0 0 1pc; " >{{ this.caption }} </p>
            </div>
          </div>
          <!-- <hr style="width: 100%; margin: 0;border: 0.5px solid #ececec;position: relative;bottom: 0;">  -->
        </div>

        <div  class="comments-container">
          <p v-if="comments.length === 0" style="display: flex;
            align-items: center; 
            justify-content: center; 
            height: 100%;
            color: rgb(211, 211, 211);"
          >No comments</p>

          <div v-if="comments.length !== 0">
          <v-col v-for="(comment,id) in this.sortedComments" :key="id">
            <div class="comment-container">
              <div>
                <div class="circle" style="width: 35px;min-width: 35px;height: 35px;min-height: 35px;background-color: #414141;">
                  <p class="initial" style="font-size: 1.35pc;font-weight: 600;">{{ comment.username[0] || 'U' }}</p>
                </div>
              </div>
              <div style="margin: 1px 0 0 9px; max-width: 100%;">
                <p class="line" style="font-weight:600;font-size: 13px;" >@{{ comment.username }}</p>
                <p class="line" style="font-weight:400;font-size: 13px;white-space: normal;" >{{ comment.comment }}</p>
              </div>
            </div>
          </v-col>
          </div>
        </div >
        <div  class="add-comment-container">
          <div style="display: flex; flex-direction: row;align-items: center;margin-bottom: 10px;">
            <i
              id="like-btn"
              @click="clickLike"
              class="fa fa-heart"
              style="cursor: pointer; font-size: 30px; color:rgb(229, 229, 229);"
              v-if="this.is_like==false"
            ></i>   
            <i
              id="like-btn"
              @click="clickLike"
              class="fa fa-heart"
              style="cursor: pointer; font-size: 30px; color:#EE3457;"
              v-if="this.is_like"
            ></i>          
            <p style="color: rgb(0, 0, 0);margin-left: 8px; font-size: 15px;font-family: Roboto;">
              {{ this.like_count }} {{ this.like_count <= 1 ? 'like' : 'likes' }}
            </p>
          </div>

          <div style="display: flex;">
            <v-text-field
            v-model="comment"
            variant="outlined"
            label="Add comment"
            single-line
          ></v-text-field>
          <v-btn v-if="this.comment" @click="addComment" class="blue-btn" style="height: 55px;background-color: #000000;" > <i class="fa fa-send"></i> </v-btn>
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
      id: localStorage.getItem("id"),
      player: null,
      video: localStorage.getItem("filename"),
      caption: "",
      username: "",
      comment: null,
      views: 0,
      like_count: 0,
      type: localStorage.getItem("type"),
      is_like: false,
      comments: [],
      comment_count: 0,
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
      // Long id, String video, String preview, String caption, Integer views, String username,  Integer like_count, Integer comment_count, Boolean is_like, LocalDateTime created
      axios.get("http://localhost:8080/api/video/detail/"+localStorage.getItem("filename"))
            .then((res) => {
                this.caption = res.data.caption;
                this.views = res.data.views;
                this.username = res.data.username;
                this.like_count = res.data.like_count;
                this.is_like = res.data.is_like;
                this.comment_count = res.data.comment_count;
                this.comments = res.data.comments;
          })
    }, 
    clickLike() {
      var likeBtn = document.getElementById('like-btn');
      if (this.is_like) {
        this.is_like = false;
        axios.get("http://localhost:8080/api/video/like/"+this.video)
        .then((res)=>{
          this.is_like = false;
          likeBtn.style.color = 'rgb(229, 229, 229)';
          console.log(res.data.message)
        })
      } else {
        axios.get("http://localhost:8080/api/video/like/"+this.video)
        .then((res)=>{
          this.is_like = true;
          likeBtn.style.color = 'EE3457';
          console.log(res.data.message)
        })
      }
    },
    addComment(){
      const form = new FormData();
      form.append('comment',this.comment)
      form.append('video',this.video)
      axios.post("http://localhost:8080/api/video/comment", form)
      this.comment = null;
    },
  },
  created(){
    setInterval(() => {
      console.log(this.comments);
      this.fetchData();
	  }, 100)
  
  },
  computed: {
    sortedComments() {
    return this.comments.slice().sort((a, b) => {
      return new Date(a.created) - new Date(b.created);
    });
  },
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
  direction: ltr;
  line-height: 1.5;
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
  min-width: 50px;
  min-height: 50px;
}
.initial{
  color: white;
  font-weight: bolder;
  font-size: 2pc;
  text-align: center;
}
.comments-container{
  box-shadow: inset 0px 0px 4px rgba(0, 0, 0, 0.15);
  margin: 0 1pc 0 1pc;
  height: 70vh;
  overflow-x: auto;
  overflow-y: auto;
  position: relative;
  border-radius: 5px;

}
.add-comment-container{
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
  overflow: scroll;
}

.comment-container{
  width: 100%;
  border-radius: 4px;
  padding: 5px 0 5px 0;
  display: flex;
}

</style>