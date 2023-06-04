import { PerspectiveCamera } from "three";
import { reactive } from "vue";
import { VehicleCameraContext } from "./VehicleCamera";

const aspect = window.innerWidth / window.innerHeight;

export interface ICameraState {
  cam: PerspectiveCamera;
  vehicleCam: VehicleCameraContext;
}

const defaultCam = new PerspectiveCamera(70, aspect, 0.1, 2000);

const camState = reactive<ICameraState>({
  cam: defaultCam,
  vehicleCam: new VehicleCameraContext(defaultCam),
});

export function useCamera() {
  return {
    camState,
    switchCamera,
  };
}
/**
 * Switches Perspective when c is pressed.
 * Check which perspective is in use and changes it accordingly.
 * @param vCam VehicleCameraContext
 */
export function switchCamera(vCam: VehicleCameraContext) {
  window.addEventListener("keypress", (event) => {
    if (event.key === "c") {
      vCam.switchCameraState();
    }
  });
}
