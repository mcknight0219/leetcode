package com.company;

import sun.reflect.generics.tree.Tree;

import java.util.*;


public class Solution {

    /**
     * The linke-list related functions
     */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if( head == null )
            return null;

        int count = 0;
        Stack<ListNode> stk = new Stack<ListNode>();

        // dummy node
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode pre = dummy;
        while( ++count != m ) {
            pre = head;
            head = head.next;
        }

        // begin push in n - m + 1 element
        count = n - m + 1;
        while( count-- != 0 ) {
            stk.push(head);
            head = head.next;
        }

        ListNode after = stk.peek().next;

        while( !stk.empty() ) {
            ListNode node = stk.pop();
            pre.next = node;
            pre = node;
        }
        pre.next = after;

        return dummy.next;
    }

    /**
     * Count the length and then call reverseBetween(), still O(n)
     */
    public ListNode reverse(ListNode head)
    {
        int count = 0;
        ListNode copyHead = head;
        while( head != null ) {
            count++;
            head = head.next;
        }

        return reverseBetween(copyHead, 1, count);
    }

    /**
     * Find the middle element of a list.
     */
    final ListNode findMiddle(ListNode head)
    {
        ListNode one = head;
        ListNode two = head;

        while( two != null && two.next != null ) {
            one = one.next;
            two = two.next.next;
        }
        return one;
    }

    public void reorderList(ListNode head) {
        if( head == null || head.next == null ) {
            return;
        }

        ListNode middle = findMiddle(head);
        // Need to break the link between middle and its pre
        ListNode pre = new ListNode(-1);
        pre.next = head;
        while( pre.next != middle ) {
            pre = pre.next;
        }
        pre.next = null;

        ListNode part2 = reverse(middle);
        ListNode part1 = head;

        // Start merging two list
        while( part1 != null && part2 != null ) {
            ListNode tmp1 = part1.next;
            part1.next = part2;
            ListNode tmp2 = part2.next;
            part2.next = (tmp1 == null) ? part2.next : tmp1;

            part1 = tmp1;
            part2 = tmp2;
        }
    }

    public int countList(ListNode head)
    {
        int count = 0;
        while( head != null ) {
            count++;
            head = head.next;
        }
        return count;
    }

    /**
     * Find intersection of two singlely-linked list, null if non-intersective
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB)
    {
        int lenA = countList(headA);
        int lenB = countList(headB);
        int d = Math.abs(lenA - lenB);

        ListNode longer =  (lenA >= lenB) ? headA : headB;
        ListNode shorter = (lenA >= lenB) ? headB : headA;
        while( d-- != 0) {
            longer = longer.next;
        }

        while( longer != null && shorter != null ) {
            if( longer.equals(shorter) ) {
                return longer;
            }
            longer = longer.next;
            shorter = shorter.next;
        }
        return null;
    }

    /**
     * Insertion sorting a list
     */
    public ListNode insertionSortList(ListNode head)
    {
        if( head == null || head.next == null ) {
            return head;
        }

        // Initialization
        ListNode sorted = head;
        ListNode current = head.next;
        sorted.next = null;

        while( current != null ) {
            // Insert current into sorted
            ListNode first = sorted;
            ListNode pre = null;
            while( first != null && current.val > first.val ) {
                pre = first;
                first = first.next;
            }

            // Save the next one for insertion
            ListNode after = current.next;
            if( first == null ) {
                pre.next = current;
                current.next = null;
            } else if( pre == null ) {
                current.next = first;
                sorted = current;
            } else {
                ListNode tmp = pre.next;
                pre.next = current;
                current.next = tmp;
            }
            current = after;
        }

        return sorted;
    }

    /**
     * Determine if list has a cycle.
     */
    public boolean hasCycle(ListNode head)
    {
        if( head == null || head.next == null ) {
            return false;
        }

        ListNode one = head.next;
        ListNode two = head.next.next;

        while( one != null && two != null ) {
            if( one.equals(two) ) {
                return true;
            }
            one = one.next;
            two = two.next == null ? null : two.next.next;
        }
        return false;
    }


    /**
     * Too many edge cases, must be careful!
     */
    public ListNode partition(ListNode head, int x)
    {
        if( head == null || head.next == null ) {
            return head;
        }

        ListNode pre = new ListNode(-1);
        ListNode dummy = pre;
        while( head != null && head.val < x ) {
            pre.next = head;
            head = head.next;
            pre = pre.next;
        }

        // trivial case: all elements smaller than x
        if( head == null ) {
            return dummy.next;
        }

        // Starting from head, move all nodes less than x after
        // pre, therefore the original ordering is kept
        if( pre.next == null ) {
            pre.next = head;
        }
        ListNode sentinel  = (dummy.next == head) ? dummy : head;

        if( sentinel != dummy ) {
            head = head.next;
        }

        while( head != null ) {
            if (head.val < x) {
                // heavy lifting move
                ListNode tmp1 = head.next;
                sentinel.next = tmp1;
                ListNode tmp2 = pre.next;
                pre.next = head;
                head.next = tmp2;

                head = tmp1;
                pre = pre.next;

            } else {
                sentinel.next = head;
                head = head.next;
                sentinel = sentinel.next;
            }
        }

        return dummy.next;

    }

    class ListComparator implements Comparator<ListNode>
    {
        @Override
        public int compare(ListNode a, ListNode b)
        {
            return a.val < b.val ? -1 : (a.val == b.val ? 0 : 1);
        }

    }

    final int recursion_(ListNode n, int x)
    {
        if( n == null) {
            return 0;
        }

        int rCount = 1 + recursion_(n.next, x);
        if( rCount == x + 1) {
            n.next = (n.next == null) ? null : n.next.next;
        }
        return rCount;
    }

    /**
     * Remove nth element from end of list. n is always valid
     */
    public ListNode removeNthFromEnd(ListNode head, int n)
    {
        if( head == null ) {
            return null;
        }

        if( recursion_(head, n) == n ) {
            return head.next;
        }
        return head;
    }

    /**
     * Remove duplicates from sorted list
     */
    public ListNode deleteDuplicates(ListNode head)
    {
        if( head == null || head.next == null ) {
            return head;
        }

        ListNode copyHead = head;
        while( head != null ) {
            ListNode start = head;
            int val = start.val;
            while( head.next != null && val == head.next.val ) {
                head = head.next;
            }
            start.next = head.next;
            head = head.next;
        }

        return copyHead;
    }

    /**
     * Delete all elements that have duplicates
     */
    public ListNode deleteDuplicates_v2(ListNode head)
    {
        if( head == null || head.next == null ) {
            return head;
        }

        ListNode dummy = new ListNode(-1);
        ListNode pre = dummy;

        while( head != null ) {
            pre.next = head;
            boolean duplicated = false;

            while( head.next != null && head.val == head.next.val ) {
                duplicated = true;
                head = head.next;
            }

            if( duplicated ) {
                pre.next = head.next;
            } else {
                pre = pre.next;
            }
            head = head.next;

        }

        return dummy.next;
    }

    /*
     * Rotate list to right by n places. Be careful about edge cases.
     */
    public ListNode rotateRight(ListNode head, int n)
    {
        if( head == null || head.next == null || n == 0) {
            return head;
        }

        ListNode copyHead = head;
        int length = 1;
        while( head.next != null ) {
            head = head.next;
            length++;
        }
        head.next = copyHead;

        ListNode pre = new ListNode(-1);
        n = length - (n % length);
        if( n == 0 ) {
            head.next= null;
            return copyHead;
        }

        while( n-- > 0 ) {
            pre.next = copyHead;
            copyHead = copyHead.next;
            pre = pre.next;
        }

        pre.next = null;
        head = copyHead;

        return head;
    }

    /**
     * Merge N sorted lists. It doesn't work by merging every two lists because it's O(N*M) where
     * M is the length of lists. Have to use a priority queue
     */
    public ListNode mergeKLists(List<ListNode> lists)
    {
        if( lists.size() == 0) {
            return null;
        }
        if( lists.size() == 1 ) {
            return lists.get(0);
        }
        Comparator<ListNode> comparator = new ListComparator();
        PriorityQueue<ListNode> heap = new PriorityQueue<ListNode>(lists.size(), comparator);

        for( ListNode node : lists ) {
            ListNode head = node;
            while( head != null ) {
                heap.add(head);
                head = head.next;
            }
        }

        ListNode dummy = new ListNode(-1);
        ListNode pre = dummy;
        Iterator<ListNode> it = heap.iterator();
        while( it.hasNext() ) {
            pre.next = it.next();
            pre = pre.next;
            it.remove();
        }

        pre.next = null;

        return dummy.next;
    }

    /**
     * Merge-sorting a linked list. It's actually a good exercise for Divide-and-Conquer
     */
    public ListNode sortList(ListNode head)
    {
        return null;
    }

    /**
     * Return start node of cycle if there is any, null if not
     */
    public ListNode detectCycle(ListNode head)
    {
        if( head == null || head.next == null ) {
            return null;
        }

        ListNode one = head.next;
        ListNode two = head.next.next;

        while( one != null && two != null ) {
            if( one.equals(two) ) {
                break;
            }
            one = one.next;
            two = two.next == null ? null : two.next.next;
        }

        if( one != null && two != null ) {
            return one.next.next;
        } else {
            return null;
        }
    }

    /**
     * The String processing related functions
     */

    /**
     * Evaluate reverse Polish notation
     */
    public int evalRPN(String[] tokens)
    {
        Stack<Integer> operands = new Stack<Integer>();
        for( String tok : tokens ) {
            if( isOperator(tok) ) {
                int op1 = operands.pop();
                int op2 = operands.pop();
                int result = 0;
                switch( tok.charAt(0) ) {
                    case '+':
                        result = op2 + op1;
                        break;
                    case '-':
                        result = op2 - op1;
                        break;
                    case '*':
                        result = op2 * op1;
                        break;
                    case '/':
                        result = op2 / op1;
                        break;
                    default:
                        break;
                }
                operands.push(result);
            } else {
                operands.push(Integer.valueOf(tok));
            }
        }
        return operands.pop();

    }

    private static final String[] operator = {"+", "-", "*", "/"};

    private boolean isOperator(String s)
    {
        for( String i : operator ) {
            if( i.equals(s) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Breaking words according to dictionary
     */
    public boolean wordBreak(String s, Set<String> dict)
    {
        /* Recursive solution, just not applicable
        if( s.isEmpty() ) {
            return true;
        }

        for( int i = 0; i <= s.length(); ++i ) {
            String target = s.substring(0, i);
            if( dict.contains(target) && wordBreak(s.substring(i), dict)) {
                return true;
            }
        }

        return false;
        */

        List<Boolean> solution = getWordBreakSol(s, dict);

        return solution.get(solution.size() - 1);
    }


    public final List<Boolean> getWordBreakSol(String s, Set<String> dict)
    {
        List<Boolean> solution = new ArrayList<Boolean>();
        for( int i = 0; i <= s.length(); ++i ) {
            solution.add(false);
        }
        solution.set(0, true);

        s = "#" + s;
        for( int i = 1; i < s.length(); ++i ) {
            for( int j = 0; j < i; ++j ) {
                String tmp = s.substring(j+1, i+1);
                if( solution.get(j) && dict.contains(tmp) ) {
                    solution.set(i, true);
                    break;
                }
            }
        }

        return solution;
    }


    private List<List<String>> backtracking(String s, Set<String> dict)
    {
        List<List<String>> result = new ArrayList<List<String>>(0);
        if( s.isEmpty() ) {
            return result;
        }
        // Start backtracking
        List<Boolean> partition = getWordBreakSol(s, dict);
        // find a lump of true where multiple word breaking is possible
        // Note first element is always true
        int start = 1;
        while( !partition.get(start) ) {
            start++;
        }

        while( start < partition.size() && partition.get(start) ) {
            String head = s.substring(0, start);
            String rest = s.substring(start);
            List<List<String>> tail = backtracking(rest, dict);
            for( List<String> l : tail ) {
                l.add(0, head);
            }
            if( tail.isEmpty() ) {
                tail = new ArrayList<List<String>>();
                List<String> tmp = new ArrayList<String>();
                tmp.add(head);
                tail.add(tmp);
            }

            result.addAll(tail);
            start++;
        }

        return result;
    }

    public final List<List<String>> wordBreakAll(String s, Set<String> dict)
    {
        if( dict.isEmpty() ) {
            return null;
        }
        return backtracking(s, dict);
    }

    private boolean checkPalindrome(String s)
    {
        int len = s.length();
        if( len == 1) {
            return true;
        }

        int i, j;
        if( len % 2 == 0 ) {
            // even length
            i = len / 2 - 1;
            j = len / 2;
        } else {
            // odd length
            i = len / 2 - 1;
            j = len / 2 + 1;
        }

        while( i >=0 && j < s.length() && s.charAt(i) == s.charAt(j) ) {
            --i;
            ++j;
        }

        return i < 0 && j == s.length();
    }

    // if two words only differ from one letter
    private boolean transformable(String from, String to)
    {
        int places = 0;
        for( int i = 0; i < from.length(); ++i ) {
            if( from.charAt(i) != to.charAt(i) ) {
                places++;
            }
            if( places > 1 ) {
                break;
            }
        }

        return places == 1;
    }

    public int ladderLength(String start, String end, Set<String> dict)
    {
        Deque<String> bsf = new ArrayDeque<String>();
        bsf.add(start);
        bsf.add("");

        int dist = 1;
        while( !bsf.isEmpty() ) {
            String s = bsf.peek();
            bsf.pop();
            if( !s.equals("")) {
                String tmp = s;
                for(int i = 0; i < tmp.length(); ++i ) {
                    for( char c = 'a'; c <= 'z'; ++c ) {
                        if( c == tmp.charAt(i) ) {
                            continue;
                        }
                        StringBuilder sb = new StringBuilder(tmp);
                        sb.setCharAt(i, c);
                        String mutate = sb.toString();
                        if( mutate.equals(end) ) {
                            dist++;
                            return dist;
                        }
                        if( dict.contains(mutate) ) {
                            bsf.add(mutate);
                            dict.remove(mutate);
                        }
                    }
                }
            } else if( !bsf.isEmpty()) {
                dist++;
                bsf.add("");
            }
        }

        return 0;
    }

    private boolean isGoodCandidate(List<String> candidate, String criteria, List<List<String>> result)
    {
        if( candidate.get(0).equals(criteria) ) {
            //Collections.reverse(candidate);
            result.add(candidate);
            return true;
        }

        return false;
    }

    private void backtracking(List<String> beg, String criteria, Map<String, ArrayList<String>> trace, List<List<String>> result)
    {
        if( isGoodCandidate(beg, criteria, result) ) {
            return;
        }

        for( String e : trace.get(beg.get(0)) ) {
            List<String> tmp = new ArrayList<String>(beg);
            tmp.add(0, e);
            backtracking(tmp, criteria, trace, result);
        }
    }

    // We know for sure that by tracing from end, we wil certainly arrive at end
    private List<List<String>> gatherPath(Map<String, ArrayList<String>> trace, String start, String end)
    {
        List<List<String>> path = new ArrayList<List<String>>();
        List<String> first = new ArrayList<String>();
        first.add(end);
        backtracking(first, start, trace, path);

        return path;
    }

    @SuppressWarnings("unchecked")
    public List<List<String>> findLadders(String start, String end, Set<String> dict)
    {
        // We use a map to record a word's predecessor, so at the end,
        // we could trace back and print the path
        Map<String, ArrayList<String>> trace = new HashMap<String, ArrayList<String>>();
        Deque<String> bsf = new ArrayDeque<String>();
        bsf.add(start);
        bsf.add("");

        int dist = 1;
        boolean found = false;

        while( !bsf.isEmpty() ) {
            String s = bsf.pop();
            if( !s.equals("") ) {
                for(int i = 0; i < s.length(); ++i ) {
                    for (char c = 'a'; c <= 'z'; ++c) {
                        if (c == s.charAt(i)) {
                            continue;
                        }
                        StringBuilder sb = new StringBuilder(s);
                        sb.setCharAt(i, c);
                        String mutate = sb.toString();
                        if (mutate.equals(end)) {
                            if( !found ) {
                                dist++;
                            }
                            found = true; // finish this level
                            List<String> old = new ArrayList<String>();
                            if (trace.containsKey(mutate)) {
                                old = trace.get(mutate);
                            }
                            old.add(s);
                            trace.put(mutate, (ArrayList<String>) old);

                            continue;
                        }
                        if (dict.contains(mutate)) {
                            bsf.add(mutate);
                            List<String> old = new ArrayList<String>();
                            if (trace.containsKey(mutate)) {
                                old = trace.get(mutate);
                            }
                            old.add(s);

                            trace.put(mutate, (ArrayList<String>) old);
                            dict.remove(mutate);
                        }
                    }
                }

            } else if( !bsf.isEmpty() ) {
                if( found ) {
                    break;
                }
                dist++;
                bsf.add("");
            }
        }

        if( trace.isEmpty() ) {
            return new ArrayList<List<String>>();
        }
        return gatherPath(trace, start, end);
    }

    private boolean isGoodCandidatePermutation(List<Integer> candidate, int size, List<List<Integer>> result)
    {
        if( size == 0) {
            result.add(candidate);
            return true;
        }

        return false;
    }

    private void backtrackingPermutation(List<Integer> seed, List<Integer> num, List<List<Integer>> result)
    {
        if( isGoodCandidatePermutation(seed, num.size(), result) ) {
            return;
        }

        for( int i : num ) {
            List<Integer> tmp = new ArrayList<Integer>(seed);
            List<Integer> copy = new ArrayList<Integer>(num);
            copy.remove((Integer)i);
            tmp.add(i);
            backtrackingPermutation(tmp, copy, result);
        }
    }

    public List<List<Integer>> permute(int[] num)
    {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> seed = new ArrayList<Integer>();
        List<Integer> numList = new ArrayList<Integer>();
        for( int i : num ) {
            numList.add(i);
        }

        backtrackingPermutation(seed, numList, result);

        return result;
    }


    private boolean isGoodCandidatePermutationUnique(List<Integer> candidate, int size, List<List<Integer>> result)
    {
        if( size == 0 ) {
            result.add(candidate);
            return true;
        }
        return false;
    }

    private void backtrackingPermutationUnique(List<Integer> seed, List<Integer> num, List<List<Integer>> result)
    {
        if( isGoodCandidatePermutationUnique(seed, num.size(), result) ) {
            return;
        }

        for( Integer i : num) {
            List<Integer> tmp = new ArrayList<Integer>(seed);
            List<Integer> copy = new ArrayList<Integer>(num);
            copy.remove((Integer)i);
            tmp.add(i);
            backtrackingPermutationUnique(tmp, copy, result);
        }
    }

    public List<List<Integer>> permuteUnique(int[] num)
    {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> seed = new ArrayList<Integer>();
        List<Integer> numList = new ArrayList<Integer>();
        for( int i : num ) {
            numList.add(i);
        }
        backtrackingPermutationUnique(seed, numList, result);

        return result;
    }

    private <E> List<E> setToList(Set<E> set)
    {
        List<E> result = new ArrayList<E>();
        for( E elem : set ) {
            result.add(elem);
        }
        return result;
    }

    private <E> void removeAll(List<E> l, E obj)
    {
        Iterator<E> iterator = l.iterator();
        while( iterator.hasNext() ) {
            if( obj.equals(iterator.next()) ) {
                iterator.remove();
            }
        }
    }


    private boolean isGoodCandidateParenthesis(String s, int left, int right, List<String> result)
    {
        if( left == 0 && right == 0 && s.charAt(s.length()-1) == ')') {
            //if( !result.contains(s) ) {
                result.add(s);
            //}

            return true;
        }
        return false;
    }

    private void backtrackingParenthesis(String s, int left, int right, List<String> result)
    {
        if( isGoodCandidateParenthesis(s, left, right, result) ) {
            return;
        }

        if( left != 0 ) {
            backtrackingParenthesis(s.concat("("), left - 1, right, result);
        }
        if( right != 0 && right > left) {
            backtrackingParenthesis(s.concat(")"), left, right - 1, result);
        }

    }

    /**
     * Excellent example of backtracking
     */
    public List<String> generateParenthesis(int n)
    {
        List<String> result = new ArrayList<String>();
        backtrackingParenthesis("(", n - 1, n, result);

        return result;
    }

    public boolean isValidSudoku(char[][] board)
    {
        Set<Character> check = new HashSet<Character>();
        int height = board.length;
        int width = board[0].length;

        // check rows
        for( int i = 0; i < height; ++i ) {
            check.clear();
            for( int j = 0; j < width; j++ ) {
                char c = board[i][j];
                if( c != '.') {
                    if( check.contains(c) ) {
                        return false;
                    } else {
                        check.add(c);
                    }
                }
            }
        }

        // check columns
        for( int i = 0; i < height; ++i ) {
            check.clear();
            for( int j = 0; j < width; j++ ) {
                char c = board[j][i];
                if( c != '.') {
                    if( check.contains(c) ) {
                        return false;
                    } else {
                        check.add(c);
                    }
                }
            }
        }

        // check square
        for( int m = 0; m < 9; m++ ) {
            check.clear();
            for( int i = (m / 3) * 3; i < 3 + (m / 3) * 3; ++i ) {
                for( int j = (m % 3) * 3; j < 3 + (m % 3) * 3; ++j ) {
                    char c = board[i][j];
                    if( c != '.' ) {
                        if( check.contains(c) ) {
                            return false;
                        } else {
                            check.add(c);
                        }
                    }
                }
            }
        }

        return true;
    }


    class Couple<E>
    {
        private E first;
        private E second;

        public Couple(E left, E right) {
            this.first = left;
            this.second = right;
        }

        public E getFirst()  { return this.first; }
        public E getSecond() { return this.second; }

        public boolean equals(Object other)
        {
            if( this == other ) {
                return true;
            }

            Couple<E> o = (Couple<E>)other;
            return o.getFirst() == first && o.getSecond() == second;
        }
    }


    public List<String> anagrams(String[] strs)
    {
        List<String> result = new ArrayList<String>();
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for( String s : strs ) {
            String sorted = sortStr(s);
            List<String> l = map.get(sorted);
            if( l == null ) {
                l = new ArrayList<String>();
            }
            l.add(s);
            map.put(sorted, l);
        }

        for( String key : map.keySet() ) {
            List<String> l = map.get(key);
            if( l.size() > 1 ) {
                result.addAll(l);
            }
        }

        return result;
    }

    // For purpose of anagrams, we just add up chars
    private String sortStr(String str)
    {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public int findPeakElement(int[] num)
    {
        if( num.length == 1 ) {
            return 0;
        }
        if( num.length == 2) {
            return num[0] > num[1] ? 0 : 1;
        }

        int first = 0;
        int last  = num.length - 1;
        while( first <= last ) {
            int mid = (first + last) >> 1;

            if( mid == 0 ) {
                if( num[mid] > num[mid+1]) {
                    return mid;
                } else {
                    return mid + 1;
                }
            }
            if( mid == num.length - 1) {
                if( num[mid] > num[mid - 1]) {
                    return mid;
                } else {
                    return mid - 1;
                }
            }

            if( num[mid] > num[mid - 1] && num[mid] > num[mid + 1] ) {
                return mid;
            }

            if( num[mid - 1] > num[mid] ) {
                last = mid - 1;
            } else if( num[mid + 1] > num[mid] ) {
                first = mid + 1;
            }
        }

        return -1;
    }

    /**
     * BINARY TREE RELATED PROBLEMS
     */
    public boolean hasPathSum(TreeNode root, int sum)
    {
        if( root == null ) {
            return false;
        }
        if( root.left == null && root.right == null ) {
            return root.val == sum;
        }

        boolean result = false;
        if( root.left != null ) {
            result = hasPathSum(root.left, sum - root.val);
        }
        if( root.right != null ) {
            result |= hasPathSum(root.right, sum - root.val);
        }

        return result;
    }


    private void dfs(TreeNode root, int sum, Set<TreeNode> mark, Map<TreeNode, TreeNode> path, List<TreeNode> leaves)
    {
        if( mark.contains(root) ) {
            // node has been visited before
            return;
        }

        mark.add(root);
        if( root.left == null && root.right == null ) {
            // it's the node we want
            if( root.val == sum ) {
                leaves.add(root);
            }
        }

        if( root.left != null ) {
            path.put(root.left, root);
            dfs(root.left, sum - root.val, mark, path, leaves);
        }
        if( root.right != null ) {
            path.put(root.right, root);
            dfs(root.right, sum - root.val, mark, path, leaves);
        }
    }

    // Trace the path
    private List<List<Integer>> tracePath(TreeNode root, Map<TreeNode, TreeNode> path, List<TreeNode> leaves)
    {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        for( TreeNode n : leaves ) {
            List<Integer> trail = new ArrayList<Integer>();
            trail.add(n.val);
            TreeNode cur = path.get(n);
            while( cur != root ) {
                trail.add(0, cur.val);
                cur = path.get(cur);
            }
            trail.add(0, root.val);
            result.add(trail);
        }

        return result;

    }

    public List<List<Integer>> pathSum(TreeNode root, int sum)
    {
        if( root == null ) {
            return null;
        }

        Set<TreeNode> mark = new HashSet<TreeNode>();
        Map<TreeNode, TreeNode> path = new HashMap<TreeNode, TreeNode>();
        List<TreeNode> leaves = new ArrayList<TreeNode>();

        dfs(root, sum, mark, path, leaves);
        if( leaves.isEmpty() ) {
            return new ArrayList<List<Integer>>();
        }

        return tracePath(root, path, leaves);
    }



    private void dfsFlatten(TreeNode root, Set<TreeNode> mark, Deque<TreeNode> leaves)
    {
        if( mark.contains(root) ) {
            return;
        }
        mark.add(root);

        if( root.left == null && root.right == null ) {
            leaves.push(root);
            return;
        }
        if( root.left != null ) {
            dfsFlatten(root.left, mark, leaves);
        }
        if( root.right != null ) {
            TreeNode leftmost = null;
            if( !leaves.isEmpty() ) {
                leftmost = leaves.getFirst();
                leaves.removeFirst();
                leftmost.right = root.right;
                root.right = null;
            }
            dfsFlatten(leftmost == null ? root.right : leftmost.right, mark, leaves);
        }
    }

    // Move left child to its right side
    private void rectify(TreeNode root)
    {
        TreeNode cur = root;
        while( cur.left != null || cur.right != null ) {
            if( cur.left != null ) {
                cur.right = cur.left;
                cur.left = null;
            }
            cur = cur.right;
        }
    }

    /**
     * This is basically a dfs. Has to be careful about pointer manipulation
     */
    public void flatten(TreeNode root)
    {
        if( root == null ) {
            return;
        }

        Set<TreeNode> mark = new HashSet<TreeNode>();
        Deque<TreeNode> leaves = new ArrayDeque<TreeNode>();

        dfsFlatten(root, mark, leaves);
        rectify(root);
    }

    private boolean areSymmetric(TreeNode left, TreeNode right)
    {
        if( (left == null && right != null) || (left != null && right == null) ) {
            return false;
        }
        if( left == null && right == null ) {
            return true;
        }

        if( left.val == right.val ) {
            return areSymmetric(left.left, right.right) && areSymmetric(left.right, right.left);
        }
        return false;

    }

    public boolean isSymmetric(TreeNode root)
    {
        return root != null && areSymmetric(root.left, root.right);
    }



    private void dfsSumNumbers(TreeNode root, Set<TreeNode> mark, Map<TreeNode, TreeNode> path, List<TreeNode> leaves)
    {
        if( mark.contains(root) ) {
            return;
        }
        mark.add(root);

        if( root.left == null && root.right == null ) {
            leaves.add(root);
            return;
        }

        if( root.left != null ) {
            path.put(root.left, root);
            dfsSumNumbers(root.left, mark, path, leaves);
        }
        if( root.right != null ) {
            path.put(root.right, root);
            dfsSumNumbers(root.right, mark, path, leaves);
        }
    }

    private int getSum(TreeNode root, Map<TreeNode, TreeNode> map, List<TreeNode> leaves)
    {
        int sum = 0;
        for( TreeNode node : leaves ) {
            TreeNode cur = node;
            int factor = 1;
            int num = 0;
            while( cur != root ) {
                num += factor * cur.val;
                factor *= 10;
                cur = map.get(cur);
            }
            num += factor * cur.val;

            sum += num;
        }

        return sum;
    }

    /**
     * Use a dfs algo to retrieve the string represents the path
     */
    public int sumNumbers(TreeNode root)
    {
        Set<TreeNode> mark = new HashSet<TreeNode>();
        Map<TreeNode, TreeNode> path = new HashMap<TreeNode, TreeNode>();
        List<TreeNode> leaves = new ArrayList<TreeNode>();

        dfsSumNumbers(root, mark, path, leaves);
        return getSum(root, path, leaves);
    }


    // Check if all nodes in tree have value greater than a number
    private boolean greater(TreeNode root, int n)
    {
        boolean result = root.val > n;
        if( root.left != null ) {
            result = result && greater(root.left, n);
        }

        if( root.right != null ) {
            result = result && greater(root.right, n);
        }

        return result;
    }

    private boolean smaller(TreeNode root, int n)
    {
        boolean result = root.val < n;
        if( root.left != null ) {
            result = result && smaller(root.left, n);
        }

        if( root.right != null ) {
            result = result && smaller(root.right, n);
        }

        return result;
    }

    public boolean isValidBST(TreeNode root)
    {
        if( root == null ) {
            return true;
        }

        if( root.left == null && root.right == null ) {
            return true;
        }

        boolean result = true;
        if( root.left != null ) {
            result = (smaller(root.left, root.val) && isValidBST(root.left));
        }

        if( root.right != null ) {
            result = result && greater(root.right, root.val) && isValidBST(root.right);
        }



        return result;
    }

    /**
     * Divide and conquer
     */
    public TreeNode sortedArrayToBST(int[] num)
    {
        if( num.length == 0 ) {
            return null;
        }

        if( num.length == 1 ) {
            return new TreeNode(num[0]);
        }

        int mid = num.length / 2;
        TreeNode left = null;
        if( mid - 1 >= 0) {
            left = sortedArrayToBST(Arrays.copyOfRange(num, 0, mid));
        }

        TreeNode right = null;
        if( mid + 1 < num.length ) {
            right = sortedArrayToBST(Arrays.copyOfRange(num, mid + 1, num.length));
        }

        TreeNode root = new TreeNode(num[mid]);
        root.left = left;
        root.right = right;

        return root;
    }

    private ListNode getNthList(ListNode head, int n)
    {
        // count starts at 0
        while( n > 0 && head != null ) {
            head = head.next;
            n--;
        }
        return head;        
    }

    // Follow the same divide and conquer strategy as before
    public TreeNode sortedListToBST(ListNode head) 
    {
        if( head == null ) {
            return null;
        }

        if( head.next == null ) {
            return new TreeNode(head.val);
        }

        int length = countList(head);
        int mid = length / 2;
        ListNode midNode = getNthList(head, mid);
        TreeNode left = null;
        if( mid - 1 >= 0 ) {
            // Get the list node before mid and set its next null
            ListNode endOfFirstList = getNthList(head, mid - 1);
            if( endOfFirstList != null ) {
                endOfFirstList.next = null;
                left = sortedListToBST(head);
            } 
        }


        TreeNode right = null;
        if( mid + 1 < length ) {
            if( midNode.next != null ) {
                right = sortedListToBST(midNode.next);
            }
        }

        TreeNode root = new TreeNode(midNode.val);
        root.left = left;
        root.right = right;

        return root;
    }


    /**
     * I first heard about 8 queen problem probably in high school when I saw some books
     * about computer programming competition. And now it's ten years later, I finally sit
     * down to solve it ! It's frustrating to discover your inability to embrace the challenge.
     */
    public void backtracking(int[] queen, int rest, boolean[] col, boolean[] up, boolean[] down, List<int[]> result)
    {
        if( rest == queen.length ) {
            int[] copy = Arrays.copyOf(queen, queen.length);
            result.add(copy);
            return;
        }

        // iterate through each column
        for( int i = 0; i < queen.length; ++i ) {
            if( !col[i] && !up[rest + i] && !down[rest - i + queen.length - 1] ) {
                queen[rest] = i;
                col[i] = true;
                up[rest + i] = true;
                down[rest - i + queen.length - 1] = true;
                backtracking(queen, rest + 1, col, up, down, result);

                // remove this queen
                col[i] = false;
                up[rest + i] = false;
                down[rest - i + queen.length - 1] = false;
            }
        }
    }

    public List<String[]> solveNQueens(int n)
    {
        // Since only one queen can occupy a row.
        // so queen[i] is the column of row i's queen.
        int[] queen = new int[n];

        // col[i] is false if it's not covered
        boolean[] col = new boolean[n];
        // diagonal from SW to NE
        boolean[] up = new boolean[2 * n - 1];
        // diagonal from NW to SE
        boolean[] down = new boolean[2 * n - 1];

        List<int[]> result = new ArrayList<int[]>();

        backtracking(queen, 0, col, up, down, result);

        return convertBoard(result);
    }

    private List<String[]> convertBoard(List<int[]> board)
    {
        List<String[]> result = new ArrayList<String[]>();
        for( int[] b : board ) {
            String[] strBoard = new String[board.get(0).length];

            int i = 0;
            for( int c : b ) {
                strBoard[i++] = getRowStr(c, board.get(0).length);
            }
            result.add(strBoard);
        }

        return result;
    }

    private String getRowStr(int c, int n)
    {
        StringBuilder sb = new StringBuilder();
        while( n-- > 0 ) {
            if( n == c ) {
                sb.append('Q');
                continue;
            }
            sb.append('.');
        }

        return sb.toString();
    }

    // Count possible arrangements of n queens on board
    public int totalNQueens(int n)
    {
        // Since only one queen can occupy a row.
        // so queen[i] is the column of row i's queen.
        int[] queen = new int[n];

        // col[i] is false if it's not covered
        boolean[] col = new boolean[n];
        // diagonal from SW to NE
        boolean[] up = new boolean[2 * n - 1];
        // diagonal from NW to SE
        boolean[] down = new boolean[2 * n - 1];

        backtracking(queen, 0, col, up, down);

        return count;
    }

    // This count is exclusively used for Queen-II problem
    private int count = 0;

    public void backtracking(int[] queen, int rest, boolean[] col, boolean[] up, boolean[] down)
    {
        if( rest == queen.length ) {
            count++;
            return;
        }

        // iterate through each column
        for( int i = 0; i < queen.length; ++i ) {
            if( !col[i] && !up[rest + i] && !down[rest - i + queen.length - 1] ) {
                queen[rest] = i;
                col[i] = true;
                up[rest + i] = true;
                down[rest - i + queen.length - 1] = true;
                backtracking(queen, rest + 1, col, up, down);

                // remove this queen
                col[i] = false;
                up[rest + i] = false;
                down[rest - i + queen.length - 1] = false;
            }
        }
    }


    /**
     * Sudoku solver. Remember solving this problem when I first preparing
     * job hunting. :)
     *
     *
     */
    public void solveSudoku(char[][] board)
    {
        int w = board.length;

        // We use each bit to indicate if a number has already been filled
        BitSet[] row = new BitSet[w];
        BitSet[] col = new BitSet[w];
        BitSet[] cell = new BitSet[w];
        List<Integer> unfilled = new ArrayList<Integer>();

        initSudoku(board, unfilled, row, col, cell);
        backtracking(board, unfilled, row, col, cell);
    }

    private void initSudoku(char[][] board, List<Integer> unfilled, BitSet[] row, BitSet[] col, BitSet[] cell)
    {
        for( int i = 0; i < board.length; ++i ) {
            row[i] = new BitSet(board.length);
            col[i] = new BitSet(board.length);
            cell[i] = new BitSet(board.length);
        }


        for( int i = 0; i < board.length; ++i ) {
            for( int j = 0; j < board.length; ++j ) {
                char c = board[i][j];
                if( c == '.' ) {
                    unfilled.add(i * board.length + j);
                    continue;
                }

                int idx = c - '1';
                row[i].set(idx);
                col[j].set(idx);

                int cellNo = i / 3 * 3 + j / 3;
                cell[cellNo].set(idx);
            }
        }
    }

    private void backtracking(char[][] board, List<Integer> unfilled, BitSet[] row, BitSet[] col, BitSet[] cell)
    {
        if( unfilled.size() == 0 ) {
            return;
        }

        int pos = unfilled.remove(0);
        int r = pos / board.length;
        int c = pos % board.length;
        for( int i = 0; i < board.length; ++i ) {
            int cellNo = r / 3 * 3 + c / 3;

            if( !row[r].get(i) && !col[c].get(i) && !cell[cellNo].get(i) ){
                board[r][c] = (char)(i + '1');
                row[r].set(i);
                col[c].set(i);
                cell[cellNo].set(i);
                backtracking(board, unfilled, row, col, cell);
                // Remove position
                row[r].set(i, false);
                col[c].set(i, false);
                cell[cellNo].set(i, false);
            }
        }
        unfilled.add(r * board.length + c);
    }

    /**
     * Final frontier of my understanding of recursion
     */
    public void hanoi(int n)
    {

    }

    private void backtracking(int[] candidates, List<Integer> possible, int target, Set<List<Integer>> result)
    {
        if( target  == 0 ) {
            List<Integer> copy = new ArrayList<Integer>(possible);
            Collections.sort(copy);
            result.add(copy);
            return;
        }

        for( int i : candidates ) {
            if( i <= target ) {
                possible.add(i);
                backtracking(candidates, possible, target - i, result);
                possible.remove((Integer)i);
            }
        }
    }

    // The same number can appear multiple of times
    public List<List<Integer>> combinationSum(int[] candidates, int target)
    {
        Set<List<Integer>> result = new HashSet<List<Integer>>();
        backtracking(candidates, new ArrayList<Integer>(), target, result);

        return new ArrayList<List<Integer>>(result);
    }


    public String longestCommonPrefix(String[] strs)
    {
        if( strs.length == 0 ) {
            return "";
        }

        if( strs.length == 1 ) {
            return strs[0];
        }

        boolean discrepancy = false;
        int j = 0;
        while( !discrepancy ) {
            for( int i = 1; i < strs.length; ++i ) {
                if( j == strs[i].length() || strs[i].charAt(j) != strs[0].charAt(j) ) {
                    discrepancy = true;
                    break;
                }
            }
            ++j;
        }

        return strs[0].substring(0, j - 1);
    }


    public int removeElement(int[] A, int elem)
    {
        int end = A.length - 1;
        int start = 0;
        while( start <= end ) {
            if( elem == A[start] ) {
                int tmp = A[end];
                A[end] = A[start];
                A[start] = tmp;
                end--;
            } else {
                start++;
            }
        }

        return end + 1;
    }

    /**
     * Do a binary search first and search both directions
     */
    public int[] searchRange(int[] A, int target)
    {
        int beg = 0;
        int end = A.length - 1;
        while( beg <= end ) {
            int mid = (beg + end) >> 1;
            int a = A[mid];
            if( a == target ) {
                int left = mid;
                while( left >= 0 && A[left] == target ) {
                    left--;
                }
                int right = mid;
                while( right < A.length && A[right] == target ) {
                    right++;
                }
                return new int[]{left+1, right-1};
            } else if( a < target ) {
                beg = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return new int[]{-1, -1};
    }

    public void setZeroes(int[][] matrix)
    {
        int m = matrix.length;
        int n = matrix[0].length;

        Set<Integer> rowSet = new HashSet<Integer>();
        Set<Integer> colSet = new HashSet<Integer>();

        for( int i = 0; i < m; ++i ) {
            for ( int j = 0; j < n; ++j ) {
                if( matrix[i][j] == 0 ) {
                    rowSet.add(i);
                    colSet.add(j);
                }
            }
        }

        for( int r : rowSet ) {
            Arrays.fill(matrix[r], 0);
        }

        for( int c : colSet ) {
            for( int i = 0; i < m; ++i ) {
                matrix[i][c] = 0;
            }
        }
    }

    // Do a double binary search
    public boolean searchMatrix(int[][] matrix, int target)
    {
        int beg = 0;
        int end = matrix.length - 1;
        while( beg <= end ) {
            int mid = (beg + end) >> 1;
            if( matrix[mid][0] == target ) {
                return true;
            } else if( matrix[mid][0] < target ) {
                beg = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        // Probably in end row
        int r = end < 0 ? 0 : end;
        beg = 0; end = matrix[r].length - 1;
        while( beg <= end ) {
            int mid = (beg + end) >> 1;
            if( matrix[r][mid] == target ) {
                return true;
            } else if( matrix[r][mid] < target ) {
                beg = mid + 1;
            }  else {
                end = mid - 1;
            }
        }
        return false;
    }


    private void backtracking(String s, List<String> ip, List<String> result)
    {
        if( (ip.size() == 1 && s.length() > 9) || (ip.size() == 2 && s.length() > 6) || (ip.size() == 3 && s.length() > 3) ) {
            return;
        }

        if( s.isEmpty() && ip.size() < 4 ) {
            return;
        }

        // No need to continue
        if( !s.isEmpty() && ip.size() == 4) {
            return;
        }

        if( s.isEmpty() && ip.size() == 4) {
            StringBuilder sb = new StringBuilder();
            for( String piece : ip ) {
                sb.append(piece);
                sb.append(".");
            }
            String addr = sb.toString();
            result.add(addr.substring(0, addr.length() - 1));
            return;
        }

        if( s.charAt(0) == '0' ) {
            ip.add(s.charAt(0) + "");
            backtracking(s.substring(1), ip, result);
            ip.remove(ip.size() - 1);
        }

        if( s.charAt(0) > '2' ) {
            ip.add(s.charAt(0) + "");
            backtracking(s.substring(1), ip, result);
            ip.remove(ip.size() - 1);

            if( s.length() > 1 ) {
                ip.add(s.substring(0, 2));
                backtracking(s.substring(2), ip, result);
                ip.remove(ip.size() - 1);
            }
        }

        if( s.charAt(0) == '1' ) {
            ip.add(s.charAt(0) + "");
            backtracking(s.substring(1), ip, result);
            ip.remove(ip.size() - 1);

            if( s.length() > 1 ) {
                ip.add(s.substring(0, 2));
                backtracking(s.substring(2), ip, result);
                ip.remove(ip.size() - 1);
            }

            if( s.length() > 2 ) {
                ip.add(s.substring(0, 3));
                backtracking(s.substring(3), ip, result);
                ip.remove(ip.size() - 1);
            }
        }

        if( s.charAt(0) == '2' ) {
            ip.add(s.charAt(0) + "");
            backtracking(s.substring(1), ip, result);
            ip.remove(ip.size() - 1);

            if( s.length() > 1 ) {
                ip.add(s.substring(0, 2));
                backtracking(s.substring(2), ip, result);
                ip.remove(ip.size() - 1);
            }

            if( s.length() > 2 && (s.charAt(1) < '5' || (s.charAt(1) == '5' && s.charAt(2) < '6')) ) {
                ip.add(s.substring(0, 3));
                backtracking(s.substring(3), ip, result);
                ip.remove(ip.size() - 1);
            }

        }

    }


    public List<String> restoreIpAddresses(String s)
    {
        List<String> result = new ArrayList<String>();
        backtracking(s, new ArrayList<String>(), result);

        return result;
    }

    public int lengthOfLastWord(String s)
    {
        s = s.trim();
        if( s.length() == 0 ) {
            return 0;
        }

        int len = 0;
        for( int j = s.length() - 1; j >= 0; --j ) {
            if( s.charAt(j) != ' ' ) {
                len++;
            } else {
                break;
            }
        }
        return len;
    }

    public List<List<Integer>> subsets(int[] S)
    {
        boolean[] mask = new boolean[S.length];
        Set<List<Integer>> result = new HashSet<List<Integer>>();

        Arrays.sort(S);
        backtracking(mask, 0, result, S);

        return new ArrayList<List<Integer>>(result);
    }

    private void backtracking(boolean[] mask, int curBit, Set<List<Integer>> result, int[] candidate)
    {
        if( curBit == mask.length ) {
            List<Integer> subset = new ArrayList<Integer>();
            for( int i = 0; i < mask.length; ++i ) {
                if( mask[i] ) {
                    subset.add(candidate[i]);
                }
            }
            result.add(subset);
            return;
        }
        mask[curBit] = false;
        backtracking(mask, curBit + 1, result, candidate);
        mask[curBit] = true;
        backtracking(mask, curBit + 1, result, candidate);
    }



    private final TreeNode MarkedTreeNode = new TreeNode(Integer.MAX_VALUE);
    /**
     * Binary Tree Problems
     */
    public List<List<Integer>> levelOrder(TreeNode root)
    {
        if( root == null ) {
            return new ArrayList<List<Integer>>();
        }
        Deque<TreeNode> level = new ArrayDeque<TreeNode>();
        level.push(root);
        level.add(MarkedTreeNode);

        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> cur = new ArrayList<Integer>();

        while( !level.isEmpty() ) {
            TreeNode first = level.pop();
            if( first != MarkedTreeNode ) {
                cur.add(first.val);
                if( first.left != null ) {
                    level.add(first.left);
                }
                if( first.right != null ) {
                    level.add(first.right);
                }
            } else if( cur.isEmpty() ) {
                break;
            } else {
                // Current level is done
                result.add(new ArrayList<Integer>(cur));
                cur.clear();

                level.add(MarkedTreeNode);
            }
        }

        return result;
    }

    // Similar to previous program, just alter the sequence of left
    // and right visiting every level
    public List<List<Integer>> zigzagLevelOrder(TreeNode root)
    {
        if( root == null ) {
            return new ArrayList<List<Integer>>();
        }
        Deque<TreeNode> level = new ArrayDeque<TreeNode>();
        level.push(root);
        level.add(MarkedTreeNode);

        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> cur = new ArrayList<Integer>();

        boolean leftAndRight = false;
        while( !level.isEmpty() ) {
            TreeNode first = level.pop();
            if( first != MarkedTreeNode ) {
                cur.add(first.val);
                if( leftAndRight ) {
                    if (first.left != null) {
                        level.add(first.left);
                    }
                    if (first.right != null) {
                        level.add(first.right);
                    }
                } else {
                    if (first.right != null) {
                        level.add(first.right);
                    }
                    if (first.left != null) {
                        level.add(first.left);
                    }
                }
            } else if( cur.isEmpty() ) {
                break;
            } else {
                // Current level is done
                result.add(new ArrayList<Integer>(cur));
                cur.clear();

                level.add(MarkedTreeNode);
                leftAndRight ^= true;
            }
        }

        return result;
    }

    // Backtracking! Backtracking! Backtracking!!!
    public List<List<String>> partition(String s)
    {
        List<List<String>> result = new ArrayList<List<String>>();
        List<String> oneParition = new ArrayList<String>();
        
        backtrackingPalindrome(s, oneParition, result);
        
        return result;
    }

    @SuppressWarnings("unchecked")
    private void backtrackingPalindrome(String s, List<String> partition, List<List<String>> result)
    {
    	if( s.isEmpty() ) {
            result.add(new ArrayList(partition));
            return;
        }

        for( int i = 1; i <= s.length(); ++i ) {
            String candidate = s.substring(0, i);
            if( checkPalindrome(candidate) ) {
                partition.add(candidate);
                backtrackingPalindrome(s.substring(i), partition, result);
                partition.remove(partition.size() - 1);
            }
        }
    }

    // Greedy algorithms
    public boolean canJump(int[] A)
    {
        if( A.length == 0 ) {
            return true;
        }
        // Cannot jump anywhere
        if( A[0] == 0 ) {
            return false;
        }

        for( int i = A.length - 2; i >= 0; ) {
            if( A[i] == 0 ) {
                int j = i - 1;
                while( j >= 0 && j + A[j] <= i ) {
                    j--;
                }
                if( j < 0 ) {
                    return false;
                }
                i = j;
            } else {
                i--;
            }
        }
        return true;
    }

    public int firstMissingPositive(int[] A)
    {
        double sum = (double)(1 + A.length) * (double)A.length / 2;
        int iSum = (int) sum;

        for (int aA : A) {
            if (aA > 0) {
                iSum -= aA;
            }
        }

        return iSum;
    }

    public int divide(int dividend, int divisor)
    {
        if( divisor == 0 ) {
            return Integer.MAX_VALUE;
        }

        boolean sign = (dividend < 0 && divisor > 0) || (dividend >0 && divisor < 0);

        dividend = Math.abs(dividend);
        divisor = Math.abs(divisor);

        int result = 0;
        while(dividend >=  divisor ) {
            if( result < 0 ) {
                return Integer.MAX_VALUE;
            }
            dividend -= divisor;
            result++;
        }

        return result * (sign ? - 1 : 1);
    }

    public List<List<Integer>> threeSum(int[] num) 
    {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        Arrays.sort(num);
        
        for( int i = 0; i < num.length - 2; ) {
            int j = i + 1;
            int k = num.length - 1;
            int target = - 1 * num[i];
            while( j < k ) {
                int tmp = num[j] + num[k];
                if( tmp == target ) {
                    List<Integer> ll = new ArrayList<Integer>();
                    ll.add(num[i]); ll.add(num[j]); ll.add(num[k]);
                    result.add(ll);
                    while( j < num.length - 1 && num[j + 1] == num[j] ) {
                        j++;
                    }
                    j++;

                    while( k > 0 && num[k - 1] == num[k] ) {
                        k--;
                    }

                    k--;
                } else if( tmp < target ) {
                    j++;
                } else {
                    k--;
                }
            }

            while( i < num.length - 1 && num[i + 1] == num[i] ) {
                ++i;
            }
            ++i;
        }    

        return result;
    }

    // Dynamic programming
    public int climbStairs(int n) 
    {
        int[] comb = new int[n];
        comb[0] = 1;
        comb[1] = 2;
        for( int i = 2; i < n; ++i ) {
            comb[i] = comb[i - 1] + comb[i - 2];
        }

        return comb[n - 1];
    }

    // How smart it is to just compare A[0] and A[2] since the array
    // is sorted !
    public int removeDuplicates(int[] A) 
    {
        int n = A.length;
        if (n <= 2) return n;       // no need to deal with n<=2 case.

        int len = 2, itor = 2;
        while (itor < n) {
            if (A[itor] != A[len-2])
                A[len++] = A[itor];
            itor++;
        }
        return len;
    }

    
    public String simplifyPath(String path) 
    {
        Deque<String> parts = new ArrayDeque<String>();

        for( int i = 0; i < path.length(); ) {
            if( path.charAt(i) == '/' ) {
                ++i;
                continue;
            }

            if( path.charAt(i) == '.') {
                int j = i + 1;
                while( j < path.length() && path.charAt(j) == '.' ) {
                    ++j;
                }
                if( j == i + 1) {
                    i = j;
                    continue;
                }
                if( path.charAt(j) == '/') {

                }
            }
            int j = i;
            while( path.charAt(j) != '/' && j < path.length() ) {
                j++;
            }
            parts.add(path.substring(i, j));
            i = j + 1;
        }

        StringBuilder sb = new StringBuilder();
        for( String p : parts ) {
            sb.append('/');
            sb.append(p);
        }

        if( sb.length() == 0 ) {
            return "/";
        }
        return sb.toString();
    }

    class LexicographicComparator implements  Comparator<Integer>
    {
        @Override
        public int compare(Integer a, Integer b)
        {
            if( a == b)
                return 0;

            String sa = Integer.toString(a);
            String sb = Integer.toString(b);

            return -1 * (sa + sb).compareTo(sb + sa);
        }
    }


    public String largestNumber(int[] num) 
    {
        List<Integer> nums = new ArrayList<Integer>(num.length);
        for( int i: num) {
            nums.add(i);
        }

        Collections.sort(nums, new LexicographicComparator());
        // Concat sequentially
        StringBuilder result = new StringBuilder();
        for( Integer i: nums) {
            result.append(i);
        }

        // Need some filtering
        String s = result.toString();
        int k = 0;
        for( ; k < s.length() - 1; ++k ) {
            if( s.charAt(k) != '0')
                break;
        }
        return s.substring(k);
    }

    private String getRange(int lower, int upper)
    {
        if( lower + 1 < upper) {
            if( lower == upper - 2 ) {
                return String.valueOf(lower + 1);
            } else {
                return String.valueOf(lower + 1) + "->" + String.valueOf(upper - 1);
            }
        }
        return "";
    }

    // A seemingly naive problem, but requires subtle implementation
    public List<String> findMissingRanges(int[] A, int lower, int upper) 
    {
        List<String> ranges = new ArrayList<String>();
        
        int[] expand = new int[A.length + 2];
        for( int i = 0; i < A.length; ++i ) {
            expand[i] = A[i];
        }
        expand[A.length] = lower;
        expand[A.length+1] = upper;
        Arrays.sort(expand);

        int j = 0;
        for( ; j < expand.length - 1; ++j ) {
            if( expand[j] < lower )
                continue;

            if( expand[j] == upper)
                break;

            String rg = getRange(expand[j], expand[j + 1]);
            if ( !rg.isEmpty() )
                ranges.add(rg);
        }

        return ranges;
    }

    public int majorityElement(int[] num)
    {
        Map<Integer, Integer> count = new HashMap<Integer, Integer>(num.length / 2);
        for( int i : num ) {
            if( !count.containsKey(i) ) {
                count.put(i, 0);
            }
            int n = count.get(i);
            if( n >= num.length / 2 ) {
                return i;
            }
            count.put(i, n+1);
        }
        // Majority element always exists
        return Integer.MAX_VALUE;
    }

    static final TreeLinkNode EMPTY_NODE = new TreeLinkNode(Integer.MAX_VALUE);
    /**
     * Connect tree node to its immediate cousin
     * The algorithm is intuitive but uses O(2^(N-1))
     *
     * @param root
     */
    public void connect(TreeLinkNode root)
    {
        // trivia cases
        if( root == null || root.left == null || root.right == null )
            return;

        Deque<TreeLinkNode> nodes = new ArrayDeque<TreeLinkNode>();
        nodes.push(root);
        nodes.add(EMPTY_NODE);

        while( !nodes.isEmpty() ) {
            TreeLinkNode curr = nodes.peek();
            if( curr == EMPTY_NODE ) {
                nodes.pop();
                continue;
            }

            if( curr.left != null )
                nodes.add(curr.left);
            if( curr.right != null )
                nodes.add(curr.right);

            nodes.pop();
            TreeLinkNode next = nodes.peek();
            // curr is the last element on current level
            if( next == EMPTY_NODE ) {
                curr.next = null;
                // add a placeholder on end of next level
                nodes.add(EMPTY_NODE);
            } else {
                curr.next = next;
            }
        }
    }

    /**
     * Connect tree node to its immediate cousin.
     * Use constant extra space. This means level
     * traversing isn't applicable
     *
     * @param root
     */
    public void connect_v2(TreeLinkNode root)
    {
        
    }

    /**
     */
    public List<List<Integer>> generate(int numRows) 
    {
        List<Integer> r1 = Arrays.asList(new Integer[]{1});
        List<Integer> r2 = Arrays.asList(new Integer[]{1, 1});

        List<List<Integer>> rows = new ArrayList<List<Integer>>();
        rows.add(r1);
        rows.add(r2);

        if( numRows <= 2 ) {
            return rows.subList(0, numRows);
        }

        for( int i = 2; i < numRows; ++i ) {
            List<Integer> prev = rows.get(rows.size()-1);
            List<Integer> curr = new ArrayList<Integer>(prev.size()+1);
            curr.add(1);
            for( int j = 0; j < prev.size() - 1; ++j ) {
                curr.add(prev.get(j) + prev.get(j+1));
            }
            curr.add(1);
            rows.add(curr);
        }

        return rows;
    }

    private int Max(int... args)
    {
        if( args.length == 1 )
            return 0;

        int max = Integer.MIN_VALUE;
        int i = 0;
        for( int j = 0; j < args.length; ++j ) {
            if( args[j] > max ) {
                max = args[j];
                i = j;
            }
        }
        return i;
    }


    /**
     * Find the area of largest rectangle. Seems like
     * a two dimensional DP.
     */
    public int maximalRectangle(char[][] matrix) 
    {
        int h = matrix.length;
        int w = matrix[0].length;
        int[][] area = new int[h][w];
        // holds top left/bottom right coordinates for the cell
        int[][][] info = new int[h][w][4];

        for( int i = 0; i < h; ++i ) {
            for( int j =0; j < w; ++j ) {
                if( i + j == 0 ) {
                    area[0][0] = matrix[i][j] == '1' ? 1 : 0;
                    info[0][0] = new int[]{0, 0, 0, 0};
                    continue;
                }
                // no contribution 
                if( matrix[i][j] == '0' ) {
                    if( j == 0 ) {
                        area[i][j] = area[i-1][j];
                        info[i][j] = info[i-1][j];
                    } else if( i == 0 ) {
                        area[i][j] = area[i][j-1];
                        info[i][j] = info[i][j-1];
                    } else {
                        if( area[i-1][j] >= area[i][j-1] ) {
                            area[i][j] = area[i-1][j];
                            info[i][j] = info[i-1][j];
                        } else {
                            area[i][j] = area[i][j-1];
                            info[i][j] = info[i][j-1];
                        }
                    }
                }  else {
                    if( j == 0 ) {
                        // check maximal so far is adjacent
                        if( info[i-1][j][2] == i-1) {
                            area[i][j] = area[i-1][j] + 1;
                            info[i][j] = info[i-1][j];
                            info[i][j][2] = i;
                            info[i][j][3] = j;
                        } else {
                            int u = i;
                            while( matrix[u][0] == '1' ) {
                                u--;
                            }
                            int a = i - u;
                            if( a > area[i-1][0] ) {
                                area[i][j] = a;
                                info[i][j][0] = u + 1;
                                info[i][j][1] = 0;
                                info[i][j][2] = i;
                                info[i][j][3] = 0;
                            } else {
                                area[i][j] = area[i-1][j];
                                info[i][j] = info[i-1][j];
                            }

                        }
                    } else if( i == 0 ) {
                        if( info[i][j-1][3] == j-1 ) {
                            area[i][j] = area[i][j-1] + 1;
                            info[i][j] = info[i][j-1];
                            info[i][j][2] = i;
                            info[i][j][3] = j;
                        } else {
                            int l = j;
                            while( matrix[0][l] == '1' ) {
                                l--;
                            }
                            int a = j - l;
                            if( a > area[0][j-1] ) {
                                area[i][j] = a;
                                info[i][j][0] = 0;
                                info[i][j][1] = l+1;
                                info[i][j][2] = 0;
                                info[i][j][3] = j;
                            } else {
                                area[i][j] = area[i][j-1];
                                info[i][j] = info[i][j-1];
                            }
                        }
                    } else {
                        int l = j;
                        while( l >= 0 && matrix[i][l] != '0' ) {
                            l--;
                        }
                        int u = i;
                        while( u >= 0 && matrix[u][j] != '0' ) {
                            u--;
                        }
                        int a = (i - u) * (j - l);
                        int   maxArea = Integer.MIN_VALUE;
                        int[] maxInfo = new int[4];
                        if( area[i][j-1] > maxArea ) {
                            maxArea = area[i][j-1];
                            maxInfo = info[i][j-1];
                        }
                        if( area[i-1][j-1] > maxArea ) {
                            maxArea = area[i-1][j-1];
                            maxInfo = info[i-1][j-1];
                        }
                        if( area[i-1][j] > maxArea ) {
                            maxArea = area[i-1][j];
                            maxInfo = info[i-1][j];
                        }

                        if( a > maxArea ) {
                            area[i][j] = a;
                            info[i][j][0] = u + 1;
                            info[i][j][1] = l + 1;
                            info[i][j][2] = i;
                            info[i][j][3] = j;
                        } else {
                            area[i][j] = maxArea;
                            info[i][j] = maxInfo;
                        }
                    }
                }   
            }
        }

        return area[h-1][w-1]; 
    }
}











































