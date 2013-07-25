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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

/**
 *
 * @author kdsweenx
 */
public class Image2D {
    private static Hashtable<String, Texture> cache=new Hashtable<String, Texture>();
    private static Hashtable<String, BufferedImage> cache2=new Hashtable<String, BufferedImage>();
    Rect drawable;//0,0 to 1,1
    Texture img;
    String s;
    TextureCoords subimg;
    Vector2 pos;
    Vector2 dim; //x is width, y is height
    float scalex=1.0f;
    float scaley=1.0f;
    Color tint;
    float percentTint;
    float angle=0.0f;
    int depth;
    Rect myRect;
    
    public Image2D(String s){
        this.s=s;
        if(cache.containsKey(s)){
            img=cache.get(s);
            subimg=img.getImageTexCoords();
            BufferedImage bi=cache2.get(s);
            dim=new Vector2(bi.getWidth(),bi.getHeight());
            drawable=new Rect(0,0,(int)subimg.right(),(int)subimg.bottom());
        }else{
            try{
                BufferedImage bi = ImageIO.read(new File(s));
                img=TextureIO.newTexture(new File(s), false);
                subimg=img.getImageTexCoords();
                dim=new Vector2(bi.getWidth(),bi.getHeight());
                cache.put(s, img);
                cache2.put(s, bi);
                drawable=new Rect(0,0,(int)subimg.right(),(int)subimg.bottom());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void setScale(float x, float y){
        this.scalex=x;
        this.scaley=y;
    }
    
    public void setDrawnArea(Rectangle sub){
        this.drawable=new Rect(sub);
    }
    
    public void setPos(Vector2 p){
        this.pos=p;
    }
    
    public void setRotation(float r){
        this.angle=r;
        //calcRect();
    }
    
    public void giveShading(Color c, float tint){
        this.tint=c;
        this.percentTint=tint;
    }
    
    public void setDepth(int i){
        this.depth=i;
    }
    
    public void calcRect(){
        try {
            this.myRect = new Rect((int) (pos.getX() - (dim.getX() / 2)), 
                    (int) ( pos.getY() - (dim.getY() / 2)), 
                    (int) (dim.getX() * scalex), 
                    (int) (dim.getY() * scaley), 
                    angle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Rect getRectangle(){
        this.calcRect();
        return myRect;
    }
    
    
    public void Render(GL2 gl){
        gl.glPushMatrix();
        img.enable(gl);
        img.bind(gl);
        
        //Vector2 ur=new Vector2(pos.getX()-(dim.getX()/2),pos.getY()-(dim.getY()/2));
        Rect imgR=myRect;
//        
//        gl.glBegin(GL2.GL_QUADS);
//        //upper-left
//        gl.glTexCoord2f(drawable.x,drawable.y);
//        gl.glVertex2f((float)imgR.UpperLeftCorner.getX(), (float)imgR.UpperLeftCorner.getY());
//        //upper-right
//        gl.glTexCoord2f(drawable.x+drawable.width, drawable.y);
//        gl.glVertex2f((float)imgR.UpperRightCorner.getX(), (float)imgR.UpperRightCorner.getY());
//        //bottom-right
//        gl.glTexCoord2f(drawable.x+drawable.width, drawable.y+drawable.height);
//        gl.glVertex2f((float)imgR.LowerRightCorner.getX(), (float)imgR.LowerRightCorner.getY());
//        //bottom-left
//        gl.glTexCoord2f(drawable.x,drawable.y+drawable.height);
//        gl.glVertex2f((float)imgR.LowerLeftCorner.getX(), (float)imgR.LowerLeftCorner.getY());
        
        gl.glTranslatef((float)imgR.UpperLeftCorner.getX(),(float)imgR.UpperLeftCorner.getY(), 0);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(0, 0);
            gl.glVertex2f(0,0);
            gl.glTexCoord2f(0,this.img.getHeight());
            gl.glVertex2f(0,(float)this.dim.getY());
            gl.glTexCoord2f(this.img.getWidth(), this.img.getHeight());
            gl.glVertex2f((float)this.dim.getX(), (float)this.dim.getY());
            gl.glTexCoord2f(this.img.getWidth(), 0);
            gl.glVertex2f((float)this.dim.getX(),0);
        }
        
        gl.glEnd();
        gl.glPopMatrix();
        
    }
    
    public String toString(){
        String s="name of File: "+this.s;
        return s;
    }
}
