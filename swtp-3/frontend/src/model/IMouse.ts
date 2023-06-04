export interface IMouse {
  sessionID: string;
  userName: string;
  roomNumber: number;
  x: number;
  y: number;
}

export class Mouse implements IMouse {
  sessionID: string;
  userName: string;
  roomNumber: number;
  x: number;
  y: number;

  constructor(
    sessionID: string,
    userName: string,
    roomNumber: number,
    x: number,
    y: number
  ) {
    this.sessionID = sessionID;
    this.userName = userName;
    this.roomNumber = roomNumber;
    this.x = x;
    this.y = y;
  }
}
