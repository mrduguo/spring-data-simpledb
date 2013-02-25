package org.springframework.data.simpledb.core;

import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import org.springframework.data.simpledb.util.MapUtils;

import java.util.*;

/**
 * Taking into account SimpleDb limitations, constructs requests that comply.
 */
public class SimpleDbRequestBuilder {

    private static final int MAX_NUMBER_OF_ATTRIBUTES_PER_SIMPLE_DB_REQUEST = 256;

    public static List<PutAttributesRequest> createPutAttributesRequests(String domain, String itemName, Map<String, String> rawAttributes) {
        List<PutAttributesRequest> putAttributesRequests = new LinkedList<>();

        List<Map<String, String>> attributeChunks = MapUtils.splitToChunksOfSize(rawAttributes, MAX_NUMBER_OF_ATTRIBUTES_PER_SIMPLE_DB_REQUEST);

        for (Map<String, String> chunk : attributeChunks) {
            PutAttributesRequest request = createPutAttributesRequest(domain, itemName, chunk);
            putAttributesRequests.add(request);
        }

        return putAttributesRequests;


    }

    private static PutAttributesRequest createPutAttributesRequest(String domain, String itemName, Map<String, String> chunk) {
        final PutAttributesRequest putRequest = new PutAttributesRequest();
        putRequest.setDomainName(domain);
        putRequest.setItemName(itemName);

        List<ReplaceableAttribute> simpleDbAttributes = toReplaceableAttributeList(chunk, false);
        putRequest.setAttributes(simpleDbAttributes);
        return putRequest;

    }


    private static List<ReplaceableAttribute> toReplaceableAttributeList(Map<String, String> attributes, boolean replace) {
        final List<ReplaceableAttribute> result = new ArrayList<>();

        for (final Map.Entry<String, String> entry : attributes.entrySet()) {
            result.add(new ReplaceableAttribute(entry.getKey(), entry.getValue(), replace));
        }

        return result;
    }


}
