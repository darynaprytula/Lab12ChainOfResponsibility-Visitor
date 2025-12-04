package org.example.task2;

public class Main {
    public static void main(String[] args) {
        Group<Integer> nestedGroup = new Group<>();
        Signature<Integer> print =
                new Signature<>(System.out::println);
        Signature<Integer> square =
                new Signature<>(x -> System.out.println("square = " + x * x));

        nestedGroup
                .addTask(print)
                .addTask(square);

        Group<Integer> group = new Group<>();
        Signature<Integer> cube =
                new Signature<>(x -> System.out.println("cube = " + x * x * x));

        group
                .addTask(nestedGroup)
                .addTask(cube);

        group.apply(10);

        System.out.println("Outer group_id: " + group.getHeader("group_id"));
        System.out.println("Nested group_id: " + nestedGroup.getHeader("group_id"));
        System.out.println("print.signature group_id: " + print.getHeader("group_id"));
        System.out.println("square.signature group_id: " + square.getHeader("group_id"));
        System.out.println("cube.signature group_id: " + cube.getHeader("group_id"));
    }
}
