<template>
  <b><span class="animate-charcter">Home</span></b>
  <div>
    <h3>Choose an image</h3>
    <div>
      <select v-model="selectedId" @change="showImage">
        <option value="-1" disabled selected>Select an image</option>
        <option v-for="image in imageList" :value="image.id" :key="image.id">{{ image.name }}</option>
      </select>
    </div>
    <div v-if="selectedImage">
      <h3 for="filter-select">Filter:</h3>
      <select id="filter-select" v-model="selectedFilter" @change="applyFilter">
        <option value="">Select a filter</option>
        <option value="HistoEqualisation">Histogram Equalization</option>
        <option value="Sobel">Sobel</option>
      </select>
    </div>
    <div v-if="selectedImage">
      <h3>{{ selectedImage.name }}</h3>
      <div :id="'gallery-' + selectedImage.id">
        <img :src="'/images/' + selectedImage.id" alt="Selected image">
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue';
import router from '@/router';
import { api } from '@/http-api';
import { ImageType } from '@/image';
import axios from 'axios';

interface SelectedImage {
  id: number;
  name: string;
}

export default {
  name: 'ImageGallery',
  setup() {
    const selectedId = ref(-1);
    const selectedImage = ref<SelectedImage | null>(null);
    const imageList = ref<ImageType[]>([]);
    const selectedFilter = ref('');

    getImageList();

    function getImageList() {
      api.getImageList()
        .then((data: ImageType[]) => {
          imageList.value = data;
        })
        .catch((e: Error) => {
          console.log(e.message);
        });
    }

    function showImage() {
      const image = imageList.value.find((image) => image.id === selectedId.value);
      if (image) {
        selectedImage.value = { id: image.id, name: image.name };
      }
    }

    function applyFilter() {
      if (selectedFilter.value !== '') {
        const algorithm = selectedFilter.value;
        axios
          .get(`/images/${selectedImage.value?.id}`, {
            params: {
              algorithm,
            },
            responseType: 'blob',
          })
          .then((response) => {
            const reader = new window.FileReader();
            reader.readAsDataURL(response.data);
            reader.onload = () => {
              const galleryElt = document.getElementById(`gallery-${selectedImage.value?.id}`);
              if (galleryElt) {
                const imgElt = galleryElt.querySelector('img');
                if (imgElt && reader.result) {
                  imgElt.setAttribute('src', reader.result as string);
                }
              }
              router.push({ name: 'image', params: { id: selectedId.value.toString() } });
            };
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }

    return {
      selectedId,
      selectedImage,
      imageList,
      selectedFilter,
      showImage,
      applyFilter,
    };
  },
};
</script>

<style scoped>

.animate-charcter
{
   text-transform: uppercase;
  background-image: linear-gradient(
    -225deg,
    #231557 0%,
    #44107a 29%,
    #ff1361 67%,
    #fff800 100%
  );
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
