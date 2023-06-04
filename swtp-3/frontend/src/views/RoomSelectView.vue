<template>
  <div class="bg-back-folder-gray fixed w-full h-full">
    <div class="flex justify-between my-8 mx-20">
      <GoBackButton
        class="drop-shadow-md place-self-end"
        v-on:click="$router.push('/')"
      />
      <h1 class="font-bold text-6xl text-white font-sans self-center uppercase">
        Servers
      </h1>
      <div class="w-16"></div>
    </div>

    <div
      v-for="room in roomListItems"
      v-bind:key="room.roomNumber"
      class="flex justify-between items-center py-3 pl-5 my-5 mx-20 bg-room-list-bg-gray text-white font-sans drop-shadow-md rounded-md"
    >
      <span class="font-bold text-xl">Raum {{ room.roomNumber }}</span>
      <div class="flex justify-end items-center pr-3 text-lg">
        <span>{{ room.userList.length }} Players</span>
        <ButtonJythonUpload :room-number="room.roomNumber"></ButtonJythonUpload>
        <div v-if="room.jythonScript">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="icon icon-tabler icon-tabler-file-code inline-block m-2 mt-[0.8em]"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            stroke-width="2"
            stroke="currentColor"
            fill="none"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none" />
            <path d="M14 3v4a1 1 0 0 0 1 1h4" />
            <path
              d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z"
            />
            <path d="M10 13l-1 2l1 2" />
            <path d="M14 13l1 2l-1 2" />
          </svg>
        </div>
        <button
          v-on:click="$router.push('/' + room.roomNumber)"
          class="bg-button-green font-semibold rounded w-16 py-1"
        >
          Join
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoomBox } from "@/services/useRoomList";
import { onMounted, computed } from "vue";
import { useUser } from "@/services/useUser";
import GoBackButton from "../components/GoBackButton.vue";
import ButtonJythonUpload from "@/components/ButtonJythonUpload.vue";

const { roomListState, getRoomList } = useRoomBox();
const { createUser } = useUser();

const roomListItems = computed(() => {
  return roomListState.rooms.roomList;
});

onMounted(() => {
  createUser();
  // 600ms chosen to account for slower Connection (and slower Computers)
  setTimeout(function () {
    getRoomList();
  }, 600);
});
</script>
