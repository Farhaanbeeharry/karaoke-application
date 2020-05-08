public class HashFB<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    private int n;        // number of key-value pairs
    private int m;        // number of chains
    private Node[] st;    // array of linked-list symbol tables

    // a helper linked list data type
    private static class Node {
        private final Object key;
        private Object val;
        private Node next;

        public Node(Object key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    //Initializes an empty symbol table.
    public HashFB() {
        this(INIT_CAPACITY);
    }

    //Initializes an empty symbol table with {@code m} chains.
    public HashFB(int m) {
        this.m = m;
        st = new Node[m];
    }

    // resize the hash table to have the given number of chains,
    // rehashing all of the keys
    @SuppressWarnings("unchecked")
    private void resize(int chains) {
        HashFB<Key, Value> temp = new HashFB<Key, Value>(chains);
        for (int i = 0; i < m; i++) {
            for (Node x = st[i]; x != null; x = x.next) {
                temp.put((Key) x.key, (Value) x.val);
            }
        }

        this.m = temp.m;
        this.n = temp.n;
        this.st = temp.st;
    }

    // hash value between 0 and m-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    //Returns the value associated with the specified key in this symbol table.
    @SuppressWarnings("unchecked")
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int i = hash(key);
        for (Node x = st[i]; x != null; x = x.next) {
            if (key.equals(x.key)) return (Value) x.val;
        }
        return null;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Removes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            remove(key);
            return;
        }

        // double table size if average length of list >= 10
        if (n >= 10 * m) resize(2 * m);


        int i = hash(key);
        for (Node x = st[i]; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        n++;
        st[i] = new Node(key, val, st[i]);
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     */
    public void remove(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to remove() is null");

        int i = hash(key);
        st[i] = remove(st[i], key);

        // halve table size if average length of list <= 2
        if (m > INIT_CAPACITY && n <= 2 * m) resize(m / 2);
    }

    // remove key in linked list beginning at Node x
    // warning: function call stack too large if table is large
    private Node remove(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) {
            n--;
            return x.next;
        }
        x.next = remove(x.next, key);
        return x;
    }

    /**
     * Returns all keys in the symbol table.
     */
    @SuppressWarnings("unchecked")
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < m; i++) {
            for (Node x = st[i]; x != null; x = x.next) {
                queue.enqueue((Key) x.key);
            }
        }
        return queue;
    }

}

