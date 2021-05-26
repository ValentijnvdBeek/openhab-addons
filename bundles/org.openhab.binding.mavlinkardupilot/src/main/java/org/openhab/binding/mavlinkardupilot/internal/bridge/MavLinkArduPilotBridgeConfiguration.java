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
package org.openhab.binding.mavlinkardupilot.internal.bridge;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link MavLinkArduPilotBridge} class contains fields mapping thing configuration parameters.
 *
 * @author Valentijn van de Beek - Initial contribution
 */
@NonNullByDefault
public final class MavLinkArduPilotBridgeConfiguration {
    private @Nullable String uri = "127.0.0.1";
    private @Nullable Integer port = 5760;
    private @Nullable String type = "tcp"; // Enumeration?

    /*
     * @return The current uri that the binding is connected to.
     */
    public @Nullable String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public @Nullable Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public @Nullable String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
