/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author kdsweenx
 */
public class ImageCollection {
    ViewScreen view;
    private Node bufferStart;
    
    public ImageCollection(ViewScreen view){
        this.view=view;
        bufferStart=new Node(null, 0, null);
    }
    
    private void addOne(Image2D hold, int depth, Vector2 position){
        Vector2 pos = position.clone();
        pos.add(view.loc());
        hold.setDepth(depth);
        hold.setPos(pos);
    }
    private void addTwo(Image2D hold, int depth, Vector2 position){
        if(bufferStart.getNext()==null){
            Node newNode=new Node(hold, depth, bufferStart);
            bufferStart.setNext(newNode);
        }
        Node search = bufferStart.getNext();
        while (search != null) {
            if (search.depth > depth) {
                Node newNode = new Node(hold, depth, search.getPrevious());
                break;
            } else if (search.getNext() == null) {
                Node newNode = new Node(hold, depth, search);
                break;
            } else {
                search = search.getNext();
            }
        }
    }
   
    private void addToColl(Image2D hold, int depth, Vector2 position){
        addOne(hold, depth, position);
       
        addTwo(hold, depth, position);  
    }
    
    public void Draw(Image2D i, Vector2 pos){
        addToColl(i,0,pos);
    }
    /**
     * Draws an Image passed into it at the depth provided. The Higher depth means it will be drawn later, or on top of others. The position passed in will be the center of the image.
     * @param i the Image2D to be drawn
     * @param pos the Position at which the Image2D will be drawn
     * @param depth the depth at which the image will be drawn
     */
    public void Draw(Image2D i, Vector2 pos, int depth){
        addToColl(i, depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos,float angle, int depth){
        i.setRotation(angle);
        addToColl(i, depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle, Color tint, int depth){
        i.giveShading(tint, 0.25f);
        addToColl(i,depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle, Color tint, float scaleX,float scaleY, int depth){
        i.setRotation(angle);
        i.setScale(scaleX, scaleY);
        i.giveShading(tint,0.25f);
        addToColl(i, depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle,float scale, int depth){
        i.setRotation(angle);
        i.setScale(scale, scale);
        addToColl(i, depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle, float scaleX, float scaleY, int depth){
        i.setRotation(angle);
        i.setScale(scaleX, scaleY);
        addToColl(i, depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, Rectangle sourceDrawnArea, int depth){
        if(sourceDrawnArea!=null){
            i.setDrawnArea(sourceDrawnArea);
        };
        addToColl(i,depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle, Rectangle sourceDrawnArea, int depth){
        i.setRotation(angle);
        if(sourceDrawnArea!=null){
            i.setDrawnArea(sourceDrawnArea);
        }
        addToColl(i,depth,pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle, float scaleX,float scaleY, Rectangle sourceDrawnArea, int depth){
        i.setRotation(angle);
        i.setScale(scaleX, scaleY);
        if(sourceDrawnArea!=null){
            i.setDrawnArea(sourceDrawnArea);
        }
        addToColl(i, depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle, Color tint, float scaleX, float scaleY, Rectangle sourceDrawnArea, int depth){
        i.setRotation(angle);
        i.setScale(scaleX, scaleY);
        if(sourceDrawnArea!=null){
            i.setDrawnArea(sourceDrawnArea);
        }
        i.giveShading(tint, 0.25f);
        addToColl(i, depth, pos);
    }
    public void Draw(Image2D i, Vector2 pos, float angle, Color tint,float percentTint, float scaleX, float scaleY, Rectangle sourceDrawnArea, int depth){
        i.setRotation(angle);
        i.setScale(scaleX, scaleY);
        if(sourceDrawnArea!=null){
            i.setDrawnArea(sourceDrawnArea);
        }
        i.giveShading(tint, percentTint);
        addToColl(i, depth, pos);
    }

    
    
    
    
    public void Render(GLAutoDrawable drawable){
        Node hold=bufferStart.getNext();
        while(hold != null){
            Rect r= new Rect(-50,-50,(int)view.getWidth()+50,(int)view.getHeight()+50);
            hold.draw(drawable.getGL().getGL2(), r);
            hold=hold.getNext();
        }
    }

    
    
    private class Node implements Serializable{
        private Node previous;
        private Node next;
        public int depth;
        private Image2D image;
       
        public Node(Image2D i, int depth, Node previous){
            this.previous=previous;
            this.depth=depth;
            this.image=i;
            if(previous!=null){
                next=previous.next;
                previous.setNext(this);
            }
            if(next!=null){
                next.setPrevious(this);
            }
        }
       
        public void setPrevious(Node p){
            previous=p;
        }
        public void setNext(Node n){
            next=n;
        }
       
        public void draw(GL2 gl, Rect r){
            //if(image instanceof Command){
                //image.Draw(g, context);
            if(r.intersects(image.getRectangle())){
                image.Render(gl);
                //System.out.println(image);
            }
        }
       
        public Node getPrevious(){
            return previous;
        }
        public Node getNext(){
            return next;
        }
       
    }

}
