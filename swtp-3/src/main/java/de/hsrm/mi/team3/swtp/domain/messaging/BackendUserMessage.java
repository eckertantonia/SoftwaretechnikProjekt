package de.hsrm.mi.team3.swtp.domain.messaging;

import de.hsrm.mi.team3.swtp.domain.User;

public record BackendUserMessage(BackendOperation operation, User user) {}
