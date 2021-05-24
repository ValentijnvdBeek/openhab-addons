package org.openhab.binding.mavlinkardupilot.internal.bridge;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mavlinkardupilot.internal.actions.MavLinkArduPilotActions;
import org.openhab.binding.mavlinkardupilot.internal.thing.MavLinkArduPilotHandler;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dronefleet.mavlink.MavlinkConnection;
import io.dronefleet.mavlink.common.Heartbeat;
import io.dronefleet.mavlink.common.MavAutopilot;
import io.dronefleet.mavlink.common.MavState;
import io.dronefleet.mavlink.common.MavType;

//@Component(service = BaseBridgeHandler.class, configurationPid = "bridge.mavlinkardupilot")
@NonNullByDefault
public class MavLinkArduPilotAbstractBridge extends BaseBridgeHandler {

    int systemId = 255;
    int componentId = 0;
    @Nullable
    MavlinkConnection connection;
    @Nullable
    Heartbeat heartbeat;
    @Nullable
    Thread heartbeatThread;
    private final Logger logger = LoggerFactory.getLogger(MavLinkArduPilotHandler.class);

    public MavLinkArduPilotAbstractBridge(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        // This example uses a TCP socket, however we may also use a UDP socket by injecting
        // PipedInputStream/PipedOutputStream to MavlinkConnection, or even USB by using any
        // implementation that will eventually yield an InputStream and an OutputStream.
        scheduler.execute(() -> {
            try (Socket socket = new Socket("127.0.0.1", 5760)) {

                connection = MavlinkConnection.create(socket.getInputStream(), socket.getOutputStream());

                heartbeat = Heartbeat.builder().type(MavType.MAV_TYPE_GCS).autopilot(MavAutopilot.MAV_AUTOPILOT_INVALID)
                        .systemStatus(MavState.MAV_STATE_UNINIT).mavlinkVersion(3).build();
                updateStatus(ThingStatus.ONLINE);
                // Write an unsigned heartbeat

                // Write a signed heartbeat
                // int linkId = 1;
                // long timestamp = System.currentTimeMillis() * 1000;
                // byte[] secretKey = MessageDigest.getInstance("SHA-256")
                // .digest("a secret phrase".getBytes(StandardCharsets.UTF_8));
                // connection.send2(systemId, componentId, heartbeat, linkId, timestamp, secretKey);
            } catch (IOException eof) {
                // The stream has ended.
            }
            startHB();
        });
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void updateState(String channelID, State state) {
        ChannelUID channelUID = new ChannelUID(this.getThing().getUID(), channelID);
        updateState(channelUID, state);
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(MavLinkArduPilotActions.class);
    }

    public void startHB() {
        heartbeatThread = new Thread(() -> {
            try {
                connection.send2(systemId, componentId, heartbeat);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Heartbeat could not be send");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("Heartbeat thread couldn't sleep");
            }
        });
        heartbeatThread.start();
    }

    @Override
    public void dispose() {
        if (heartbeatThread.isAlive()) {
            heartbeatThread.interrupt();
        }
    }
}
