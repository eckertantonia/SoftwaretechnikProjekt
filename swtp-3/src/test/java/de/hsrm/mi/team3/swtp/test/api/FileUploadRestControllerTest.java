package de.hsrm.mi.team3.swtp.test.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hsrm.mi.team3.swtp.services.RoomBoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FileUploadRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private RoomBoxService roomBoxService;

  private static final String MOCK_JYTHON_CONTENT = "print(\"test\")";
  private static final String REST_ROUTE = "/api/upload/1";

  private static final MockMultipartFile MOCK_JYTHON_FILE =
      new MockMultipartFile("file", "mockFile.py", "text/plain", MOCK_JYTHON_CONTENT.getBytes());

  private static final int FIRST_ROOM_NUMBER = 1;

  @BeforeEach
  void initRoomRestTest() {
    roomBoxService.clearRoombox();
    roomBoxService.addRoom();
    roomBoxService.addRoom();
  }

  @Test
  @DisplayName("FileUploadRestController: /api/upload/{roomNumber} post should save script in room")
  void fileUploadPostTest() throws Exception {
    assertThat(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER).getJythonScript()).isEmpty();
    mockMvc.perform(multipart(REST_ROUTE).file(MOCK_JYTHON_FILE)).andExpect(status().isOk());
    assertThat(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER).getJythonScript())
        .isNotEmpty()
        .isEqualTo(MOCK_JYTHON_CONTENT);
  }

  @Test
  @DisplayName(
      "FileUploadRestController: /api/upload/{roomNumber} get should get specific roomScript")
  void fileUploadGetTest() throws Exception {
    assertThat(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER).getJythonScript()).isEmpty();

    mockMvc.perform(multipart(REST_ROUTE).file(MOCK_JYTHON_FILE)).andExpect(status().isOk());
    mockMvc
        .perform(get(REST_ROUTE))
        .andExpect(status().isOk())
        .andExpect(content().string(MOCK_JYTHON_CONTENT));

    assertThat(roomBoxService.getSpecificRoom(FIRST_ROOM_NUMBER).getJythonScript())
        .isNotEmpty()
        .isEqualTo(MOCK_JYTHON_CONTENT);
  }
}
