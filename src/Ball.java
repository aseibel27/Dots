import processing.core.PApplet;
import processing.core.PVector;

public class Ball {
    PApplet parent;
    PVector position, velocity;
    float rotation, radius, m, viewRadius, viewAngle;
    float startTime, currentTime, attackTime = 200, cooldownTime = 200;
    boolean ATTACKING, INITIATE_ATTACK, ATTACK_COOLDOWN;
    final float PI = 3.1415926536f;
    public static int[][] colors = {
        {168, 167, 122},// normal
        {194, 46, 40},  // fight
        {169, 143, 243},// flying
        {163, 62, 161}, // poison
        {226, 191, 101},// ground
        {182, 161, 54}, // rock
        {166, 185, 26}, // bug
        {99, 87, 151},  // ghost
        {183, 183, 206},// steel
        {238, 129, 48}, // fire
        {99, 144, 240}, // water
        {122, 199, 76}, // grass
        {247, 208, 44}, // electric
        {249, 85, 135}, // psychic
        {150, 217, 214},// ice
        {111, 53, 252}, // dragon
        {112, 87, 70}   // dark
    };
    
    Ball(PApplet p) {
        parent = p; // make ball a child of pApplet
        position = new PVector(parent.random(0, 1)*parent.width, parent.random(0, 1)*parent.height);
        velocity = PVector.random2D().mult(3); // unit vector random direction
        rotation = velocity.heading()-PI/2;
        radius = 30; // set specified radius
        m = radius*.1f; // momentum?
        viewRadius = 2f*radius;
        viewAngle = PI/3;
        ATTACKING = false;
        INITIATE_ATTACK = false;
        ATTACK_COOLDOWN = false;
    }
    
    // update ball's velocity at given time step
    public void update() {
        position.add(velocity);
    }
    
    static float speed,
        maxSpeed = 5f, 
        rotSpeed = 0.07f,
        friction = 0.85f,
        accel = 0.5f;
    
    public void move() {

        // ATTACK 
        // initiate attack if key pressed, but only if not attacking or in cooldown
        if (Application.ATTACK_KEY & !ATTACKING & !ATTACK_COOLDOWN) INITIATE_ATTACK = true;
        // increase velocity upon initiating attack
        if (INITIATE_ATTACK) {
            velocity.x = 0f; velocity.y = 1f;
            velocity.mult(3*maxSpeed).rotate(rotation);
            INITIATE_ATTACK = false;
            ATTACKING = true;
            startTime = parent.millis();
        }
        // ATTACK remains true until attackTime is exceeded
        // once attackTime is exceeded, attack stops and cooldown starts
        if (ATTACKING) {
            currentTime = parent.millis();
            if (attackTime <= (currentTime-startTime)) {
                velocity.mult(0);
                ATTACKING = false;
                ATTACK_COOLDOWN = true;
                startTime = parent.millis();
            }
        }
        // ATTACK_COOLDOWN remains true until cooldownTime exceeded
        if (ATTACK_COOLDOWN) {
            currentTime = parent.millis();
            if (cooldownTime <= (currentTime-startTime)) {
                ATTACK_COOLDOWN = false;
            }
        }
        // can't turn if attacking
        if (!ATTACKING) {
            // TURNING
            if (Application.TURNLEFT) {
                rotation -= rotSpeed;
            } else if (Application.TURNRIGHT) {
                rotation += rotSpeed;
            }
        }
        // FORWARD
        speed = velocity.mag();
        if (Application.FORWARD) {
            velocity.x = 0f; velocity.y = 1f;
            speed += accel;
            if (speed > maxSpeed) {
                // don't limit max speed when attacking
                if (ATTACKING) speed -= accel;
                else speed = maxSpeed;
            }
            velocity.mult(speed).rotate(rotation);
        } else {
            if (speed > 0.01) {
                velocity.mult(friction);
            } else if (speed > 0 & speed <= 0.01) {
                velocity.mult(0f);
            }
        }
    }

    // if ball escapes window boundary, set position
    // to boundary and flip velocity
    public void checkBoundaryCollision() {
        if (position.x > parent.width-radius) {
            position.x = parent.width-radius;
            velocity.x *= -1;
        } if (position.x < radius) {
            position.x = radius;
            velocity.x *= -1;
        } if (position.y > parent.height-radius) {
            position.y = parent.height-radius;
            velocity.y *= -1;
        } if (position.y < radius) {
            position.y = radius;
            velocity.y *= -1;
        }
    }

    public static void CheckCollision(Ball b1, Ball b2) {
        PVector F = PVector.sub(b2.position, b1.position);
        float d = F.magSq();
        float r = b1.radius + b2.radius;
        if (d < r*r) {
            PVector impulse = PVector.mult(F, 2*b1.m*PVector.dot(PVector.sub(b2.velocity, b1.velocity), F)/(b1.m+b2.m)/d);
            b1.velocity.add(impulse);
            b2.velocity.sub(impulse);
            F.normalize().mult(r - PApplet.sqrt(d));
            b1.position.sub(F);
            b2.position.add(F);
        }
    }
    
    public void display(int t) { 
        parent.translate(position.x, position.y);
        parent.rotate(rotation);
        parent.fill(colors[t][0], colors[t][1], colors[t][2]);
        parent.ellipse(0f, 0f, radius*2, radius*2);
        parent.fill(colors[t][0], colors[t][1], colors[t][2], 100);
        parent.arc(0f, 0f, viewRadius*2, viewRadius*2, -viewAngle+PI/2, viewAngle+PI/2);
    }
}