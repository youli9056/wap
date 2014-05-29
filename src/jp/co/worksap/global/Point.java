package jp.co.worksap.global;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This is an immutable object, represents a point in the road map.
 * @author Yuri
 */
public class Point implements Cloneable{
    public static enum Type{
        START,
        GOAL,
        CHECKPOINT,
        NORMAL,
        OBSTACLE
    };
    private final int x;
    private final int y;
    private final Type type;
    private Integer hashValue;
    private Type forType(char c){
        switch(c){
            case '#': 
                return Type.OBSTACLE;
            case '.':
                return Type.NORMAL;
            case '@':
                return Type.CHECKPOINT;
            case 'S':
                return Type.START;
            case 'G':
                return Type.GOAL;
            default:
                throw new NoSuchElementException();
        }
    }
    public Point(int x, int y, char c){
        this.x = x;
        this.y = y;
        this.type = forType(c);
    }
    public Point(Point point){
        this.x = point.x;
        this.y = point.y;
        this.type = point.type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int hashCode() {
        if(this.hashValue!=null)
            return this.hashValue;
        int hash = 5;
        hash = 29 * hash + this.x;
        hash = 29 * hash + this.y;
        hash = 29 * hash + Objects.hashCode(this.type);
        this.hashValue = hash;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + ", type=" + type + '}';
    }
    
}
