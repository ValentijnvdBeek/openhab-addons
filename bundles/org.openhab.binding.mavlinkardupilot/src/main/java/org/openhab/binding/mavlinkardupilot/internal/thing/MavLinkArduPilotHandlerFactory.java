/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mavlinkardupilot.internal.thing;

import static org.openhab.binding.mavlinkardupilot.internal.MavLinkArduPilotBindingConstants.*;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mavlinkardupilot.internal.bridge.MavLinkArduPilotAbstractBridge;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link MavLinkArduPilotHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Valentijn van de Beek - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "MavLinkArduPilotHandlerFactory", service = {ThingHandlerFactory.class,
        MavLinkArduPilotDiscoveryService.class })
public class MavLinkArduPilotHandlerFactory extends BaseThingHandlerFactory
        implements MavLinkArduPilotDiscoveryService {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_SAMPLE, BRIDGE_TYPE_SAMPLE);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_SAMPLE.equals(thingTypeUID)) {
            return new MavLinkArduPilotHandler(thing);
        }

        if (BRIDGE_TYPE_SAMPLE.equals(thingTypeUID)) {
            return new MavLinkArduPilotAbstractBridge((Bridge) thing);
        }

        return null;
    }

    public void sendWayPoint(String data) {
    }
}
