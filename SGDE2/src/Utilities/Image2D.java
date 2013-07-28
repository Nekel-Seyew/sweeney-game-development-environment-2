/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import Game.Game;
import Game.GameBase;


public class Image2D{
    private static TextureLoader loader;
    private static GL gl;
    
    
    //private cTexture texture;
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
//            texture = loader.getTexture(s);
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
        return new Rect((int)pos.getX(), (int)pos.getY(), (int)dim.getX(), (int)dim.getY(), angle);
    }
    
    public void Render(GL gl){
        this.draw((int)pos.getX(), (int)pos.getY(), gl);
    }
    
    private void draw(int x, int y, GL gll){
        GL2 gl = gll.getGL2();
        float width=texture.getImageWidth();
        float height=texture.getImageHeight();
        
        float X,Y;
        X=((float)x)/GameBase.game.WIDTH;
        Y=((float)y)/GameBase.game.HEIGHT;
//        X=-1+X;
//        Y=1-Y;
        
        gl.glPushMatrix();
        texture.bind(gl);
        texture.enable(gl);
        gl.glTranslatef(X, Y, 0);
//        gl.glColor3f(1, 1, 1);
        
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(0, 0);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2f(0, texture.getHeight());
            gl.glVertex2f(0, (float)dim.getY());
            gl.glTexCoord2f(texture.getWidth(), texture.getHeight());
            gl.glVertex2f((float)dim.getX(), (float)dim.getY());
            gl.glTexCoord2f(texture.getWidth(), 0);
            gl.glVertex2f((float)dim.getX(), 0);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }
    
    public static void init(GL gl){
        Image2D.gl=gl;
        Image2D.loader =new TextureLoader(Image2D.gl);
    }
    
    /**
     * Internal class for manipulating textures. Source:http://www.cokeandcode.com/info/showsrc/showsrc.php?src=../spaceinvaders103/org/newdawn/spaceinvaders/jogl/Texture.java
     */
    protected static class cTexture{
        private int target;
        private int TextureID;
        private int height;
        private int width;
        private int texWidth;
        private int texHeight;
        private float widthRatio;
        private float heightRatio;
        
        public cTexture(int target, int textureID){
            this.target=target;
            this.TextureID=textureID;
        }
        
        public void bind(GL gl){
            gl.glBindTexture(target, TextureID);
        }
        
        public void setHeight(int height){
            this.height=height;
            setHeight();
        }
        
        public void setWidth(int width){
            this.width=width;
            setWidth();
        }
        
        public int getImageHeight(){
            return height;
        }
        public int getImageWidth(){
            return width;
        }
        
        public float getHeight(){
            return heightRatio;
        }
        public float getWidth(){
            return widthRatio;
        }
        
        public void setTextureHeight(int texHeight){
            this.texHeight=texHeight;
            setHeight();
        }
        public void setTextureWidth(int texWidth){
            this.texWidth=texWidth;
            setWidth();
        }
        
        private void setHeight(){
            if(texHeight!=0){
                heightRatio=((float)height)/texHeight;
            }
        }
        
        private void setWidth(){
            if(texWidth!=0){
                widthRatio=((float)width)/texWidth;
            }
        }
    }
    
    /**
     * Internal class to maintain a cache of textures and to proper load them as well.
     * Source: http://www.cokeandcode.com/info/showsrc/showsrc.php?src=../spaceinvaders103/org/newdawn/spaceinvaders/jogl/TextureLoader.java
     */
    private static class TextureLoader{
        private Hashtable<String, cTexture> table = new Hashtable<String, cTexture>();
        private GL gl;
        private ColorModel glAlphaColorModel;
        private ColorModel glColorModel;
        
        public TextureLoader(GL gl){
            this.gl=gl;
            glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                    new int[]{8,8,8,8},
                    true,
                    false,
                    ComponentColorModel.TRANSLUCENT,
                    DataBuffer.TYPE_BYTE);
            glColorModel= new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                    new int[]{8,8,8,0},
                    false,
                    false,
                    ComponentColorModel.TRANSLUCENT,
                    DataBuffer.TYPE_BYTE);
        }
        private int createTextureID(){
            int[] tmp=new int[1];
            gl.glGenTextures(1, tmp, 0);
            return tmp[0];
        }
        
        public cTexture getTexture(String resourceName) throws IOException{
            if(table.containsKey(resourceName)){
                return table.get(resourceName);
            }
            cTexture tex = getTexture(resourceName, GL.GL_TEXTURE_2D, GL.GL_RGBA, GL.GL_LINEAR, GL.GL_LINEAR);
            table.put(resourceName, tex);
            return tex;
        }
        
        public cTexture getTexture(String resourceName, int target, int dstPixelFormat,int minFilter, int magFilter) throws IOException{
            int srcPixelFormat=0;
            int textureID=createTextureID();
            cTexture texture= new cTexture(target,textureID);
            BufferedImage bufferedImage = loadImage(resourceName);
            texture.setWidth(bufferedImage.getWidth());
            texture.setHeight(bufferedImage.getHeight());
            if(bufferedImage.getColorModel().hasAlpha()){
                srcPixelFormat=GL.GL_RGBA;
            }else{
                srcPixelFormat=GL.GL_RGB;
            }
            ByteBuffer textureBuffer = convertImageData(bufferedImage, texture);
            if(target==GL.GL_TEXTURE_2D){
                gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
                gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, magFilter);
            }
            gl.glTexImage2D(target, 
                    0, 
                    dstPixelFormat, 
                    get2Fold(bufferedImage.getWidth()), 
                    get2Fold(bufferedImage.getHeight()), 
                    0, srcPixelFormat, 
                    GL.GL_UNSIGNED_BYTE, 
                    textureBuffer);
            return texture;
        }
        
        private int get2Fold(int fold){
            int ret=2;
            while(ret<fold){
                ret*=2;
            }
            return ret;
        }
        private ByteBuffer convertImageData(BufferedImage img, cTexture tex){
            ByteBuffer imageBuffer=null;
            WritableRaster raster;
            BufferedImage texImage;
            
            int texWidth= get2Fold(img.getWidth());
            int texHeight=get2Fold(img.getHeight());
            
            tex.setTextureHeight(texHeight);
            tex.setTextureWidth(texWidth);
            
            if(img.getColorModel().hasAlpha()){
                raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
                texImage= new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
            }else{
                raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
                texImage= new BufferedImage(glColorModel, raster, false, new Hashtable());
            }
            
            Graphics g = texImage.getGraphics();
            g.setColor(new Color(0f,0f,0f,0f));
            g.fillRect(0, 0, texWidth, texHeight);
            g.drawImage(img, 0, 0, null);
            
            byte[] data =((DataBufferByte)texImage.getRaster().getDataBuffer()).getData();
            imageBuffer= ByteBuffer.allocateDirect(data.length);
            imageBuffer.order(ByteOrder.nativeOrder());
            imageBuffer.put(data, 0, data.length);
            imageBuffer= (ByteBuffer)imageBuffer.rewind();
            
            return imageBuffer;
        }
        
        private BufferedImage loadImage(String ref) throws IOException{
//            URL url = TextureLoader.class.getClassLoader().getResource(ref);
//            if(url==null){
//                throw new IOException("Cannot find: "+ref);
//            }
//            BufferedImage img= ImageIO.read(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref)));
            return ImageIO.read(new File(ref));
        }
        
    }
}
