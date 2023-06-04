import type { IVehicle } from "@/model/IVehicle";
import * as THREE from "three";
import config from "../../../../swtp.config.json";

export class VehicleCameraContext {
  private states: CameraState[];
  private currentStateCount = 0;
  constructor(camera: THREE.PerspectiveCamera) {
    this.states = [new ThirdPersonState(camera), new FirstPersonState(camera)];
  }

  /**
   * fullfills the updateMethode of the current cameraState
   * @param speed
   * @param vehicle
   */
  request(vehicle: IVehicle, vehicle3d: THREE.Group) {
    this.states[this.currentStateCount].update(vehicle, vehicle3d);
  }
  /**
   * switches the camera state
   */
  switchCameraState() {
    this.currentStateCount = (this.currentStateCount + 1) % 2;
  }
}

abstract class CameraState {
  protected _currentCameraPos = new THREE.Vector3();
  protected _currentCameraLookAt = new THREE.Vector3();
  protected _camera: THREE.PerspectiveCamera;

  constructor(camera: THREE.PerspectiveCamera) {
    this._camera = camera;
  }

  /**
   * Fixes camera to vehicle by updating camera position with an offset and a lookAt direction.
   * @param vehicle
   */
  public abstract update(vehicle: IVehicle, vehicle3D: THREE.Group): void;

  /**
   * calculates lookAt or offset relative to vehicle position
   * @param
   * @param vector
   * @returns vector
   */
  protected calcVectorsOfVehiclePos(
    vector: THREE.Vector3,
    vehicle: IVehicle,
    vehicle3D: THREE.Group
  ) {
    if (vehicle.speed < 0) {
      vector.set(vector.x, vector.y, -vector.z);
    }
    vector.applyQuaternion(vehicle3D.quaternion);
    vector.add(vehicle3D.position);
    return vector;
  }
}

class FirstPersonState extends CameraState {
  /**
   * Fixes camera to vehicle by updating camera position with an offset and a lookAt direction.
   * @param vehicleSpeed
   */
  public update(vehicle: IVehicle, vehicle3D: THREE.Group): void {
    const offset = config.allVehicleTypes.find(
      (v) => v.name === vehicle.vehicleType
    )?.firstPersonCameraOffset as number[];

    const lookAt = config.allVehicleTypes.find(
      (v) => v.name === vehicle.vehicleType
    )?.firstPersonCameraLookat as number[];

    const idealOffset = super.calcVectorsOfVehiclePos(
      new THREE.Vector3(offset[0], offset[1], offset[2]), //vector for camera offset in relation to vehicle
      vehicle,
      vehicle3D
    );
    const idealLookat = super.calcVectorsOfVehiclePos(
      new THREE.Vector3(lookAt[0], lookAt[1], lookAt[2]), //vector for camera lookat in relation to vehicle
      vehicle,
      vehicle3D
    );

    this._currentCameraPos.copy(idealOffset);
    this._currentCameraLookAt.copy(idealLookat);

    this._camera.position.copy(this._currentCameraPos);
    this._camera.lookAt(this._currentCameraLookAt);
  }
}

class ThirdPersonState extends CameraState {
  _prevVehicleSpeed = 0;
  protected calcVectorsOfVehiclePos(
    vector: THREE.Vector3,
    vehicle: IVehicle,
    vehicle3D: THREE.Group
  ) {
    if (vehicle.speed < -vehicle.maxSpeed + 0.01) {
      vector.set(vector.x, vector.y, -vector.z);
    }
    vector.applyQuaternion(vehicle3D.quaternion);
    vector.add(vehicle3D.position);
    return vector;
  }

  /**
   * Fixes camera to vehicle by updating camera position with an offset and a lookAt direction.
   * @param vehicleSpeed
   */
  public update(vehicle: IVehicle, vehicle3D: THREE.Group): void {
    const offset = config.allVehicleTypes.find(
      (v) => v.name === vehicle.vehicleType
    )?.thirdPersonCameraOffset as number[];
    const idealOffset = this.calcVectorsOfVehiclePos(
      new THREE.Vector3(offset[0], offset[1], offset[2]), //vector for camera offset in relation to vehicle
      vehicle,
      vehicle3D
    );
    const lookAt = config.allVehicleTypes.find(
      (v) => v.name === vehicle.vehicleType
    )?.thridPersonCameraLookat as number[];

    const idealLookat = this.calcVectorsOfVehiclePos(
      new THREE.Vector3(lookAt[0], lookAt[1], lookAt[2]), //vector for camera lookat in relation to vehicle
      vehicle,
      vehicle3D
    );
    const maxSpeedWithOffset = -vehicle.maxSpeed + 0.01;
    let lerpDuration = 0.1;
    if (
      (this._prevVehicleSpeed < maxSpeedWithOffset &&
        vehicle.speed > maxSpeedWithOffset) ||
      (this._prevVehicleSpeed > maxSpeedWithOffset &&
        vehicle.speed < maxSpeedWithOffset)
    ) {
      lerpDuration = 1;
    }
    this._currentCameraPos.lerp(idealOffset, lerpDuration);
    this._currentCameraLookAt.lerp(idealLookat, lerpDuration);

    this._camera.position.copy(this._currentCameraPos);
    this._camera.lookAt(this._currentCameraLookAt);
    this._prevVehicleSpeed = vehicle.speed;
  }
}
