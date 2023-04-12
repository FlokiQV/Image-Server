<script setup lang="ts">
import { ref } from 'vue';
import { api } from '@/http-api';

const target = ref<HTMLInputElement>();
const error = ref<string | null>(null);

function submitFile() {
  if (target.value !== null && target.value !== undefined && target.value.files !== null) {
    const file = target.value.files[0];
    if (file === undefined)
      return;
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
    if (!allowedTypes.includes(file.type)) {
      error.value = 'Invalid file type. Please select a JPEG, JPG or PNG file.';
      return;
    }
    let formData = new FormData();
    formData.append("file", file);
    api.createImage(formData).then(() => {
      if (target.value !== undefined)
        target.value.value = '';
    }).catch(e => {
      console.log(e.message);
    });
  }
  
}

function handleFileUpload(event: Event) {
  target.value = (event.target as HTMLInputElement);
  error.value = null;
}
</script>

<template>
  <b><span class="animate-charcter">Upload</span></b>
  <div>
    <h3>Upload an image</h3>
    <div>
      <input type="file" id="file" ref="file" @change="handleFileUpload" />
    </div>
    <div v-if="error" class="error">{{ error }}</div>
    <br>
    <div>
      <button id="applybutton" @click="submitFile">Submit</button>
    </div>
  </div>
</template>

<style scoped>
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
</style>
