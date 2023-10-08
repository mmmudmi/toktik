<template>
  <nav>
    <v-toolbar class="Navbar">
      <v-toolbar-title>
        <v-row align="start">
          <v-col class="shrink">
            <v-img src="src/images/toktik_wide.png"  width="100"></v-img>
          </v-col>
          <v-col></v-col>
          <v-col>
            <v-btn class="reg-btn">
              My videos
            </v-btn>
          </v-col>
          <v-col>
            <span class="font-weight-light">Todo</span>
            <span>nn</span>
          </v-col>>
        </v-row>

      </v-toolbar-title>
    </v-toolbar>
  </nav>
</template>

<script>
export default {

}
</script>

<style scoped>
.Navbar{
  background-color: #ffffff;
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2);
}
.reg-btn{
  background-color: #ffffff;
  border: 1.5px solid #cecece;
  font-size: 12px;
  padding: 5px;
}
</style>
