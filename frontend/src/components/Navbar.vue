<template>
  <nav>
    <v-toolbar class="Navbar">
      <v-toolbar-title>
        <v-row align="start">
          <v-col class="shrink">
            <a href="/home">
              <v-img src="https://scalable.ap-south-1.linodeobjects.com/toktik_wide.png" style="cursor: pointer;margin-left:1pc" width="100" ></v-img>
            </a>
          </v-col>
          <v-col align="end">
              <v-btn class="icon-button" @click="triggerNoti">
                <span class="material-icons">notifications</span>
                <span class="icon-button__badge" v-if="unreadCount > 0">
                  {{ unreadCount }}
                </span>
              </v-btn>

            <a href="/myVideos" style="color: black;">
              <v-btn class="reg-btn" style="width: 6.2pc"> My videos </v-btn>
            </a>
            <a href="/upload" style="color: black;">
            <v-btn class="reg-btn" style="width: 6.2pc" @click="navigateToUploadPage">
              <i class="fa fa-plus" style="font-size:12px; padding-right: 5px"></i>
              Upload
            </v-btn>
            </a>
            <v-btn class="red-btn" style="width: 6.2pc" @click="logout"> Log out</v-btn>

          </v-col>>
        </v-row>
      </v-toolbar-title>
    </v-toolbar>
  </nav>
  <div class="notification_dd" id="notification_dd" style="display: none;">
    <p style="font-weight: 600;padding: 1pc 1pc 0.5pc 1pc;">Notifications</p>
    <v-col v-for="(noti,id) in this.notifications" :key="id">
            <div class="noti-container" @click="navigateToVideo(noti.video.video)">
              <div class="preview_container">
                <div v-if="!noti.is_read" style="background: #EE3457;width: 7px;height: 7px;border-radius: 50%;position: absolute;right: 10px;"></div>
                <img :src="noti.video.preview" style="width: 100%;height: 100%;" class="preview" >
              </div>
              <div style="margin: 1px 0 0 9px;">
                <div style="display: flex;flex-direction: row;">
                <!-- <div class="circle" style="width: 25px;min-width: 25px;height: 25px;min-height: 25px;background-color: #414141;">
                  <p class="initial" style="font-size: 0.9pc;font-weight: 600;">{{ noti.user[0] || 'U' }}</p>
                </div> -->
              </div>
                <p class="line" style="font-weight:400;font-size: 13px;white-space: normal;" >{{ noti.notification }}</p>
              </div>              
            </div>
    </v-col>
  </div>
  
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      noti_page: 0,
      noti_size: 6,
      is_noti_on: 0,
      unreadCount: 0,
      notifications: [],
    };
  },
  created() {
    this.getNotification()
  },
  methods: {
    countUnreadNoti(){
      return this.notifications.reduce((count, notification) => {
      if (!notification.is_read) {
        count++;
      }
      return count;
      }, 0);
    },
    getNotification(){
      axios.get("http://127.0.0.1:8080/api/u/notification",{
        params: {page: this.noti_page, size: this.noti_size},
      })
      .then((res) => {
        // NotificationRecord(VideoSlimRecord video, String notification, Boolean is_read, LocalDateTime created)
        // VideoSlimRecord(Long id, String video, String preview, String caption, LocalDateTime created)
            if (res.data.length >= 1) {
              res.data.forEach(item => {
                this.notifications.push(item)
              })
              this.unreadCount = this.countUnreadNoti();
              console.log(this.unreadCount)
            } else {
              this.noti_page -=1;
            }
          })
    },
    readAllNoti(){
      axios.post("http://127.0.0.1:8080/api/u/notification/read")
      .then((res) => {
        console.log(res.data.message)
      })
    },
    navigateToVideo(Filename){
      this.$router.push({ name: 'play', params: {"video": Filename}})
    },
    triggerNoti(){
      if(this.is_noti_on == 1){
        this.is_noti_on=0
        document.getElementById('notification_dd').style.display='none';
        // this.readAllNoti();
      } else{
        this.is_noti_on=1
        document.getElementById('notification_dd').style.display='block';
      }
    },
    navigateToUploadPage(){ this.$router.push('upload'); },
    navigateToHomePage(){ this.$router.push('home'); },
    navigateToMyVideosPage(){ this.$router.push('myVideos'); },
    logout(){
      console.log('exit')
      axios.get("http://127.0.0.1:8080/api/auth/logout")
        .then((res) => {
          let data = res.data
          if (data.success) {
            this.$router.push({ name: 'welcome' })
            alert(data.message)
          } else {
            alert(data.message)
          }
        }).catch(
        err => {
          console.log(err)
        })
    },
  },
  
}
</script>

<style scoped>
@import '@/styles/btn-style.css';

.Navbar{
  background-color: #ffffff;
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2);
  z-index: 10;
}
.icon-button {
  position: relative;
  align-items: center;
  justify-content: center;
  width: 10px;
  min-width: 10px;
  height: 40px;
  color: #333333;
  /* background: #ffdada; */
  border: 1.5px solid #dcdcdc00;
  outline: none;
  border-radius: 50%;
  margin-right: 13px;
}

.icon-button:hover {
  cursor: pointer;
}

.icon-button:active {
  background: #cccccc;
}

.icon-button__badge {
  position: absolute;
  top: 0px;
  font-size: 9px;
  right: -11px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  font-weight: 600;
  background: #EE3457;
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.notification_dd{
  position: absolute;
  top: 3.6pc;
  right: 8pc;
  width: 16pc;
  height: 53vh;
  border-radius: 5px;
  background-color: #ffffff;
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.3);
  overflow: scroll;
  z-index: 5;
  transition: height 0.3s ease;
}
/* .notification_dd:before{
    content: "";
    position: absolute;
    top: -10px;
    right: 15px;
    border: 10px solid;
    z-index: 12;
    border-color: transparent transparent #000000 transparent;
} */
.noti-container{
  width: 100%;
  border-radius: 4px;
  padding: 6px 0 0px 0;
  display: flex;
  overflow: scroll;
  cursor: pointer;
}
.noti-container:hover{
  background-color: #f9f9f9;
}
.line {
  font-size: 15px;
  color: rgb(0, 0, 0);
  font-weight: 500;
  white-space: nowrap;
  direction: ltr;
  line-height: 1.5;
  pointer-events: auto;
  overflow: scroll;
}
.line::-webkit-scrollbar {
  width: 0; /* Hide scrollbar in Webkit browsers */
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
.preview{
  /* height: 25pc;
  width: 100%; */
  object-fit: cover;
}
.preview_container{
  height: 3pc;
  min-height: 3pc;
  width: 3pc;
  min-width: 3pc;
}
</style>
