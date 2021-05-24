package org.openhab.binding.mavlinkardupilot.internal.discovery;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mavlinkardupilot.internal.MavLinkArduPilotBindingConstants;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.ThingTypeUID;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = DiscoveryService.class, configurationPid = "discovery.mavlinkardupilot")
@NonNullByDefault
public class MavLinkArduPilotBridgeDiscovery extends AbstractDiscoveryService {
    private static final int DISCOVER_TIMEOUT_SECONDS = 60;
    private final Logger logger = LoggerFactory.getLogger(MavLinkArduPilotBridgeDiscovery.class);
    private int timeout;

    private @Nullable ScheduledFuture<?> scheduledStop;

    public MavLinkArduPilotBridgeDiscovery(@Nullable Set<ThingTypeUID> supportedThingTypes, int timeout,
            boolean backgroundDiscoveryEnabledByDefault) {
        super(supportedThingTypes, 0, backgroundDiscoveryEnabledByDefault);
        this.timeout = timeout;
    }

    public MavLinkArduPilotBridgeDiscovery() {
        super(Collections.singleton(MavLinkArduPilotBindingConstants.BRIDGE_TYPE_SAMPLE), DISCOVER_TIMEOUT_SECONDS,
                true);
    }

    private synchronized void stopTimeout() {
        if (scheduledStop != null) {
            scheduledStop.cancel(false);
            scheduledStop = null;
        }
    }

    protected synchronized void resetTimeout() {
        stopTimeout();

        // schedule an automatic call of stopScan when timeout is reached
        if (timeout > 0) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        stopScan();
                    } catch (Exception e) {
                        logger.debug("Exception occurred during execution: {}", e.getMessage(), e);
                    }
                }
            };

            scheduledStop = scheduler.schedule(runnable, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    protected void startScan() {
        if (isBackgroundDiscoveryEnabled()) {
            super.stopScan();
            return;
        }
        resetTimeout();
    }

    @Override
    protected synchronized void stopScan() {
        if (isBackgroundDiscoveryEnabled()) {
            super.stopScan();
            return;
        }
        stopTimeout();
        super.stopScan();
    }

    @Override
    public synchronized void abortScan() {
        stopTimeout();
        super.abortScan();
    }

    @Override
    protected void startBackgroundDiscovery() {
        // Remove results that are restored after a restart
        removeOlderResults(new Date().getTime());
    }
}
