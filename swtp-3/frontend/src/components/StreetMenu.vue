<template>
  <div
    class="fixed bottom-6 left-1/2 -translate-x-1/2 pointer-events-none z-10"
  >
    <div class="tabs flex gap-3">
      <div v-for="tab in menuTabs" v-bind:key="tab.name">
        <StreetMenuTab
          :blockType="tab.name"
          :imgSrc="`${tab.name}-pictogram.svg`"
          :isActive="menuTabState.currentActiveTab === tab.name"
          @click="changeCurrentTab(tab.name)"
        />
      </div>
    </div>

    <div class="flex justify-items-center items-center">
      <div id="streetMenuFolder" class="hover:cursor-default">
        <StreetMenuFolder :types="filteredStreetBlocks" />
      </div>

      <BullDozerBtn
        cursorSrc="/assets/img/bulldozer-cursor.svg"
        class="pointer-events-auto"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import StreetMenuFolder from "./StreetMenuFolder.vue";
import BullDozerBtn from "./BullDozerBtn.vue";
import StreetMenuTab from "./StreetMenuTab.vue";
import { useStreetBlock } from "@/services/useStreetBlock";
import swtpConfigJSON from "../../../swtp.config.json";
import { computed } from "vue";
import { StreetBlock } from "@/model/IStreetBlock";

const { changeCurrentTab, menuTabState } = useStreetBlock();

const streetTypes = swtpConfigJSON.streetTypes;

const menuTabs = swtpConfigJSON.menuTabs;

const filteredStreetBlocks = computed(() => {
  let streetBlocks: StreetBlock[] = [];
  let filtered = streetTypes.filter((street) =>
    street.vehicleTypes.includes(menuTabState.currentActiveTab)
  );
  streetBlocks = filtered.map(
    (street) =>
      new StreetBlock(
        street.name,
        0,
        street.imgPath,
        street.possibleRotations,
        street.vehicleTypes
      )
  );
  return streetBlocks;
});
</script>
