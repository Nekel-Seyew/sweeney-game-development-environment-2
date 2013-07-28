/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Game.Game;
import Game.GameBase;
import Utilities.Image2D;
import Utilities.ImageCollection;
import Utilities.Vector2;

/**
 *
 * @author kdsweenx
 */
public class Test extends Game{

    @Override
    public void InitializeAndLoad() {
        
    }

    @Override
    public void Update() {
        
    }

    @Override
    public void Draw(ImageCollection batch) {
        batch.Draw(new Image2D("Sprites/Wyvern.jpg"), new Vector2(-100,0));
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
