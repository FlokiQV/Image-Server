<script setup lang="ts">
import { ref } from 'vue';

const darkMode = ref(false);

function toggleDarkMode() {
  darkMode.value = !darkMode.value;
}
</script>

<template>
  <div id="app" :class="{ 'dark-mode': darkMode }">
    <nav>
      <ul>
        <li>
          <router-link to="/">Home</router-link>
        </li>
        <li>
          <router-link to="/gallery">Gallery</router-link>
        </li>
        <li>
          <router-link to="/upload">Upload</router-link>
        </li>
        <li>
          <router-link to="/delete">Delete</router-link>
        </li>
      </ul>
      <button @click="toggleDarkMode">{{ darkMode ? 'Light' : 'Dark' }} mode</button>
    </nav>

    <br>    <br>
    <div>
      <!-- <h1>PDL - L3</h1> -->
      <router-view />
    </div>
  </div>
</template>

<style>
#app {
  text-align: center;
  font-family: Arial, Helvetica, sans-serif;
  color: #2c3e50;
  transition: background-color 0.5s ease;
}

.dark-mode {
  background-color: #333;
  color: white;
}
.dark-mode h1,
.dark-mode h2,
.dark-mode h3,
.dark-mode h4,
.dark-mode h5,
.dark-mode h6 {
  color: white;
}

.dark-mode input[type='file'] {
  color: rgba(0, 0, 0, 0)
}



ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #333;
}
li {
  float: left;
}
li a {
  display: block;
  color: white;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}

.error {
  color: red;
  font-weight: bold;
  list-style-type: none;
  margin-top: 1em;

}

.gallery {
  display: flex;
  flex-wrap: wrap;
}

.gallery img {
  width: 317px;
  height: 200px;
  margin: 5pt;
  margin-right: 40px;
  float: left;
}

.animate-charcter
{
   text-transform: uppercase;
   background-image: linear-gradient(to right, #2495db, #6341ee);

  background-size: auto auto;
  background-clip: border-box;
  background-size: 200% auto;
  color: #fff;
  background-clip: text;
  text-fill-color: transparent;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: textclip 2s linear infinite;
  display: inline-block;
      font-size: 80px;
}
@keyframes textclip {
  to {
    background-position: 200% center;
  }
}
</style>
