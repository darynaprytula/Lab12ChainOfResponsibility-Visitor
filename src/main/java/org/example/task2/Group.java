package org.example.task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Group<T> extends Task<T> {

    private String groupUuid;
    private List<Task<T>> tasks = new ArrayList<>();

    public Group<T> addTask(Task<T> task) {
        tasks.add(task);
        return this;
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public List<Task<T>> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    @Override
    public void freeze() {
        super.freeze();
        if (groupUuid == null) {
            groupUuid = UUID.randomUUID().toString();
        }
        setHeaderInternal(GroupStampingVisitor.GROUP_ID_HEADER, groupUuid);

        for (Task<T> task : tasks) {
            task.freeze();
        }
    }

    @Override
    public void apply(T arg) {
        this.freeze();

        Stamping.applyStamp(this, new GroupStampingVisitor());

        tasks = Collections.unmodifiableList(tasks);

        for (Task<T> task : tasks) {
            task.apply(arg);
        }
    }

    void setHeaderInternal(String header, String headerValue) {
        super.setHeader(header, headerValue);
    }

    List<Task<?>> getTasksForStamping() {
        return new ArrayList<>(tasks);
    }
}
