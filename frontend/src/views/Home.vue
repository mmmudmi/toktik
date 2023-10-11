<template>
  <Navbar />
  <div style="margin: 2pc;">
    <v-row>
      <v-col v-for="(card, id) in videos" :key="id" cols="12" sm="6" md="3">
        <v-card class="vid-card" @click="redirect(id)">
          <v-row>
            <h3 class="txt-card">{{ card.title }}</h3>
          </v-row>
          <v-row class="card-bottom">
            <v-col style="text-align: left">
              <h5 class="txt-card">{{ card.user }}</h5>
            </v-col>
            <v-col style="text-align: right">
              <h5 class="txt-card">{{ card.views }} views</h5>
            </v-col>
          </v-row>
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
        videos: {"id1":{"title":"hello","views":2,"user":"mmmummmudmimmmudmimmmudmimmmudmidmi"},
          "id2":{"title":"hi","views":10,"user":"mimi"},
          "id3":{"title":"hohoho","views":8,"user":"may"},
          "id4":{"title":"heyyy","views":2,"user":"mild"},
          "id5":{"title":"good job","views":13,"user":"mmmudmi"},
          "id6":{"title":"niceeeeee","views":23,"user":"mmmudmi"},
          "id7":{"title":"su su","views":20,"user":"mimi"}
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
.vid-card{
  width: 100%;
  height: 30pc;
  padding: 1.5pc;
  overflow: hidden;
}
.txt-card{
  color: white;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.4);
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
</style>
