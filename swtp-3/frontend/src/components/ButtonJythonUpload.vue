<template>
  <form class="pl-5" @submit.prevent="submitForm">
    <div class="flex items-center h-12">
      <input
        class="file:bg-button-blue-bright file:font-semibold file:rounded file:w-40 file:py-1 file:ml-3 file:mr-4 file:text-white file:border-0"
        type="file"
        accept=".jy, .py"
        @change="onChangeFile"
      />
    </div>
  </form>
</template>

<script setup lang="ts">
import { logger } from "@/helpers/Logger";
import { getRoomList } from "@/services/useRoomList";
import { reactive } from "vue";

let files: File[] = reactive([]);

const props = defineProps<{
  roomNumber: number;
}>();

/**
 * Function that is triggered when a file was selected
 *
 * @param event event that triggers
 */
function onChangeFile(event: any) {
  const target = event.target;
  if (target.files != null) {
    if (
      target.files[0].name.split(".")[1] == "py" ||
      target.files[0].name.split(".")[1] == "jy"
    ) {
      files = files.concat(target.files[0]);
      submitForm(props.roomNumber);
    } else {
      logger.log("Only python or jython files allowed!");
    }
  }
}

/**
 * asynchronous function to send a selected file from the frontend to the backend
 */
async function submitForm(roomNumber: any) {
  const formData = new FormData();
  const postURL = "/api/upload/" + roomNumber;
  formData.append("file", files[0]);

  const reqOptions = {
    method: "POST",
    headers: {},
    body: formData,
  };
  await fetch(postURL, reqOptions);
  getRoomList();
}
</script>
