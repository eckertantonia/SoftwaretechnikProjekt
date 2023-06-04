package de.hsrm.mi.team3.swtp.services;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.RoomBox;
import de.hsrm.mi.team3.swtp.domain.User;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/** implementation of RoomBoxService */
@Service
public class RoomBoxServiceImplementation implements RoomBoxService {

  Logger logger = LoggerFactory.getLogger(RoomBoxServiceImplementation.class);
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("scopes.xml");

  /**
   * determines next room number
   *
   * @return next valid room number
   */
  public int nextRoomNumber() {
    RoomBox roomBox = getRoomBoxSingelton();
    if (roomBox.getRooms().size() + 1 == 0) {
      return 1;
    }
    return roomBox.getRooms().size() + 1;
  }

  /**
   * checks if room already exists or creates new room
   *
   * @return existing or new room
   */
  public Room addRoom() {
    RoomBox roomBox = getRoomBoxSingelton();
    int newRoomNumber = this.nextRoomNumber();
    roomBox.addRoom(newRoomNumber, new Room(newRoomNumber));
    return this.getRoomsFromRoomBox().get(newRoomNumber);
  }

  /**
   * get map with all existing romms
   *
   * @return map with all existing romms
   */
  @Override
  public Map<Integer, Room> getRoomsFromRoomBox() {
    RoomBox roomBox = getRoomBoxSingelton();
    return roomBox.getRooms();
  }

  /**
   * get room by roomnumber
   *
   * @param roomNumber requested room number
   * @return requested room according to roomnumber
   */
  public Room getSpecificRoom(int roomNumber) {
    return this.getRoomsFromRoomBox().get(roomNumber);
  }

  /** clears the roombox */
  public void clearRoombox() {
    RoomBox roomBox = getRoomBoxSingelton();
    roomBox.getRooms().clear();
  }

  /**
   * get RoomBox Singelton instance
   *
   * @return RoomBox Singelton
   */
  public RoomBox getRoomBoxSingelton() {
    return (RoomBox) applicationContext.getBean("roomBoxSingleton");
  }

  /**
   * @param sessionID SessionID of wanted User
   * @return Either the User with the given SessionID or null if not present
   */
  public Optional<User> getUserBySessionID(String sessionID) {
    Optional<User> userOpt = Optional.empty();
    for (Room room : this.getRoomsFromRoomBox().values()) {
      for (User user : room.getUserList()) {
        if (user.getSessionID().contains(sessionID)) {
          userOpt = Optional.of(user);
        }
      }
    }
    return userOpt;
  }

  @Override
  public void resetEverything() {
    int countRooms = this.getRoomsFromRoomBox().size();
    for (int i = 0; i < countRooms; i++) {
      for (int j = 0; j < this.getSpecificRoom(i + 1).getUserList().size(); j++) {
        User user = this.getSpecificRoom(i + 1).getUserList().get(j);
        this.getSpecificRoom(i + 1).removeUserFromList(user);
        user.setCurrentRoomNumber(0);
      }
      Room roomToRemove = this.getSpecificRoom(i + 1);
      this.getRoomBoxSingelton().removeRoom(roomToRemove);
    }
  }

  @Override
  public void removeSpecificRoom(int roomNumber) {
    this.getRoomBoxSingelton().removeRoom(this.getSpecificRoom(roomNumber));
  }
}
