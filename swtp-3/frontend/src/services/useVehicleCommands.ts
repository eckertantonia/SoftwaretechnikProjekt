import { logger } from "@/helpers/Logger";
import { getSessionIDFromCookie } from "@/helpers/SessionIDHelper";
import type { Direction } from "@/model/DirektionCommands";
import { Client } from "@stomp/stompjs";

const webSocketUrl = `ws://${window.location.host}/stomp-broker`;
const stompClient = new Client({ brokerURL: webSocketUrl });

const stompClientConnect = setInterval(function () {
  if (!stompClient.connected) {
    stompClient.activate();
  } else {
    clearTimeout(stompClientConnect);
  }
}, 20);

export function useVehicleCommands() {
  return { publishVehicleCommands };
}

export interface IVehicleCommandMessage {
  commands: Direction[];
  userSessionId: string;
}
export class VehicleCommandMessage implements IVehicleCommandMessage {
  commands: Direction[];
  userSessionId: string;
  constructor(commands: Direction[], userSessionId: string) {
    this.commands = commands;
    this.userSessionId = userSessionId;
  }
}

/**
 * sends the commands to the backend
 * @param commands
 */
function publishVehicleCommands(commands: Direction[]) {
  const message = new VehicleCommandMessage(
    commands,
    getSessionIDFromCookie() as string
  );
  const DEST =
    "/topic/3d/commands/" +
    (location.pathname.split("/")[1] as unknown as number);
  if (!stompClient.connected) {
    stompClient.activate();
  }
  stompClient.onWebSocketError = (event) => {
    logger.error("WS-error", JSON.stringify(event)); /* WS-Error */
    location.href = "/500";
  };
  stompClient.onStompError = (frame) => {
    logger.error("STOMP-error", JSON.stringify(frame)); /* STOMP-Error */
    location.href = "/500";
  };
  try {
    stompClient.publish({
      destination: DEST,
      headers: {},
      body: JSON.stringify(message),
    });
  } catch (err) {
    console.error("Error while publishing VehicleCommands", err);
  }
}
