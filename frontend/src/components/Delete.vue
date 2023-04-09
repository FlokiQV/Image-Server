<template>
  <b><span class="animate-charcter">Delete</span></b>
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

// Appeler la fonction pour récupérer la liste d'images 
getImageList();

// Fonction pour récupérer la liste d'images via l'API
function getImageList() {
  api.getImageList()
    .then((data) => {
      imageList.value = data;
    })
    .catch((e) => {
      console.log(e.message);
    });
}

// Fonction pour supprimer l'image sélectionnée via l'API
function deleteImage() {
  if (selectedId.value !== -1) {
    api.deleteImage(selectedId.value)
      .then(() => {
         // Réinitialiser l'id de l'image sélectionnée et récupérer la nouvelle liste d'images
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
