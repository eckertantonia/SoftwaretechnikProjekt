import { logger } from "@/helpers/Logger";
import type * as THREE from "three";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { reactive } from "vue";

const gltfloader = new GLTFLoader();
const glbState = reactive<GLBState>({
  blockMap: new Map(),
});

export function useGLB() {
  return { glbState, loadModel };
}

export interface GLBState {
  blockMap: Map<string, Promise<THREE.Group>>;
}

/**
 * loads the block assets from filepath
 * returns a Promise
 */
async function loadModel(filepath: string): Promise<THREE.Group> {
  return new Promise((resolve, reject) => {
    try {
      gltfloader.load(
        filepath,
        (data) => resolve(data.scene),
        function (xhr) {
          logger.log((xhr.loaded / xhr.total) * 100 + "% loaded");
        },
        reject
      );
    } catch (error) {
      logger.error(error);
    }
  });
}
