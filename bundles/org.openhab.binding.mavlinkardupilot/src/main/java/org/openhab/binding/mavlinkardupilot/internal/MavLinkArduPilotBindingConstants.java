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
package org.openhab.binding.mavlinkardupilot.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link MavLinkArduPilotBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Valentijn van de Beek - Initial contribution
 */
@NonNullByDefault
public class MavLinkArduPilotBindingConstants {

    private static final String BINDING_ID = "mavlinkardupilot";
    private static final String BINDING_BROKER = "mavlinkardupilot-broker";
    public static final String PARAMETER_HOSTNAME = "127.0.0.1";
    public static final String PARAMETER_PORT = "5760";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "sample");
    public static final ThingTypeUID BRIDGE_TYPE_SAMPLE = new ThingTypeUID(BINDING_BROKER, "sample_broker");

    // List of all Channel ids
    public static final String CHANNEL_1 = "channel1";
}
