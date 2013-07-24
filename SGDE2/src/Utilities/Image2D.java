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
import javax.media.opengl.GL;

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
    Vector2 dim;
    float scalex;
    float scaley;
    
    public Image2D(String s){
        this.s=s;
        if(cache.containsKey(s)){
            img=cache.get(s);
            subimg=img.getImageTexCoords();
        }else{
            try{
                String suff=s.substring(s.indexOf(".")+1);
                img=TextureIO.newTexture(getClass().getClassLoader().getResource(s), true, suff);
                subimg=img.getImageTexCoords();
                cache.put(s, img);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    public void Render(GL gl){
        
    }
}
