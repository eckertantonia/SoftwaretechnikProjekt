package de.hsrm.mi.team3.swtp.domain.messaging;

public record BackendVehiclePositionMessage(
    String userSessionId, String vehicleType, int posX, int posZ) {}
