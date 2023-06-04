package de.hsrm.mi.team3.swtp.domain.messaging;

import de.hsrm.mi.team3.swtp.domain.VehicleCommands;
import java.util.List;

public record BackenVehicleCommandMessage(List<VehicleCommands> commands, String userSessionId) {}
