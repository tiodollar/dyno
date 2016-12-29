
package com.netflix.dyno.contrib;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ecwid.consul.v1.health.model.HealthService;

/**
 * 
 * Class with support for consul simple/commons operations
 */
public class ConsulHelper {

    public static String findHost(HealthService healthService) {
        HealthService.Service service = healthService.getService();
        HealthService.Node node = healthService.getNode();

        if (StringUtils.isNotBlank(service.getAddress())) {
            return service.getAddress();
        } else if (StringUtils.isNotBlank(node.getAddress())) {
            return node.getAddress();
        }
        return node.getNode();
    }

    public static Map<String, String> getMetadata(HealthService healthService) {
        return getMetadata(healthService.getService().getTags());
    }

    public static Map<String, String> getMetadata(List<String> tags) {
        LinkedHashMap<String, String> metadata = new LinkedHashMap<>();
        if (tags != null) {
            for (String tag : tags) {
                String[] parts = StringUtils.split(tag, "=");
                switch (parts.length) {
                case 0:
                    break;
                case 1:
                    metadata.put(parts[0], parts[0]);
                    break;
                case 2:
                    metadata.put(parts[0], parts[1]);
                    break;
                default:
                    String[] end = Arrays.copyOfRange(parts, 1, parts.length);
                    metadata.put(parts[0], StringUtils.join(end, "="));
                    break;
                }

            }
        }

        return metadata;
    }
}
