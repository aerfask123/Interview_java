package com.interview.problem;

import java.util.Map;

public class LRUCache<K, V> {

    public static void main(String[] args) {
        LRUCache<Integer, Integer> cache = new LRUCache<Integer, Integer>(10);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.put(4, 4);
        cache.put(5, 5);
        cache.put(6, 6);
        cache.put(7, 7);
        cache.put(8, 8);
        cache.put(9, 9);
        cache.put(10, 10);
        System.out.println(cache);
        cache.put(11, 11);
        System.out.println(cache);
        cache.put(6, 12);
        System.out.println(cache);
        cache.put(2, 34);
        System.out.println(cache);
        cache.put(1, 3);
        System.out.println(cache);
        cache.get(8);
        System.out.println(cache);
    }

    Entry head, tail;
    int capacity;
    int size;
    Map<K, Entry> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.cache = new java.util.HashMap<K, Entry>(capacity + 2);
        initLinkedList();
    }

    private void initLinkedList() {
        this.head = new Entry();
        this.tail = new Entry();
        this.head.next = this.tail;
        this.tail.pre = this.head;
    }

    public V get(K key) {
        Entry node = cache.get(key);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return (V) cache.get(key).value;
    }

    public void put(K key, V value) {
        Entry node = cache.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
            return;
        }

        if (size == capacity) {
            Entry lastNode = tail.pre;
            deleteNode(lastNode);
            cache.remove(lastNode.key);
            size--;
        }

        Entry newNode = new Entry(key, value);
        addNode(newNode);
        cache.put(key, newNode);
        size++;
    }

    private void addNode(Entry node) {
        head.next.pre = node;
        node.next = head.next;

        node.pre = head;
        head.next = node;
    }

    private void deleteNode(Entry node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }

    private void moveToHead(Entry node) {
        deleteNode(node);
        addNode(node);
    }

    private static class Entry<K, V> {
        public Entry pre;
        public Entry next;
        public K key;
        public V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Entry() {

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Entry<K, V> node = this.head;
        while (node != null) {
            if (node.key == null) {
                node = node.next;
                continue;
            }
            sb.append(node.key + "," + node.value);
            sb.append("    ");
            node = node.next;
        }
        return sb.toString();
    }
}
