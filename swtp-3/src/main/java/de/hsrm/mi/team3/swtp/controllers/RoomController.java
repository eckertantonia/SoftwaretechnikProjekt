package de.hsrm.mi.team3.swtp.controllers;

import de.hsrm.mi.team3.swtp.api.dtos.GetUserResponseDTO;
import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.Vehicle;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendMouseMessage;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendOperation;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendRoomMessage;
import de.hsrm.mi.team3.swtp.services.BackendInfoService;
import de.hsrm.mi.team3.swtp.services.RoomBoxServiceImplementation;
import de.hsrm.mi.team3.swtp.services.RoomServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class RoomController {

  @Autowired RoomBoxServiceImplementation roomBoxService;
  @Autowired RoomServiceImplementation roomService;
  @Autowired BackendInfoService backservice;
  Logger logger = LoggerFactory.getLogger(RoomController.class);

  /**
   * Used to differentiate and update specific rooms.
   *
   * @param operation Operation that is used
   * @param roomNumber Room on which the changes occured
   */
  @MessageMapping("/topic/room/{roomNumber}")
  public void roomTopic(
      @Payload BackendRoomMessage frontendRoom, @DestinationVariable int roomNumber) {
    // saves the jython script to the room
    Room backendRoom = this.roomBoxService.getSpecificRoom(roomNumber);
    if (!frontendRoom.roomName().isEmpty()) {
      this.roomService.updateRoom(
          backendRoom,
          frontendRoom.jythonScript(),
          frontendRoom.roomMap(),
          frontendRoom.roomName(),
          frontendRoom.roomNumber(),
          frontendRoom.userList());
    }
    backservice.sendRoom(
        "room/" + roomNumber,
        BackendOperation.UPDATE,
        BackendRoomMessage.from(
            backendRoom.getRoomName(),
            backendRoom.getRoomNumber(),
            backendRoom.getUserList(),
            backendRoom.getJythonScript(),
            backendRoom.getRoomMap()));
  }

  /**
   * This mapping send the mouse to all other subscribers.
   *
   * @param mouse Mouse that is being updated
   * @param roomNumber Roomnumber of room that is to be updated
   */
  @MessageMapping("/topic/mouse/{roomNumber}")
  public void sendMouseToClients(
      @Payload BackendMouseMessage mouse, @DestinationVariable int roomNumber) {
    backservice.sendMouse("mouse/" + roomNumber, mouse);
  }

  /*
   * Gets a newUser from the client, adds this user to the room and sends the room
   * to the client.
   *
   * @param newUser
   */
  @MessageMapping("/topic/user")
  public void getUser(@Payload GetUserResponseDTO user) {
    if (roomBoxService.getRoomsFromRoomBox().size() <= 4) {
      while (roomBoxService.getRoomsFromRoomBox().size() <= 4) {
        roomBoxService.addRoom();
        logger.info("RoomBox: {}", roomBoxService.getRoomsFromRoomBox());
      }
    }
  }

  @MessageMapping("/topic/vehicle/delete/{roomNumber}")
  public void deleteVehicle(@DestinationVariable int roomNumber, @Payload String sessionID) {
    this.roomService.deleteVehicleFromUser(roomNumber, sessionID);
    this.backservice.sendVehicle(
        "vehicle/" + roomNumber, sessionID, BackendOperation.DELETE, new Vehicle());
  }
}
