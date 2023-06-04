package de.hsrm.mi.team3.swtp.controllers;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.Vehicle;
import de.hsrm.mi.team3.swtp.domain.VehicleCommands;
import de.hsrm.mi.team3.swtp.domain.messaging.BackenVehicleCommandMessage;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendOperation;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendRoomMessage;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendVehiclePositionMessage;
import de.hsrm.mi.team3.swtp.services.BackendInfoService;
import de.hsrm.mi.team3.swtp.services.RoomBoxService;
import de.hsrm.mi.team3.swtp.services.RoomService;
import de.hsrm.mi.team3.swtp.services.VehicleService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class VehicleController {
  Logger logger = LoggerFactory.getLogger(VehicleController.class);
  @Autowired VehicleService vehicleService;

  @Autowired BackendInfoService bInfoService;

  @Autowired RoomBoxService roomBoxService;

  @Autowired RoomService roomService;

  /**
   * Receives a command from client to execute vehicleservice Methods
   *
   * @param commands
   * @param roomNumber
   */
  @MessageMapping("topic/3d/commands/{roomNumber}")
  public void getCars(
      @Payload BackenVehicleCommandMessage commandVehicleMessage,
      @DestinationVariable int roomNumber) {

    List<VehicleCommands> commands = commandVehicleMessage.commands();

    Vehicle vehicle =
        roomService
            .getUserByID(roomNumber, commandVehicleMessage.userSessionId())
            .get()
            .getVehicle();
    Room room = roomBoxService.getSpecificRoom(roomNumber);
    // if there is no vehicle -> skip
    if (vehicle == null) {
      return;
    }
    if (!commands.contains(VehicleCommands.FORWARD)
        && !commands.contains(VehicleCommands.BACKWARD)) {
      vehicleService.carRunOutSpeed(vehicle, room);
    }
    if (commands.contains(VehicleCommands.FORWARD)) {
      vehicleService.moveForward(vehicle, room);
    }
    if (commands.contains(VehicleCommands.BACKWARD)) {
      vehicleService.moveBackward(vehicle, room);
    }
    if (commands.contains(VehicleCommands.LEFT)) {
      vehicleService.rotateLeft(vehicle);
    }
    if (commands.contains(VehicleCommands.RIGHT)) {
      vehicleService.rotateRight(vehicle);
    }
    bInfoService.sendVehicle(
        "vehicle/" + roomNumber,
        commandVehicleMessage.userSessionId(),
        BackendOperation.UPDATE,
        vehicle);
    bInfoService.sendRoom(
        "room/" + roomNumber,
        BackendOperation.UPDATE,
        BackendRoomMessage.from(
            room.getRoomName(),
            room.getRoomNumber(),
            room.getUserList(),
            room.getJythonScript(),
            room.getRoomMap()));
  }

  /** Creates new vehicle at drop position */
  @MessageMapping("topic/3d/createvehicle/{roomNumber}")
  public void createVehicle(
      @Payload BackendVehiclePositionMessage newVehicleMessage,
      @DestinationVariable int roomNumber) {

    Vehicle vehicle =
        roomService.getUserByID(roomNumber, newVehicleMessage.userSessionId()).get().getVehicle();
    if (vehicle == null) {
      double[] vector = new double[] {newVehicleMessage.posX(), 0, newVehicleMessage.posZ()};
      roomService
          .getUserByID(roomNumber, newVehicleMessage.userSessionId())
          .get()
          .setVehicle(new Vehicle(newVehicleMessage.vehicleType(), vector));
      vehicle =
          roomService.getUserByID(roomNumber, newVehicleMessage.userSessionId()).get().getVehicle();
    }
    bInfoService.sendVehicle(
        "vehicle/" + roomNumber,
        newVehicleMessage.userSessionId(),
        BackendOperation.CREATE,
        vehicle);

    if (!roomBoxService.getSpecificRoom(roomNumber).isJythonRunning()) {
      roomService.executeJython(roomBoxService.getSpecificRoom(roomNumber));
    }
  }
}
