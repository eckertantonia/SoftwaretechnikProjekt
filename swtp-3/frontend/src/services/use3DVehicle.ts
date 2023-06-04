import { logger } from "@/helpers/Logger";
import { Client } from "@stomp/stompjs";
import { reactive, readonly } from "vue";
import {
  Vehicle,
  type IVehicle,
  type IVehicleMessage,
} from "../model/IVehicle";
import { MessageOperator } from "../model/MessageOperators";
import config from "../../../swtp.config.json";
const webSocketUrl = `ws://${window.location.host}/stomp-broker`;
const stompClient = new Client({ brokerURL: webSocketUrl });

export interface IVehicleState {
  vehicles: Map<string, IVehicle>;
  botVehicle: Map<string, IVehicle>;
  errorMessage: string;
}

const vehicleState = reactive<IVehicleState>({
  vehicles: new Map<string, IVehicle>(),
  botVehicle: new Map<string, IVehicle>(),
  errorMessage: "",
});

export function use3DVehicle() {
  return {
    vehicleState: readonly(vehicleState),
    receiveVehicle,
  };
}

/**
 * Subscribes to the Vehicle-Topic and updates the vehicleState.
 */
function receiveVehicle() {
  const DEST =
    "/topic/vehicle/" + (location.pathname.split("/")[1] as unknown as number);
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
  stompClient.onConnect = () => {
    stompClient.subscribe(DEST, (message) => {
      checkIfVehicleIsBot(JSON.parse(message.body));
    });
  };
}

function checkIfVehicleIsBot(vehicle: IVehicleMessage) {
  if (vehicle.userSessionId.includes(config.botIdentifier)) {
    handleMessage(vehicleState.botVehicle, vehicle);
  } else {
    handleMessage(vehicleState.vehicles, vehicle);
  }
}

function handleMessage(
  vehiclemap: Map<string, IVehicle>,
  jsonObject: IVehicleMessage
) {
  if (jsonObject.operator === MessageOperator.DELETE) {
    vehiclemap.delete(jsonObject.userSessionId);
  }
  if (jsonObject.operator !== MessageOperator.UPDATE)
    logger.log("HANDLE MESSAGE: ", jsonObject);
  if (jsonObject.operator === MessageOperator.DELETE) {
    logger.log(
      "SESSIONID: ",
      vehicleState.vehicles.get(jsonObject.userSessionId)
    );
    logger.log("map vor delete:", vehicleState.vehicles);
    vehicleState.vehicles.delete(jsonObject.userSessionId);
    logger.log("VEHICLE DELETED");
    logger.log(vehicleState.vehicles);
  }
  if (
    jsonObject.operator === MessageOperator.CREATE ||
    jsonObject.operator === MessageOperator.UPDATE
  ) {
    if (jsonObject.userSessionId.includes(config.botIdentifier)) {
      vehiclemap.set(
        jsonObject.userSessionId,
        new Vehicle(
          jsonObject.vehicleType,
          (jsonObject.postitionZ - 1 - config.gridSize / 2) * config.blocksize,
          jsonObject.postitionY,
          (jsonObject.postitionX - 1 - config.gridSize / 2) * config.blocksize,
          jsonObject.rotationX,
          (((jsonObject.rotationY - 90) % 360) * Math.PI) / 180,
          jsonObject.rotationZ,
          jsonObject.speed,
          jsonObject.maxSpeed
        )
      );
    } else {
      vehiclemap.set(
        jsonObject.userSessionId,
        new Vehicle(
          jsonObject.vehicleType,
          jsonObject.postitionX,
          jsonObject.postitionY,
          jsonObject.postitionZ,
          jsonObject.rotationX,
          jsonObject.rotationY,
          jsonObject.rotationZ,
          jsonObject.speed,
          jsonObject.maxSpeed
        )
      );
    }
  }
}
