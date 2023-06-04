package de.hsrm.mi.team3.swtp.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.RoomBox;
import de.hsrm.mi.team3.swtp.domain.User;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
class UserTest {

  private final String ROOMNAME = "RoomName";
  private final int ROOMNUMBER = 1;

  private final String SESSIONID = "session-id-test-1";
  private final int USERROOMNUMBER = 1;
  private final String USERNAME = "User-One";
  private final long USERLOGINTIME = new Date().getTime();

  private final String SESSIONIDTWO = "session-id-test-2";
  private final int USERROOMNUMBERTWO = 1;
  private final String USERNAMETWO = "User-Twoe";
  private final long USERLOGINTIMETWO = new Date().getTime();

  User userOne = null;
  User userTwo = null;

  Room roomOne = null;

  RoomBox roomBox = null;

  @BeforeEach
  public void benutzerprofilInit() {
    userOne = new User();
    userOne.setSessionID(SESSIONID);
    userOne.setUserName(USERNAME);
    userOne.setCurrentRoomNumber(USERROOMNUMBER);
    userOne.setLoginTime(USERLOGINTIME);

    userTwo = new User();
    userTwo.setSessionID(SESSIONIDTWO);
    userTwo.setUserName(USERNAMETWO);
    userTwo.setCurrentRoomNumber(USERROOMNUMBERTWO);
    userTwo.setLoginTime(USERLOGINTIMETWO);

    roomOne = new Room(ROOMNAME, ROOMNUMBER);
    roomBox = new RoomBox();
  }

  @Test
  @DisplayName("User: toString()")
  void userToString() {
    String toStr = userOne.toString();
    String toStrTwo = userTwo.toString();

    assertThat(toStr)
        .contains(SESSIONID)
        .contains(USERNAME)
        .contains(Integer.toString(USERROOMNUMBER))
        .contains(Long.toString(USERLOGINTIME));

    assertThat(toStrTwo)
        .contains(SESSIONIDTWO)
        .contains(USERNAMETWO)
        .contains(Integer.toString(USERROOMNUMBERTWO))
        .contains(Long.toString(USERLOGINTIMETWO));
  }
}
