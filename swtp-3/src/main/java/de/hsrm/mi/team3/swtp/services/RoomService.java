package de.hsrm.mi.team3.swtp.services;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.User;
import de.hsrm.mi.team3.swtp.domain.Vehicle;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface RoomService {
  public void addNewUserToRoom(Room room, User user);

  public void removeUserFromRoom(Room room, User user);

  public List<User> getUserList(Room room);

  public void saveScriptToRoom(MultipartFile file, Room room);

  public void updateRoom(
      Room room,
      String jythonScript,
      String roomMap,
      String roomName,
      int roomNumber,
      List<User> userList);

  public void executeJython(Room room);

  public Optional<User> getUserByID(int roomNumber, String sessionID);

  public void deleteVehicleFromUser(int roomNumber, String sessionID);

  public List<Vehicle> getVehicleList(Room room);
}
