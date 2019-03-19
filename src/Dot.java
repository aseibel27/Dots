import processing.core.PApplet;
import processing.core.PImage;

public class Dot extends Ball {
    
    public String number;
    public String name;
    public Type type1;
    public Type type2;
    int HP;
    int Att;
    int Def;
    int SpA;
    int SpD;
    int Spe;
    public float Height;
    public float Weight;
    public PImage sprite;

    public int lvl;
    int[] iv = new int[6];
    int[] ev = new int[6];
    public int hp;
    public int att;
    public int def;
    public int spa;
    public int spd;
    public int spe;

    public Dot(PApplet p, String[] stats) {
        super(p);
        number = stats[0];
        name = stats[1];
        type1 = Type.valueOf(stats[2]);
        if (stats[3].length() != 0) {
            type2 = Type.valueOf(stats[3]);
        } else {
            type2 = null;
        }
        HP = Integer.parseInt(stats[6]);
        Att = Integer.parseInt(stats[7]);
        Def = Integer.parseInt(stats[8]);
        SpA = Integer.parseInt(stats[9]);
        SpD = Integer.parseInt(stats[10]);
        Spe = Integer.parseInt(stats[11]);
        Height = Float.parseFloat(stats[4]);
        Weight = Float.parseFloat(stats[5]);
        sprite = p.loadImage(Assets.path + "sprites\\" + this.number + ".png");

        hp = PApplet.floor(((2*HP+iv[0]+PApplet.floor(ev[0]/4))*lvl)/100) + lvl + 10;
        att = PApplet.floor(((2*Att+iv[1]+PApplet.floor(ev[1]/4))*lvl)/100) + 5;
        def = PApplet.floor(((2*Def+iv[2]+PApplet.floor(ev[2]/4))*lvl)/100) + 5;
        spa = PApplet.floor(((2*SpA+iv[3]+PApplet.floor(ev[3]/4))*lvl)/100) + 5;
        spd = PApplet.floor(((2*SpD+iv[4]+PApplet.floor(ev[4]/4))*lvl)/100) + 5;
        spe = PApplet.floor(((2*Spe+iv[5]+PApplet.floor(ev[5]/4))*lvl)/100) + 5;
    }

    public void update() {
        super.update();
    }

    public void display() {
        parent.push();
        super.display(this.type1.ordinal());
        this.parent.scale(0.5f);
        this.parent.rotate(PI);
        this.parent.image(sprite, 0, 0);
        parent.pop();
    }

}

class Attack {

    public int Power;
    public int Accuracy;

    Attack() {

    }


}