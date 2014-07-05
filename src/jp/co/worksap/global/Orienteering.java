package jp.co.worksap.global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import static jp.co.worksap.global.Point.Type.*;

/**
 * Class of finding an shortest path from start to goal and pass all the mandatory points.
 * @author Yuri
 */
public class Orienteering {

    private Point[][] roadMap;
    private Point start;
    private Point goal;
    private Map<Point,Map<Point,Integer>> distance;
    private List<Point> mandatoryPoints;
    private int width;
    private int height;
    
    private boolean connected = true;
    public static void main(String[] args) throws IOException{
        Orienteering ort = new Orienteering();
        ort.readInRoadMap();
        ort.calMandatoryPointsDistance();
        int dis = ort.getDistance();
        System.out.println(dis);
    }

    private void readInRoadMap() throws IOException {
        distance = new HashMap<Point,Map<Point,Integer>>();
        mandatoryPoints = new ArrayList<Point>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        BufferedReader reader = new BufferedReader(
//                new FileReader("C:\\Documents and Settings\\Administrator\\My Documents\\NetBeansProjects\\"
//                        + "example6.txt"));

        String line = reader.readLine();
        String[] params = line.split(" ");
        width = Integer.valueOf(params[0]);
        height = Integer.valueOf(params[1]);
        roadMap = new Point[height][width];
        int row = 0;
        while((line = reader.readLine())!=null){
            for(int col = 0; col < width; col++){
                Point point = new Point(row,col,line.charAt(col));
                roadMap[row][col] = point;
                switch(point.getType()){
                    case NORMAL:
                    case OBSTACLE:
                        break;
                    case START:
                        start = point;
                         break;
                    case GOAL:
                        goal = point;
                         break;
                    case CHECKPOINT:
                        mandatoryPoints.add(point);
                         break;
                }
            }
            row++;
        }
        reader.close();
    }

    /**
     * Calculate all mandatory points distance between each pair
     * inculding start and goal.
     */
    private void calMandatoryPointsDistance() {
        mandatoryPoints.add(goal);
        mandatoryPoints.add(start);
       for(int i = 0; i < mandatoryPoints.size()-1; i++){
            Point outPoint = mandatoryPoints.get(i);
            for(int j = i+1; j < mandatoryPoints.size(); j++){
                
                    Point inPoint = mandatoryPoints.get(j);
                    Integer dis = calDistance(outPoint, inPoint);
                    if(dis<0){
                        this.connected = false;
                        return;
                    }
                    //build distance stucture
                    Map<Point,Integer> outEntry = distance.get(outPoint);
                    if(outEntry == null){
                        outEntry = new HashMap<Point,Integer>();
                        distance.put(outPoint, outEntry);
                    }
                    outEntry.put(inPoint, dis);
                    
                    Map<Point,Integer> inEntry = distance.get(inPoint);
                    if(inEntry == null){
                        inEntry = new HashMap<Point, Integer>();
                        distance.put(inPoint, inEntry);
                    }
                    inEntry.put(outPoint, dis);
                
            }
        }
       //remove start from mandatoryPoints
       mandatoryPoints.remove(mandatoryPoints.size()-1);
       //remove goal from mandatoryPoints
       mandatoryPoints.remove(mandatoryPoints.size()-1);
    }
    
    /**
     * Similar to TSM problem, find the minimal distance travel all the mandatory points
     * with pruning tricks.
     * @return 
     */
    private int getDistance() {
        if(!this.connected)
            return -1;   
        permutation(mandatoryPoints,0);
        return min;
    }
    int min = Integer.MAX_VALUE;
    int cur = 0;
    
    /**
     * Using permutation method for finding minmum distance from start to goal point
     * pass all the mandatory points with pruning.
     * @param mPoints
     * @param begin 
     */
    private void permutation(List<Point> mPoints, int begin){
        if(begin==mPoints.size()){
            int dis = getDistance(begin);
            cur+=dis;
            min = Math.min(min, cur);
            cur-=dis;
        } else{
            for(int i = begin; i<mPoints.size(); i++){
                Collections.swap(mPoints, begin, i);
                int dis = getDistance(begin);
                cur += dis;
                if(cur < min)
                    permutation(mPoints,begin+1);
                Collections.swap(mPoints, begin, i);
                cur -= dis;
            }
        }
    }
    /**
     * Using A star to detect nearest distance between tow points.
     * @param outPoint
     * @param inPoint
     * @return minal distance between tow points; -1 if not reachable.
     */
    private Integer calDistance(Point outPoint, Point inPoint) {
        HashSet<Point> closePoints = new HashSet<Point>();
        //initial path, only contains the from point
        ConstantTimePriorityQueue openQueue = new ConstantTimePriorityQueue();
        int initH = calH(outPoint,inPoint);
        Path initPath = new Path(outPoint,ImmutableStack.EMPTYSTACK,initH,0,initH);
        openQueue.put(outPoint, initPath);
        
        while(!openQueue.isEmpty()){
            Path curPath = openQueue.dequeue();
            Point curPoint = curPath.getCurrentStep();
//            
//            if(curPoint.equals(inPoint)){
//                return curPath.getCost();
//            }
            //get the neighbor points that are reachable
            ArrayList<Point> potentialForwardPoints = getReachableNeighbor(curPoint);
            ImmutableStack<Point> furtherPath = curPath.getPath().push(curPoint);
            int curG = curPath.getG();
            for(Point point: potentialForwardPoints){
                if(point.equals(inPoint))
                    return curPath.getCost();
                if(closePoints.contains(point))
                    continue;
                Path tmpPath = openQueue.get(point);
                if(tmpPath==null){//this point isn't in open list
                    int tmpG = curG+1;
                    int tmpH = calH(point,inPoint);
                    tmpPath = new Path(point,furtherPath,tmpG+tmpH,tmpG,tmpH);
                    openQueue.put(point, tmpPath);
                }else{//this point is already in open list
                    //if this point's G is larger than that from curPoint
                    if(tmpPath.getG()>curG+1){
                        openQueue.remove(point);
                        int tmpG = curG+1;
                        int tmpH = tmpPath.getH();
                        tmpPath = new Path(point,furtherPath,tmpG+tmpH,tmpG,tmpH);
                        openQueue.put(point, tmpPath);
                    }
                }
            }
            closePoints.add(curPoint);
        } 
        return -1;
    }

    /**
     * Return all the point's neighbors which are reachable.
     * @param cp center point
     * @return reachable neighbors
     */
    private ArrayList<Point> getReachableNeighbor(Point cp) {
        ArrayList<Point> neighbor = new ArrayList<Point>(4);
        int x = cp.getX();
        int y = cp.getY();
        y -= 1;
        if(y>=0){//left point
            Point p = roadMap[x][y];
            if(p.getType()!=OBSTACLE)
                neighbor.add(p);
        }
        y += 2;
        if(y<width){//right point
            Point p = roadMap[x][y];
            if(p.getType()!=OBSTACLE)
                neighbor.add(p);
        }
        y = cp.getY();
        x -= 1;
        if(x >=0 ){//upper point
            Point p = roadMap[x][y];
            if(p.getType()!=OBSTACLE)
                neighbor.add(p);
        }
        x += 2;
        if(x<height){//down point
            Point p = roadMap[x][y];
            if(p.getType()!=OBSTACLE)
                neighbor.add(p);
        }
        return neighbor;
    }

    /**
     * Calculate the heuristic value from point p to poing g
     * @param p start point
     * @param g goal point
     * @return 
     */
    private int calH(Point p,Point g) {
        return Math.abs(p.getX()-g.getX())+Math.abs(p.getY()-g.getY());
    }

    /**
     * Get the distence between the index-1(th) node and index(th) point inmandatoryPoints.
     * If index out of boundry it means the start point or goal point.
     * @param index
     * @return 
     */
    private int getDistance(int index) {
        Point prevPoint ;
        Point curPoint;
        if(index == 0){
            prevPoint = start;
            if(mandatoryPoints.isEmpty())
                curPoint = goal;
            else
                curPoint = mandatoryPoints.get(index);
        }else if(index == mandatoryPoints.size()){
            return distance.get(goal).get(mandatoryPoints.get(index-1));
        }else{
            prevPoint = mandatoryPoints.get(index-1);
            curPoint = mandatoryPoints.get(index);
        }
            return distance.get(prevPoint).get(curPoint);
    }

    /**
     * Simulate a point's travel path using ImmutableStack.
     * Desinged for a star algorithm.
     * @see ImmutableStack
     */
    class Path implements Comparable<Path>{
        private Point currentStep;
        private ImmutableStack<Point> path;
        private int cost;
        private int g;
        private int h;
        public Path(Point currentStep, ImmutableStack<Point>path, int cost,int g,int h){
            this.currentStep = currentStep;
            this.path = path;
            this.cost = cost;
            this.g = g;
            this.h = h;
        }
        public Path(Point currentStep, ImmutableStack<Point>path){
            this.currentStep = currentStep;
            this.path = path;
            this.cost = -1;
        }

        @Override
        public int hashCode() {
            return this.currentStep.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Path other = (Path) obj;
            if (!this.currentStep.equals(other.currentStep)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Path o) {
             return this.cost - o.cost;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public Point getCurrentStep() {
            return currentStep;
        }

        public void setCurrentStep(Point currentStep) {
            this.currentStep = currentStep;
        }

        public ImmutableStack<Point> getPath() {
            return path;
        }

        public void setPath(ImmutableStack<Point> path) {
            this.path = path;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }

    }
    
}
