package de.hsrm.mi.team3.swtp.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.User;
import de.hsrm.mi.team3.swtp.services.RoomBoxService;
import de.hsrm.mi.team3.swtp.services.RoomService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RoomBoxServiceTest {

  @Autowired RoomBoxService roomBoxService;
  @Autowired RoomService roomService;

  private final String SESSION_ID = "session-id-test-1";
  private final int USER_ROOMNUMBER = 1;
  private final String USERNAME = "User-One";

  private final String SESSIONID_TWO = "session-id-test-2";
  private final int USER_ROOMNUMBER_TWO = 1;
  private final String USERNAME_TWO = "User-Two";

  private final int ROOMBOX_SIZE_BEFORE_ADDITION = 0;
  private final int ROOMBOX_SIZE_AFTER_ADDITION = 1;

  private final int NEXT_ROOMNUMBER_BEFORE_ADDITION = 1;
  private final int NEXT_ROOMNUMBER_AFTER_ADDITION = 2;

  private final int ROOMNUMBER_AFTER_FIRST_ADDITION = 1;
  private final int ROOMNUMBER_AFTER_SECOND_ADDITION = 2;

  User userOne = null;
  User userTwo = null;

  @BeforeEach
  public void benutzerprofil_init() {
    roomBoxService.clearRoombox();
    assertThat(roomBoxService).isNotNull();
    userOne = new User();
    userOne.setSessionID(SESSION_ID);
    userOne.setUserName(USERNAME);
    userOne.setCurrentRoomNumber(USER_ROOMNUMBER);

    userTwo = new User();
    userTwo.setSessionID(SESSIONID_TWO);
    userTwo.setUserName(USERNAME_TWO);
    userTwo.setCurrentRoomNumber(USER_ROOMNUMBER_TWO);
  }

  @Test
  @DisplayName("RoomBoxService: AddRoom")
  void addRoomToRoomBox() {
    assertThat(roomBoxService.getRoomsFromRoomBox()).hasSize(ROOMBOX_SIZE_BEFORE_ADDITION);
    roomBoxService.addRoom();
    assertThat(roomBoxService.getRoomsFromRoomBox()).hasSize(ROOMBOX_SIZE_AFTER_ADDITION);
  }

  @Test
  @DisplayName("RoomBoxService: nextRoomNumber")
  void getNextRoomNumber() {
    assertThat(roomBoxService.nextRoomNumber()).isEqualTo(NEXT_ROOMNUMBER_BEFORE_ADDITION);
    roomBoxService.addRoom();
    assertThat(roomBoxService.nextRoomNumber()).isEqualTo(NEXT_ROOMNUMBER_AFTER_ADDITION);
  }

  @Test
  @DisplayName("RoomBoxService: getSpecificRoom")
  void getSpecificRoom() {
    Room room = roomBoxService.addRoom();
    assertThat(roomBoxService.getSpecificRoom(ROOMNUMBER_AFTER_FIRST_ADDITION)).isEqualTo(room);
  }

  @Test
  @DisplayName("RoomBoxService: getRoomsFromRoomBox")
  void getRoomsFromRoomBox() {
    Room roomOne = roomBoxService.addRoom();
    Room roomTwo = roomBoxService.addRoom();
    Map<Integer, Room> rooms = new HashMap<Integer, Room>();
    rooms.put(ROOMNUMBER_AFTER_FIRST_ADDITION, roomOne);
    rooms.put(ROOMNUMBER_AFTER_SECOND_ADDITION, roomTwo);
    assertThat(roomBoxService.getRoomsFromRoomBox()).isEqualTo(rooms);
  }

  @Test
  @DisplayName("RoomBoxService: Remove Room from Roombox if Room Present")
  void removeRoomFromRoomBox() {
    Room roomOne = roomBoxService.addRoom();
    Map<Integer, Room> rooms = new HashMap<Integer, Room>();
    rooms.put(ROOMNUMBER_AFTER_FIRST_ADDITION, roomOne);
    assertThat(roomBoxService.getRoomsFromRoomBox()).isEqualTo(rooms);

    roomBoxService.removeSpecificRoom(roomOne.getRoomNumber());
  }
}
