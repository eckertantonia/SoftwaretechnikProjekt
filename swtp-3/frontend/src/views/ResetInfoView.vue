<template>
  <h1>Everything is being reset!</h1>
  <h2>Please wait.</h2>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { deleteSessionId } from "@/helpers/SessionIDHelper";
import { logger } from "@/helpers/Logger";
import { deleteName } from "@/helpers/UsernameHelper";

onMounted(() => {
  deleteSessionId();
  deleteName();
  resetEverything();
  // Timeout necessary due to Firefox' request lifetime
  setTimeout(function () {
    location.href = "/";
  }, 100);
});

function resetEverything() {
  const DEST = "/api/reset";
  fetch(DEST, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (!response.ok) {
        logger.log("Error @ Restarting everything.");
        location.href = "/500";
      } else {
        return response.text();
      }
    })
    .then(() => {
      logger.log("Done! Game has been reset. Please refresh.");
    })
    .catch(() => {
      logger.log("Error @ Restarting everything.");
      location.href = "/500";
    });
}
</script>
