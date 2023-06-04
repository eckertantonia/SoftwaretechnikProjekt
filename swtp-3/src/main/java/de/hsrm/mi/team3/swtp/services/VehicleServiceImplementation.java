package de.hsrm.mi.team3.swtp.services;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.StreetBlock;
import de.hsrm.mi.team3.swtp.domain.Vehicle;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImplementation implements VehicleService {

  Logger logger = LoggerFactory.getLogger(VehicleService.class);

  public static final double DRIVE_DISTANCE = 8;
  // TODO: get gridsize from config
  public static final double GRIDSIZE = 50;
  // TODO: get blocksize from config
  public static final double BLOCKSIZE = 16;
  // TODO: get getCollisionList from config
  String[] collisionList = {"cream_sky_scraper", "red_sky_scraper", "brown-house"};
  // TODO: get getCollisionList from config
  String[] offroadList = {"trees"};
  public static final double OFFROAD_SLOWING_FACTOR = 0.8;
  @Autowired RoomService roomService;

  /**
   * rotates the given vehicle to the left
   *
   * @param vehicle
   */
  @Override
  public void rotateLeft(Vehicle vehicle) {
    if (vehicle.getCurrentSpeed() == 0) {
      return;
    }
    if (vehicle.getCurrentSpeed() > 0) {
      vehicle.setRotationY(vehicle.getRotationY() + Vehicle.HANDLING);
    } else {
      vehicle.setRotationY(vehicle.getRotationY() - Vehicle.HANDLING);
    }
  }

  /**
   * rotates the given vehicle to the right
   *
   * @param vehicle
   */
  @Override
  public void rotateRight(Vehicle vehicle) {
    if (vehicle.getCurrentSpeed() == 0) {
      return;
    }
    if (vehicle.getCurrentSpeed() > 0) {
      vehicle.setRotationY(vehicle.getRotationY() - Vehicle.HANDLING);
    } else {
      vehicle.setRotationY(vehicle.getRotationY() + Vehicle.HANDLING);
    }
  }

  /**
   * moves the vehicle forwards
   *
   * @param vehicle
   */
  @Override
  public void moveForward(Vehicle vehicle, Room room) {
    if (vehicle.getCurrentSpeed() < 0) {
      calculateSpeed(vehicle, Vehicle.BRAKE_SPEED);
    } else {
      calculateSpeed(vehicle, Vehicle.ACCELERATION);
    }
    move(vehicle, room);
  }

  /**
   * moves the vehicle backwards
   *
   * @param vehicle
   */
  @Override
  public void moveBackward(Vehicle vehicle, Room room) {
    if (vehicle.getCurrentSpeed() > 0) {
      calculateSpeed(vehicle, -Vehicle.BRAKE_SPEED);
    } else {
      calculateSpeed(vehicle, -Vehicle.ACCELERATION);
    }
    move(vehicle, room);
  }

  /**
   * lets the vehicle roll out with the speed it still has
   *
   * @param vehicle
   */
  @Override
  public void carRunOutSpeed(Vehicle vehicle, Room room) {
    if (vehicle.getCurrentSpeed() > 0) {
      double newSpeed = Math.round(this.accelerate(vehicle, Vehicle.RUN_OUT_SPEED) * 1000) / 1000.0;
      if (newSpeed < 0.00001) {
        vehicle.setCurrentSpeed(0);
      } else {
        vehicle.setCurrentSpeed(newSpeed);
      }
    } else if (vehicle.getCurrentSpeed() < 0) {
      double newSpeed =
          Math.round(this.accelerate(vehicle, -Vehicle.RUN_OUT_SPEED) * 1000) / 1000.0;
      if (newSpeed > -0.00001) {
        vehicle.setCurrentSpeed(0);
      } else {
        vehicle.setCurrentSpeed(newSpeed);
      }
    }
    move(vehicle, room);
  }

  /**
   * calculates and sets the new position of the car
   *
   * @param vehicle
   */
  private void move(Vehicle vehicle, Room room) {
    double[] moveTo = {0, 0, 0};
    moveTo[0] =
        (DRIVE_DISTANCE * vehicle.getCurrentSpeed() * Math.sin(vehicle.getRotationY()))
            + vehicle.getPosX();
    moveTo[2] =
        (DRIVE_DISTANCE * vehicle.getCurrentSpeed() * Math.cos(vehicle.getRotationY()))
            + vehicle.getPosZ();

    checkCollision(vehicle, moveTo, room);

    vehicle.setPosX(moveTo[0]);
    vehicle.setPosZ(moveTo[2]);
  }

  /**
   * calculates the speed of the vehicle
   *
   * @param vehicle
   * @param acceleration
   */
  private void calculateSpeed(Vehicle vehicle, double acceleration) {
    double newSpeed = accelerate(vehicle, acceleration);

    if (Math.abs(newSpeed) >= Vehicle.MAX_SPEED) {
      if (acceleration < 0) {
        vehicle.setCurrentSpeed(-Vehicle.MAX_SPEED);
      } else {
        vehicle.setCurrentSpeed(Vehicle.MAX_SPEED);
      }
    } else {
      vehicle.setCurrentSpeed(newSpeed);
    }
  }

  /**
   * calculates the acceleration of the vehicle
   *
   * @param vehicle
   * @param acceleration
   * @return new speed of vehicle (double)
   */
  private double accelerate(Vehicle vehicle, double acceleration) {
    return vehicle.getCurrentSpeed() + acceleration;
  }

  /**
   * checks if the vehicle collides with something
   *
   * @param vehicle
   * @param moveTo
   * @param room
   */
  private void checkCollision(Vehicle vehicle, double[] moveTo, Room room) {
    if (room.getRoadMap() == null) {
      return;
    }

    checkOutOfGrid(vehicle, moveTo);
    checkOffRoad(vehicle, moveTo, room);
    checkPlayerVehicleCollision(vehicle, moveTo, room);
    checkObstacleCollision(vehicle, moveTo, room);
    // TODO: check other collisions
  }

  /**
   * checks if the vehicle collides with another Player vehicle
   *
   * @param vehicle
   * @param moveTo
   * @param room
   */
  private void checkPlayerVehicleCollision(Vehicle vehicle, double[] moveTo, Room room) {
    List<Vehicle> vehicles = roomService.getVehicleList(room);
    for (Vehicle v : vehicles) {
      if (v != vehicle) {
        double distanceBetweenVehicles =
            Math.sqrt(Math.pow(moveTo[0] - v.getPosX(), 2) + Math.pow(moveTo[2] - v.getPosZ(), 2));
        if (distanceBetweenVehicles < (Vehicle.COLLISION_WIDTH + Vehicle.COLLISION_WIDTH)) {

          double moveOtherVehicleToX =
              ((DRIVE_DISTANCE * 2) * vehicle.getCurrentSpeed() * Math.sin(vehicle.getRotationY()))
                  + v.getPosX();
          double moveOtherVehicleToZ =
              ((DRIVE_DISTANCE * 2) * vehicle.getCurrentSpeed() * Math.cos(vehicle.getRotationY()))
                  + v.getPosZ();
          v.setPosX(moveOtherVehicleToX);
          v.setPosZ(moveOtherVehicleToZ);

          vehicle.setCurrentSpeed(0);
        }
      }
    }
  }

  private void checkOutOfGrid(Vehicle vehicle, double[] moveTo) {
    int[] gridPosition = getGridPosition(moveTo);
    if (gridPosition[0] < 1
        || gridPosition[0] > GRIDSIZE - 1
        || gridPosition[1] < 1
        || gridPosition[1] > GRIDSIZE - 1) {
      vehicle.setCurrentSpeed(0);
      moveTo[0] = vehicle.getPosX();
      moveTo[2] = vehicle.getPosZ();
    }
  }

  private void checkOffRoad(Vehicle vehicle, double[] moveTo, Room room) {
    int[] gridPosition = getGridPosition(moveTo);
    StreetBlock block = room.getStreetBlock(gridPosition[1], gridPosition[0]);

    if (block == null) {
      // wenn man auf nichts fährt ist man langsamer
      vehicle.setCurrentSpeed(vehicle.getCurrentSpeed() * OFFROAD_SLOWING_FACTOR);
      return;
    }
    // wenn man offroad fährt ist man langsamer
    for (String ele : offroadList) {
      if (ele.equals(block.getType())) {
        vehicle.setCurrentSpeed(vehicle.getCurrentSpeed() * OFFROAD_SLOWING_FACTOR);
        return;
      }
    }
  }

  private void checkObstacleCollision(Vehicle vehicle, double[] moveTo, Room room) {
    double[] checkCollision = {0, 0, 0};
    checkCollision[0] =
        ((DRIVE_DISTANCE + Vehicle.COLLISION_WIDTH * 2)
                * vehicle.getCurrentSpeed()
                * Math.sin(vehicle.getRotationY()))
            + vehicle.getPosX();
    checkCollision[2] =
        ((DRIVE_DISTANCE + Vehicle.COLLISION_WIDTH * 2)
                * vehicle.getCurrentSpeed()
                * Math.cos(vehicle.getRotationY()))
            + vehicle.getPosZ();

    int[] gridPosition = getGridPosition(checkCollision);
    StreetBlock block = room.getStreetBlock(gridPosition[1], gridPosition[0]);
    if (block == null) {
      return;
    }
    for (String ele : collisionList) {
      if (ele.equals(block.getType())) {
        vehicle.setCurrentSpeed(0);
        moveTo[0] = vehicle.getPosX();
        moveTo[2] = vehicle.getPosZ();
      }
    }
  }

  private int[] getGridPosition(double[] moveTo) {

    int[] gridPosition = new int[2];
    // (moveTo[0] - BLOCKSIZE / 2): to get the car to middle of the block
    gridPosition[0] = (int) (((moveTo[0] - BLOCKSIZE / 2) / BLOCKSIZE) + 1.0 + (GRIDSIZE / 2));
    gridPosition[1] = (int) (((moveTo[2] - BLOCKSIZE / 2) / BLOCKSIZE) + 1.0 + (GRIDSIZE / 2));
    return gridPosition;
  }
}
