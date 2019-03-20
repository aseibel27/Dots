import processing.core.PApplet;

public class Application extends PApplet {
    
    public World w;
    public Ball agent;
    public static boolean ATTACK_KEY, FORWARD, BACKWARD, TURNRIGHT, TURNLEFT;

    public static void main(String[] args) {
        PApplet.main("Application");
    }

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        noStroke();
        imageMode(CENTER);
        Assets.LoadAll();
        w = new World(this);
        w.AddDot(25);
        w.AddDot(1);
        w.AddDot(4);
        w.AddDot(7);
        agent = w.dots.get(0);
    }

    public void draw() {
        background(255);
        agent.move();
        w.update();
        w.display();
    }

    public void keyPressed() {
        final int k = keyCode;
        if (k == ' ' | k == ENTER | k == RETURN) ATTACK_KEY = true;
        if      (k == UP    | k == 'W')   FORWARD = true;
        else if (k == DOWN  | k == 'S')   BACKWARD = true;
        else if (k == LEFT  | k == 'A')   TURNLEFT  = true;
        else if (k == RIGHT | k == 'D')   TURNRIGHT  = true;
    }
    
    public void keyReleased() {
        final int k = keyCode;
        if (k == ' ' | k == ENTER | k == RETURN) ATTACK_KEY = false;
        if      (k == UP    | k == 'W')   FORWARD = false;
        else if (k == DOWN  | k == 'S')   BACKWARD = false;
        else if (k == LEFT  | k == 'A')   TURNLEFT  = false;
        else if (k == RIGHT | k == 'D')   TURNRIGHT  = false;
    }
}