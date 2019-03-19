import processing.core.PApplet;
import java.util.ArrayList;

public class World {
    public PApplet parent;
    public ArrayList<Dot> dots;

    World(PApplet p) {
        parent = p;
        dots = new ArrayList<Dot>();
    }

    public void AddDot(int n) {
        dots.add(new Dot(parent, Assets.stats.get(n).split(",")));
    }

    public void Kill(Dot d) {
        dots.remove(d);
    }
    
    public void update() {
        for (Dot d : dots) {
            d.update();
        }
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i<dots.size(); i++){
            for (int j = dots.size()-1; j>i; j--){
                Ball.CheckCollision(dots.get(i), dots.get(j));
            }  
        }

        for (Ball b : dots) {
            b.checkBoundaryCollision();
        }
    }

    public void display() {
        for (Dot d : dots) {
            d.display();
        }
    }

}