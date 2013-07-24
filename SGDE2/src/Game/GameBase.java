/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.Frame;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

/**
 *
 * @author kdsweenx
 */
public class GameBase {
    public static JFrame frame;
    public static GLCanvas canvas;
    public static Game game;
    public static FPSAnimator animator;
    
    public GameBase(Game game, String name){
        game=game;
        frame= new JFrame(name);
        canvas= new GLCanvas();
        canvas.setPreferredSize(new Dimension((int)game.viewscreen.getWidth(), (int)game.viewscreen.getHeight()));
        canvas.addGLEventListener(game);
        animator= new FPSAnimator(canvas, game.FPS);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);

    }
    
    public void run(){
        animator.start();
    }
}
