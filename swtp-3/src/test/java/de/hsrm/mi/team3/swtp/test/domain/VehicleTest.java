package de.hsrm.mi.team3.swtp.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.hsrm.mi.team3.swtp.domain.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
class VehicleTest {
  private static final double[] POS_VECTOR = {1, 2, 3};
  private static final double[] ROTATION_VECTOR = {0, 0, 0};
  private static final double SET_ROTATIONX = 10;
  private static final double GET_ROTATIONX_POST_SET = SET_ROTATIONX % (2 * Math.PI);
  private static final double SET_ROTATIONY = 10;
  private static final double GET_ROTATIONY_POST_SET = SET_ROTATIONY % (2 * Math.PI);
  private static final double SET_ROTATIONZ = 10;
  private static final double GET_ROTATIONZ_POST_SET = SET_ROTATIONZ % (2 * Math.PI);
  private final double MAX_SPEED = 0.6;

  @Test
  @DisplayName("Vehicle: Test initialization")
  void vehicleInitialization() {
    Vehicle vehicle = new Vehicle("car", POS_VECTOR);
    assertThat(vehicle.getPosVector()).isEqualTo(POS_VECTOR);
    assertThat(vehicle.getPosX()).isEqualTo(POS_VECTOR[0]);
    assertThat(vehicle.getPosY()).isEqualTo(POS_VECTOR[1]);
    assertThat(vehicle.getPosZ()).isEqualTo(POS_VECTOR[2]);

    assertThat(vehicle.getRotationVector()).isEqualTo(ROTATION_VECTOR);
    assertThat(vehicle.getRotationX()).isEqualTo(ROTATION_VECTOR[0]);
    assertThat(vehicle.getRotationY()).isEqualTo(ROTATION_VECTOR[1]);
    assertThat(vehicle.getRotationZ()).isEqualTo(ROTATION_VECTOR[2]);
  }

  @Test
  @DisplayName("Vehicle: setRotation should calculate rotation correctly")
  void vehicleSetRotation() {
    Vehicle vehicle = new Vehicle("car", POS_VECTOR);

    vehicle.setRotationX(SET_ROTATIONX);
    assertThat(vehicle.getRotationX()).isEqualTo(GET_ROTATIONX_POST_SET);

    vehicle.setRotationX(SET_ROTATIONY);
    assertThat(vehicle.getRotationX()).isEqualTo(GET_ROTATIONY_POST_SET);

    vehicle.setRotationX(SET_ROTATIONZ);
    assertThat(vehicle.getRotationX()).isEqualTo(GET_ROTATIONZ_POST_SET);
  }

  @Test
  @DisplayName("Vehicle: setPosVector should set posVector correctly")
  void vehicleSetPos() {
    Vehicle vehicle = new Vehicle("car", POS_VECTOR);

    vehicle.setPosVector(POS_VECTOR);
    assertThat(vehicle.getPosVector()).isEqualTo(POS_VECTOR);
  }

  @Test
  @DisplayName("Vehicle: toString contains info")
  void vehicleToString() {
    Vehicle vehicle = new Vehicle("car", POS_VECTOR);
    vehicle.setCurrentSpeed(MAX_SPEED);
    vehicle.setRotationVector(ROTATION_VECTOR);
    vehicle.setPosVector(POS_VECTOR);

    assertThat(vehicle.toString())
        .contains(String.valueOf(MAX_SPEED))
        .contains(String.valueOf(ROTATION_VECTOR[0]))
        .contains(String.valueOf(ROTATION_VECTOR[1]))
        .contains(String.valueOf(ROTATION_VECTOR[2]))
        .contains(String.valueOf(POS_VECTOR[0]))
        .contains(String.valueOf(POS_VECTOR[1]))
        .contains(String.valueOf(POS_VECTOR[2]));
  }
}
