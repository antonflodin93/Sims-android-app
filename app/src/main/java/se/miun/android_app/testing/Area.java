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
    float getxmin(){
        return xmin;
    }
    float getxmax(){
        return xmax;
    }
    float getymin(){
        return ymin;
    }
    float getymax(){
        return ymax;
    }
    Integer getrow(){
        return row;
    }
    Integer getcollumn(){
        return collumn;
    }
   //Vector<Integer> position = new Vector<>(2);
    private float xmin, xmax, ymin, ymax;
    private int row, collumn;
}
