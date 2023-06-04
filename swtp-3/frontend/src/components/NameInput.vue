<template>
  <form @submit.prevent="submitForm">
    <div class="flex w-full justify-center mb-5">
      <input
        class="font-semibold text-inactive-folder-gray py-2 px-4 w-1/2 mr-5 text-3xl rounded placeholder:opacity-50 placeholder:font-light"
        v-model="nameInput"
        placeholder="Username"
        type="text"
        name="name"
      />
      <button
        type="submit"
        class="bg-button-green font-semibold rounded py-4 px-8 text-4xl"
      >
        Play
      </button>
    </div>
    <p class="text-error-message-pink">{{ message }}</p>
  </form>
</template>

<script setup lang="ts">
import { ref } from "vue";

let nameInput = "";
let message = ref("");
function submitForm(submitEvent: any) {
  document.cookie = "name=" + submitEvent.target.elements.name.value;
  if (nameInput != "") {
    location.href = "/rooms";
  } else {
    message.value = "Bitte keinen leeren Namen wählen";
  }
}
</script>
