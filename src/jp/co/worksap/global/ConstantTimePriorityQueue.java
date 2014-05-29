package jp.co.worksap.global;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import jp.co.worksap.global.Orienteering.Path;

/**
 *
 * @author Yuri
 */
public class ConstantTimePriorityQueue<K,V> {
    HashMap<Point,Path> hashMap;
    TreeMap<Integer,Set<Path>> treeMap;
    
    /**
     * Value has default comparator
     */
    public ConstantTimePriorityQueue(){
        this.hashMap = new HashMap<Point,Path>();
        this.treeMap = new TreeMap<Integer,Set<Path>>();
    }
    public Path get(Point k){
        return this.hashMap.get(k);
    }
    public void put(Point key, Path value){
        this.hashMap.put(key, value);
        Set<Path> q = this.treeMap.get(value.getCost());
        if(q == null){
            q = new HashSet<Path>();
            this.treeMap.put(value.getCost(), q);
        }
        q.add(value);
    }
//    public void remove(K key){
//        V value = this.hashMap.get(key);
//        this.hashMap.remove(key);
//        this.treeMap.remove(value);
//    }
//     public void remove(K key, V value){
//        this.hashMap.remove(key);
//        this.treeMap.remove(value);
//    }
    /**
     * This method doesnot remove first element
     * @return the first priority element
     */
    public Path dequeue(){
        Iterator<Entry<Integer,Set<Path>>> entryIterator = this.treeMap.entrySet().iterator();
        Iterator<Path> elmentIterator = entryIterator.next().getValue().iterator();
        Path res = elmentIterator.next();
        elmentIterator.remove();
        this.hashMap.remove(res.getCurrentStep());
        if(!elmentIterator.hasNext()){
            entryIterator.remove();
        }
        return res;
    }
    public boolean isEmpty(){
        return this.hashMap.isEmpty();
    }

    void remove(Point point) {
        Path path = this.hashMap.get(point);
        this.hashMap.remove(point);
        
        Set<Path> elements = this.treeMap.get(path.getCost());
        elements.remove(path);
        if(elements.isEmpty())
            this.treeMap.remove(path.getCost());
    }
    
}
