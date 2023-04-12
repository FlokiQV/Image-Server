
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
<<<<<<< HEAD
      <select class="select-box" id="filter-select" v-model="selectedFilter" @change="applyFilter">
        <option value="">Select a filter</option>
=======
      <select id="filter-select" v-model="selectedFilter" @change="applyFilter">
>>>>>>> 19a03eea06cbe062551861f5def19c1fae87f5a5
        <option value="HistoEqualisation">Histogram Equalization</option>
        <option value="Sobel">Sobel</option>
        <option value="flou">flou</option>
        <option value="IncreaseLuminosity">Luminosity</option>
        <option value="Color">Color</option>

      </select>

      <div v-if="selectedFilter === 'Sobel'">
        <button @click="downloadImage">Download</button>

      </div>
      

      <div v-if="selectedFilter === 'HistoEqualisation'">
        <button @click="downloadImage">Download</button>

      </div>


      <div v-if="selectedFilter === 'flou'">
        <label for="blurParam">Blur Parameter: </label>
        <input type="number" id="blurParam" v-model="blurParam" min="1" max="10"/>
<<<<<<< HEAD
        <button id="applybutton" @click="applyBlur">Apply</button>
=======
        <button @click="applyBlur">Apply</button>
        <button @click="downloadImage">Download</button>

>>>>>>> 19a03eea06cbe062551861f5def19c1fae87f5a5
      </div>

      <div v-if="selectedFilter === 'IncreaseLuminosity'">
        <label for="lumParam">Lum Parameter: </label>
        <input type="number" id="lumParam" v-model="lumParam" min="1" max="10" />
<<<<<<< HEAD
        <button id="applybutton" @click="applyLum">Apply</button>
=======
        <button @click="applyLum">Apply</button>
        <button @click="downloadImage">Download</button>

>>>>>>> 19a03eea06cbe062551861f5def19c1fae87f5a5
      </div>

      <div v-if="selectedFilter === 'Color'">
        <label for="ColorParam">Color Parameter: </label>
        <input type="number" id="ColorParam" v-model="ColorParam" min="1" max="10"/>
<<<<<<< HEAD
        <button  id="applybutton" @click="applyColor">Apply</button>
=======
        <button @click="applyColor">Apply</button>
        <button @click="downloadImage">Download</button>

>>>>>>> 19a03eea06cbe062551861f5def19c1fae87f5a5
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
    const lumFilterParam = ref(1);
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
      const imgElt = galleryElt.querySelector('img');
      const filteredImgElt = galleryElt.querySelector('.filtered-image'); // nouvelle ligne
      const imgSrc = filteredImgElt ? filteredImgElt.src : imgElt.src; // nouvelle ligne
      if (imgElt) {
        const link = document.createElement('a');
        link.download = `${filteredImage.value.name}.png`;
        link.href = imgSrc;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }
    }
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
#applybutton {
  margin-left: 150px;
  
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
</style>