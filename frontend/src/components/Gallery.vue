<template>
  <div>
    <b>
    <span class="animate-charcter">Gallery</span>
  </b>
<div class="button-container">
  <button class="download-all" @click="downloadAllImages">Download All</button>
</div>

<div class="gallery" v-if="imageList.length > 0">
  <div v-for="image in imageList" :key="image.id" class="image-container">
    <Image :id="image.id" />
    <button class="download-image" @click="downloadImage(image)">Download</button>
  </div>
</div>

<p class="error-message" v-if="error">{{ error }}</p>
<p class="no-images-message" v-else>There are no images to display.</p>
</div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { api } from '@/http-api';
import { ImageType } from '@/image'
import Image from './Image.vue';

const imageList = ref<ImageType[]>([]);
const error = ref<string | null>(null);

api.getImageList()
  .then((data) => {
    imageList.value = data;
  })
  .catch(e => {
    error.value = "Failed to load images. Please try again later.";
    console.error(e);
  });
function downloadAllImages() {
  for (const image of imageList.value) {
    const link = document.createElement('a');
    link.href = `/images/${image.id}`;
    link.download = image.name;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
function downloadImage(image: ImageType) {
  const link = document.createElement('a');
  link.href = `/images/${image.id}`;
  link.download = image.name;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}
</script>
<style scoped>
.title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
}
.button-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}
.download-all,
.download-image {
  background-color: #5E5DF0;
  color: #FFFFFF;
  border-radius: 999px;
  font-family: Inter, sans-serif;
  font-size: 16px;
  font-weight: 700;
  line-height: 24px;
  padding: 8px 18px;
  cursor: pointer;
  box-shadow: 0 5px 15px -5px rgba(0, 0, 0, 0.2);
  transition: all 0.2s ease-in-out;
  margin: 0 10px;
  border: none;
}
.download-all:hover,
.download-image:hover {
  background-color: #4544d8;
  box-shadow: 0 5px 15px -5px rgba(0, 0, 0, 0.4);
}
.gallery {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
.image-container {
  margin: 20px;
  text-align: center;
}
.error-message,
.no-images-message {
  text-align: center;
  font-size: 16px;
  margin-top: 40px;
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