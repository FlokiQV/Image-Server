<template>
  
  <figure :id="'gallery-'+id" :data-href="'/images/'+id"></figure>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
import { api } from '@/http-api';
import { ref } from 'vue';
import axios from 'axios';

const props = defineProps<{ id: number }>()


api.getImage(props.id)
  .then((data: Blob) => {
    const reader = new window.FileReader();
    reader.readAsDataURL(data);
    reader.onload = () => {
      const galleryElt = document.getElementById("gallery-"+props.id);
      if (galleryElt !== null) {
        const imgElt = document.createElement("img");
        galleryElt.appendChild(imgElt);
        if (imgElt !== null && reader.result as string) {
          imgElt.setAttribute("src", (reader.result as string));
        }
        galleryElt.addEventListener('click', () => {
          const href = galleryElt.getAttribute('data-href');
          if (href !== null) {
            window.location.href = href;
          }
        });
      }
    };
  })
  .catch(e => {
    console.log(e.message);
  });
</script>
