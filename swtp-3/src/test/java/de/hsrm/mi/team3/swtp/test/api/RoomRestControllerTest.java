package de.hsrm.mi.team3.swtp.test.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hsrm.mi.team3.swtp.domain.User;
import de.hsrm.mi.team3.swtp.services.RoomBoxService;
import de.hsrm.mi.team3.swtp.services.RoomService;
import java.util.Date;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RoomRestControllerTest {

  Logger logger = LoggerFactory.getLogger(RoomRestControllerTest.class);

  static final int FIRST_ROOM_NUMBER = 1;
  static final int SECOND_ROOM_NUMBER = 2;
  static final String MAP_JSON_STRING =
      "[{\"streetType\": \"road-cross\",\"rotation\": 90,\"posX\": 1,\"posY\": 1}]";
  static final String USER_SESSION_ID = "sid";
  static final String USER_SESSION_ID_TWO = "sid2";
  static final String NONEXISTENT_SESSION_ID = "nonexistentsid";
  static final String USER_NAME = "username";
  static final String USER_NAME_TWO = "username2";
  static final long LOGIN_TIME = new Date().getTime();

  @Autowired private MockMvc mockMvc;

  @Autowired RoomBoxService roomBoxService;

  @Autowired RoomService roomService;

  @BeforeEach
  void initRoomRestTest() {
    roomBoxService.clearRoombox();
    roomBoxService.addRoom();
    roomBoxService.addRoom();
  }

  @Test
  @DisplayName("RoomRestController: /roomlist returns actual roomlist")
  void restRouteRoomlistShouldReturnRoomlistDTO() throws Exception {
    mockMvc
        .perform(get("/api/roomlist").contentType("application/json"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("[0].roomNumber", is(1)))
        .andExpect(jsonPath("[1].roomNumber", is(2)));
  }

  @Test
  @DisplayName(
      "RoomRestController: /room/map/{number} returns rooms map, or [] in case of empty map")
  void restRouteRoomNumberReturnsRoomMap() throws Exception {
    mockMvc
        .perform(get("/api/room/map/1").contentType("application/json"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(0)))
        .andExpect(jsonPath("$", Matchers.empty()));

    roomService.updateRoom(
        roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER),
        "",
        MAP_JSON_STRING,
        "default-name",
        FIRST_ROOM_NUMBER,
        null);

    mockMvc
        .perform(get("/api/room/map/1").contentType("application/json"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("[0].streetType", is("road-cross")))
        .andExpect(jsonPath("[0].rotation", is(90)))
        .andExpect(jsonPath("[0].posX", is(1)))
        .andExpect(jsonPath("[0].posY", is(1)));
  }

  @Test
  @DisplayName(
      "RoomRestController: /room/map/{number} returns rooms map, or [] in case of empty map")
  void restRouteChangeRoomOfUser() throws Exception {
    User user = new User(USER_SESSION_ID, 0, USER_NAME, LOGIN_TIME);
    User user2 = new User(USER_SESSION_ID_TWO, 0, USER_NAME_TWO, LOGIN_TIME);

    roomService.addNewUserToRoom(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER), user);
    assertThat(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER).getUserList())
        .containsExactlyElementsOf(List.of(user));

    assertThat(roomBoxService.getSpecificRoom(SECOND_ROOM_NUMBER).getUserList())
        .containsExactlyElementsOf(List.of());

    String contentString =
        "{\"sessionID\":\""
            + user.getSessionID()
            + "\",\"currentRoomNumber\":\""
            + user.getCurrentRoomNumber()
            + "\",\"userName\":\""
            + user.getUserName()
            + "\",\"loginTime\":\"1674821124945\"}";

    String contentStringTwo =
        "{\"sessionID\":\""
            + user2.getSessionID()
            + "\",\"currentRoomNumber\":\""
            + user2.getCurrentRoomNumber()
            + "\",\"userName\":\""
            + user2.getUserName()
            + "\",\"loginTime\":\"1674821124945\"}";

    logger.info(contentString);

    mockMvc
        .perform(post("/api/room/2").contentType("application/json").content(contentString))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(roomBoxService.getSpecificRoom(SECOND_ROOM_NUMBER).getUserList()).hasSize(1);
    assertThat(
            roomBoxService
                .getSpecificRoom(SECOND_ROOM_NUMBER)
                .getUserList()
                .get(0)
                .getCurrentRoomNumber())
        .isEqualTo(2);

    mockMvc
        .perform(post("/api/room/2").contentType("application/json").content(contentStringTwo))
        .andExpect(status().isOk());

    assertThat(roomBoxService.getSpecificRoom(SECOND_ROOM_NUMBER).getUserList())
        .size()
        .isEqualTo(2);
    assertThat(
            roomBoxService
                .getSpecificRoom(SECOND_ROOM_NUMBER)
                .getUserList()
                .get(1)
                .getCurrentRoomNumber())
        .isEqualTo(2);
  }

  @Test
  @DisplayName("RoomRestController: Logout User from a specific room")
  void restRouteLogoutUserFromRoom() throws Exception {
    User user = new User(USER_SESSION_ID, 0, USER_NAME, LOGIN_TIME);
    roomService.addNewUserToRoom(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER), user);
    assertThat(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER).getUserList())
        .containsExactlyElementsOf(List.of(user));

    mockMvc
        .perform(
            post("/api/user/logout")
                .contentType("text/plain")
                .content("{\"sessionID:\"" + user.getSessionID() + "}"))
        .andExpect(status().isOk());

    assertThat(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER).getUserList()).size().isZero();
  }
}
