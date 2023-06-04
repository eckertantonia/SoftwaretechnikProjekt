<template>
  <svg
    v-for="(item, key, index) in mouseDict"
    v-bind:style="{
      transform: `translateX(${item[0]}px) translateY(${item[1]}px)`,
    }"
    class="top-0 left-0 transition-transform duration-100 ease-linear overflow-visible"
    v-bind:fill="colors[index]"
    v-bind:key="key"
  >
    <path
      d="M5.65376 12.3673H5.46026L5.31717 12.4976L0.500002 16.8829L0.500002 1.19841L11.7841 12.3673H5.65376Z"
    />
    <text y="25" x="13">{{ currentMouseName }}</text>
  </svg>
</template>

<script setup lang="ts">
import { Mouse } from "@/model/IMouse";
import { useRoom } from "@/services/useRoom";
import { useUser } from "@/services/useUser";
import { onMounted, reactive, ref } from "vue";
import { getSessionIDFromCookie } from "@/helpers/SessionIDHelper";

// https://vueuse.org/core/usemouse/
import { useMouse } from "@vueuse/core";
import { getNameFromCookie } from "@/helpers/UsernameHelper";

const colors = ref([
  "#A9E5BB",
  "#FCF6B1",
  "#F72C25",
  "#F4989C",
  "#DAC4F7",
  "#A9E5BB",
  "#FCF6B1",
  "#F72C25",
  "#F4989C",
  "#DAC4F7",
  "#A9E5BB",
  "#FCF6B1",
  "#F72C25",
  "#F4989C",
  "#DAC4F7",
]);

const { x, y } = useMouse({ touch: false });
let lastXsent = 0;
let lastYsent = 0;

//const mouseMap = reactive(new Map<string, number[]>());
let mouseDict: { [sessionID: string]: number[] } = reactive({});
let currentMouseName = ref("");

const { roomState, receiveRoom, joinRoom } = useRoom();
const { publishMouse, mouseState, receiveMouse } = useUser();

onMounted(() => {
  receiveRoom();
  // location.pathname.split("/")[2] as unknown as number -> Get room number from url, since new rooms arent dynamically created yet
  joinRoom(location.pathname.split("/")[1] as unknown as number);
  receiveMouse(location.pathname.split("/")[1] as unknown as number);
});

setInterval(function () {
  if (
    Math.abs(lastXsent - x.value) > 0.1 ||
    Math.abs(lastYsent - y.value) > 0.1
  ) {
    lastXsent = x.value;
    lastYsent = y.value;
    publishMouse(
      new Mouse(
        getSessionIDFromCookie() as string,
        getNameFromCookie() as string,
        roomState.room.roomNumber,
        x.value,
        y.value
      ),
      location.pathname.split("/")[1] as unknown as number
    );
  }
  if (
    mouseState.mouse.sessionID != "" &&
    mouseState.mouse.sessionID != null &&
    mouseState.mouse.sessionID != getSessionIDFromCookie()
  ) {
    mouseDict[mouseState.mouse.sessionID] = [
      mouseState.mouse.x,
      mouseState.mouse.y,
    ];
    currentMouseName.value = mouseState.mouse.userName;
  }
  // 20 milliseconds set, to update the mouse position every 20 ms if movement is detected
}, 20);
</script>
