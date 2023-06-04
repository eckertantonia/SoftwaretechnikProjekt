<template>
  <Renderer
    ref="renderer"
    resize="window"
    antialias
    :orbit-ctrl="{
      autoRotate: false,
      enableDamping: true,
      dampingFactor: 0.05,
    }"
  >
    <Camera
      :position="{ y: 1500, z: 400 }"
      :look-at="{ x: 0, y: 0, z: 0 }"
      :near="0"
      :far="5500"
    />
    <Scene ref="scene" background="#fff">
      <PointLight :position="{ y: 5000, z: 50 }" />
    </Scene>
  </Renderer>
</template>

<script lang="ts">
import { Camera, PointLight, Renderer, Scene } from "troisjs";
import { useGLB } from "@/services/glbBlockLoader";
import { SceneManager } from "@/views/threeD/SceneManager";
import config from "../../../../swtp.config.json";
import { use3DVehicle } from "@/services/use3DVehicle";
import { useVehicleCommands } from "../../services/useVehicleCommands";
import { useKeyInput } from "./keyInputHandler";
import { useStreets } from "@/services/useStreets";
import { useRoom } from "@/services/useRoom";
import { jsonToState } from "@/services/JSONparser";

const { glbState, loadModel } = useGLB();
const { publishVehicleCommands } = useVehicleCommands();
const { keysPressed, inputs } = useKeyInput();
const { receiveVehicle } = use3DVehicle();
const { streetsState } = useStreets();
const { roomState, receiveRoom } = useRoom();
const sendInterval = 100;

config.miscModels.forEach((element) => {
  glbState.blockMap.set(element.name, loadModel(element.glbPath));
});

config.streetTypes.forEach((element) => {
  if (element.glbPath) {
    glbState.blockMap.set(element.name, loadModel(element.glbPath));
  }
});

config.allVehicleTypes.forEach((element) => {
  if (element.glbPath) {
    glbState.blockMap.set(element.name, loadModel(element.glbPath));
  }
});

config.botVehicles.forEach((element) => {
  if (element.glbPath) {
    glbState.blockMap.set(element.name, loadModel(element.glbPath));
  }
});

export default {
  components: {
    Camera,
    PointLight,
    Renderer,
    Scene,
  },
  methods: {
    updateMap(sceneManager: SceneManager) {
      if (
        JSON.stringify(streetsState.streets) !=
        JSON.stringify(sceneManager.data)
      ) {
        sceneManager.updateData(streetsState.streets as any);
      }
    },
  },
  mounted() {
    receiveRoom();
    receiveVehicle();
    const blockMap = glbState.blockMap;
    const scene = (this.$refs.scene as typeof Scene).scene;
    const renderer = (this.$refs.renderer as any).renderer;
    const sceneManager = new SceneManager(scene, blockMap, renderer);
    sceneManager.initScene();
    inputs();
    //sends VehicleCommands to backend in a set interval
    setInterval(() => {
      publishVehicleCommands(Array.from(keysPressed.directions));
      if (
        JSON.stringify(roomState.room.roomMap) !=
        JSON.stringify(streetsState.streets)
      ) {
        jsonToState(roomState.room.roomMap);
        this.updateMap(sceneManager);
      }
    }, sendInterval);
  },
};
</script>
