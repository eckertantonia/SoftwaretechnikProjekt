import { logger } from "@/helpers/Logger";
import { Client } from "@stomp/stompjs";
import { getSessionIDFromCookie } from "@/helpers/SessionIDHelper";
import {
  VehiclePositionMessage,
  type IVehiclePositionMessage,
} from "../model/IVehiclePositionMessage";
const webSocketUrl = `ws://${window.location.host}/stomp-broker`;
const positionMessageClient = new Client({ brokerURL: webSocketUrl });

export function use3DVehiclePosition() {
  return {
    publishVehiclePosition,
  };
}

/**
 * All Data needed to create a new vehicle is collected here.
 * @param posX
 * @param posY
 * @param vehicleType
 */
function publishVehiclePosition(
  posX: number,
  posY: number,
  vehicleType: string
) {
  const sessionID = getSessionIDFromCookie();

  const vehiclePositionMessage = new VehiclePositionMessage(
    sessionID as string,
    vehicleType,
    posX,
    posY
  );
  sendVehiclePositionMessage(vehiclePositionMessage);
}
/**
 * Connection to Backend
 */
function sendVehiclePositionMessage(
  vehiclePositionMessage: IVehiclePositionMessage
) {
  const DEST =
    "/topic/3d/createvehicle/" +
    (location.pathname.split("/")[1] as unknown as number);
  if (!positionMessageClient.connected) {
    positionMessageClient.activate();
  }
  positionMessageClient.onWebSocketError = (event) => {
    logger.error("WS-error", JSON.stringify(event)); /* WS-Error */
    location.href = "/500";
  };
  positionMessageClient.onStompError = (frame) => {
    logger.error("STOMP-error", JSON.stringify(frame)); /* STOMP-Error */
    location.href = "/500";
  };
  positionMessageClient.onConnect = () => {
    try {
      positionMessageClient.publish({
        destination: DEST,
        headers: {},
        body: JSON.stringify(vehiclePositionMessage),
      });
    } catch (err) {
      logger.error("Error while publishing vehicle! ", err);
    }
  };
}
