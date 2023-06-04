package de.hsrm.mi.team3.swtp.domain.messaging;

import de.hsrm.mi.team3.swtp.domain.Vehicle;
import de.hsrm.mi.team3.swtp.domain.VehicleBot;

/*
 * record for sending and receiving Vehicles
 */
public record BackendVehicleMessage(
    BackendOperation operator,
    String userSessionId,
    String vehicleType,
    double postitionX,
    double postitionY,
    double postitionZ,
    double rotationX,
    double rotationY,
    double rotationZ,
    double speed,
    double maxSpeed) {

  public static BackendVehicleMessage from(
      BackendOperation operator, String userSessionId, Vehicle vehicle) {
    return new BackendVehicleMessage(
        operator,
        userSessionId,
        vehicle.getVehicleType(),
        vehicle.getPosVector()[0],
        vehicle.getPosVector()[1],
        vehicle.getPosVector()[2],
        vehicle.getRotationX(),
        vehicle.getRotationY(),
        vehicle.getRotationZ(),
        vehicle.getCurrentSpeed(),
        Vehicle.MAX_SPEED);
  }

  public static BackendVehicleMessage from(
      BackendOperation operator, String botID, VehicleBot vehicle) {
    return new BackendVehicleMessage(
        operator,
        botID,
        vehicle.getVehicleType().getType(),
        vehicle.getCurrentPos()[0],
        0,
        vehicle.getCurrentPos()[1],
        0,
        vehicle.getCurrentRotation(),
        0,
        0,
        0);
  }
}
