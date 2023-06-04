<template>
  <div
    class="bg-back-folder-gray bg-blend-overlay h-screen text-white pt-20 flex justify-center items-center bg-[url('/assets/img/bg-menu.png')] bg-center bg-cover"
  >
    <div class="text-center">
      <h1 class="font-bold text-[10rem] uppercase">Zychose</h1>
      <NameInput />
    </div>
  </div>
</template>

<script setup lang="ts">
import NameInput from "@/components/NameInput.vue";
import { checkIfNameCookieExists } from "@/helpers/UsernameHelper";
import { onMounted } from "vue";

onMounted(() => {
  if (checkIfNameCookieExists()) {
    location.href = "/rooms";
  }
});
</script>
