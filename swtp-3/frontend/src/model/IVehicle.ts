import type { MessageOperator } from "./MessageOperators";

export interface IVehicle {
  vehicleType: string;
  postitionX: number;
  postitionY: number;
  postitionZ: number;
  rotationX: number;
  rotationY: number;
  rotationZ: number;
  speed: number;
  maxSpeed: number;
}
export class Vehicle implements IVehicle {
  vehicleType: string;
  postitionX: number;
  postitionY: number;
  postitionZ: number;
  rotationX: number;
  rotationY: number;
  rotationZ: number;
  speed: number;
  maxSpeed: number;
  constructor(
    vehicleType: string,
    postitionX: number,
    postitionY: number,
    postitionZ: number,
    rotationX: number,
    rotationY: number,
    rotationZ: number,
    speed: number,
    maxSpeed: number
  ) {
    this.vehicleType = vehicleType;
    this.postitionX = postitionX;
    this.postitionY = postitionY;
    this.postitionZ = postitionZ;
    this.rotationX = rotationX;
    this.rotationY = rotationY;
    this.rotationZ = rotationZ;
    this.speed = speed;
    this.maxSpeed = maxSpeed;
  }
}
export interface IVehicleMessage {
  operator: MessageOperator;
  userSessionId: string;
  vehicleType: string;
  postitionX: number;
  postitionY: number;
  postitionZ: number;
  rotationX: number;
  rotationY: number;
  rotationZ: number;
  speed: number;
  maxSpeed: number;
}
