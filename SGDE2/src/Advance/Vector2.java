/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Advance;

/**
 *
 * @author KyleSweeney
 */
public class Vector2<T> {
    protected T x,y;
    
    public Vector2(T x, T y){
        this.x=x;
        this.y=y;
    }
    
    public T getX(){
        return x;
    }
    
    public T getY(){
        return y;
    }
    
    public void dX(T dx){
        //this.x+=dx;
    }
}
