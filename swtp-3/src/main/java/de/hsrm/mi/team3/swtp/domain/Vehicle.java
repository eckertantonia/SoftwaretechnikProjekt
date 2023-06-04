package de.hsrm.mi.team3.swtp.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehicle {

  public static final double RUN_OUT_SPEED = -0.023;
  public static final double MAX_SPEED = 0.22;
  public static final double HANDLING = 0.168;
  public static final double ACCELERATION = 0.02;
  public static final double BRAKE_SPEED = 0.4;
  public static final double COLLISION_WIDTH = 1.5;
  private String vehicleType;
  private double currentSpeed;
  private double[] rotationVector;
  private double[] posVector;

  public Vehicle() {
    this.vehicleType = "car";
    this.currentSpeed = 0;
    this.rotationVector = new double[] {0, 0, 0};
    this.posVector = new double[] {0, 0, 0};
  }

  public Vehicle(String vehicleType, double[] posVector) {
    this.vehicleType = vehicleType;
    this.posVector = posVector;
    this.currentSpeed = 0;
    this.rotationVector = new double[] {0, 0, 0};
  }

  public double getRotationX() {
    return rotationVector[0];
  }

  public double getRotationY() {
    return rotationVector[1];
  }

  public double getRotationZ() {
    return rotationVector[2];
  }

  public void setRotationX(double rotationX) {
    rotationVector[0] = rotationX % (2 * Math.PI);
  }

  public void setRotationY(double rotationY) {
    rotationVector[1] = rotationY % (2 * Math.PI);
  }

  public void setRotationZ(double rotationZ) {
    rotationVector[2] = rotationZ % (2 * Math.PI);
  }

  public double getPosX() {
    return this.posVector[0];
  }

  public double getPosY() {
    return this.posVector[1];
  }

  public double getPosZ() {
    return this.posVector[2];
  }

  public void setPosX(double posX) {
    this.posVector[0] = posX;
  }

  public void setPosY(double posY) {
    this.posVector[1] = posY;
  }

  public void setPosZ(double posZ) {
    this.posVector[2] = posZ;
  }

  @Override
  public String toString() {
    return "Vehicle [currentSpeed="
        + currentSpeed
        + ", rotationVector="
        + Arrays.toString(rotationVector)
        + ", posVector="
        + Arrays.toString(posVector)
        + "]";
  }
}
