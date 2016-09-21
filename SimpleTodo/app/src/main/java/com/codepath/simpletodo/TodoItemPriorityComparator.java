package com.codepath.simpletodo;

import java.util.Comparator;

/**
 * Created by akueisara on 9/20/2016.
 */
public class TodoItemPriorityComparator implements Comparator<TodoItem> {
    @Override
    public int compare(TodoItem one, TodoItem another) {
        return one.priority - another.priority;
    }
}
