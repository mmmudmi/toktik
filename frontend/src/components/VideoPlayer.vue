<template>
  <div>
    <video 
    :autoplay="autoplay"
    :width="width"
    :height="height"
    ref="player" 
    class="video-js">
      <slot
      name="controls"
      :play="play"
      :pause="pause"
      :preload="preload"
      :toggle-play="togglePlay"
      :playing="playing"
    ></slot>
    </video>
  </div>
</template>

<script>
import videojs from 'video.js';
const EVENTS = [
"play",
  "pause",
  "ended",
  "loadeddata",
  "waiting",
  "playing",
  "timeupdate",
  "canplay",
  "canplaythrough",
  "statechanged",
]
export default {
  name: 'VideoPlayer',
  props: {
    autoplay: { type: Boolean, required: false, default: true },
    loop: { type: Boolean, required: false, default: true },
    preload: { type: String, required: false, default: "auto" },

    options: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  data() {
    return {
      player: null
    }
  },
  mounted() {
    this.player = videojs(this.$refs.player, this.options, () => {
      this.player.log('onPlayerReady', this);
    });
  },
  beforeDestroy() {
    if (this.player) {
      this.player.dispose();
    }
  },
  methods: {
    play() {
      this.$refs.player.play();
      this.setPlaying(true);
    },

    pause() {
      this.$refs.player.pause();
      this.setPlaying(false);
    },

    togglePlay() {
      if (this.playing) {
        this.pause();
      } else {
        this.play();
      }
    },

    setPlaying(state) {
      this.playing = state;
    },

    seekToPercentage(percentage) {
      this.$refs.player.currentTime = (percentage / 100) * this.duration;
    },

    convertTimeToDuration(seconds) {
      return [parseInt((seconds / 60) % 60, 10), parseInt(seconds % 60, 10)]
        .join(":")
        .replace(/\b(\d)\b/g, "0$1");
    },


  },
}
</script>
<style>
.video-js {
    position:inherit;
    background-color:rgb(255, 255, 255);
}
</style>