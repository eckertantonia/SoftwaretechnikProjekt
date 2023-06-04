import * as THREE from "three";
import type { Scene } from "three";
import type { IStreetInformation } from "@/services/useStreets";
import { useCamera } from "./CameraManager";
import { use3DVehicle } from "../../services/use3DVehicle";
import type { VehicleCameraContext } from "./VehicleCamera";
import { RGBELoader } from "three/examples/jsm/loaders/RGBELoader";
import type { IVehicle } from "../../model/IVehicle";
import { getSessionIDFromCookie } from "@/helpers/SessionIDHelper";
import { logger } from "@/helpers/Logger";
import { FontLoader } from "three/examples/jsm/loaders/FontLoader.js";
import { TextGeometry } from "three/examples/jsm/geometries/TextGeometry.js";
import { TTFLoader } from "three/examples/jsm/loaders/TTFLoader.js";
import { useRoom } from "@/services/useRoom";
import config from "../../../../swtp.config.json";
import { useStreets } from "@/services/useStreets";

const { camState, switchCamera } = useCamera();
const { vehicleState } = use3DVehicle();
const { streetsState } = useStreets();
const { roomState } = useRoom();
type StreetBlock = IStreetInformation;
const testObjectName = "text";

/**
 * Manages Scene with all Objects
 */
export class SceneManager {
  scene: Scene;
  blockMap: Map<string, Promise<THREE.Group>>;
  data: StreetBlock[];
  streets: THREE.Group[];
  private renderer: THREE.Renderer;
  private vehicles: Map<string, THREE.Group> = new Map<string, THREE.Group>(); // list of all Object u should update every frame.
  private botVehicles: Map<string, THREE.Group> = new Map<
    string,
    THREE.Group
  >();
  private vehicleCamera: VehicleCameraContext =
    camState.vehicleCam as VehicleCameraContext;

  constructor(
    scene: Scene,
    blockMap: Map<string, Promise<THREE.Group>>,
    renderer: THREE.Renderer
  ) {
    this.scene = scene;
    this.blockMap = blockMap;
    this.streets = [];
    this.data = JSON.parse(JSON.stringify(streetsState.streets as any));
    this.renderer = renderer;
  }
  /**
   * runs all functions to create the scene
   */
  initScene() {
    this.createLandscape();
    this.createGrid();
    this.handleRender();
    this.addSkybox();
    switchCamera(this.vehicleCamera);
  }

  /**
   *
   * @param objectKey
   * @param posX
   * @param posY
   * @param posZ
   * @param rotation
   *
   * adds loaded tile to Scene
   */
  addBlockToScene(
    objectKey: string,
    posX: number,
    posY: number,
    posZ: number,
    rotation: number
  ) {
    const blockPromise = this.blockMap.get(objectKey);
    if (blockPromise != undefined) {
      blockPromise
        ?.then((block) => {
          const clonedBlock = block.clone();
          clonedBlock.position.set(posX, posY, posZ);
          clonedBlock.rotateY(rotation);
          this.scene.add(clonedBlock);
          if (objectKey != "landscape") {
            this.streets.push(clonedBlock);
          }
        })
        .catch((error) => {
          this.getErrorBlock(posX, posY, posZ);
          logger.error(error);
        });
    } else {
      this.getErrorBlock(posX, posY, posZ);
    }
  }

  /**
   *
   * errorblock when promise could not be loaded
   *
   * @param posX
   * @param posY
   * @param posZ
   *
   */
  private getErrorBlock(posX: number, posY: number, posZ: number) {
    const geometry = new THREE.BoxGeometry(
      config.blocksize,
      1,
      config.blocksize
    );
    const material = new THREE.MeshBasicMaterial({ color: "#FF0000" });
    const cube = new THREE.Mesh(geometry, material);
    cube.position.set(posX, posY, posZ);
    this.scene.add(cube);
  }

  /**
   * generates the objects according to the (json-)array
   */
  createGrid() {
    this.streets.forEach((block: THREE.Group) => {
      this.scene.remove(block);
    });
    this.streets = [];
    this.data.forEach((streetBlock: StreetBlock) => {
      this.addBlockToScene(
        streetBlock.streetType,
        (streetBlock.posX - 1 - config.gridSize / 2) * config.blocksize,
        0,
        (streetBlock.posY - 1 - config.gridSize / 2) * config.blocksize,
        Number(streetBlock.rotation) * (Math.PI / 180)
      );
    });
  }

  /**
   * creates landscape with ground
   */
  createLandscape() {
    this.addBlockToScene("landscape", 0, -17, 0, 0);
  }
  /**
   * adds new car
   */
  addVehicle(
    vehicle: IVehicle,
    vehicleSessionId: string,
    vehicleMap: Map<string, THREE.Group>
  ) {
    const blockPromise = this.blockMap.get(vehicle.vehicleType);
    logger.log("Type", this.blockMap.get(vehicle.vehicleType));
    logger.log("Block", blockPromise);
    if (blockPromise !== undefined && !vehicleMap.has(vehicleSessionId)) {
      blockPromise
        ?.then((block) => {
          const car = block.clone();

          if (vehicleSessionId.includes("bot")) {
            logger.log("add Bot");
          }
          car.position.set(
            vehicle.postitionX,
            vehicle.postitionY,
            vehicle.postitionZ
          );
          car.rotation.set(
            vehicle.rotationX,
            vehicle.rotationY,
            vehicle.rotationZ
          );
          this.scene.add(car);

          if (vehicleSessionId === getSessionIDFromCookie()) {
            this.vehicleCamera.request(vehicle, car);
          } else if (!vehicleSessionId.includes(config.botIdentifier)) {
            this.addTextToVehicle(
              roomState.room.userList.find(
                (x) => x.sessionID == vehicleSessionId
              )!.userName,
              vehicle.vehicleType,
              car
            );
          }
          vehicleMap.set(vehicleSessionId, car);
        })
        .catch((error) => {
          this.getErrorBlock(0, 0, 0);
          logger.error(error);
        });
    } else {
      this.getErrorBlock(0, 0, 0);
    }
  }
  /**
   * adds skybox to the scene.
   */
  addSkybox() {
    const scene = this.scene;
    new RGBELoader()
      .setPath("/assets/skybox/")
      .load("skylight.hdr", function (texture: any) {
        texture.mapping = THREE.EquirectangularReflectionMapping;

        scene.background = texture;
        scene.environment = texture;
      });
  }
  /**
   *
   * Renders and animates the scene.
   *
   */
  handleRender() {
    const animate = () => {
      this.updateVehicleMap(
        vehicleState.vehicles as Map<string, IVehicle>,
        this.vehicles
      );
      for (const [key, val] of this.vehicles) {
        this.updateVehicle(
          val,
          vehicleState.vehicles.get(key) as IVehicle,
          key,
          config.VehicleLerpSpeed
        );
      }
      this.updateVehicleMap(
        vehicleState.botVehicle as Map<string, IVehicle>,
        this.botVehicles
      );
      for (const [key, val] of this.botVehicles) {
        this.updateVehicle(
          val,
          vehicleState.botVehicle.get(key) as IVehicle,
          key,
          config.botVehicleLerpSpeed
        );
      }
      //every vehicle gets rendered
      this.renderer.render(this.scene, camState.cam as THREE.PerspectiveCamera);
      requestAnimationFrame(animate);
    };
    animate();
  }

  /**
   *
   * updates the vehicle with lerping
   *
   * @param threeVehicle
   */
  private updateVehicle(
    threeVehicle: THREE.Group,
    vehicle: IVehicle,
    sessionID: string,
    lerpSpeed: number
  ) {
    const quaternion = new THREE.Quaternion();
    const destination = new THREE.Vector3(
      vehicle.postitionX,
      vehicle.postitionY,
      vehicle.postitionZ
    );

    const newRotation = new THREE.Euler(
      vehicle.rotationX,
      vehicle.rotationY,
      vehicle.rotationZ,
      "XYZ"
    );

    const newQuaterion = quaternion.setFromEuler(newRotation);
    threeVehicle.quaternion.slerp(newQuaterion, lerpSpeed);
    threeVehicle.position.lerp(destination, lerpSpeed);

    if (sessionID === getSessionIDFromCookie()) {
      this.vehicleCamera.request(vehicle, threeVehicle);
    } else {
      threeVehicle
        .getObjectByName(testObjectName)
        ?.lookAt(camState.cam.position);
    }
  }

  /**
   * checks if vehicles are added or removed and updates the map
   */
  private updateVehicleMap(
    vehicleMap: Map<string, IVehicle>,
    threeDvehicles: Map<string, THREE.Group>
  ) {
    for (const [key, val] of vehicleMap) {
      logger.log("Vehicle von " + key + " wurde hinzugefügt");
      if (!threeDvehicles.has(key)) {
        this.addVehicle(val, key, threeDvehicles);
      }
    }
    for (const [key, val] of threeDvehicles) {
      if (!vehicleMap.has(key)) {
        logger.log("Vehicle von " + key + " wurde gelöscht");
        this.scene.remove(val);
        threeDvehicles.delete(key);
      }
    }
  }

  private addTextToVehicle(
    text: string,
    vehicleType: string,
    vehicle: THREE.Group
  ) {
    const textHightOverVehicle = config.allVehicleTypes.find(
      (v) => v.name === vehicleType
    )?.textHightOverVehicle as number;
    const fontLoader = new FontLoader();
    const ttfloader = new TTFLoader();
    ttfloader.load(config.fontPath, function (json) {
      const font = fontLoader.parse(json);
      const textGeometry = new TextGeometry(text, {
        font: font,
        height: 0.1,
        size: config.fontSize,
      });
      textGeometry.center();
      const textmesh = new THREE.Mesh(textGeometry);

      textmesh.position.set(0, textHightOverVehicle, 0);

      textmesh.name = testObjectName;
      textmesh.quaternion.copy(camState.cam.quaternion);
      vehicle.add(textmesh);
    });
  }

  public updateData(newdata: StreetBlock[]) {
    this.data = JSON.parse(JSON.stringify(newdata));
    this.createGrid();
  }
}
