package wheeloffortune.gui;

import java.awt.Color;
import java.awt.Graphics;

import wheeloffortune.engine.gui.GifDisplay;
import wheeloffortune.engine.gui.Screen;
import wheeloffortune.game.Game;
import wheeloffortune.engine.gui.Screen;

public class PlayerSelect extends Screen
{
 
    public PlayerSelect() {
        setBackgroundColor(new Color(178, 238, 255));
    }
    @Override
    public void layout() {
        
        addButton(width / 3, height / 2 - 50 , width / 3, 50, "2 Players", "twoPlayerButton");
        addButton(width / 3, height / 2 + 50, width / 3, 50, "3 Players", "threePlayerButton");
        addButton(width / 3, height / 2 + 150, width / 3, 50, "4 Players", "fourPlayerButton");
        addComponent(new GifDisplay(0, 0, width/4, height/2, Images.Carlton));
        addComponent(new GifDisplay(width - (width/4), 0, width/4, height/2, Images.Carlton));
        addImage(width / 2- width/8 , height / 50, width / 4, height / 3, Images.logo);
        
    }
   
    

}
