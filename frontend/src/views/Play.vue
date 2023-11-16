<template>
  <Navbar />
  <div class="toktik-page">
    <div v-if="this.username==null">
      <PageLoader />
    </div>
    <v-row no-gutters v-else>
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
            <v-row justify="end" style="padding: 0 20px 0 40px; ">
              <i class="fa fa-play" style="color: rgb(0, 0, 0); margin-right: 8px;position: relative; top: 3px;"></i>
              <p class="txt-card" style="font-size: 15px;">
                 {{ this.views }} {{ this.views <= 1 ? 'view' : 'views' }}
              </p>            
            </v-row>
            <!-- <span class="left"></span> 
            <span class="right"></span>  -->
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
import PageLoader from '@/components/PageLoader.vue';
import { Client } from '@stomp/stompjs';



export default {
  name: "Watch",
  components: {Navbar,VideoPlayer,PageLoader},
  data(){
    return{
      stompClient: null,
      client: null,
      connected: false,
      player: null,
      video: this.$route.params.video,
      caption: "",
      username: null,
      comment: null,
      views: 0,
      like_count: 0,
      is_like: false,
      comments: [],
      comment_count: 0,
      videoOptions: {
        autoplay: true,
        controls: true,
        loop: true,
        sources: [
          {
            src: "/api/video/playlist/"+this.$route.params.video,
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
      axios.get("/api/video/detail/"+this.$route.params.video)
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
        axios.get("/api/video/like/"+this.$route.params.video)
        .then((res)=>{
          this.is_like = false;
          likeBtn.style.color = 'rgb(229, 229, 229)';
          console.log(res.data.message)
        })
      } else {
        axios.get("/api/video/like/"+this.$route.params.video)
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
      axios.post("/api/video/comment", form)
      this.comment = null;
    },
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
      // this.connect();
    } else {
      localStorage.removeItem('token')
      axios.defaults.headers.common['Authorization'] = null;
      this.$router.push({ name: 'welcome'})
    }
  },
  mounted() {
    console.log('Component did mount');
    // Client is imported from '@stomp/stompjs'
    this.client = new Client();
    this.client.configure({
      brokerURL: 'ws://localhost:7887/api/socket',
      onConnect: () => {
        console.log('Play onConnect');

        this.client.subscribe('/sub/likes/'+this.video, frame => {
          const count = parseInt(frame.body);
          console.log("update like count: ", count);
          this.like_count = count;
        });
        //CommentRecord(String username, String comment, LocalDateTime created)
        this.client.subscribe('/sub/comment/'+this.video, frame => {
          const record = frame.body;
          console.log("added comment: ", record);
          this.comments.push(JSON.parse(record));
        });
        this.client.subscribe('/sub/views/'+this.video, frame => {
          const count = parseInt(frame.body);
          console.log("old view count: ", this.views);
          console.log("update view count: ", count);
          this.views = count;
        });
      },
      // Helps during debugging, remove in production
      debug: (str) => {
        console.log(new Date(), str);
      }
    });

    this.client.activate();
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
.previous {
  background-color: #f1f1f1;
  color: black;
}

.next {
  background-color: #f1f1f1;
  color: black;
}

.right{
  cursor: pointer;
  position:relative;
  display:block;
  margin: 100px 0 0 100px;
  width:35px;
  height: 35px;
  border: solid 3px #999;
  border-radius: 100%;
  z-index: 1;
  transition: all .2s linear;
  &:before, &:after{
    content:"";
    position: absolute;
    width:35%;
    height: 10%;
    top:41%;
    left:55%;
    background: #999;
    z-index: 2;
    transform: translate(-50%, -50%) rotate(45deg);
    transition: all .2s linear;
  }
  &:after{
    z-index: 3;
    top:59%;
    left:55%;
    transform: translate(-50%, -50%) rotate(-45deg);
  }
  &:hover{
    border: solid 4px #777;
    &:after, &:before{
      background: #777;
    }
  }
  &:active{
    border: solid 5px #111;
    &:after, &:before{
      background: #111;
    }
  }
}

.left{
  cursor: pointer;
  position:relative;
  display:block;
  margin: 100px 0 0 100px;
  width:35px;
  height: 35px;
  border: solid 3px #999;
  border-radius: 100%;
  z-index: 1;
  transition: all .2s linear;
  &:before, &:after{
    content:"";
    position: absolute;
    width:35%;
    height: 10%;
    top:41%;
    left:48%;
    background: #999;
    z-index: 2;
    transform: translate(-50%, -50%) rotate(135deg);
    transition: all .2s linear;
  }
  &:after{
    z-index: 3;
    top:59%;
    left:48%;
    transform: translate(-50%, -50%) rotate(-135deg);
  }
  &:hover{
    border: solid 4px #777;
    &:after, &:before{
      background: #777;
    }
  }
  &:active{
    border: solid 5px #111;
    &:after, &:before{
      background: #111;
    }
  }
}


</style>