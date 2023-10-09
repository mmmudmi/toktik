<template>
  <nav>
    <v-toolbar class="Navbar">
      <v-toolbar-title>
        <v-row align="start">
          <v-col class="shrink">
            <v-img src="src/images/toktik_wide.png"  width="100"></v-img>
          </v-col>
          <v-col align="end">
            <v-btn class="reg-btn"> My videos </v-btn>
            <v-btn class="reg-btn" @click="navigateToUploadPage"> Upload </v-btn>
            <v-btn class="red-btn"> Log out</v-btn>
          </v-col>>
        </v-row>
      </v-toolbar-title>
    </v-toolbar>
  </nav>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    navigateToUploadPage(){ this.$router.push('upload'); }
  }
}
</script>

<style scoped>
@import '@/styles/btn-style.css';

.Navbar{
  background-color: #ffffff;
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2);
}
</style>
