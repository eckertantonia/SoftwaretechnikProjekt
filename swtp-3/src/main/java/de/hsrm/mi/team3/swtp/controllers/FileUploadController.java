package de.hsrm.mi.team3.swtp.controllers;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendOperation;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendRoomMessage;
import de.hsrm.mi.team3.swtp.services.BackendInfoService;
import de.hsrm.mi.team3.swtp.services.RoomBoxService;
import de.hsrm.mi.team3.swtp.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

  @Autowired private RoomBoxService roomBoxService;

  @Autowired private RoomService roomService;

  @Autowired private BackendInfoService backservice;

  Logger logger = LoggerFactory.getLogger(FileUploadController.class);

  /**
   * Function to save the received script file to the selected room and update all other rooms.
   *
   * @param file The received File
   * @param roomNumber The room number
   */
  @PostMapping("/api/upload/{roomNumber}")
  public void uploadJythonFile(
      @RequestParam("file") MultipartFile file, @PathVariable("roomNumber") int roomNumber) {

    Room room = this.roomBoxService.getSpecificRoom(roomNumber);

    this.roomService.saveScriptToRoom(file, room);

    backservice.sendRoom(
        "room/" + roomNumber,
        BackendOperation.UPDATE,
        BackendRoomMessage.from(
            room.getRoomName(),
            room.getRoomNumber(),
            room.getUserList(),
            new String(room.getJythonScript().getBytes()),
            room.getRoomMap()));
  }

  @GetMapping("/api/upload/{roomNumber}")
  public String getRoomScript(@PathVariable("roomNumber") int roomNumber) {
    return this.roomBoxService.getSpecificRoom(roomNumber).getJythonScript();
  }
}
