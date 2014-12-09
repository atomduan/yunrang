package com.yunrang.hadoop.app.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Design and implement a data structure for Least Recently Used (LRU) cache. It
 * should support the following operations: get and set. get(key) - Get the
 * value (will always be positive) of the key if the key exists in the cache,
 * otherwise return -1. set(key, value) - Set or insert the value if the key is
 * not already present. When the cache reached its capacity, it should
 * invalidate the least recently used item before inserting a new item.
 * 
 * @author juntaoduan
 *
 */
public class LRUCache {
	private int capacity;
	private Map<Integer, HNode> cache;
	private LRUHeap heap;

	public LRUCache(int capacity) {
		this.capacity = capacity;
		this.cache = new HashMap<Integer, HNode>();
		this.heap = new LRUHeap();
	}

	public int get(int key) {
		HNode n = this.cache.get(key);
		if (n != null) {
			n.count = n.count + 1;
			return n.value;
		} else {
			return -1;
		}
	}

	public void set(int key, int value) {
		if (this.capacity == 0)
			return;
		if (cache.containsKey(key) == false) {
			HNode n = new HNode(key, value);
			if (this.cache.size() < capacity) {
				this.heap.add(n);
			} else {
				HNode old = this.heap.peek(n);
				this.cache.remove(old.key);
			}
			this.cache.put(key, n);
		} else {
			cache.get(key).value = value;
		}
	}

	private static class HNode {
		int key;
		int value;
		int count;
		public HNode(int key, int value) {
			this.key = key;
			this.value = value;
			this.count = 0;
		}
	}

	private static class LRUHeap {
		private List<HNode> heap = new ArrayList<HNode>();
		public void rebuild() {
			this.doRebuild(heap, 0);
		}
		private void doRebuild(List<HNode> heap, Integer idx) {
			int cl = idx * 2 + 1;
			int cr = idx * 2 + 2;
			if (cl >= heap.size() && cr >= heap.size())
				return;
			if (cl < heap.size()) {
				this.doRebuild(heap, cl);
			}
			if (cr < heap.size()) {
				this.doRebuild(heap, cr);
			}
			if (cl < heap.size() && cr >= heap.size()) {
				if (heap.get(idx).count > heap.get(cl).count) {
					this.swap(heap, idx, cl);
					this.doRebuild(heap, cl);
				}
			} else 
			if (cl < heap.size() && cr < heap.size()) {
				if (heap.get(cl).count < heap.get(cr).count) {
					if (heap.get(idx).count > heap.get(cl).count) {
						this.swap(heap, idx, cl);
						this.doRebuild(heap, cl);
					}
				} else {
					if (heap.get(idx).count > heap.get(cr).count) {
						this.swap(heap, idx, cr);
						this.doRebuild(heap, cr);
					}
				}
			}
			
		}
		private void swap(List<HNode> heap, int a, int b) {
			HNode tmp = heap.get(a);
			heap.set(a, heap.get(b));
			heap.set(b, tmp);
		}
		public void add(HNode node) {
			this.heap.add(node);
		}
		public HNode peek(HNode rep) {
			if (this.heap.get(0).value != 0) {
				this.rebuild();
			}
			HNode head = this.heap.get(0);
			this.heap.set(0, rep);
			return head;
		}
	}

	public static void main(String[] args) {
		LRUCache cache = new LRUCache(9);
		cache.set(1, 1);
		cache.set(2, 2);
		cache.set(3, 3);
		cache.set(4, 4);
		cache.set(5, 5);
		cache.set(6, 6);
		cache.set(7, 7);
		cache.set(8, 8);
		cache.set(9, 9);
		System.out.println(cache.get(1));
		System.out.println(cache.get(2));
		System.out.println(cache.get(3));
		System.out.println(cache.get(4));
		System.out.println(cache.get(5));
		System.out.println(cache.get(5));
		System.out.println(cache.get(7));
		System.out.println(cache.get(8));
		System.out.println(cache.get(9));
		System.out.println(cache.get(1));
		System.out.println(cache.get(2));
		System.out.println(cache.get(4));
		System.out.println(cache.get(5));
		System.out.println(cache.get(7));
		System.out.println(cache.get(4));
		cache.set(10, 10);
		System.out.println(cache.get(5));
		System.out.println(cache.get(10));
		System.out.println(cache.get(10));
		System.out.println(cache.get(10));
		System.out.println(cache.get(10));
		cache.set(11, 11);
		System.out.println(cache.get(10));
	}
}
