<template>
  <div
    class="tile-ele m-1.5 inline-block hover:cursor-pointer h-20 w-20 bg-street-menu-tile-bg-turquoise rounded-lg"
    :class="
      prop.currentBlock.name === activeBlock.streetBlock.name
        ? 'active outline bg-active-block-turquoise outline-white -outline-offset-2'
        : 'inactive'
    "
  >
    <div
      :id="prop.currentBlock.name"
      @click="changeActiveStreetBlock(prop.currentBlock)"
      @keyup.r="changeActiveRotation(prop.currentBlock)"
      tabindex="0"
      class="tile-current flex justify-center items-center rounded-lg focus:outline-none"
    >
      <img
        class="h-16 w-16 m-2"
        :src="prop.currentBlock.imgPath"
        :alt="prop.currentBlock.name"
        draggable="false"
        fetchpriority="high"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useStreetBlock } from "@/services/useStreetBlock";
import type { StreetBlock } from "@/model/IStreetBlock";

const prop = defineProps<{
  currentBlock: StreetBlock;
}>();

const {
  activeBlock,
  changeCurrentStreetType,
  toggleBulldozer,
  changeRotation,
  bulldozerActive,
  changeRotationTriggered,
} = useStreetBlock();

/**
 * changes the active StreetBlock in streetBlockState and rotates StreetBlock if StreetBlock is clicked multiple times
 * @param type selected StreetBlock
 */
function changeActiveStreetBlock(type: StreetBlock) {
  if (type.name === activeBlock.streetBlock.name && !bulldozerActive.value) {
    const block = document.getElementById(prop.currentBlock.name);
    if (block) {
      let nextRotIndex =
        (activeBlock.streetBlock.possibleRotations.indexOf(
          activeBlock.streetBlock.currentRotation
        ) +
          1) %
        activeBlock.streetBlock.possibleRotations.length;

      let nextRot = activeBlock.streetBlock.possibleRotations[nextRotIndex];
      block.style.rotate = `${nextRot}deg`;
      changeRotation(nextRot);
    }
    return;
  }
  changeCurrentStreetType(type);

  toggleBulldozer(false);
  const entireDoc = document.documentElement;
  entireDoc.style.cursor = "default";
}

function changeActiveRotation(type: StreetBlock) {
  if (type.name === activeBlock.streetBlock.name && !bulldozerActive.value) {
    const block = document.getElementById(prop.currentBlock.name);
    changeRotationTriggered(true);
    if (block) {
      let nextRotIndex =
        (activeBlock.streetBlock.possibleRotations.indexOf(
          activeBlock.streetBlock.currentRotation
        ) +
          1) %
        activeBlock.streetBlock.possibleRotations.length;

      let nextRot = activeBlock.streetBlock.possibleRotations[nextRotIndex];
      block.style.rotate = `${nextRot}deg`;
      changeRotation(nextRot);
    }
    return;
  }
}
</script>
