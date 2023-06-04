import { reactive } from "vue";

import type { Direction } from "@/model/DirektionCommands";

const keys: Set<string> = new Set();
const keyMap = {
  FORWARD: ["w", "ArrowUp"],
  BACKWARD: ["s", "ArrowDown"],
  LEFT: ["a", "ArrowLeft"],
  RIGHT: ["d", "ArrowRight"],
};

const directionCommands = reactive({
  directions: new Set<Direction>(),
});

export function useKeyInput() {
  return { keysPressed: directionCommands, inputs };
}

/**
 * Detects when a key gets pressed the keyvalue gets saved in keys
 * when key gets released the keyvalue will deleted
 * it also updates the directions
 */
function inputs() {
  window.addEventListener("keydown", (event) => {
    keys.add(event.key);
    updateDirections();
  });

  window.addEventListener("keyup", (event) => {
    keys.delete(event.key);
    updateDirections();
  });
}
/**
 * Function for updating the directions
 */
function updateDirections() {
  directionCommands.directions.clear();
  for (const [direction, keysPressed] of Object.entries(keyMap)) {
    if (keysPressed.some((key) => keys.has(key))) {
      directionCommands.directions.add(direction as Direction);
    }
  }
}
