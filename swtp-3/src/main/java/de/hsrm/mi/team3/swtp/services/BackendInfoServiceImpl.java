package de.hsrm.mi.team3.swtp.services;

import de.hsrm.mi.team3.swtp.domain.User;
import de.hsrm.mi.team3.swtp.domain.Vehicle;
import de.hsrm.mi.team3.swtp.domain.VehicleBot;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendMouseMessage;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendOperation;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendRoomMessage;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendUserMessage;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendVehicleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/** Implementation of BackendInfoService */
@Service
public class BackendInfoServiceImpl implements BackendInfoService {

  @Autowired SimpMessagingTemplate messaging;
  static final String GENERIC_TOPIC_START = "/topic/";

  /**
   * hands roomdata from front- to backend
   *
   * @param topicname object of action
   * @param operation type of action (defined by enum BackendOperation)
   * @param room room instance
   */
  @Override
  public void sendRoom(String topicname, BackendOperation operation, BackendRoomMessage room) {
    messaging.convertAndSend(GENERIC_TOPIC_START + topicname, room);
  }

  /**
   * hands userdata from front- to backend
   *
   * @param topicname object of action
   * @param operation type of action (defined by enum BackendOperation)
   * @param user user instance
   */
  @Override
  public void sendUser(String topicname, BackendOperation operation, User user) {
    messaging.convertAndSend(
        GENERIC_TOPIC_START + topicname, new BackendUserMessage(operation, user));
  }

  /**
   * hands mouse position from front- to backend
   *
   * @param topicname object of action
   * @param mouse mouse message with current mouse position and room information
   */
  @Override
  public void sendMouse(String topicname, BackendMouseMessage mouse) {
    messaging.convertAndSend(GENERIC_TOPIC_START + topicname, mouse);
  }

  /**
   * sends vehicle data to frontend
   *
   * @param topicname
   * @param operation
   * @param vehicle
   */
  @Override
  public void sendVehicle(
      String topicname, String sessionID, BackendOperation operation, Vehicle vehicle) {
    messaging.convertAndSend(
        GENERIC_TOPIC_START + topicname, BackendVehicleMessage.from(operation, sessionID, vehicle));
  }

  /**
   * sends vehicle data to frontend
   *
   * @param topicname
   * @param operation
   * @param vehicle
   */
  @Override
  public void sendVehicle(
      String topicname, String sessionID, BackendOperation operation, VehicleBot vehicle) {
    messaging.convertAndSend(
        GENERIC_TOPIC_START + topicname, BackendVehicleMessage.from(operation, sessionID, vehicle));
  }
}
