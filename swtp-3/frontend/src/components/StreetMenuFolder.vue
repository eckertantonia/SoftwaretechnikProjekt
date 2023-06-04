<template>
  <div
    class="pointer-events-auto flex flex-wrap rounded-b-lg rounded-tr-lg bg-street-menu-bg-gray items-center bottom-6 p-1 shadow-lg w-[25.5rem] h-[6.3rem] min-w-[23.5rem] overflow-hidden"
  >
    <div
      id="scrollbox"
      class="flex flex-wrap mh-[6.3rem] w-[23.5rem] h-[6.3rem] min-w-[23.5rem] overflow-hidden"
    >
      <div v-for="t in props.types" v-bind:key="t.name">
        <StreetBlockIcon :currentBlock="t" />
      </div>
      <div class="p-[5em]"></div>
    </div>
    <div>
      <a
        @click="scrollByGivenValue(-1 * streetBlockSize)"
        class="pointer-events-auto absolute -mt-8 -ml-[0.45rem]"
      >
        <img
          src="/assets/img/arrow-pictogram.svg"
          alt="arrow-up"
          class="h-7 w-7 rotate-180 cursor-pointer"
          :class="isMinimumHeight ? 'opacity-20 cursor-default' : 'opacity-100'"
        />
      </a>
      <a
        @click="scrollByGivenValue(streetBlockSize)"
        class="pointer-events-auto absolute -ml-[0.45rem] cursor-pointer"
      >
        <img
          src="/assets/img/arrow-pictogram.svg"
          alt="arrow-down"
          class="h-7 w-7"
          :class="isMinimumHeight ? 'opacity-20 cursor-default' : 'opacity-100'"
        />
      </a>
    </div>
  </div>
</template>

<script setup lang="ts">
import StreetBlockIcon from "./StreetBlockIcon.vue";
import { ref, watch } from "vue";
import { useStreetBlock } from "@/services/useStreetBlock";
import type { StreetBlock } from "@/model/IStreetBlock";
import swtpConfigJSON from "../../../swtp.config.json";

const { resetCurrentChangedTab, menuTabState } = useStreetBlock();

const streetTypes = swtpConfigJSON.streetTypes;

const props = defineProps<{
  types: StreetBlock[];
}>();

let scrollHeight = ref(0);
// Größe des StreetBlocks - ergo Reihenhöhe beim Scrollen. Hat sich so nach mehrfachem Testen als bester Wert erwiesen
const streetBlockSize = 92;

/**
 * Dynamische Berechnung der höchsten Scrollhöhe: Anzahl der Straßenblöcke aus JSON durch 4, aufgerundet damit man die
 * Anzahl der Reihen hat (jeweils 4 in einer Reihe). Multiplizert mit der Streetblocksize damit die Höhe gesamt stimmt,
 * davon nochmal eine Streetblocksize abgezogen (höchste Höhe ist oben, ergo muss eine Reihe abgezogen werden).
 */
let maxScrollHeight =
  Math.ceil(props.types.length / 4) * streetBlockSize - streetBlockSize;

let isMinimumHeight = ref(false);
if (maxScrollHeight === 0) {
  isMinimumHeight = ref(true);
} else {
  isMinimumHeight = ref(false);
}

/**
 * Wenn ich den Tab wechsle, möchte ich dass die aktuelle Reihe ohne smoothes Scrollen wieder direkt oben beginnt.
 * watch hört, ob der aktuelle Tab geändert wurde, reagiert in dem aktuellen Folder und resettet den State.
 */

watch(menuTabState, () => {
  if (menuTabState.currentTabChanged) {
    let currentMenu = document.getElementById("scrollbox") as HTMLElement;

    if (currentMenu) {
      currentMenu.scroll({
        top: 0,
        behavior: "auto",
      });
    }
    /**
     * Aktuelle Höhe muss ständig aktualisiert werden, Größe (Höhe) des Folders wird hier
     * dynamisch berechnet mithilfe der Anzahl der zugewiesenen Blöcke pro Typ.
     */
    let streetFolder = streetTypes.filter((street) =>
      street.vehicleTypes.includes(menuTabState.currentActiveTab)
    );

    maxScrollHeight =
      Math.ceil(streetFolder.length / 4) * streetBlockSize - streetBlockSize;

    if (maxScrollHeight === 0) {
      isMinimumHeight.value = true;
    } else {
      isMinimumHeight.value = false;
    }

    scrollHeight.value = 0;
    resetCurrentChangedTab();
  }
});

function scrollByGivenValue(additionalInput: number) {
  if (scrollHeight.value === maxScrollHeight) {
    if (additionalInput < 0) {
      scrollHeight.value += additionalInput;
    } else {
      scrollHeight.value = maxScrollHeight;
    }
  } else if (scrollHeight.value === 0) {
    if (additionalInput > 0) {
      scrollHeight.value += additionalInput;
    } else {
      scrollHeight.value = 0;
    }
  } else {
    scrollHeight.value += additionalInput;
  }

  let currentMenu = document.getElementById("scrollbox") as HTMLElement;
  if (currentMenu) {
    currentMenu.scroll({
      top: scrollHeight.value,
      behavior: "smooth",
    });
  }
}
</script>
