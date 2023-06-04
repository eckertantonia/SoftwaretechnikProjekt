<template>
  <div
    id="wrapper"
    v-bind:class="
      currentVehicle.type !== ''
        ? `duration-1000 bg-white opacity-70 h-screen`
        : ``
    "
  >
    <table
      id="gridTable"
      class="bg-[#008000] w-full border-spacing-0 border-separate table-fixed max-h-full overflow-hidden"
      @wheel="zoomOnWheel($event)"
      @wheel.prevent
      @mousedown="startDragThroughGrid($event)"
      @mouseup="stopDragThroughGrid()"
      @mousemove="dragToNewPosition($event)"
    >
      <tr
        v-for="row in checkedGridSize"
        v-bind:key="row"
        class="box-border h-20 p-0"
      >
        <td
          v-for="col in checkedGridSize"
          class="box-border w-20 p-0 border border-white/20 hover:border-white hover:shadow-[inset_0_0_4px_1px_#fff] hover:opacity-50 bg-cover bg-no-repeat bg-center"
          v-bind:key="col"
          @dragover.prevent="dragOver(row, col)"
          @dragleave="dragLeave(row, col)"
          @dragenter.prevent
          v-on:drop="onDrop(row, col)"
          v-on:click="cellClicked(row, col)"
          v-on:mouseover="onHover(row, col)"
          v-on:mouseleave="onEndHover(row, col)"
        ></td>
      </tr>
    </table>
  </div>
</template>

<script setup lang="ts">
import { useStreets, type IStreetInformation } from "../services/useStreets";
import swtpConfigJSON from "../../../swtp.config.json";
import { computed, onMounted, ref, watch } from "vue";
import { useVehicle } from "@/services/useVehicle";
import router from "@/router";
import { logger } from "@/helpers/Logger";
import { use3DVehiclePosition } from "@/services/use3DVehiclePosition";
import { useStreetBlock } from "@/services/useStreetBlock";
import { useRoom } from "@/services/useRoom";
import { jsonToState } from "@/services/JSONparser";

/**
 * @param {number} gridSize defines the size of the grid component
 */
const props = defineProps<{
  gridSize: any;
}>();

let isDragging = false;
let isFirstDrag = false;

let currX: number;
let currY: number;
let initX: number;
let initY: number;
let offsetX = 0;
let offsetY = 0;
const entireDoc = document.documentElement;

let hoverX = ref(0);
let hoverY = ref(0);

onMounted(() => {
  document.addEventListener("contextmenu", (event) => {
    event.preventDefault();
  });
  initializeStreetState();
  // Template loads table first, slower connections need extra time -> 700 ms tested to be sufficient
  setTimeout(function () {
    stateToGrid();
  }, 700);

  const grid = document.getElementById("gridTable") as HTMLTableElement;

  // xCenter: ges.Breite --> Breite des Fenster/2 nochmal subtrahieren, um in der Mitte zu sein
  let xCenter = grid.offsetWidth / 2 - window.innerWidth / 2;
  let yCenter = grid.offsetHeight / 2 - window.innerHeight / 2;

  entireDoc.scroll(xCenter, yCenter);
  offsetX = xCenter;
  offsetY = yCenter;
});

/**
 * Achtet auf Gridgröße und hält die Grenzen ein, damit man beim Draggen stets im Bild bleibt.
 * @param mousePos aktuelle event.client Mausposition
 * @param offsetPos aktuelle, letzte Position vom Drag - hier neu reingegeben damit für X & Y nicht
 * extra Funktionen geschrieben werden müssen.
 */
function calculateDragPosition(mousePos: number, offsetPos: number) {
  const grid = document.getElementById("gridTable") as HTMLTableElement;
  let currPos: number;
  currPos = mousePos - offsetPos;
  if (currPos <= mousePos) {
    currPos = mousePos;
  } else if (currPos >= grid.offsetWidth - mousePos) {
    currPos = grid.offsetWidth - mousePos;
  } else {
    currPos = mousePos - offsetPos;
  }
  return currPos;
}

function startDragThroughGrid(event: MouseEvent) {
  event.preventDefault();

  // 2 -> rechter Mausbutton
  if (event.button === 2) {
    if (!isFirstDrag) {
      // Startposition
      initX = offsetX + event.clientX;
      initY = offsetY + event.clientY;
    } else {
      initX = calculateDragPosition(event.clientX, offsetX);
      initY = calculateDragPosition(event.clientY, offsetY);
    }
    isDragging = true;
  }
}

function stopDragThroughGrid() {
  initX = currX;
  initY = currY;
  isDragging = false;
}

function dragToNewPosition(event: MouseEvent) {
  if (isDragging) {
    if (!isFirstDrag) {
      currX = event.clientX + initX;
      currY = event.clientY + initY;
      isFirstDrag = true;
    } else {
      currX = event.clientX - initX;
      currY = event.clientY - initY;
    }

    offsetX = currX;
    offsetY = currY;

    entireDoc.scrollTop = -1 * currY;
    entireDoc.scrollLeft = -1 * currX;
  }
}

/*
  * Hardcoded Wert 24, weil 1rem entspricht 16px, also 1920/16 = 120 -> 120/5rem (cell-width) = 24 cells
  //TODO ?min-gridSize dynamisch berechenbar machen/ cell-size in config einstellbar ----- storyless task oder issue?
 */
const checkedGridSize = computed(() => {
  if (isNaN(props.gridSize)) {
    return 100;
  } else {
    if (props.gridSize < 24) {
      return 24;
    } else {
      return props.gridSize;
    }
  }
});

const { updateStreetState, placedStreet, streetsState, initializeStreetState } =
  useStreets();
const { currentVehicle } = useVehicle();
const {
  activeBlock,
  isRotationTriggeredState,
  changeRotationTriggered,
  bulldozerActive,
  changeCurrentStreetType,
} = useStreetBlock();
const streetTypes = swtpConfigJSON.streetTypes;
const { publishVehiclePosition } = use3DVehiclePosition();
const config = swtpConfigJSON;
const { roomState } = useRoom();

watch(roomState, () => {
  if (roomState.room.roomMap != JSON.stringify(streetsState.streets)) {
    jsonToState(roomState.room.roomMap);
    stateToGrid();
  }
});

watch(isRotationTriggeredState, () => {
  if (isRotationTriggeredState) {
    const table = document.getElementById("gridTable") as HTMLTableElement;
    const cell = table.rows[hoverX.value - 1].cells[hoverY.value - 1];
    setCellBackgroundStyle(cell, {
      streetType: activeBlock.streetBlock.name,
      rotation: activeBlock.streetBlock.currentRotation,
      posX: hoverX.value,
      posY: hoverY.value,
    });
    changeRotationTriggered(false);
  }
});
watch(bulldozerActive, () => {
  changeCurrentStreetType(null);
});
/**
 * cellClicked handles the click event for cells.
 * Data like Typestreet and rotation of the selected Street are passed in through a state.
 * Changes the backgroundImage of the cell. The Cell will be saved in the state.
 * @param {number} posX position on x axis (click)
 * @param {number} posY position on y axis (click)
 */
function cellClicked(posX: number, posY: number): void {
  logger.log("(posX,posY): ", [posX, posY]);
  const table = document.getElementById("gridTable") as HTMLTableElement;
  const cell = table.rows[posX - 1].cells[posY - 1];
  let activeStreet: IStreetInformation = {
    streetType: activeBlock.streetBlock.name,
    rotation: activeBlock.streetBlock.currentRotation,
    posX: posX,
    posY: posY,
  };
  setCellBackgroundStyle(cell, activeStreet);
  updateStreetState(activeStreet);
}

/**
 * onDrop function for the drag&drop process
 * @param {number} posX position on x axis (click)
 * @param {number} posY position on y axis (click)
 */
function onDrop(posX: number, posY: number) {
  logger.log("Vehicle-Position: ", posX, posY);
  const vehicleType = currentVehicle.type;
  changeTo3DView(posX, posY, vehicleType);
}

/**
 * Changes the View to the 3D-View
 */
function changeTo3DView(posX: number, posY: number, vehicleType: string) {
  const entireDoc = document.documentElement;
  entireDoc.style.cursor = "default";
  let wrapper = document.getElementById("wrapper");
  if (wrapper != null) {
    wrapper.classList.remove("opacity-70");
    wrapper.classList.add(
      "absolute",
      "duration-1000",
      "bg-white",
      "opacity-0",
      "h-screen",
      "w-screen",
      "z-30"
    );
  }
  //Calculating correct position in 3D World
  posX = (posX - 1 - config.gridSize / 2) * config.blocksize;
  posY = (posY - 1 - config.gridSize / 2) * config.blocksize;
  publishVehiclePosition(posX, posY, vehicleType);
  setTimeout(function () {
    router.push((location.pathname.split("/")[1] as unknown as number) + "/3d");
  }, 800);
}

/**
 * Zoom mit Mausrad, scale = aktuelle Skalierung
 */
let scale = 1;
function zoomOnWheel(event: WheelEvent) {
  event.preventDefault();
  scale += event.deltaY * -0.01;
  scale = Math.min(Math.max(1, scale), 4);
  const element = document.getElementById("gridTable");

  if (element) {
    element.classList.add("origin-left-top");
    element.style.transform = `scale(${scale})`; // muss direkt über style geändert werden, lösung mit tailwind nicht möglich
  }
}

/**
 * dragOver function of the drag&drop process
 * @param {number} posX position on the x axis while dragging over the grid
 * @param {number} posY position on the y axis while dragging over the grid
 */
function dragOver(posX: number, posY: number) {
  const table = document.getElementById("gridTable") as HTMLTableElement;
  const cell = table.rows[posX - 1].cells[posY - 1] as HTMLTableCellElement;
  cell.classList.add(
    "shadow-[inset_0_0_4px_1px_rgba(255,255,0,1)]",
    "border-yellow-300"
  );
}

/**
 * dragLeave function of the drag&drop process
 * @param {number} posX position on the x axis while leaving a cell on the grid
 * @param {number} posY position on the y axis while leaving a cell on the grid
 */
function dragLeave(posX: number, posY: number) {
  const table = document.getElementById("gridTable") as HTMLTableElement;
  const cell = table.rows[posX - 1].cells[posY - 1] as HTMLTableCellElement;
  cell.classList.remove(
    "shadow-[inset_0_0_4px_1px_rgba(255,255,0,1)]",
    "border-yellow-300"
  );
}

/**
 * getting the hovered cell and changing the backgroundImage to fit the active block of the street menu
 * @param {number} x position on x axis (click)
 * @param {number} y position on y axis (click)
 */
function onHover(x: number, y: number): void {
  hoverX.value = x;
  hoverY.value = y;
  const table = document.getElementById("gridTable") as HTMLTableElement;
  const cell = table.rows[x - 1].cells[y - 1];
  setCellBackgroundStyle(cell, {
    streetType: activeBlock.streetBlock.name,
    rotation: activeBlock.streetBlock.currentRotation,
    posX: x,
    posY: y,
  });
}

/**
 * when the mouse exits the previously hovered cell, we check, if a street was placed.
 * if yes: change backgroundImage back to the right one of the placed street
 * if no: reset the backgroundImage to nothing
 * @param {number} x position on x axis (click)
 * @param {number} y position on y axis (click)
 */
function onEndHover(x: number, y: number): void {
  const table = document.getElementById("gridTable") as HTMLTableElement;
  const cell = table.rows[x - 1].cells[y - 1];
  const street = placedStreet(x, y);
  if (street !== undefined) {
    setCellBackgroundStyle(cell, street);
  } else {
    cell.style.backgroundImage = "";
  }
}

/**
 * Function to display the streets that are saved in the state.
 */
function stateToGrid(): void {
  const table = document.getElementById("gridTable") as HTMLTableElement;
  for (let row of table.rows) {
    for (let col of row.cells) {
      col.style.backgroundImage = "";
    }
  }
  logger.log("STATE: ", streetsState.streets);
  for (const street of streetsState.streets) {
    const cell = table.rows[street.posX - 1].cells[street.posY - 1];
    setCellBackgroundStyle(cell, street);
  }
}

/**
 * Function that sets the style of the given Cell. The given street is needed for information about the streetType and rotation.
 * @param {HTMLTableCellElement} cell cell object which style should be set
 * @param {IStreetInformation} street information about the street
 */
function setCellBackgroundStyle(
  cell: HTMLTableCellElement,
  street: IStreetInformation
): void {
  for (const streetType of streetTypes) {
    if (streetType.name === street.streetType) {
      cell.style.backgroundImage = `url(${streetType.imgPath})`;
      cell.style.transform = `rotate(${street.rotation}deg)`;
    }
  }
}
</script>
