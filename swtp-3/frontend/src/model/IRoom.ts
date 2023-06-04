import type { IUser } from "./IUser";

export interface IRoom {
  roomName: string;
  roomNumber: number;
  userList: IUser[];
  jythonScript: string;
  roomMap: string;
}

export class Room implements IRoom {
  roomName: string;
  roomNumber: number;
  userList: IUser[];
  jythonScript: string;
  roomMap: string;

  constructor(
    roomName: string,
    roomNumber: number,
    userList: IUser[],
    jythonScript: string,
    roomMap: string
  ) {
    this.roomName = roomName;
    this.roomNumber = roomNumber;
    this.userList = userList;
    this.jythonScript = jythonScript;
    this.roomMap = roomMap;
  }
}
