package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static ListNode genList(final int[] vals)
    {
        ListNode head = new ListNode(vals[0]);
        ListNode pre = head;
        for( int i = 1; i < vals.length; ++i ) {
            ListNode node = new ListNode(vals[i]);
            pre.next = node;
            pre = node;
        }
        return head;
    }

    private static void printList(final ListNode head)
    {
        ListNode pre = head;
        while( pre != null ) {
            System.out.printf("%s->", pre);
            pre = pre.next;
        }
        System.out.printf("null\n");
    }

    private static TreeNode assembleTree()
    {
        // Leaf
        TreeNode n1 = new TreeNode(15);
        TreeNode n2 = new TreeNode(7);

        TreeNode n3 = new TreeNode(9);
        TreeNode n4 = new TreeNode(20);
        n4.left = n1;
        n4.right = n2;

        TreeNode root = new TreeNode(3);
        root.left = n3;
        root.right = n4;

        return root;
    }

    private static TreeLinkNode assembleLinkTree()
    {
        TreeLinkNode n1 = new TreeLinkNode(4);
        TreeLinkNode n2 = new TreeLinkNode(5);
        TreeLinkNode n3 = new TreeLinkNode(6);
        TreeLinkNode n4 = new TreeLinkNode(7);

        TreeLinkNode n5 = new TreeLinkNode(2);
        n5.left = n1; n5.right = n2;
        TreeLinkNode n6 = new TreeLinkNode(3);
        n6.left = n3; n6.right = n4;

        TreeLinkNode root = new TreeLinkNode(1);
        root.left = n5;
        root.right = n6;

        return root;
    }

    public static void main(String[] args) {
        /*
        Set<String> dict = new HashSet<String>();
        dict.add("ted");
        dict.add("tex");
        dict.add("red");
        dict.add("tax");
        dict.add("tad");
        dict.add("den");
        dict.add("rex");
        dict.add("pee");
        */


        /*
        char board[][] = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        */
        char board[][] = {
                {'1', '1'},
                {'1', '1'}
        };

        TreeNode root = assembleTree();

        //ListNode head = genList(new int[]{1, 2, 3, 4});
        Solution solution = new Solution();

        //solution.connect(assembleLinkTree());

        System.out.println(solution.maximalRectangle(board));
    }
}
