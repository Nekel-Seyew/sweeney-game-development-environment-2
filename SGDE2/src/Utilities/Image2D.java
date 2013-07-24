/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import com.jogamp.opengl.util.texture.Texture;
import java.util.Hashtable;
import Game.Game;
import Game.GameBase;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Color;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

/**
 *
 * @author kdsweenx
 */
public class Image2D {
    private static Hashtable<String, Texture> cache;
    Rect drawable;
    Texture img;
    String s;
    TextureCoords subimg;
    Vector2 pos;
    Vector2 dim; //x is width, y is height
    float scalex;
    float scaley;
    Color tint;
    float percentTint;
    float angle;
    
    public Image2D(String s){
        this.s=s;
        if(cache.containsKey(s)){
            img=cache.get(s);
            subimg=img.getImageTexCoords();
        }else{
            try{
                String suff=s.substring(s.indexOf("."));
                img=TextureIO.newTexture(getClass().getClassLoader().getResource(s), true, suff);
                subimg=img.getImageTexCoords();
                dim=new Vector2(Math.abs(subimg.left()-subimg.right()),Math.abs(subimg.top()-subimg.bottom()));
                cache.put(s, img);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void setScale(float x, float y){
        this.scalex=x;
        this.scaley=y;
    }
    
    public void setRect(Rect sub){
        this.drawable=sub;
    }
    
    public void setPos(Vector2 p){
        this.pos=p;
    }
    
    public void setRotation(float r){
        this.angle=r;
    }
    
    public void setTint(Color c, float tint){
        this.tint=c;
        this.percentTint=tint;
    }
    
    
    
    
    public void Render(GL2 gl, ViewScreen vs){
        img.enable(gl);
        img.bind(gl);
        
        Vector2 ur=new Vector2(pos.getX()-(dim.getX()/2),pos.getY()-(dim.getY()/2));
        Rect imgR=new Rect((int)ur.getX(), (int)ur.getY(), (int)(dim.getX()*scalex),(int)(dim.getY()*scaley), angle);
        
        gl.glBegin(GL2.GL_QUADS);
        //upper-left
        gl.glTexCoord2f(drawable.x,drawable.y);
        gl.glVertex2f((float)imgR.UpperLeftCorner.getX(), (float)imgR.UpperLeftCorner.getY());
        //upper-right
        gl.glTexCoord2f(drawable.x+drawable.width, drawable.y);
        gl.glVertex2f((float)imgR.UpperRightCorner.getX(), (float)imgR.UpperRightCorner.getY());
        //bottom-right
        gl.glTexCoord2f(drawable.x+drawable.width, drawable.y+drawable.height);
        gl.glVertex2f((float)imgR.LowerRightCorner.getX(), (float)imgR.LowerRightCorner.getY());
        //bottom-left
        gl.glTexCoord2f(drawable.x,drawable.y+drawable.height);
        gl.glVertex2f((float)imgR.LowerLeftCorner.getX(), (float)imgR.LowerLeftCorner.getY());
        gl.glEnd();
        
    }
}
