package de.hsrm.mi.team3.swtp.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.hsrm.mi.team3.swtp.domain.Roadmap;
import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.VehicleBot;
import de.hsrm.mi.team3.swtp.domain.VehicleNeighbour;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
class VehicleBotTest {

  Room testRoom = null;
  Room testRoom2 = null;
  VehicleBot testBot1 = null;
  VehicleBot testBot2 = null;
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
          + "road-straight"
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

  @Test
  @DisplayName("VehicleBot: RoadMap init")
  void roadMapInit() {
    roadMap = new Roadmap(jsonmap);
    assertThat(roadMap.getStreetBlock(1, 1).getType()).isEqualTo("road-straight");
  }

  @Test
  @DisplayName("VehicleBot: getNeighbours()")
  void checkNeighbours() {
    testRoom = new Room(0);
    testRoom.setRoomMap(jsonmap);
    testBot1 = new VehicleBot(testRoom);
    testBot1.setCurrentRotation(90);
    testBot1.setCurrentPos(2, 2);
    assertThat(testBot1.getCurrentStreetBlock()).isNotNull();
    testBot1.setCurrentPos(1, 2);
    assertThat(testBot1.getNeighbours()).isNotNull();
    testBot1.refreshNeighbours();
    assertThat(testBot1.getNeighbours().get(VehicleNeighbour.VEHICLETOP).getType())
        .isEqualTo("road-straight");
  }

  @Test
  @DisplayName("VehicleBot: random Vehicle-Model check")
  void randomModelCheck() {
    testRoom = new Room(0);
    testRoom.setRoomMap(jsonmap);
    testBot1 = new VehicleBot(testRoom);
    assertThat(testBot1.getVehicleType()).isNotNull();
  }

  @Test
  @DisplayName("VehicleBot: drive()")
  void moveCheck() {
    testRoom = new Room(0);
    testRoom.setRoomMap(jsonmap);
    testBot1 = new VehicleBot(testRoom);
    testBot1.setCurrentRotation(90);
    testBot1.setCurrentPos(2, 2);
  }
}
