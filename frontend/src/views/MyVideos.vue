<template>
  <Navbar />
  <div style="margin: 2pc">
    <v-row class="container">
      <div class="circle">
        <p class="initial">{{ this.username[0] || 'U' }}</p>
      </div>
      <p class="name">@{{ this.username || 'Unknown User' }}</p>
      <hr class="line-below-name">
    </v-row>
    <v-row>
      <v-col v-for="(card, id) in videos" :key="id" cols="12" sm="6" md="3">
        <v-card class="card-container" @click="redirect(id)">
          <div class="vid">
            <v-btn @click="remove" id="remove" density="compact" icon="mdi-close" style="color: #fff; background-color:transparent"></v-btn>
            <v-row style="position: relative; left: 1.5pc; bottom: 1.5pc;z-index: 2;">
              <i class="fa fa-play" style="color: white; margin-right: 10px;"></i>
              <p class="txt-card" style="font-size: 15px;position: absolute;left: 18px;bottom: -3px">{{ card.views }} views</p>
            </v-row>
          </div>
          <div class="description" style="z-index: 2;">
            <div class="line">caption caption caption caption caption caption caption caption caption</div>
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
      videos: {"id1":{"caption":"hello","views":2,"user":"mmmummmudmimmmudmimmmudmimmmudmidmi"},
        "id2":{"caption":"hi","views":10,"user":"mimi"},
        "id3":{"caption":"hohoho","views":8,"user":"may"},
        "id4":{"caption":"heyyy","views":2,"user":"mild"},
        "id5":{"caption":"good job","views":13,"user":"mmmudmi"},
        "id6":{"caption":"niceeeeee","views":23,"user":"mmmudmi"},
        "id7":{"caption":"su su","views":20,"user":"mimi"}
      },
    }
  },
  methods:{

  },
  mounted() {

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

.circle {
  width: 80px;
  height: 80px;
  background-color: #000000;
  border-radius: 50%;
  //border: 2px solid black;
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
  padding-top: 100%;
  background-color: #e3e3e3;
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
</style>
