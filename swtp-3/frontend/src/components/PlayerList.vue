<template>
  <div class="text-white fixed w-72 mt-5 left-1/2 -translate-x-1/2 z-10">
    <div
      class="bg-back-folder-gray rounded-full cursor-pointer px-8 py-2 mb-3 shadow-lg text-center"
      @click="changeVisibilty()"
    >
      {{ roomState.room.userList.length }} Players online
    </div>
    <div
      id="playerlist"
      class="bg-back-folder-gray rounded shadow-lg p-3"
      :class="showPlayerList ? '' : 'hidden'"
    >
      <span v-for="user in roomState.room.userList" v-bind:key="user.sessionID">
        <PlayerListItem
          :userName="user.userName"
          :userLoginTime="user.loginTime"
        />
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoom } from "@/services/useRoom";
import PlayerListItem from "@/components/PlayerListItem.vue";
import { ref } from "vue";
const { roomState } = useRoom();

let showPlayerList = ref(false);

/**
 * Change visibility of player list (visible or hidden)
 */
function changeVisibilty() {
  showPlayerList.value = !showPlayerList.value;
}
</script>
