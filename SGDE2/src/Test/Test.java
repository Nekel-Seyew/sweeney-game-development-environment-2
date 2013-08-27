/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Game.Game;
import Game.GameBase;
import Utilities.Image2D;
import Utilities.ImageCollection;
import Utilities.KeyBoard;
import Utilities.Vector2;

/**
 *
 * @author kdsweenx
 */
public class Test extends Game{
    
    Vector2 pos;

    @Override
    public void InitializeAndLoad() {
        pos=new Vector2();
    }

    @Override
    public void Update() {
        if(keyboard.isKeyDown(KeyBoard.w)){
            pos.dY(5);
        }
        if(keyboard.isKeyDown(KeyBoard.a)){
            pos.dX(-5);
        }
        if(keyboard.isKeyDown(KeyBoard.s)){
            pos.dY(-5);
        }
        if(keyboard.isKeyDown(KeyBoard.d)){
            pos.dX(5);
        }
    }

    @Override
    public void Draw(ImageCollection batch) {
        batch.Draw(new Image2D("Sprites/Wyvern2.jpg"), pos);
//        batch.Draw(new Image2D("images/nehe.png"), new Vector2(-100,-100));
    }

    @Override
    public void UnloadContent() {
        
    }
    
    
    public static void main(String[] args){
        Game test= new Test();
        GameBase game=new GameBase(test,"Hello World");
        game.run();
    }
    
}
