//package org.openhab.binding.mavlinkardupilot.internal.discovery;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//import javax.jmdns.ServiceInfo;
//
//import org.openhab.binding.mavlinkardupilot.internal.MavLinkArduPilotBindingConstants;
//import org.openhab.core.config.discovery.DiscoveryResult;
//import org.openhab.core.config.discovery.DiscoveryResultBuilder;
//import org.openhab.core.config.discovery.mdns.MDNSDiscoveryParticipant;
//import org.openhab.core.thing.ThingTypeUID;
//import org.openhab.core.thing.ThingUID;
//import org.osgi.service.component.annotations.Component;
//
//@Component(service = MDNSDiscoveryParticipant.class, configurationPid = "discovery.mavlinkardupilotbroker")
//public class MavLinkArduPilotBrokerDiscovery implements MDNSDiscoveryParticipant {
//
//    @Override
//    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
//        return Collections.singleton(MavLinkArduPilotBindingConstants.BRIDGE_TYPE_SAMPLE);
//    }
//
//    @Override
//    public String getServiceType() {
//        return "sample_broker";
//    }
//
//    @Override
//    public DiscoveryResult createResult(ServiceInfo service) {
//        if (service.getName().equals("mavlinkardupilot-broker")) {
//            ThingUID uid = getThingUID(service);
//
//            if (uid != null) {
//                Map<String, Object> properties = new HashMap<>();
//                int port = service.getPort();
//                String host = service.getInetAddresses()[0].getHostAddress();
//
//                properties.put(MavLinkArduPilotBindingConstants.PARAMETER_HOSTNAME, host);
//                properties.put(MavLinkArduPilotBindingConstants.PARAMETER_PORT, port);
//
//                return DiscoveryResultBuilder.create(uid).withProperties(properties)
//                        .withRepresentationProperty(uid.getId()).withLabel("Minecraft Server (" + host + ")").build();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Check if service is a minecraft server.
//     *
//     * @param service the service to check.
//     * @return true if minecraft server, else false.
//     */
//    private boolean isMinecraftServer(ServiceInfo service) {
//        return (service != null && service.getType() != null && service.getType().equals(getServiceType()));
//    }
//
//    @Override
//    public ThingUID getThingUID(ServiceInfo service) {
//        if (isMinecraftServer(service) && service.getInetAddresses().length > 0) {
//            String host = service.getInetAddresses()[0].getHostAddress();
//            host = host.replace('.', '_');
//
//            return new ThingUID(MavLinkArduPilotBindingConstants.BRIDGE_TYPE_SAMPLE, host);
//        }
//
//        return null;
//    }
//}
