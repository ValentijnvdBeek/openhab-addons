package org.openhab.binding.mavlinkardupilot.internal.thing;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface MavLinkArduPilotDiscoveryService {

    void sendWayPoint(String data);
}
