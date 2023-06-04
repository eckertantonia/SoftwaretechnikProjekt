package de.hsrm.mi.team3.swtp.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.User;
import de.hsrm.mi.team3.swtp.services.RoomBoxService;
import de.hsrm.mi.team3.swtp.services.RoomService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RoomServiceTest {

  @Autowired RoomService roomService;

  @Autowired RoomBoxService roomBoxService;

  private final String ROOMNAMEONE = "RoomNameOne";
  private final String ROOMNAME_POST_UPDATE = "RoomNamePostUpdate";
  private final int ROOMNUMBERONE = 1;

  private final String ROOMNAMETWO = "RoomNameTwo";
  private final int ROOMNUMBERTWO = 2;

  private final String SESSIONID = "session-id-test-1";
  private final int USERROOMNUMBER = 1;
  private final String USERNAME = "User-One";

  private final String SESSIONIDTWO = "session-id-test-2";
  private final int USERROOMNUMBERTWO = 1;
  private final String USERNAMETWO = "User-Two";

  private final String NOTPRESENTSESSIONID = "not-present";

  private final int USERLISTSIZE_BEFORE_ADDITION = 0;
  private final int USERLIST_SIZE_MIDADDITON = 1;
  private final int USERLISTSIZE_AFTER_ADDITION = 2;

  private final MockMultipartFile FIRST_JYTHON_FILE =
      new MockMultipartFile("data", "jythonScript.py", "text/plain", "print('test!')".getBytes());

  User userOne = null;
  User userTwo = null;

  Room roomOne = null;
  Room roomTwo = null;

  @BeforeEach
  public void benutzerprofil_init() {
    assertThat(roomService).isNotNull();
    userOne = new User();
    userOne.setSessionID(SESSIONID);
    userOne.setUserName(USERNAME);
    userOne.setCurrentRoomNumber(USERROOMNUMBER);

    userTwo = new User();
    userTwo.setSessionID(SESSIONIDTWO);
    userTwo.setUserName(USERNAMETWO);
    userTwo.setCurrentRoomNumber(USERROOMNUMBERTWO);

    roomOne = new Room(ROOMNAMEONE, ROOMNUMBERONE);
    roomTwo = new Room(ROOMNAMETWO, ROOMNUMBERTWO);
  }

  @Test
  @DisplayName("Room: User present in Room after adding")
  void roomAddUser() {
    assertThat(roomService.getUserList(roomOne)).hasSize(USERLISTSIZE_BEFORE_ADDITION);
    roomService.addNewUserToRoom(roomOne, userOne);
    assertThat(roomService.getUserList(roomOne)).containsExactlyElementsOf(List.of(userOne));

    assertThat(roomService.getUserList(roomOne)).hasSize(USERLIST_SIZE_MIDADDITON);
    roomService.addNewUserToRoom(roomOne, userTwo);
    assertThat(roomService.getUserList(roomOne))
        .containsExactlyElementsOf(List.of(userOne, userTwo));

    assertThat(roomService.getUserList(roomOne)).hasSize(USERLISTSIZE_AFTER_ADDITION);
    assertThat(roomService.getUserList(roomOne))
        .containsExactlyElementsOf(List.of(userOne, userTwo));
  }

  @Test
  @DisplayName("Room: User not present in room after removing")
  void roomRemoveUser() {
    roomService.addNewUserToRoom(roomOne, userOne);
    assertThat(roomService.getUserList(roomOne)).containsExactlyElementsOf(List.of(userOne));

    roomService.removeUserFromRoom(roomOne, userOne);
    assertThat(roomService.getUserList(roomOne)).hasSize(USERLISTSIZE_BEFORE_ADDITION);
    assertThat(roomService.getUserList(roomOne)).containsExactlyElementsOf(List.of());
  }

  @Test
  @DisplayName("Room: Save Java Script")
  void saveJythonScriptToRoom() throws IOException {
    roomService.saveScriptToRoom(FIRST_JYTHON_FILE, roomOne);
    assertThat(roomOne.getJythonScript()).isNotEmpty();
    assertThat(roomOne.getJythonScript()).isEqualTo(new String(FIRST_JYTHON_FILE.getBytes()));
  }

  @Test
  @DisplayName("Room: Update Room")
  void updateRoom() {
    roomService.updateRoom(roomOne, "", "", ROOMNAME_POST_UPDATE, ROOMNUMBERONE, List.of());
    assertThat(roomService.getUserList(roomOne)).containsExactlyElementsOf(List.of());
    assertThat(roomOne.getRoomMap()).isEmpty();
    assertThat(roomOne.getJythonScript()).isEmpty();
    assertThat(roomOne.getRoomName()).isEqualTo(ROOMNAME_POST_UPDATE);
    assertThat(roomOne.getRoomNumber()).isEqualTo(ROOMNUMBERONE);
    assertThat(roomOne.getUserList()).containsExactlyElementsOf(List.of());
  }

  @Test
  @DisplayName(
      "Room: Get user by sessionID if room is not known and null if user with given sessionID is not present")
  void getUserFromRoomBox() {
    Room roomOne = roomBoxService.addRoom();
    roomOne.addUserToList(userOne);
    roomService.addNewUserToRoom(roomOne, userOne);

    Optional<User> getUserBySessionIdPresent = roomBoxService.getUserBySessionID(SESSIONID);
    assertThat(getUserBySessionIdPresent).isPresent().contains(userOne);

    Optional<User> getUserBySessionIdNotPresent =
        roomBoxService.getUserBySessionID(NOTPRESENTSESSIONID);
    assertThat(getUserBySessionIdNotPresent).isEmpty();
  }

  @Test
  @DisplayName("RoomService: executeJython(Room room)")
  void executeJythonTest() {

    roomService.saveScriptToRoom(FIRST_JYTHON_FILE, roomOne);
    assertThat(roomOne.getJythonScript()).isNotNull();

    roomService.executeJython(roomOne);
    assertThat(roomOne.isJythonRunning()).isFalse();
    assertThat(roomOne.getVehicleBots()).isEmpty();
  }
}
