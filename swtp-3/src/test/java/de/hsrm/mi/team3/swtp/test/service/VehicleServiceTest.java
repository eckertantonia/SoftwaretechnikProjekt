package de.hsrm.mi.team3.swtp.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.Vehicle;
import de.hsrm.mi.team3.swtp.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VehicleServiceTest {
  @Autowired VehicleService vehicleService;

  private Vehicle vehicle;
  Room room = new Room(0);
  private final double NO_SPEED = 0;
  private final float DISTANCE = 8;

  @BeforeEach
  public void vehicleInit() {
    vehicle = new Vehicle();
  }

  @Test
  @DisplayName("Vehicle: Rotate stopped vehicle left")
  void testLeftRotationNoSpeed() {
    double rotationYBefore = vehicle.getRotationY();
    vehicle.setCurrentSpeed(0);
    assertThat(vehicle.getCurrentSpeed()).isZero();
    vehicleService.rotateLeft(vehicle);
    assertThat(vehicle.getRotationY()).isEqualTo(rotationYBefore);
  }

  @Test
  @DisplayName("Vehicle: Rotate moving vehicle left")
  void testLeftRotationWithSpeed() {
    double rotationYBefore = vehicle.getRotationY();
    vehicle.setCurrentSpeed(Vehicle.MAX_SPEED);
    assertThat(vehicle.getCurrentSpeed()).isPositive();
    vehicleService.rotateLeft(vehicle);
    assertThat(vehicle.getRotationY()).isGreaterThan(rotationYBefore);
  }

  @Test
  @DisplayName("Vehicle: Rotate stopped vehicle right")
  void testRightRotationNoSpeed() {
    double rotationYBefore = vehicle.getRotationY();
    vehicle.setCurrentSpeed(0);
    assertThat(vehicle.getCurrentSpeed()).isZero();
    vehicleService.rotateRight(vehicle);
    assertThat(vehicle.getRotationY()).isEqualTo(rotationYBefore);
  }

  @Test
  @DisplayName("Vehicle: Rotate moving vehicle Right")
  void testRightRotationWithSpeed() {
    double rotationYBefore = vehicle.getRotationY();
    vehicle.setCurrentSpeed(Vehicle.MAX_SPEED);
    assertThat(vehicle.getCurrentSpeed()).isPositive();
    vehicleService.rotateRight(vehicle);
    assertThat(vehicle.getRotationY()).isLessThan(rotationYBefore);
  }

  @Test
  @DisplayName("Vehicle: Move vehicle forwards")
  void testMoveVehicleForwards() {
    double xPosBefore = vehicle.getPosX();
    double zPosBefore = vehicle.getPosZ();
    vehicle.setCurrentSpeed(NO_SPEED);
    vehicleService.moveForward(vehicle, room);
    double xPosAfter =
        DISTANCE * vehicle.getCurrentSpeed() * Math.sin(vehicle.getRotationY()) + xPosBefore;
    assertThat(vehicle.getPosX()).isEqualTo(xPosAfter);
    assertThat(vehicle.getPosZ())
        .isEqualTo(
            DISTANCE * vehicle.getCurrentSpeed() * Math.cos(vehicle.getRotationY()) + zPosBefore);
  }

  @Test
  @DisplayName("Vehicle: Move vehicle forward while driving backwards")
  void testMoveVehicleForwardDrivingBackwards() {
    double xPosBefore = vehicle.getPosX();
    double zPosBefore = vehicle.getPosZ();
    vehicle.setCurrentSpeed(-Vehicle.MAX_SPEED);
    vehicleService.moveForward(vehicle, room);
    double xPosAfter =
        DISTANCE * vehicle.getCurrentSpeed() * Math.sin(vehicle.getRotationY()) + xPosBefore;
    assertThat(vehicle.getPosX()).isEqualTo(xPosAfter);
    assertThat(vehicle.getPosZ())
        .isEqualTo(
            DISTANCE * vehicle.getCurrentSpeed() * Math.cos(vehicle.getRotationY()) + zPosBefore);
  }

  @Test
  @DisplayName("Vehicle: Move vehicle backwards")
  void testMoveVehicleBackwards() {
    double xPosBefore = vehicle.getPosX();
    double zPosBefore = vehicle.getPosZ();
    vehicle.setCurrentSpeed(NO_SPEED);
    vehicleService.moveBackward(vehicle, room);
    double xPosAfter =
        DISTANCE * vehicle.getCurrentSpeed() * Math.sin(vehicle.getRotationY()) + xPosBefore;
    assertThat(vehicle.getPosX()).isEqualTo(xPosAfter);
    assertThat(vehicle.getPosZ())
        .isEqualTo(
            DISTANCE * vehicle.getCurrentSpeed() * Math.cos(vehicle.getRotationY()) + zPosBefore);
  }

  @Test
  @DisplayName("Vehicle: Move vehicle backwards while driving forwards")
  void testMoveVehicleBackwardsDrivingForwards() {
    double xPosBefore = vehicle.getPosX();
    double zPosBefore = vehicle.getPosZ();
    vehicle.setCurrentSpeed(-Vehicle.MAX_SPEED);
    vehicleService.moveBackward(vehicle, room);
    double xPosAfter =
        DISTANCE * vehicle.getCurrentSpeed() * Math.sin(vehicle.getRotationY()) + xPosBefore;
    assertThat(vehicle.getPosX()).isEqualTo(xPosAfter);
    assertThat(vehicle.getPosZ())
        .isEqualTo(
            DISTANCE * vehicle.getCurrentSpeed() * Math.cos(vehicle.getRotationY()) + zPosBefore);
  }

  @Test
  @DisplayName("Vehicle: Run out of speed")
  void testVehicleRunOutOfSpeed() {

    // Numbers represent detailed Speed values after slowing down
    vehicle.setCurrentSpeed(Vehicle.MAX_SPEED);
    assertThat(vehicle.getCurrentSpeed()).isEqualTo(Vehicle.MAX_SPEED);
    vehicleService.carRunOutSpeed(vehicle, room);
    assertThat(vehicle.getCurrentSpeed()).isEqualTo(Vehicle.MAX_SPEED + Vehicle.RUN_OUT_SPEED);
    while (vehicle.getCurrentSpeed() != 0) {
      vehicleService.carRunOutSpeed(vehicle, room);
    }
    assertThat(vehicle.getCurrentSpeed()).isZero();

    vehicle.setCurrentSpeed(-Vehicle.MAX_SPEED);
    assertThat(vehicle.getCurrentSpeed()).isEqualTo(-Vehicle.MAX_SPEED);
    vehicleService.carRunOutSpeed(vehicle, room);
    assertThat(vehicle.getCurrentSpeed()).isEqualTo(-Vehicle.MAX_SPEED - Vehicle.RUN_OUT_SPEED);
    while (vehicle.getCurrentSpeed() != 0) {
      vehicleService.carRunOutSpeed(vehicle, room);
    }
    assertThat(vehicle.getCurrentSpeed()).isZero();
  }
}
