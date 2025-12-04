package org.example;

import org.example.task2.Group;
import org.example.task2.Signature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupStampingTest {

    @Test
    void emptyGroupStillGetsGroupIdAndFreeze() {
        Group<Integer> g = new Group<>();
        g.apply(10);

        assertNotNull(g.getGroupUuid());
        assertNotNull(g.getId());
        assertEquals(g.getGroupUuid(), g.getHeader("group_id"));
    }

    @Test
    void signatureGetsOwnIdOnFreeze() {
        Signature<Integer> s = new Signature<>(x -> {});
        s.apply(5);

        assertNotNull(s.getId());
    }

    @Test
    void groupFreezeAppliesOnlyOncePerApply() {
        Group<Integer> g = new Group<>();
        Signature<Integer> s = new Signature<>(x -> {});
        g.addTask(s);

        g.apply(1);
        String id1 = g.getId();

        g.apply(2);
        String id2 = g.getId();

        assertNotEquals(id1, id2);
    }

    @Test
    void nestedGroupsPropagateHeadersCorrectly() {
        Group<Integer> outer = new Group<>();
        Group<Integer> inner = new Group<>();
        Signature<Integer> s = new Signature<>(x -> {});

        inner.addTask(s);
        outer.addTask(inner);

        outer.apply(1);

        assertEquals(inner.getHeader("group_id"), s.getHeader("group_id"));
        assertNotEquals(outer.getHeader("group_id"), inner.getHeader("group_id"));
    }
}
