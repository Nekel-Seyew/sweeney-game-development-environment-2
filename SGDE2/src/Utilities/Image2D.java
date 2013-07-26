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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

/**
 *
 * @author kdsweenx
 */
public class Image2D {
    private static Hashtable<String, cTex> cache=new Hashtable<String, cTex>();
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
    
    //experiment
    cTex texture;
    static GL gl=null;
    static TexLoader loader=null;
    
    public Image2D(String s){
        
        if(loader==null){
            loader = new TexLoader(gl);
        }
        
        this.s=s;
        if(cache.containsKey(s)){
            texture=cache.get(s);
//            img=cache.get(s);
//            subimg=img.getImageTexCoords();
//            BufferedImage bi=cache2.get(s);
//            dim=new Vector2(bi.getWidth(),bi.getHeight());
//            drawable=new Rect(0,0,(int)subimg.right(),(int)subimg.bottom());
        }else{
            try{
                
//                BufferedImage bi = ImageIO.read(new File(s));
//                img=TextureIO.newTexture(new File(s), false);
//                subimg=img.getImageTexCoords();
//                dim=new Vector2(bi.getWidth(),bi.getHeight());
//                cache.put(s, img);
//                cache2.put(s, bi);
//                drawable=new Rect(0,0,(int)subimg.right(),(int)subimg.bottom());
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
    
    //Possible help: http://www.cokeandcode.com/info/tut2d-3.html
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
    
    public static void giveGL(GL gl){
        Image2D.gl=gl;
    }
    
    public class TexLoader{
        GL gl;
        ColorModel alphaColor;
        ColorModel colorModel;
        
        public TexLoader(GL gl){
            this.gl=gl;
            alphaColor=new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), 
                    new int[]{8,8,8,8}, true, false, 
                    ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
            colorModel=new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), 
                    new int[]{8,8,8,0}, false, false, 
                    ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
        }
        
        private int getID(){
            int[] tmp = new int[1];
            gl.glGenTextures(1, tmp, 0);
            return tmp[0];
        }
        
        public cTex getTex(String resource){
            cTex t=null;
            try{
                return getTex(resource, GL.GL_TEXTURE_2D, GL.GL_RGBA, GL.GL_LINEAR, GL.GL_LINEAR);
            }catch(Exception e){
                e.printStackTrace();
            }
            return t;
        }
        
        public cTex getTex(String name, int target, int destPixForm, int minFilter, int magFilter) throws IOException{
            int srcPixForm=0;
            int texID=getID();
            cTex texture = new cTex(target, texID);
            gl.glBindTexture(target, texID);
            BufferedImage img=ImageIO.read(new File(name));
            texture.setWidth(img.getWidth());
            texture.setHeight(img.getHeight());
            
            if(img.getColorModel().hasAlpha()){
                srcPixForm=GL.GL_RGBA;
            }else{
                srcPixForm=GL.GL_RGB;
            }
            
            ByteBuffer texBuffer= convertImageData(img, texture);
            if(target==GL.GL_TEXTURE_2D){
                gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
                gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, minFilter);
            }
            
            gl.glTexImage2D(target, 0, destPixForm, get2Fold(img.getWidth()), get2Fold(img.getHeight()), 0, srcPixForm, GL.GL_UNSIGNED_BYTE, texBuffer);
            return texture;
            
        }
        
        private int get2Fold(int num){
            int ret=2;
            while(ret<num){
                ret*=2;
            }
            return ret;
        }
        
        private ByteBuffer convertImageData(BufferedImage img, cTex tex){
            ByteBuffer buf=null;
            WritableRaster raster;
            BufferedImage texImg;
            
            int texWidth=2;
            int texHeight=2;
            while(texWidth<img.getWidth()){
                texWidth*=2;
            }
            while(texHeight<img.getHeight()){
                texHeight*=2;
            }
            
            tex.setTexWidth(texWidth);
            tex.setTexheight(texHeight);
            
            if(img.getColorModel().hasAlpha()){
                raster=Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
                texImg=new BufferedImage(alphaColor, raster, false, new Hashtable());
            }else{
                raster=Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
                texImg=new BufferedImage(colorModel, raster, false, new Hashtable());
            }
            
            Graphics g = texImg.getGraphics();
            g.setColor(new Color(0f,0f,0f,0f));
            g.fillRect(0, 0, texWidth, texHeight);
            g.drawImage(img, 0,0, null);
            
            byte[] data =((DataBufferByte)texImg.getRaster().getDataBuffer()).getData();
            buf=ByteBuffer.allocateDirect(data.length);
            buf.order(ByteOrder.nativeOrder());
            buf.put(data,0,data.length);
            
            return buf;
        }
    }
    
    private class cTex{
        int ID;
        int target;
        int width;
        int height;
        int texheight;
        int texWidth;
        
        public cTex(int target, int ID){
            this.ID=ID;
            this.target=target;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getTexheight() {
            return texheight;
        }

        public void setTexheight(int texheight) {
            this.texheight = texheight;
        }

        public int getTexWidth() {
            return texWidth;
        }

        public void setTexWidth(int texWidth) {
            this.texWidth = texWidth;
        }
        
        
    }
}
