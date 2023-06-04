<template>
  <div
    @click="changeBulldozerState()"
    class="shadow-lg border-4 rounded-full ml-4 hover:cursor-pointer bg-[#FFD941] h-16 w-16 mt-2"
    :class="
      bulldozerActive.valueOf()
        ? 'active border-white'
        : 'inactive border-[#4B5357]'
    "
  >
    <svg
      width="65%"
      height="65%"
      id="bulldozer"
      clip-rule="evenodd"
      fill-rule="evenodd"
      stroke-linejoin="round"
      stroke-miterlimit="2"
      version="1.1"
      viewBox="-40 -30 126 95"
      xml:space="preserve"
      xmlns="http://www.w3.org/2000/svg"
      style="overflow: visible"
    >
      <g transform="scale(4.1667)">
        <g transform="translate(-144.92 1441.4)">
          <path
            id="bull"
            d="m147.56-1441.4c-0.42 0.11-0.772 0.4-0.964 0.79l-0.184 0.37v7.83l-0.3 0.08c-0.393 0.08-0.738 0.31-0.957 0.65l-0.2 0.3-0.042 5h0.184c0.135 0 0.3-0.12 0.6-0.45 0.227-0.24 0.487-0.46 0.773-0.63 0.737-0.41 0.709-0.41 8.238-0.41 4.7 0 7.047 0.03 7.338 0.08 0.27 0.06 0.533 0.14 0.787 0.25 0.454 0.23 0.848 0.57 1.149 0.98l0.1 0.18h2.056l-0.043-2.41c0.051-0.89-1e-3 -1.79-0.156-2.67-0.135-0.33-0.388-0.6-0.709-0.75-0.262-0.15-0.4-0.16-2-0.18l-1.723-0.02v-1.67c0.055-0.64 0.02-1.29-0.106-1.92-0.242-0.55-0.786-0.9-1.382-0.9-0.597 0-1.141 0.35-1.383 0.9-0.126 0.63-0.162 1.29-0.106 1.93v1.67h-3.049v-7.86l-0.2-0.37c-0.116-0.25-0.307-0.46-0.546-0.6l-0.34-0.22-3.3-0.01c-1.812 0-3.407 0.02-3.535 0.06zm4.878 2.98v7.58h-2.978v-7.58h2.978z"
            fill="#4b5357"
            fill-rule="nonzero"
          />
        </g>
        <g transform="translate(-1203.5 946.31)">
          <path
            id="bull2"
            d="m1226.2-935.7v12.053h7.52l-0.02-0.759-0.02-0.751-2.24-0.751-2.24-0.745-0.73-4.36c-0.4-2.4-0.74-4.438-0.76-4.531-0.04-0.149-0.07-0.156-0.77-0.156h-0.74z"
            fill="#4b5357"
            fill-rule="nonzero"
          />
        </g>
        <g transform="translate(-146.9 665.47)">
          <path
            id="bull3"
            d="m149.1-648.72c-0.843 0.241-1.543 0.833-1.921 1.624-0.215 0.388-0.3 0.836-0.241 1.276-0.056 0.432 0.023 0.87 0.227 1.255 0.289 0.642 0.798 1.16 1.435 1.461l0.532 0.262h15.179l0.5-0.241c1.05-0.491 1.724-1.55 1.724-2.709 0-0.778-0.303-1.526-0.845-2.084-0.391-0.414-0.889-0.711-1.439-0.858-0.563-0.149-14.587-0.135-15.151 0.014z"
            fill="#4b5357"
            fill-rule="nonzero"
          />
        </g>
      </g>
    </svg>
  </div>
</template>

<script setup lang="ts">
import { useStreetBlock } from "@/services/useStreetBlock";
import { watch } from "vue";

const { bulldozerActive, toggleBulldozer } = useStreetBlock();

const props = defineProps<{
  cursorSrc: string;
}>();

const bulldozerGray = "#4B5357";
/**
 * changes BulldozerState and sets currentTileType to an empty StreetBlock
 */
function changeBulldozerState() {
  const entireDoc = document.documentElement;

  if (entireDoc) {
    if (!bulldozerActive.value) {
      toggleBulldozer(true);
    } else {
      toggleBulldozer(false);
    }
  }
}

/**
 * changes the path color of the svg
 * @param color color as string
 */
function colorBulldozer(color: string) {
  const fillSvg = document.getElementById("bull");
  const fillSvg2 = document.getElementById("bull2");
  const fillSvg3 = document.getElementById("bull3");
  if (fillSvg && fillSvg2 && fillSvg3) {
    fillSvg.style.fill = color;
    fillSvg2.style.fill = color;
    fillSvg3.style.fill = color;
  }
}

/**
 * watches streetBlockState and changes cursor style according to bulldozerActive
 */
watch(bulldozerActive, () => {
  const entireDoc = document.documentElement;
  if (entireDoc) {
    if (bulldozerActive.value) {
      entireDoc.style.cursor = `url("${props.cursorSrc}") 25 25, auto`;
      colorBulldozer("white");
    } else {
      entireDoc.style.cursor = "default";
      colorBulldozer(bulldozerGray);
    }
  }
});
</script>
