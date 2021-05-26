package org.openhab.binding.mavlinkardupilot.internal.bridge;

import static org.openhab.binding.mavlinkardupilot.internal.MavLinkArduPilotBindingConstants.THING_TYPE_BRIDGE;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mavlinkardupilot.internal.actions.MavLinkArduPilotActions;
import org.openhab.binding.mavlinkardupilot.internal.thing.MavLinkArduPilotHandler;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dronefleet.mavlink.Mavlink2Message;
import io.dronefleet.mavlink.MavlinkConnection;
import io.dronefleet.mavlink.common.Heartbeat;
import io.dronefleet.mavlink.common.MavAutopilot;
import io.dronefleet.mavlink.common.MavState;
import io.dronefleet.mavlink.common.MavType;

//@Component(service = BaseBridgeHandler.class, configurationPid = "bridge.mavlinkardupilot")
@NonNullByDefault
public class MavLinkArduPilotBridgeHandler extends BaseBridgeHandler {

    int systemId = 255;
    int componentId = 190;

    // private String uri;
    // private Integer port;
    // private String type;
    @Nullable
    MavlinkConnection connection;
    @Nullable
    Heartbeat heartbeat;

    private @Nullable ScheduledFuture<?> heartbeatFuture;

    private final Logger logger = LoggerFactory.getLogger(MavLinkArduPilotHandler.class);
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_BRIDGE);
    private @Nullable MavLinkArduPilotBridge bridge;
    private @Nullable Socket socket;

    public MavLinkArduPilotBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        // This example uses a TCP socket, however we may also use a UDP socket by injecting
        // PipedInputStream/PipedOutputStream to MavlinkConnection, or even USB by using any
        // implementation that will eventually yield an InputStream and an OutputStream.
        MavLinkArduPilotBridgeConfiguration configuration = getConfigAs(MavLinkArduPilotBridgeConfiguration.class);

        final String uri = configuration.getUri();
        final Integer port = configuration.getPort();

        if (bridge == null) {
            MavLinkArduPilotBridge currentbridge = new MavLinkArduPilotBridge();
            bridge = currentbridge;
        }

        if (uri == null || uri.isEmpty() || port == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/conf-error-no-app-name-or-port");
            return;
        }
        logger.error(String.format("Network config: %s:%d", uri, port));

        // throw new Exception(String.format("%s - %d", uri, port));
        updateStatus(ThingStatus.ONLINE);
        scheduler.submit(() -> {
                try {
                    socket = new Socket(uri, port);

                    connection = MavlinkConnection.create(socket.getInputStream(), socket.getOutputStream());

                    heartbeat = Heartbeat.builder().type(MavType.MAV_TYPE_GCS).autopilot(MavAutopilot.MAV_AUTOPILOT_INVALID)
                        .systemStatus(MavState.MAV_STATE_UNINIT).mavlinkVersion(3).build();
                    connection.send2(systemId, componentId, heartbeat);
                } catch (IOException eof) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, eof.getMessage());
                    logger.error("Shit broke yo.");
                }

                heartbeatFuture = scheduler.scheduleWithFixedDelay(this::sendHeartbeat, 0, 50L, TimeUnit.MILLISECONDS);
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

    public void printBeat() {
        logger.error("Hello there.");
    }

    public void sendHeartbeat() {
        logger.error("Hi.");
        if (connection == null) {
            return;
        }
        logger.error("Run");

        try {
            Mavlink2Message mavlink2Message = (Mavlink2Message) connection.next();
            logger.error(mavlink2Message.getPayload().toString());
            connection.send2(systemId, componentId, heartbeat);
        } catch (IOException e) {
            logger.error("BIT CONNECT");
        }
    }

    @Override
    public void dispose() {
    }
}
1
