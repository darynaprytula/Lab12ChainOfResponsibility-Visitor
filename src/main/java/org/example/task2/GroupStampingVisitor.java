package org.example.task2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GroupStampingVisitor implements StampingVisitor {

    public static final String GROUP_ID_HEADER = "group_id";

    @Override
    public Map<String, String> onGroupStart(Group<?> group,
                                            Map<String, String> inheritedHeaders) {
        String groupId = group.getGroupUuid();
        if (groupId == null) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        result.put(GROUP_ID_HEADER, groupId);
        return result;
    }

    @Override
    public void onGroupEnd(Group<?> group, Map<String, String> currentHeaders) {
    }

    @Override
    public Map<String, String> onSignature(Signature<?> signature,
                                           Map<String, String> inheritedHeaders) {
        String groupId = inheritedHeaders.get(GROUP_ID_HEADER);
        if (groupId == null) {
            return Collections.emptyMap();
        }
        return Map.of(GROUP_ID_HEADER, groupId);
    }
}
