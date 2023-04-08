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
    <div>
      <button @click="submitFile">Submit</button>
    </div>
  </div>
</template>

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
