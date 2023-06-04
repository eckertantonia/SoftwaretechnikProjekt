<template>
  <main>
    <ExitRoomButton />
    <PlayerList />
    <MultiplayerCursors />
    <StreetMenu />
    <VehicleMenu />
    <MapGrid v-bind:gridSize="swtpConfig.gridSize"></MapGrid>
  </main>
</template>

<script setup lang="ts">
import MultiplayerCursors from "../components/MultiplayerCursors.vue";
import StreetMenu from "@/components/StreetMenu.vue";
import ExitRoomButton from "@/components/ExitRoomButton.vue";
import PlayerList from "@/components/PlayerList.vue";
import MapGrid from "@/components/MapGrid.vue";
import swtpConfig from "../../../swtp.config.json";
import VehicleMenu from "@/components/VehicleMenu.vue";
</script>
