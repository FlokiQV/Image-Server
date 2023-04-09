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
        <option value="flou">flou</option>
      </select>
      <button @click="applyBlur">Apply</button>

      
      <div v-if="selectedFilter === 'flou'">
        <label for="blurParam">Blur Parameter: </label>
        <input type="number" id="blurParam" v-model="blurParam" min="1" max="10"/>
      </div>
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
import p5 from 'p5';


interface SelectedImage {
  id: number;
  name: string;
}


export default {
  name: 'ImageGallery',
  setup() {
    const selectedId = ref(-1);
    const selectedImage = ref<SelectedImage | null>(null);
    const filteredImage = ref<SelectedImage | null>(null); // add a reference to the filtered image
    const imageList = ref<ImageType[]>([]);
    const selectedFilter = ref('');
    const showFilter = ref(false);
    const blurParam = ref(1);
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
        filteredImage.value = null; // reset filteredImage when selecting a new image
        showFilter.value = false;
      }
    }
    function applyBlur() {
  if (selectedImage.value) {
    axios
      .get(`/images/${selectedImage.value?.id}`, {
        responseType: 'blob',
      })
      .then((response) => {
        const reader = new window.FileReader();
        reader.readAsDataURL(response.data);
        reader.onload = () => {
          const img = new Image();
          img.src = reader.result as string;
          img.onload = () => {
            const canvas = document.createElement('canvas');
            canvas.width = img.width;
            canvas.height = img.height;
            const context = canvas.getContext('2d')!;
            context.filter = `blur(${blurParam.value}px)`;
            context.drawImage(img, 0, 0);
            const base64 = canvas.toDataURL();
            filteredImage.value = { id: selectedImage.value!.id, name: `${selectedImage.value!.name}-blurred` }; // update filteredImage reference
            const galleryElt = filteredImage.value ? document.getElementById(`gallery-${filteredImage.value.id}`) : null;
            if (galleryElt) {
              const imgElt = galleryElt.querySelector('img');
              if (imgElt) {
                imgElt.setAttribute('src', base64);
                showFilter.value = true; // show the filter bar after applying a filter
              }
            }
          };
        };
      })
      .catch((error) => {
        console.error(error);
      });
  }
}

    
    function applyFilter() {
      if (selectedFilter.value !== '') {
        const algorithm = selectedFilter.value;
        
      // Apply the selected filter
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
                
                showFilter.value = true; // show the filter bar after applying a filter
              }
            }
          };
        })
        .catch((error) => {
          console.error(error);
        });
    
  } else {
    showFilter.value = false; // hide the filter bar if no filter is selected
  }
}

    return {
      selectedId,
      selectedImage,
      imageList,
      selectedFilter,
       blurParam ,
      showImage,
      applyFilter,
      showFilter,
      applyBlur,
    };
  },
};
</script>

<style scoped>
</style>
