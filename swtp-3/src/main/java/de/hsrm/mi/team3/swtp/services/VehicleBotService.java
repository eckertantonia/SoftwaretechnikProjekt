package de.hsrm.mi.team3.swtp.services;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.VehicleBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * VehicleBotService is the interface used by python scripts saved to rooms. Only python scripts
 * should use this.
 */
@Service
public interface VehicleBotService {

  Logger logger = LoggerFactory.getLogger(VehicleBotService.class);

  public void setRoom(Room room);

  public void createBotWithRoute(String route);

  public void createBotWithType(String vehicleType);

  public void createBotWithRouteAndType(String route, String vehicleType);

  public void createBot();

  public void start();

  public void sendBot(VehicleBot bot);
}
