<template>
  <div>
    <h3>Gallery</h3>
    <div class="gallery" v-if="imageList.length > 0">
      <Image v-for="image in imageList" :key="image.id" :id="image.id" />
    </div>
    <p  v-else-if="error">{{ error }}</p>
    <p class= "error"  v-else>le dossier images n'existe pas</p>
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
</script>

<style scoped>
</style>
