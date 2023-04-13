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
      <input type="file" id="file" ref="file"  @change="handleFileUpload" />
    </div>
    <div v-if="error" class="error">{{ error }}</div>
    <br>
    <div>
      <button id="applybutton" @click="submitFile">Submit</button>
    </div>
  </div>
</template>

<style scoped>
</style>
