package se.miun.android_app.testing;


import java.util.Vector;

public class Area {
    public Area(float xmin, float xmax, float ymin, float ymax, int row, int collumn){
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.row = row;
        this.collumn = collumn;
    }
    public float getxmin(){
        return xmin;
    }
    public float getxmax(){
        return xmax;
    }
    public float getymin(){
        return ymin;
    }
    public float getymax(){
        return ymax;
    }
    public Integer getrow(){
        return row;
    }
    public Integer getcollumn(){
        return collumn;
    }
   //Vector<Integer> position = new Vector<>(2);
    private float xmin, xmax, ymin, ymax;
    private int row, collumn;
}
