<template>
  <Navbar />
  <div style="margin: 2pc;">
    <v-row>
      <v-col v-for="(card, id) in videos" :key="id" cols="12" sm="6" md="3">
        <v-card class="card-container" @click="redirect(id)">
          <div class="vid">
            <v-row style="position: relative; left: 1.5pc; bottom: 1.5pc;z-index: 2;">
              <i class="fa fa-play" style="color: white; margin-right: 10px;"></i>
              <p class="txt-card" style="font-size: 15px;position: absolute;left: 18px;bottom: -3px">{{ card.views }} views</p>
            </v-row>
          </div>
          <div class="description" style="z-index: 2;">
            <div class="line">caption caption caption caption caption caption caption caption caption</div>
            <div class="line">@{{ card.user }}</div>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
  import Navbar from '@/components/Navbar.vue'
  import {ca} from "vuetify/locale";
  export default {
    computed: {
      ca() {
        return ca
      }
    },
    components: {Navbar},
    data(){
      return{
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
      redirect(id){
        this.$router.push({ name: 'play', params: { id } })
      },
    },
  }
</script>

<style scoped>
@import '@/styles/btn-style.css';

.card-container{
  width: 100%;
  overflow: hidden;
}
.vid{
  padding-top: 100%;
  background-color: #ffffff;
}
.description{
  background-color: #ffffff;
  padding: 1pc 1pc 0pc 1pc ;
}
.txt-card{
  color: white;
}
.card-bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  padding: 1pc;
}
.text-right {
  text-align: right;
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
  bottom: 4.75pc;
  left: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.12), rgba(0, 0, 0, 0)); /* Fade from black to transparent */
  z-index: 1;
  pointer-events: none;
}

</style>
