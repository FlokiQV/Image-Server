<template>
  <b><span class="animate-charcter">Home</span></b>
  <div>
    <h3>Choose an image</h3>
    <div>
      <select class="select-box" v-model="selectedId" @change="showImage">
        <option value="-1" disabled selected>Select an image</option>
        <option v-for="image in imageList" :value="image.id" :key="image.id">{{ image.name }}</option>
      </select>
    </div>
    <div v-if="selectedImage">
      <h3 for="filter-select">Filter:</h3>
        
        <option id="labelw"  value="">Select a filter</option>
        <select class="select-box" id="filter-select" v-model="selectedFilter" @change="applyFilter">
      
        <option value="flou">flou</option>
        <option value="HistoEqualisation">Histogram Equalization</option>
        <option value="IncreaseLuminosity">Luminosity</option>
        <option value="Sobel">Sobel</option>
        <option value="Color">Color</option>
      </select>

      <br>

      <div v-if="selectedFilter === 'Sobel'">
        <button id="applybutton" @click="downloadImage">Download</button>
      </div>
      

      <div v-if="selectedFilter === 'HistoEqualisation'">
        <button id="applybutton" @click="downloadImage">Download</button>
      </div>


      <div v-if="selectedFilter === 'flou'">
        <label id="labelw" for="blurParam">Blur Parameter: </label>
        <input type="number" id="blurParam" v-model="blurParam" min="1" max="10"/>       
        <button id="applybutton" style="margin-left: 30px;" @click="applyBlur">Apply</button>
        <button id="applybutton" style="margin-left: 30px;" @click="downloadImage">Download</button>
      </div>

      <div v-if="selectedFilter === 'IncreaseLuminosity'">
        <label id="labelw" for="lumParam">Lum Parameter: </label>
        <input type="number" id="lumParam" v-model="lumParam" min="1" max="10" />       
        <button id="applybutton" style="margin-left: 30px;"  @click="applyLum">Apply</button>
        <button id="applybutton" style="margin-left: 30px;"  @click="downloadImage">Download</button>
      </div>

      <div v-if="selectedFilter === 'Color'">
        <label id="labelw" for="ColorParam">Color Parameter: </label>
        <input type="number" id="ColorParam" v-model="ColorParam" min="1" max="10"/>
        <button  id="applybutton" style="margin-left: 30px;"  @click="applyColor">Apply</button>
        <button id="applybutton" style="margin-left: 30px;"  @click="downloadImage">Download</button>
      </div>
   </div>
    
   <br>
   <br>

   <div v-if="selectedImage" class="images-container">
 
  <div style="margin-left: 30px;" class="image-wrapper">
    <h3>Before:</h3>
    <img class="imghome" :src="'/images/' + selectedImage.id" alt="Selected image">
  </div>

  <div :id="'gallery-' + selectedImage.id" class="image-wrapper">
    <h3>After:</h3>
        <img class="imghome" :src="'/images/' + selectedImage.id" alt="Selected image">
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
    const filteredImage = ref<SelectedImage | null>(null); // add a reference to the filtered image
    const imageList = ref<ImageType[]>([]);
    const selectedFilter = ref('');
    const showFilter = ref(false);
    const blurParam = ref(1);
    const lumParam =ref(1);
    const ColorParam = ref(0);
    
 

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

    function downloadImage() {
  if (filteredImage.value) {
    const galleryElt = document.getElementById(`gallery-${filteredImage.value.id}`);
    if (galleryElt) {
      const imgElt: HTMLImageElement | null = galleryElt.querySelector('img');
      const filteredImgElt = document.createElement('img'); // create new image element
      filteredImgElt.src = imgElt ? imgElt.src : ''; // set source to filtered image data URL

      const sobelFilterId = 'sobel';
      const link = document.createElement('a');
      link.download = `${sobelFilterId}-${filteredImage.value.name ?? 'filtered-image'}.png`;
      link.href = filteredImgElt.src;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  } else {
    console.log("Filtered image does not exist");
  }
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
function applyLum() {
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
            context.drawImage(img, 0, 0);
            const imageData = context.getImageData(0, 0, canvas.width, canvas.height);
            const data = imageData.data;
            const lumFactor = lumParam.value / 10;
            for (let i = 0; i < data.length; i += 4) {
              const r = data[i];
              const g = data[i + 1];
              const b = data[i + 2];
              const gray = 0.2126 * r + 0.7152 * g + 0.0722 * b;
              data[i] = Math.min(r + gray * lumFactor, 255);
              data[i + 1] = Math.min(g + gray * lumFactor, 255);
              data[i + 2] = Math.min(b + gray * lumFactor, 255);
            }
            context.putImageData(imageData, 0, 0);
            const base64 = canvas.toDataURL();
            filteredImage.value = { id: selectedImage.value!.id, name: `${selectedImage.value!.name}-luminosity` };
            const galleryElt = filteredImage.value ? document.getElementById(`gallery-${filteredImage.value.id}`) : null;
            if (galleryElt) {
              const imgElt = galleryElt.querySelector('img');
              if (imgElt) {
                imgElt.setAttribute('src', base64);
                showFilter.value = true;
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



function applyColor() {
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
            const red = ColorParam.value * 25; // convert the color parameter to a value between 0 and 255
            context.filter = `sepia(100%) hue-rotate(${red}deg)`;
            context.drawImage(img, 0, 0);
            const base64 = canvas.toDataURL();
            filteredImage.value = { id: selectedImage.value!.id, name: `${selectedImage.value!.name}-color` }; // update filteredImage reference
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




    return {
      selectedId,
  selectedImage,
  filteredImage,
  imageList,
  selectedFilter,
  showFilter,
  blurParam,
  lumParam,
  ColorParam,
  showImage,
  applyBlur,
  applyLum,
  applyColor,
  applyFilter,
  downloadImage,
    };
    
  },
};
</script>

<style scoped>
.images-container {
  display: flex;
}

.image-wrapper {
  display: inline-block;
  margin-right: 65px;
}

</style>