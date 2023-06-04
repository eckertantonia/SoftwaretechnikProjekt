<template>
  <button
    class="rounded-full ml-7 mt-5 fixed z-10 bg-neutral-700 hover:cursor-pointer"
    @click="backToRoomList()"
  >
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="40"
      height="40"
      viewBox="0 0 50 43"
    >
      <g transform="translate(-4.246 -986.246)">
        <path
          d="M16.016,13.132V10.891h-5.6V8.65h5.6V6.41l3.361,3.361Zm-1.12-1.12v4.482h-5.6v3.361L2.571,16.493V1.928H14.9v5.6h-1.12V3.048H4.812L9.293,5.289V15.373h4.482V12.012Z"
          transform="translate(19.552 997.995)"
          fill="#fff"
        />
      </g>
    </svg>
  </button>
</template>

<script setup lang="ts">
import { useRoom } from "@/services/useRoom";

const { removeUserFromRoom } = useRoom();

function backToRoomList() {
  location.href = "/rooms";
  removeUserFromRoom();
}
</script>
