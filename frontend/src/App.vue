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
      <button id="myButton" @click="toggleDarkMode">{{ darkMode ? 'Light' : 'Dark' }} mode</button>
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
.dark-mode #labelw {
  color: white;
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
.imghome{
  width: 817px;
  height: 500px;}

#myButton {
  --glow-color: rgb(217, 176, 255);
  --glow-spread-color: rgba(191, 123, 255, 0.5); /* reduced alpha channel */
  --enhanced-glow-color: rgb(231, 206, 255);
  --btn-color: rgb(100, 61, 136);
  border: .25em solid var(--glow-color);
  padding: 1em 3em;
  color: var(--glow-color);
  font-size: 15px;
  font-weight: bold;
  background-color: var(--btn-color);
  border-radius: 1em;
  outline: none;
  box-shadow: 0 0 1em .25em var(--glow-color),
    0 0 4em 1em var(--glow-spread-color),
    inset 0 0 .75em .25em var(--glow-color);
  text-shadow: 0 0 .5em var(--glow-color);
  position: relative;
  transition: all 0.3s;
}

#myButton::after {
 pointer-events: none;
 content: "";
 position: absolute;
 top: 120%;
 left: 0;
 height: 100%;
 width: 100%;
 background-color: var(--glow-spread-color);
 filter: blur(2em);
 opacity: .7;
 transform: perspective(1.5em) rotateX(35deg) scale(1, .6);
}

#myButton:hover {
 color: var(--btn-color);
 background-color: var(--glow-color);
 box-shadow: 0 0 1em .25em var(--glow-color),
        0 0 4em 2em var(--glow-spread-color),
        inset 0 0 .75em .25em var(--glow-color);
}

#myButton:active {
 box-shadow: 0 0 0.6em .25em var(--glow-color),
        0 0 2.5em 2em var(--glow-spread-color),
        inset 0 0 .5em .25em var(--glow-color);

}



.select-box {
  position: relative;
  display: block;
  width: 15%;
  margin: 0 auto;
  font-family: 'Open Sans', 'Helvetica Neue', 'Segoe UI', 'Calibri', 'Arial', sans-serif;
  font-size: 18px;
  color: #60666d;
  }
  
  

@keyframes HideList {
  from {
    transform: scaleY(1);
  }
  to {
    transform: scaleY(0);
  }
}

#applybutton {
  text-decoration: none;
  position: relative;
  border: none;
  font-size: 14px;
  font-family: inherit;
  color: #fff;
  width: 7%;
  height: 12%;
  line-height: 2em;
  text-align: center;
  background: linear-gradient(90deg,#03a9f4,#f441a5,#ffeb3b,#03a9f4);
  background-size: 300%;
  border-radius: 30px;
  z-index: 1;
}

#applybutton:hover {
  animation: ani 8s linear infinite;
  border: none;
}

@keyframes ani {
  0% {
    background-position: 0%;
  }

  100% {
    background-position: 400%;
  }
}

#applybutton:before {
  content: '';
  position: absolute;
  top: -5px;
  left: -5px;
  right: -5px;
  bottom: -5px;
  z-index: -1;
  background: linear-gradient(90deg,#03a9f4,#f441a5,#ffeb3b,#03a9f4);
  background-size: 400%;
  border-radius: 35px;
  transition: 1s;
}

#applybutton:hover::before {
  filter: blur(20px);
}

#applybutton:active {
  background: linear-gradient(32deg,#03a9f4,#f441a5,#ffeb3b,#03a9f4);
}

.Home #applybutton{
  margin-left: 200px;
}


</style>
