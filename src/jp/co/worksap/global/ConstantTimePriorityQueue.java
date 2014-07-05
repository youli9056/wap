package jp.co.worksap.global;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import jp.co.worksap.global.Orienteering.Path;

/**
 * This class simulates a priority queue and hash map, which support constannt
 * time of get, put, remove and dequeue opration. It's designed to dynamically
 * change the element's priority and judge whether or not a specific element is
 * already added.
 *
 * @see HashMap
 * @see TreeMap
 * @author Yuri
 */
public class ConstantTimePriorityQueue {

    HashMap<Point, Path> hashMap;
    TreeMap<Integer, Set<Path>> treeMap;

    /**
     * Value has default comparator
     */
    public ConstantTimePriorityQueue() {
        this.hashMap = new HashMap<Point, Path>();
        this.treeMap = new TreeMap<Integer, Set<Path>>();
    }

    /**
     * Get the value element according to the unique key, property of HashMap.
     *
     * @see Point
     * @param key
     * @return the element associated to this key
     */
    public Path get(Point key) {
        return this.hashMap.get(key);
    }

    /**
     * Add and new element into this structure, values are compareable and keys
     * are unique. Value's priority is determined by it's cost.
     *
     * @see Path
     * @see Point
     * @param key
     * @param value
     */
    public void put(Point key, Path value) {
        this.hashMap.put(key, value);
        Set<Path> q = this.treeMap.get(value.getCost());
        if (q == null) {
            q = new HashSet<Path>();
            this.treeMap.put(value.getCost(), q);
        }
        q.add(value);
    }

    /**
     * This method doesnot remove first element, works like PriorityQueue
     *
     * @return the first priority element
     */
    public Path dequeue() {
        Iterator<Entry<Integer, Set<Path>>> entryIterator = this.treeMap.entrySet().iterator();
        Iterator<Path> elmentIterator = entryIterator.next().getValue().iterator();
        Path res = elmentIterator.next();
        elmentIterator.remove();
        this.hashMap.remove(res.getCurrentStep());
        if (!elmentIterator.hasNext()) {
            entryIterator.remove();
        }
        return res;
    }

    /**
     * Returns whether or not this queue is empty.
     *
     * @return true if this queue is empty; false otherwise.
     */
    public boolean isEmpty() {
        return this.hashMap.isEmpty();
    }

    /**
     * Simply remove one element. This funciton is designed to change one
     * element's priority, by remove it first and change it's priority and then
     * add it back.
     *
     * @param point the element's key you want to remove
     */
    public void remove(Point point) {
        Path path = this.hashMap.get(point);
        this.hashMap.remove(point);

        Set<Path> elements = this.treeMap.get(path.getCost());
        elements.remove(path);
        if (elements.isEmpty()) {
            this.treeMap.remove(path.getCost());
        }
    }

}
