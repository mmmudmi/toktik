<template>
  <Navbar />
  <div id="myProgress" style="display:none;">      
          <div id="myBar"></div>    
  </div>  
  <v-row style="height: 60vh;">
    <v-col>
      <form enctype="multipart/form-data">
        <label for="input-file" id="drop-area" >
          <input type="file" accept="video/*" id="input-file" ref="video" @change="selectFile" hidden>
          <div id="img-view">
            <i class="fa fa-upload" style="color: #6ccbd3; font-size:36px"></i>
            <p class="blue-txt">Click here<br>to upload a video</p>
            <p class="blue-txt" style="font-size: 13px;">no more than 1 minute</p>
          </div>
        </label>
      </form>
      <div id="vid-container">
        <v-btn @click="remove" id="remove" density="compact" icon="mdi-close" style="display: none;color: #fff; background-color: #EE3457"></v-btn>
        <video id="video-preview" controls style="display: none;"></video>
      </div>
    </v-col>
    <v-col>
      <div class="fill-in-container">
        <v-form>
          <p style="font-size: 14px">Caption</p>
          <v-text-field
            variant="outlined"
            v-model="title"
            label="caption"
            counter
            maxlength="100"
            single-line
            :rules="[formRequired]"
            clearable
          ></v-text-field>
          <br />
          <v-row>
            <v-col align="end">
              <v-btn class="reg-btn" @click="navigateToHomePage">
                Cancel
              </v-btn>
              <v-btn
                class="red-btn"
                :disabled="!uploadRequired"
                @click="uploadVideo"
              >
                Upload
              </v-btn>
            </v-col>
          </v-row>
        </v-form>
        <!-- <br> -->
        <!-- <div id="myProgress" >
          <div id="myBar" style="width: {{ this.uploadProgress }}%;">
            {{ this.uploadProgress }}%
          </div>
        </div> -->
      </div>
    </v-col>
  </v-row>

</template>

<script>
import axios from 'axios'
import Navbar from '@/components/Navbar.vue'
import { isJwtExpired } from 'jwt-check-expiration';

export default {
  name: "Upload",
  components: {Navbar},
  data(){
    return{
      video: '',
      title: null,
      formRequired: value => !!value || 'Field is required',
      uploadProgress: 0,
    }
  },
  computed: {
    uploadRequired() {
      return this.video !== null && this.title !== null;
    },
  },
  methods: {
    uploadVideoToS3() {
      const filename = this.video.name;
      const fileExtension = filename.slice(((filename.lastIndexOf(".") - 1) >>> 0) + 2);
      axios.get('http://127.0.0.1:8080/api/video/upload-url/' + fileExtension)
        .then((res) => {
          let data = res.data;
          this.extURL = data.message;
          // Once this.extURL is set, call putVideo
          this.putFileToS3();
        })
    },
    async putFileToS3() {
      const file = this.video;
      const PUTEndpoint = this.extURL;
      const FileName = PUTEndpoint.split("/").pop().split("?")[0]; 
      const options = {
        headers: { 
          'Content-Type': file.type,
          'x-amz-acl': "public-read",
          Authorization: "",
        },
        onUploadProgress: (progressEvent) => {
        this.uploadProgress = Math.round(
          (progressEvent.loaded / progressEvent.total) * 100
        );
      },
      };
      
      try {
        const response = await axios.put(PUTEndpoint, file,options);
        this.uploadProgress = 100;
        this.putFileToBackend(FileName);
      } catch (error) {
        console.log(error);
      }
    },
    putFileToBackend(FileName){
      const form = new FormData();
      form.append('filename', FileName)
      form.append('caption', this.title)
      axios.post("http://127.0.0.1:8080/api/video/submit", form)
        .then((res) => {
          let data = res.data
          if (data.success) {
          } else {
            alert(data.message)
          }
        }).catch(
            err => { console.log(err)})
    },
    remove(){
      const videoElement = document.getElementById('video-preview');
      videoElement.pause();
      videoElement.style.display = 'none';
      document.getElementById('remove').style.display='none';
      document.getElementById('vid-container').style.display='none';
      document.getElementById('drop-area').style.display='block';
      document.getElementById('video-preview').style.display = 'block';
      document.getElementById('input-file').value = '';
      this.thumbnail = null;
      this.video = null;
    },
    selectFile(){
      const selectedFile = event.target.files[0];
      if (selectedFile) {
        const videoElement = document.createElement('video');
        videoElement.preload = 'metadata';

        videoElement.onloadedmetadata = () => {
          const duration = videoElement.duration;
          if (duration < 1) {
            this.remove();
            document.getElementById('input-file').value = '';
            alert('Please select a video that is at least 1 second long.');
          } else if (duration > 60) {
            this.remove();
            document.getElementById('input-file').value = '';
            alert('Please select a video that is no more than 1 minute long.');
          }
        };
        videoElement.src = URL.createObjectURL(selectedFile);
      }
      this.video = this.$refs.video.files[0];
      if (this.video){
        const videoElement = document.getElementById('video-preview');
        videoElement.style.display = 'block';
        const videoURL = URL.createObjectURL(selectedFile);
        videoElement.src = videoURL;
        document.getElementById('vid-container').style.display='block';
        document.getElementById('drop-area').style.display='none';
        document.getElementById('remove').style.display='block';
      }
    },
    navigateToHomePage(){ this.$router.push('home'); },
    navigateToMyVideos(){ 
      this.$router.push({ path: 'myVideos'});
    },
    async uploadVideo(){
      if (!this.video) {
        alert('Please select a video to upload.');
        return;
      }
      try {
        this.uploadVideoToS3();
        this.navigateToMyVideos();
      } catch (error) {
        console.error('Error uploading video:', error);
      }
    },
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
.fill-in-container{
  padding: 20px;
  margin: 2pc 2pc 2pc 0pc;
  height: 38pc;
}
#drop-area{
  border: 2px dashed #CECECE;
  margin: 2pc;
  padding: 20px;
  height: 38pc;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: relative;
}
.blue-txt{
  color: #6DCBD3;
  font-size: 20px;
  font-weight: 500;
  padding: 0px 0px 10px 0px;
}
#img-view{
  width: 100%;
  border-radius: 10px;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  left: 0;
  right: 0;
  text-align: center;
}

#vid-container{
  display: none;
  margin: 2pc;
  height: 38pc;
  place-items: center;
  overflow: hidden;
  background: black;
  position: relative; /* Add relative positioning */
}
#vid-container video{
  max-width: 100%; /* Ensure the video doesn't exceed the container's width */
  max-height: 100%; /* Ensure the video doesn't exceed the container's height */
  width: auto; /* Allow the video to scale based on its aspect ratio */
  height: auto;
  position: absolute; /* Position the video absolutely */
  top: 50%; /* Center vertically */
  left: 50%; /* Center horizontally */
  transform: translate(-50%, -50%);
}
#remove {
  cursor: pointer;
  top: 5px;
  left: 5px;
  background: #EE3457;
  margin: 0;
  padding: 0;
  z-index: 1;
}
.thumbnail-frame{
  height: 10pc;
  width: 10pc;
  place-items: center;
  overflow: hidden;
  background: black;
  position: relative;
  border-radius: 3px;
}
.thumbnail-frame img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}
#myProgress {
  display: block;
  width: 100%;
  /* background-color: #ededed; */
}

#myBar {
  height: 30px;
  background-color: #7bd5ba;
  text-align: center;
  line-height: 30px;
  color: white;
}
</style>
