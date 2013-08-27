/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;


public class Image2D{
    private Texture texture;
    private Vector2 dim;
    private Vector2 pos;
    
    //stuff not using right now
    Rectangle DrawnArea;
    float scalex;
    float scaley;
    float angle;
    int depth;
    float tint;
    Color colorTint;
    
    public Image2D(String s){
        try{
            texture=TextureIO.newTexture(new File(s), false);
            dim=new Vector2(texture.getImageWidth(), texture.getImageHeight());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void setDepth(int d){
        depth=d;
    }
    public void setPos(Vector2 pos){
        this.pos=pos;
    }
    public void setRotation(float angle){
        this.angle=angle;
    }
    public void setScale(float x, float y){
        scalex=x;
        scaley=y;
    }
    public void giveShading(Color color, float tint){
        this.tint=tint;
        this.colorTint=color;
    }
    public void setDrawnArea(Rectangle area){
        this.DrawnArea=area;
    }
    public int getWidth(){
        return texture.getImageWidth();
    }
    public int getHeight(){
        return texture.getImageHeight();
    }
    
    public Rect getRectangle(){
        return new Rect((int)(pos.getX()-dim.getX()/2), 
                (int)(pos.getY()-dim.getY()/2), 
                (int)dim.getX(), 
                (int)dim.getY(), 
                angle);
    }
    
    public void Render(GL gl, ViewScreen v){
        this.draw((int)pos.getX(), (int)pos.getY(), gl,v);
    }
    
    private void draw(int x, int y, GL gll, ViewScreen v) {
        GL2 gl = gll.getGL2();

        float modX = (texture.getImageWidth() / (float) v.getWidth());
        float modY = (texture.getImageHeight() / (float) v.getHeight());
        texture.enable(gl);
        texture.bind(gl);
        float X = (float) (((-1 / (v.getWidth() / 2f)) * x) + 1);
        float Y = (float) (((-1 / (v.getHeight() / 2f)) * y) + 1);
        gl.glTranslatef(X, Y, 0);
//        gl.glColor3f(1, 1, 1);

        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().bottom());
            gl.glVertex2f(-modX, -modY);
            gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().bottom());
            gl.glVertex2f(modX, -modY);
            gl.glTexCoord2f(texture.getImageTexCoords().right(), texture.getImageTexCoords().top());
            gl.glVertex2f(modX, modY);
            gl.glTexCoord2f(texture.getImageTexCoords().left(), texture.getImageTexCoords().top());
            gl.glVertex2f(-modX, modY);
        }
        gl.glEnd();
        gl.glPopMatrix();

    }

    
    /**
     * Internal class for manipulating textures. Source:http://www.cokeandcode.com/info/showsrc/showsrc.php?src=../spaceinvaders103/org/newdawn/spaceinvaders/jogl/Texture.java
     */

}
