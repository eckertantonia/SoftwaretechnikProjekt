export interface IUser {
  sessionID: string;
  currentRoomNumber: number;
  userName: string;
  loginTime: number;
}

export class User implements IUser {
  sessionID: string;
  currentRoomNumber: number;
  userName: string;
  loginTime: number;

  constructor(
    sessionID: string,
    currentRoomNumber: number,
    userName: string,
    loginTime: number
  ) {
    this.userName = userName;
    this.sessionID = sessionID;
    this.currentRoomNumber = currentRoomNumber;
    this.loginTime = loginTime;
  }
}
