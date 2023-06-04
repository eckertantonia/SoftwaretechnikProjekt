<template>
  <div
    id="vehicleMenu"
    class="overscroll-contain overflow-auto max-h-[92vh] bg-[#e4f9ff] rounded-3xl border-street-menu-bg-gray border-[0.5rem] fixed z-10 right-8 bottom-8 p-2 text-center shadow-[5px_5px_10px_rgba(0,0,0,0.25)]"
    @dragover.prevent
    @dragenter.prevent
  >
    <VehicleType
      v-for="vehicle in vehicleTypes"
      v-bind:key="vehicle.name"
      v-bind:traffic="{
        type: vehicle.name,
        iconSrc: vehicle.iconPath,
      }"
    ></VehicleType>
  </div>
</template>

<script setup lang="ts">
import VehicleType from "@/components/VehicleType.vue";
import swtpJSON from "../../../swtp.config.json";
const vehicleTypes = swtpJSON.allVehicleTypes;
</script>
