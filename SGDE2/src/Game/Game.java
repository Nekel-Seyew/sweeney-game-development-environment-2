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
import static javax.media.opengl.GL.GL_NICEST;
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
    public int WIDTH;
    public int HEIGHT;
    
    private Color background;
    private GL gl;
    private GLU glu;
    
    /**
     * This will set up the actual game logic. Set up game objects here(like sprites, etc.)
     */
    public abstract void InitializeAndLoad();
    public abstract void Update();
    public abstract void Draw(ImageCollection batch);
    public abstract void UnloadContent();
    
    public void addInput(){
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
        background=Color.black;
        InitializeAndLoad();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        glu = new GLU();                         // get GL Utilities
        gl.glClearColor(background.getRed()/255f, background.getGreen()/255f, background.getBlue()/255f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        this.UnloadContent();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
//        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        
        this.Update();
        this.Draw(batch);
        batch.Render(drawable);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        float aspect = (float) width / height;
        this.HEIGHT=height;
        this.WIDTH=width;
        this.viewscreen.setHeight(height);
        this.viewscreen.setWidth(width);
        // Set the view port (display area) to cover the entire window
        gl.glViewport(viewscreen.GetX(), viewscreen.GetY(), width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
//      glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
        glu.gluPerspective(45, aspect, 0, 0);

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
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
    
    public GL getInstGL(){
        return this.gl;
    }
}
