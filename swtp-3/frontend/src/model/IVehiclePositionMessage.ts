export interface IVehiclePositionMessage {
  userSessionId: string;
  vehicleType: string;
  posX: number;
  posZ: number;
}
export class VehiclePositionMessage implements IVehiclePositionMessage {
  userSessionId: string;
  vehicleType: string;
  posX: number;
  posZ: number;
  constructor(
    userSessionId: string,
    vehicleType: string,
    posX: number,
    posZ: number
  ) {
    this.userSessionId = userSessionId;
    this.vehicleType = vehicleType;
    this.posX = posX;
    this.posZ = posZ;
  }
}
