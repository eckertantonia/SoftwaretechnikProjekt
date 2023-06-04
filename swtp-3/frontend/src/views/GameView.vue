<template>
  <main>
    <audio id="honk" />
    <audio id="engine" loop />
    <audio id="backgroundMusic" loop />
    <FadeToWhiteOverlay :isActive="showOverlay" />
    <Exit3DButton @click="changeTo2DView()" />
    <PlayerList />
    <ThreeDGame />
  </main>
</template>

<script setup lang="ts">
import Exit3DButton from "@/components/Exit3DButton.vue";
import FadeToWhiteOverlay from "@/components/FadeToWhiteOverlay.vue";
import PlayerList from "@/components/PlayerList.vue";
import { logger } from "@/helpers/Logger";
import { getSessionIDFromCookie } from "@/helpers/SessionIDHelper";
import { useRoom } from "@/services/useRoom";
import { useVehicle } from "@/services/useVehicle";
import ThreeDGame from "@/views/threeD/ThreeDGame.vue";
import { Client } from "@stomp/stompjs";
import { onMounted, onUnmounted, ref } from "vue";
import swtpJSON from "../../../swtp.config.json";

const vehicleTypes = swtpJSON.allVehicleTypes;
const { currentVehicle } = useVehicle();
const { roomState } = useRoom();

const webSocketUrl = `ws://${window.location.host}/stomp-broker`;
const stompVehicleClient = new Client({ brokerURL: webSocketUrl });

const publishVehicleStompClientConnection = setInterval(function () {
  if (!stompVehicleClient.connected) {
    stompVehicleClient.activate();
  } else {
    clearTimeout(publishVehicleStompClientConnection);
  }
}, 20);

let showOverlay = ref(false);

const handleKeydown = (e: KeyboardEvent) => {
  const honk = document.getElementById("honk") as HTMLAudioElement;
  const engine = document.getElementById("engine") as HTMLAudioElement;

  if (e.key === "h") {
    const vehicle = vehicleTypes.find((e) => currentVehicle.type === e.name);
    if (vehicle !== undefined) {
      honk.src = vehicle.honkAudioPath;
      honk.play();
    }
  }

  if (
    e.key === "ArrowUp" ||
    e.key === "ArrowDown" ||
    e.key === "w" ||
    e.key === "s"
  ) {
    const vehicle = vehicleTypes.find((e) => currentVehicle.type === e.name);
    if (vehicle !== undefined) {
      engine.src = vehicle.engineAudioPath;
      engine.play();
    }
  }
};

const handleKeyup = (e: KeyboardEvent) => {
  const engine = document.getElementById("engine") as HTMLAudioElement;
  const backgroundMusic = document.getElementById(
    "backgroundMusic"
  ) as HTMLAudioElement;
  if (
    e.key === "ArrowUp" ||
    e.key === "ArrowDown" ||
    e.key === "w" ||
    e.key === "s"
  ) {
    engine.pause();
    engine.currentTime = 0;
  }

  if (e.key === "1") {
    backgroundMusic.src = swtpJSON.bgMusic[0];
  } else if (e.key === "2") {
    backgroundMusic.src = swtpJSON.bgMusic[1];
  }

  if (e.key === "p") {
    if (backgroundMusic.duration > 0 && !backgroundMusic.paused) {
      backgroundMusic.pause();
    } else {
      backgroundMusic.volume = 0.1;
      backgroundMusic.play();
    }
  }
};

onMounted(() => {
  showOverlay.value = true;
  // Zahl variiert nach Hardware
  setTimeout(() => (showOverlay.value = false), 3000);
  document.addEventListener("keydown", handleKeydown);
  document.addEventListener("keyup", handleKeyup);
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleKeydown);
  document.removeEventListener("keyup", handleKeyup);
});

window.addEventListener("keyup", (event) => {
  if (event.key === "Escape") {
    changeTo2DView();
  }
});

window.onbeforeunload = function () {
  changeTo2DView();
};

/**
 * Changes the View to the 2D-View
 */
function changeTo2DView() {
  const DEST = "/topic/vehicle/delete/" + roomState.room.roomNumber;
  const sessionID = getSessionIDFromCookie();

  logger.log("NUTZER: ", sessionID);

  if (!stompVehicleClient.connected) {
    stompVehicleClient.activate();
  }
  stompVehicleClient.onWebSocketError = (event) => {
    logger.error("WS-error", JSON.stringify(event)); /* WS-Error */
    location.href = "/500";
  };
  stompVehicleClient.onStompError = (frame) => {
    logger.error("STOMP-error", JSON.stringify(frame)); /* STOMP-Error */
    location.href = "/500";
  };
  try {
    stompVehicleClient.publish({
      destination: DEST,
      headers: {},
      body: sessionID,
    });
  } catch (err) {
    logger.error("Error while publishing vehicle! ", err);
  }

  showOverlay.value = true;
  setTimeout(function () {
    location.href = "/" + roomState.room.roomNumber;
  }, 800);
}
</script>
