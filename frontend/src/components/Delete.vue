<template>
  <div>
    <h3>Delete an image</h3>
    <div>
      <select v-model="selectedId">
        <option v-for="image in imageList" :value="image.id" :key="image.id">{{ image.id }}</option>
      </select>
    </div>
    <br><br>
    <div>
      <button @click="deleteImage">Delete</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { api } from '@/http-api';
import { ImageType } from '@/image';

const selectedId = ref<number>(-1);
const imageList = ref<ImageType[]>([]);

getImageList();

function getImageList() {
  api.getImageList()
    .then((data) => {
      imageList.value = data;
    })
    .catch((e) => {
      console.log(e.message);
    });
}

function deleteImage() {
  if (selectedId.value !== -1) {
    api.deleteImage(selectedId.value)
      .then(() => {
        selectedId.value = -1;
        getImageList();
      })
      .catch((e) => {
        console.log(e.message);
      });
  }
}
</script>

<style scoped>
</style>
