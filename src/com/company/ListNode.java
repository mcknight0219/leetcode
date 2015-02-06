package com.company;

/**
 * Created by user on 27/11/14.
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int v) {
        val = v;
        next = null;
    }

    public String toString()
    {
        return Integer.toString(val);
    }
}
