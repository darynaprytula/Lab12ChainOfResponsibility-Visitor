
package org.example.task2;

import java.util.Map;

public interface StampingVisitor {

    Map<String, String> onGroupStart(Group<?> group, Map<String, String> inheritedHeaders);

    void onGroupEnd(Group<?> group, Map<String, String> currentHeaders);

    Map<String, String> onSignature(Signature<?> signature, Map<String, String> inheritedHeaders);
}
