/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import com.jogamp.opengl.util.awt.TextureRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Rectangle;
import java.io.File;
import javax.media.opengl.GL;


/**
 *
 * @author kdsweenx
 */
public class Image2D2 {

    Texture texture;
    Vector2 pos;
    int depth;
    
    public Image2D2(String s){
        try{
            texture= TextureIO.newTexture(new File(s), true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void setPosition(Vector2 pos){
        this.pos=pos;
    }
    
    public void setDepth(int depth){
        this.depth=depth;
    }
    
    public Rectangle getRect(){
        return new Rect(new Vector2(pos.getX()- texture.getImageWidth()/2,pos.getY()-texture.getImageHeight()), 
                texture.getImageWidth(), 
                texture.getImageHeight(), 
                0);
    }
    
    public void Render(GL gl){
        TextureRenderer renderer=TextureRenderer.createAlphaOnlyRenderer(texture.getWidth(), texture.getHeight(), true);
         
    }
    
}