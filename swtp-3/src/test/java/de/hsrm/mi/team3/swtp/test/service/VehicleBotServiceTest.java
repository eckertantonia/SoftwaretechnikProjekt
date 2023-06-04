package de.hsrm.mi.team3.swtp.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.hsrm.mi.team3.swtp.domain.Roadmap;
import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.User;
import de.hsrm.mi.team3.swtp.services.VehicleBotService;
import java.util.Timer;
import java.util.TimerTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class VehicleBotServiceTest {

  @Autowired VehicleBotService vehicleBotService;

  Roadmap roadMap = null;
  static final String jsonmap =
      "[{"
          + '"'
          + "streetType"
          + '"'
          + ":"
          + '"'
          + "road-straight"
          + '"'
          + ","
          + '"'
          + "rotation"
          + '"'
          + ":90,"
          + '"'
          + "posX"
          + '"'
          + ":2,"
          + '"'
          + "posY"
          + '"'
          + ":2},"
          + "{"
          + '"'
          + "streetType"
          + '"'
          + ":"
          + '"'
          + "road-curve"
          + '"'
          + ","
          + '"'
          + "rotation"
          + '"'
          + ":270,"
          + '"'
          + "posX"
          + '"'
          + ":2,"
          + '"'
          + "posY"
          + '"'
          + ":3},"
          + "{"
          + '"'
          + "streetType"
          + '"'
          + ":"
          + '"'
          + "sidewalk-straight"
          + '"'
          + ","
          + '"'
          + "rotation"
          + '"'
          + ":0,"
          + '"'
          + "posX"
          + '"'
          + ":3,"
          + '"'
          + "posY"
          + '"'
          + ":3}]";

  Room room = new Room("RoomOne", 1);

  private final String ROUTE = "l,r,l,s";
  private final String TYPE = "bike";

  @BeforeEach
  public void initRoom() {
    assertThat(vehicleBotService).isNotNull();
    room.setRoomMap(jsonmap);
    assertThat(room.getRoomMap()).isNotNull();
    vehicleBotService.setRoom(room);
  }

  @Test
  @DisplayName("VehicleBot: createBot()")
  void createBotTest() {
    vehicleBotService.createBot();
    assertThat(room.getVehicleBots()).isNotNull();
  }

  @Test
  @DisplayName("VehicleBot: createBotWithRoute(String route)")
  void createBotWithRouteTest() {
    vehicleBotService.createBotWithRoute(ROUTE);
    assertThat(room.getVehicleBots()).isNotNull();
  }

  @Test
  @DisplayName("VehicleBot: createBotWithType(VehicleType type)")
  void createBotWithTypeTest() {
    vehicleBotService.createBotWithType(TYPE);
    assertThat(room.getVehicleBots()).isNotNull();
  }

  @Test
  @DisplayName("VehicleBot: createBotWithRouteAndType(String route, String vehicleType)")
  void createBotWithRouteAndTypeTest() {
    vehicleBotService.createBotWithRouteAndType(ROUTE, TYPE);
    assertThat(room.getVehicleBots()).isNotNull();
  }

  @Test
  @DisplayName("VehicleBot: driveBot()")
  void driveBotTest() {
    assertThat(room.getUserList()).isEmpty();
    vehicleBotService.createBot();
    vehicleBotService.start();
    assertThat(room.getVehicleBots()).isEmpty();
    assertThat(room.isJythonRunning()).isFalse();

    User user = new User();
    room.addUserToList(user);
    assertThat(room.getUserList()).isNotEmpty();
    assertThat(room.getVehicleBots()).isNotNull();
    Timer timer = new Timer();
    timer.schedule(
        new TimerTask() {

          @Override
          public void run() {
            room.removeUserFromList(user);
          }
        },
        500);
    vehicleBotService.start();
    assertThat(room.getUserList()).isEmpty();
  }
}
