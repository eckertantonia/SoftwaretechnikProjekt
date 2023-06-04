import { Client } from "@stomp/stompjs";
import { reactive, readonly } from "vue";
import { useRoomBox } from "./useRoomList";
import { getSessionIDFromCookie } from "@/helpers/SessionIDHelper";
import { Room, type IRoom } from "../model/IRoom";
import { logger } from "@/helpers/Logger";
import { useUser } from "./useUser";

const { userState } = useUser();

const webSocketUrl = `ws://${window.location.host}/stomp-broker`;

export interface IRoomState {
  room: IRoom;
  errorMessage: string;
}

const roomState = reactive<IRoomState>({
  room: new Room("", 1, [], "", ""),
  errorMessage: "",
});

/**
 * @returns Export of useRoom
 */
export function useRoom() {
  return {
    roomState: readonly(roomState),
    receiveRoom,
    joinRoom,
    updateRoom,
    removeUserFromRoom,
    updateRoomMap,
  };
}

//function to save the roomMap for a Room
function updateRoomMap(rMap: string): void {
  roomState.room.roomMap = rMap;
  updateRoom(roomState.room.roomNumber);
}

const { getRoomList } = useRoomBox();
const { updateUser, getCurrentUser } = useUser();
/**
 * Subscribes to the specific Rooms topic
 *
 */
function receiveRoom() {
  const receiveRoomStompClient = new Client({ brokerURL: webSocketUrl });
  const DEST = `/topic/room/${
    location.pathname.split("/")[1] as unknown as number
  }`;

  receiveRoomStompClient.onWebSocketError = (event) => {
    logger.error("WS-error", JSON.stringify(event)); /* WS-Error */
    location.href = "/500";
  };
  receiveRoomStompClient.onStompError = (frame) => {
    logger.error("STOMP-error", JSON.stringify(frame)); /* STOMP-Error */
    location.href = "/500";
  };
  receiveRoomStompClient.onConnect = () => {
    receiveRoomStompClient.subscribe(DEST, (message) => {
      roomState.room = JSON.parse(message.body);
    });
  };
  receiveRoomStompClient.activate();
}

/** Publishes Room to the rooms specific topic
 *
 * @param operator Operation type
 * @param roomNumber Roomnumber
 */
function updateRoom(roomNumber: number) {
  const updateRoomStompClient = new Client({ brokerURL: webSocketUrl });
  const DEST = "/topic/room/" + roomNumber;
  updateRoomStompClient.onWebSocketError = (event) => {
    logger.error("WS-error", JSON.stringify(event)); /* WS-Error */
    location.href = "/500";
  };
  updateRoomStompClient.onStompError = (frame) => {
    logger.error("STOMP-error", JSON.stringify(frame)); /* STOMP-Error */
    location.href = "/500";
  };
  updateRoomStompClient.onConnect = () => {
    try {
      updateRoomStompClient.publish({
        destination: DEST,
        headers: {},
        body: JSON.stringify(roomState.room),
      });
    } catch (err) {
      logger.log("Error while publishing room! ", err);
    }
  };
  updateRoomStompClient.activate();
}

/** Changes Room a User is in to another
 *
 * @param roomNumber Room number into which the user is to be swapped
 */
function joinRoom(roomNumber: number) {
  getCurrentUser();
  const data = JSON.stringify(userState.user);
  const DEST = "/api/room/" + roomNumber;
  fetch(DEST, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: data,
  })
    .then((response) => {
      if (!response.ok) {
        location.href = "/500";
      } else {
        return response.text();
      }
    })
    .then(() => {
      roomState.room.roomNumber = roomNumber;
      updateRoom(roomNumber);
      updateUser();
      getRoomList();
    })
    .catch(() => {
      location.href = "/500";
    });
}

/**
 *  Removes a user from a room
 */
function removeUserFromRoom() {
  const DEST = "/api/user/logout";
  fetch(DEST, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ sessionId: getSessionIDFromCookie() }),
  })
    .then((response) => {
      if (!response.ok) {
        location.href = "/500";
      } else {
        updateRoom(roomState.room.roomNumber);
        return response.text();
      }
    })
    .catch(() => {
      location.href = "/500";
    });
  roomState.room.roomNumber = 0;
}
