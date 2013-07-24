/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Utilities.ImageCollection;
import Utilities.InputAdvance;
import Utilities.KeyBoard;
import Utilities.Mouse;
import Utilities.Vector2;
import Utilities.ViewScreen;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;


/**
 *
 * @author kdsweenx
 */
public abstract class Game implements GLEventListener{
    public KeyBoard keyboard;
    public Mouse mouse;
    public ImageCollection batch;
    public ViewScreen viewscreen;
    private Input input;
    public int FPS=60;
    public static int GLVersion;
    
    private Color background;
    private GL gl;
    private GLU glu;
    
    /**
     * This will set up the game window, do not make game objects here.
     */
    public void PreGameSetup(){
        
    }
    /**
     * This will set up the actual game logic. Set up game objects here(like sprites, etc.)
     */
    public abstract void InitializeAndLoad();
    public abstract void Update();
    public abstract void Draw(ImageCollection batch);
    public abstract void UnloadContent();
    
    public Game(){
        input=new Input();
        keyboard=new KeyBoard();
        mouse=Mouse.getCurrentInstance();
        viewscreen=new ViewScreen(new Vector2(800,600));
        //batch=new ImageCollection(viewscreen);
        GameBase.frame.addKeyListener(input);
        GameBase.frame.addMouseListener(input);
        GameBase.frame.addMouseMotionListener(input);
        GameBase.frame.addMouseWheelListener(input);
        GameBase.canvas.addKeyListener(input);
        GameBase.canvas.addMouseListener(input);
        GameBase.canvas.addMouseMotionListener(input);
        GameBase.canvas.addMouseWheelListener(input);
        GLVersion=Game.GL2;
        PreGameSetup();
    }
    
    @Override
    public void init(GLAutoDrawable drawable){
        gl=this.getGL(drawable);
        glu=new GLU();
        gl.glViewport((int)viewscreen.GetX(), (int)viewscreen.GetY(), 
                (int)viewscreen.getWidth(), (int)viewscreen.getHeight());
        
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        
        InitializeAndLoad();
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
        viewscreen.set(new Vector2(x,y));
        viewscreen.setHeight(height);
        viewscreen.setWidth(width);
        gl=this.getGL(drawable);
        gl.glViewport((int)viewscreen.GetX(), (int)viewscreen.GetY(), (int)viewscreen.getWidth(), (int)viewscreen.getHeight());
    }
    
    @Override
    public void display(GLAutoDrawable drawable){
        gl=this.getGL(drawable);
        gl.glClearColor(background.getRed(), background.getBlue(), background.getGreen(), background.getAlpha());
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable){
        UnloadContent();
    }
    
    protected class Input extends InputAdvance{

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keyboard.setPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyboard.setRelease(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouse.setButton(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouse.setRelease(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouse.mouseMoved(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouse.mouseMoved(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            mouse.mouseScroll(e);
        }
    
    }
    
    public static GL getGL(GLAutoDrawable drawable){
        switch(GLVersion){
            case GL1:
                return drawable.getGL();
            case GL2:
                return drawable.getGL().getGL2();
            case GL3:
                return drawable.getGL().getGL3();
            case GL4:
                return drawable.getGL().getGL4();
            case GL3p:
                return drawable.getGL().getGL3bc();
            default:
                return drawable.getGL().getRootGL();
        }
    }
    
    public GL getInstGL(){
        return this.gl;
    }
    
    /**
     * Default OpenGL
     */
    public final static int GL1  =1;
    /**
     * OpenGL 3
     */
    public final static int GL2 =2;
    /**
     * OpenGL 3.1
     */
    public final static int GL3 =3;
    /**
     * OpenGL 4
     */
    public final static int GL4 =4;
    /**
     * OpenGL 2+3
     */
    public final static int GL3p=5;
}
