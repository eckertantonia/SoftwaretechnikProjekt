<template>
  <img
    :src="props.traffic.iconSrc"
    :alt="props.traffic.type"
    width="35"
    class="p-1 hover:cursor-grab"
    draggable="true"
    id="vehicle"
    @drop="onDrop()"
    @dragstart="startDrag($event, traffic)"
  />
</template>

<script setup lang="ts">
import { useVehicle, type IVehicleInformation } from "@/services/useVehicle";

const props = defineProps<{
  traffic: IVehicleInformation;
}>();

const { updateCurrentVehicle } = useVehicle();

/**
 * Drag functionality for the unique items in the list
 *
 * @param {any} evt Drop event
 * @param {IVehicleInformation} traffic Dragged item
 */
function startDrag(evt: any, traffic: IVehicleInformation) {
  updateCurrentVehicle(traffic);
  evt.dataTransfer.dropEffect = "move";
  evt.dataTransfer.effectAllowed = "move";
}
/**
 * Drop functionality for the unique items in the list
 */
function onDrop() {
  updateCurrentVehicle({ type: "", iconSrc: "" });
}
</script>
