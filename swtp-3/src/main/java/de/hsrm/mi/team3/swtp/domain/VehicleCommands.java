package de.hsrm.mi.team3.swtp.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleCommands {
  FORWARD("FORWARD"),
  BACKWARD("BACKWARD"),
  LEFT("LEFT"),
  RIGHT("RIGHT");

  @NonNull private String command;
}
