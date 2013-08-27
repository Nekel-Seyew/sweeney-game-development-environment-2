/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Utilities.Image2D;
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
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
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
    public int WIDTH;
    public int HEIGHT;
    
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
    
    private void addInput(){
        GameBase.frame.addKeyListener(input);
        GameBase.frame.addMouseListener(input);
        GameBase.frame.addMouseMotionListener(input);
        GameBase.frame.addMouseWheelListener(input);
        GameBase.canvas.addKeyListener(input);
        GameBase.canvas.addMouseListener(input);
        GameBase.canvas.addMouseMotionListener(input);
        GameBase.canvas.addMouseWheelListener(input);
    }
    
    public Game(){
        input=new Input();
        keyboard=new KeyBoard();
        mouse=Mouse.getCurrentInstance();
        WIDTH=800;
        HEIGHT=600;
        viewscreen=new ViewScreen(new Vector2(WIDTH,HEIGHT));
        batch=new ImageCollection(viewscreen);
        GLVersion=Game.GLTwo;
        background=Color.black;
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
        ((GL2)gl).glMatrixMode(GL2.GL_PROJECTION);
        ((GL2)gl).glShadeModel(GL2.GL_SMOOTH);
        ((GL2)gl).glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        //final game setup
        addInput();
        Image2D.init(gl);
        InitializeAndLoad();
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
        viewscreen.set(new Vector2(x,y));
        WIDTH=width;
        HEIGHT=height;
        viewscreen.setHeight(height);
        viewscreen.setWidth(width);
        gl=this.getGL(drawable);
        float aspect = (float)width / height;
        gl.glViewport((int)viewscreen.GetX(), (int)viewscreen.GetY(), (int)viewscreen.getWidth(), (int)viewscreen.getHeight());
        ((GL2)gl).glMatrixMode(GL_PROJECTION);  // choose projection matrix
        ((GL2)gl).glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 0.1); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        ((GL2)gl).glMatrixMode(GL_MODELVIEW);
        ((GL2)gl).glLoadIdentity(); // reset
    }
    
    @Override
    public void display(GLAutoDrawable drawable){
        gl=this.getGL(drawable);
        gl.glClearColor(background.getRed(), background.getBlue(), background.getGreen(), background.getAlpha());
        
        //update
        Update();
        //Draw
        Draw(batch);
        //render
        batch.Render(drawable);
        
        gl.glFlush();
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable){
        UnloadContent();
    }
    
    public void run(){
        //GameBase.canvas.getBufferStrategy()
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
            case GLOne:
                return drawable.getGL();
            case GLTwo:
                return drawable.getGL().getGL2();
            case GLThree:
                return drawable.getGL().getGL3();
            case GLFour:
                return drawable.getGL().getGL4();
            case GLThreePlus:
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
    public final static int GLOne  =1;
    /**
     * OpenGL 3
     */
    public final static int GLTwo =2;
    /**
     * OpenGL 3.1
     */
    public final static int GLThree =3;
    /**
     * OpenGL 4
     */
    public final static int GLFour =4;
    /**
     * OpenGL 2+3
     */
    public final static int GLThreePlus=5;
}
