package de.hsrm.mi.team3.swtp.services;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.Vehicle;
import org.springframework.stereotype.Service;

@Service
public interface VehicleService {
  public void rotateLeft(Vehicle vehicle);

  public void rotateRight(Vehicle vehicle);

  public void moveForward(Vehicle vehicle, Room room);

  public void moveBackward(Vehicle vehicle, Room room);

  public void carRunOutSpeed(Vehicle vehicle, Room room);
}
