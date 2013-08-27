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
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import javax.media.opengl.GL2;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
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
    private GL2 gl;
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
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
         // Use linear filter for texture if image is smaller than the original texture
         gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        gl.glViewport((int) viewscreen.GetX(), (int) viewscreen.GetY(),
//                (int) viewscreen.getWidth(), (int) viewscreen.getHeight());
        //final game setup
        addInput();
        InitializeAndLoad();
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
        GL2 gl = drawable.getGL().getGL2();
        viewscreen.set(new Vector2(x,y));
        WIDTH=width;
        HEIGHT=height;
        viewscreen.setHeight(height);
        viewscreen.setWidth(width);
        float aspect = (float)width / height;
        gl.glViewport((int)viewscreen.GetX(), (int)viewscreen.GetY(), (int)viewscreen.getWidth(), (int)viewscreen.getHeight());
        ((GL2)gl).glMatrixMode(GL_PROJECTION);  // choose projection matrix
        ((GL2)gl).glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0, 0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        ((GL2)gl).glMatrixMode(GL_MODELVIEW);
        ((GL2)gl).glLoadIdentity(); // reset
    }
    
    @Override
    public void display(GLAutoDrawable drawable){
        gl=drawable.getGL().getGL2();
        gl.glClearColor(background.getRed(), background.getBlue(), background.getGreen(), background.getAlpha());
//        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //update
        Update();
        //Draw
        Draw(batch);
        //render
        batch.Render(drawable);
        
//        gl.glFlush();
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
    
    private static GL getGL(GLAutoDrawable drawable){
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
    
    private GL getInstGL(){
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
