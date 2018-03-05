package com.liuxiaozhu.hashmap;

import android.util.Log;

import java.util.Arrays;

/**
 * Author：Created by liuxiaozhu on 2018/3/2.
 * Email: chenhuixueba@163.com
 */

public class MyHashMap<K, V> {
    //加载因子
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //默认容量
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    //数组最大不超过2的30次方
    static final int MAXIMUM_CAPACITY = 1 << 30;
    //    阈值（实际装载容量）
    private int threshold;
    int size;

    private Node<K, V>[] table;
    private float loadFactor;


    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public MyHashMap(int initialCapacity) {
        this();
        if (initialCapacity <= 0) {
            throw new NullPointerException();
        }
        if (initialCapacity >= MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        threshold = tableSizeFor(initialCapacity);
    }

    final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public V get(K key) {
        int hash = getHashCode(key);
        if (table == null || table.length == 0) {
            return null;
        }
        Node head = table[(table.length - 1) & hash];
        if (head == null) {
            return null;
        } else {
            if (head.hash == hash && head.key.equals(key)) {
                return (V) head.value;
            } else {
                while (true) {
                    if (head.next != null) {
                        if (head.hash == hash && head.key.equals(key)) {
                            return (V) head.value;
                        }
                        head = head.next;
                    } else return null;
                }
            }
        }
    }

    /**
     * 添加数据
     *
     * @param key
     * @param value
     */
    public V put(K key, V value) {
        return putVal(getHashCode(key), key, value);
    }

    private V putVal(int hashCode, K key, V value) {

        if (table == null || (table.length) == 0) {
            //初始化数组
            table = resize();
        }
        int tabLength = table.length;
        int tabIndex = tabLength - 1;
        Node headInTab = table[tabIndex & hashCode];
        Node node = null;
        if (headInTab == null) {
            table[tabIndex & hashCode] = new Node<>(hashCode, key, value, null);
        } else {
            if (headInTab.hash == hashCode && (key != null && key.equals(headInTab.key))) {
                node = headInTab;
            } else {
                while (true) {
                    if (headInTab.next != null) {
                        if (headInTab.hash == hashCode && (key != null && key.equals(headInTab.key))) {
                            node = headInTab;
                            break;
                        }
                        headInTab = headInTab.next;
                    } else {
                        headInTab.next = new Node<>(hashCode, key, value, null);
                    }
                }
            }
        }
        if (node != null) {
            return (V) node.value;
        }
        size++;
        if (size > threshold) {
            table = resize();
        }

        return null;
    }

    /**
     * 数组扩容
     */
    private Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;

        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap = 0, newThr = 0;

        if (oldCap > 0) {
            //扩容
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = MAXIMUM_CAPACITY;
            } else {
                newThr = oldThr << 1;
                newCap = oldCap << 1;
            }
        } else if (oldThr > 0) {
            //设置数组大小
            newCap = oldThr;
        } else {
            //默认初始化数据
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        Node[] newTable = new Node[newCap];
        if (oldTab != null) {
            for (int i = 0; i < oldCap; i++) {
                Node<K, V> e = oldTab[i];
                if (e != null) {
                    oldTab[i] = null;
                    if (e.next == null) {
                        newTable[e.hash & (newCap - 1)] = e;
                    } else {
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTable[i] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTable[i + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTable;
    }

    /**
     * 获取hash码
     *
     * @param key 可以是null
     * @return
     */
    private int getHashCode(K key) {
        int h = 0;
        return key == null ? 0 : (h = key.hashCode() ^ (h >>> 16));
    }

    /**
     * 数据节点
     *
     * @param <K>
     * @param <V>
     */
    private class Node<K, V> {
        int hash;
        K key;
        Node<K, V> next;
        V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "MyHashMap{" +
                "threshold=" + threshold +
                ", size=" + size +
                ", table=" + Arrays.toString(table) +
                ", loadFactor=" + loadFactor +
                '}';
    }
}
