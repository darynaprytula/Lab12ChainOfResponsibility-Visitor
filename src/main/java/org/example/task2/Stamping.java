package org.example.task2;

import java.util.HashMap;
import java.util.Map;

final class Stamping {

    private Stamping() {
    }

    public static void applyStamp(Task<?> root, StampingVisitor visitor) {
        traverse(root, visitor, new HashMap<>());
    }

    private static void traverse(Task<?> task,
                                 StampingVisitor visitor,
                                 Map<String, String> inheritedHeaders) {

        if (task instanceof Group<?> group) {
            Map<String, String> toAdd =
                    visitor.onGroupStart(group, Map.copyOf(inheritedHeaders));

            Map<String, String> current = merge(inheritedHeaders, toAdd);

            current.forEach(group::setHeaderInternal);

            for (Task<?> child : group.getTasksForStamping()) {
                traverse(child, visitor, current);
            }

            visitor.onGroupEnd(group, Map.copyOf(current));

        } else if (task instanceof Signature<?> signature) {
            Map<String, String> toAdd =
                    visitor.onSignature(signature, Map.copyOf(inheritedHeaders));
            Map<String, String> current = merge(inheritedHeaders, toAdd);
            current.forEach(signature::setHeader);
        }
    }

    private static Map<String, String> merge(Map<String, String> base,
                                             Map<String, String> delta) {
        Map<String, String> result = new HashMap<>(base);
        if (delta != null) {
            result.putAll(delta);
        }
        return result;
    }
}
