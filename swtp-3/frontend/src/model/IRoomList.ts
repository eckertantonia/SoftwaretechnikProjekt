import type { IRoom } from "./IRoom";

export interface IRoomList {
  roomList: IRoom[];
}

export class RoomList implements IRoomList {
  roomList: IRoom[];

  constructor(rooms: IRoom[]) {
    this.roomList = rooms;
  }
}
