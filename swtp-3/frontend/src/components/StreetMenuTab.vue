<template>
  <button
    class="flex justify-items-center items-center pb-4 -mb-3 pointer-events-auto px-4 pt-1 rounded-t-lg"
    v-bind:class="[
      isActive ? 'bg-street-menu-bg-gray shadow-lg' : 'bg-inactive-folder-gray',
    ]"
  >
    <img
      :src="`/assets/img/${props.imgSrc}`"
      :alt="props.blockType + ' icon'"
      draggable="false"
    />
  </button>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
const props = defineProps<{
  blockType: string;
  imgSrc: string;
  isActive: boolean;
}>();
</script>
