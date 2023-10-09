<template>
  <Navbar />
  <v-row style="height: 60vh;">
    <v-col>
      <form enctype="multipart/form-data">
        <label for="input-file" id="drop-area" >
          <input type="file" accept="video/*" id="input-file" ref="video" @change="selectFile" hidden>
          <div id="img-view">
            <i class="fa fa-upload" style="color: #6ccbd3; font-size:36px"></i>
            <p class="blue-txt">Click here<br>to upload a video</p>
          </div>
        </label>
      </form>
      <div id="vid-container">
        <v-btn @click="remove" id="remove" density="compact" icon="mdi-close" style="display: none;color: #fff; background-color: #EE3457"></v-btn>
        <video id="video-preview" controls style="display: none;"></video>
      </div>

<!--      <div id="thumbnail-container">-->
<!--        <p>Thumbnail:</p>-->
<!--        <img id="thumbnail" src="" alt="Thumbnail">-->
<!--      </div>-->
    </v-col>

    <v-col>
      <div class="fill-in-container">
        <v-form>
          <p style="font-size: 14px">Title</p>
          <v-text-field
            variant="outlined"
            v-model="title"
            label="Title"
            single-line
            :rules="[formRequired]"
            clearable
          ></v-text-field>
          <p style="font-size: 14px">Description (optional)</p>
          <v-textarea
            variant="outlined"
            v-model="description"
            counter
            label="Optional"
            maxlength="120"
            single-line
          ></v-textarea>
          <br />
          <v-row>
            <v-col align="end">
              <v-btn class="reg-btn" @click="navigateToHomePage">
                Cancel
              </v-btn>
              <v-btn
                class="red-btn"
                :loading="loading"
                type="submit"
                :disabled="!uploadRequired"
              >
                Upload
              </v-btn>
            </v-col>
          </v-row>
        </v-form>
      </div>
    </v-col>
  </v-row>

</template>

<script>
import Navbar from '@/components/Navbar.vue'
export default {
  components: {Navbar},
  data(){
    return{
      video: null,
      thumbnail: null,
      title: null,
      description: null,
      formRequired: value => !!value || 'Field is required',
    }
  },
  computed: {
    uploadRequired() {
      return this.video !== null && this.title !== null;
    },
  },
  methods: {
    remove(){
      const videoElement = document.getElementById('video-preview');
      videoElement.pause();
      videoElement.style.display = 'none';
      document.getElementById('remove').style.display='none';
      document.getElementById('vid-container').style.display='none';
      document.getElementById('drop-area').style.display='block';
      document.getElementById('video-preview').style.display = 'block';
      document.getElementById('input-file').value = '';
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
            document.getElementById('input-file').value = '';
            alert('Please select a video that is at least 1 second long.');
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
    checkVideo(file) {
      const videoElement = document.createElement('video');
      videoElement.src = URL.createObjectURL(file);

      videoElement.onloadedmetadata = () => {
        const durationInSeconds = videoElement.duration;

        if (durationInSeconds > 1) {
          // Set the video in the Vue data
          this.video = URL.createObjectURL(file);

          // Set the thumbnail in the Vue data
          this.thumbnail = URL.createObjectURL(file);
        } else {
          alert("The selected video must be longer than 1 second.");
        }

        // Clean up
        videoElement.remove();
      };
    },
    async uploadVideo(){
      if (!this.video) {
        alert('Please select a video to upload.');
        return;
      }
      try {
        const formData = new FormData();
        formData.append('video', this.video)
        formData.append('title', this.title)
        formData.append('description', this.description)
        // const response = await axios.post('/api/upload-video', formData, {
        //   headers: {
        //     'Content-Type': 'multipart/form-data'
        //   }
        // });
      } catch (error) {
        console.error('Error uploading video:', error);
      }
    }

  }
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
#video-preview{
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

</style>

