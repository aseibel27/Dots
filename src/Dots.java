import processing.core.PApplet;
import processing.core.PVector;

// import processing.core.*;

public class Dots extends PApplet{
    // main program creates window
    public static void main(String[] args) {
        PApplet.main("Dots");
    }

    // changes window size
    public void settings() {
        size(640, 360);
    }

    // creates two objects of class Ball(PApplet parent, x, y, r)
    Ball[] balls =  { 
        new Ball(this, 100, 300, 20, new int[] {255,0,0}), 
        new Ball(this, 700, 400, 25, new int[] {0,255,0}),
        new Ball(this, 400, 400, 30, new int[] {0,0,255}),
        new Ball(this, 400, 100, 15, new int[] {0,0,0})
    };  
    
    // sets outline and fill colors
    public void setup() {
        noStroke(); // default disabled outline
        fill(204, 204, 204); // default color
    }
    
    // draws background, redraws Balls
    public void draw() {
        background(255); // background color
        
        PVector agentVelocity = balls[0].velocity;
        PVector agentOrientation = balls[0].orientation;
        moveObj(agentVelocity,agentOrientation);
        
        // for each ball
        for (Ball b : balls) {
            b.update(); // update position, velocity
            b.display(); // redraw
            b.checkBoundaryCollision(); // update velocity upon boundary collision
        }
        
        // check if 1st ball collides with 2nd ball
        for (int i = 0; i<balls.length; i++){
            for (int j = balls.length-1; j>i; j--){
                balls[i].checkCollision(balls[j]);
            }  
        } 
    }
        
    static boolean FORWARD, BACKWARD, TURNRIGHT, TURNLEFT;

    public void keyPressed() {
      
      redraw();   //  queue draw()
      
      final int k = keyCode;
          
      if      (k == UP    | k == 'W')   FORWARD = true;
      else if (k == DOWN  | k == 'S')   BACKWARD = true;
      else if (k == LEFT  | k == 'A')   TURNLEFT  = true;
      else if (k == RIGHT | k == 'D')   TURNRIGHT  = true;
    }
    
    public void keyReleased() {
      redraw();   //  queue draw()
    
      final int k = keyCode;
    
      if      (k == UP    | k == 'W')   FORWARD = false;
      else if (k == DOWN  | k == 'S')   BACKWARD = false;
      else if (k == LEFT  | k == 'A')   TURNLEFT  = false;
      else if (k == RIGHT | k == 'D')   TURNRIGHT  = false;
    }
// need to add both orientation and velocity vectors
    static float speed, minSpeed = 0.5f, maxSpeed = 10f, rotSpeed = 0.07f, friction = 0.9f, accel = 1.2f;
    static PVector tempVec;

    static void moveObj(PVector aVel, PVector aOrient) {
      tempVec = PVector.random2D().mult(0);
      speed = aVel.mag();
      if (TURNLEFT)  aOrient.rotate(-rotSpeed);
      else if (TURNRIGHT) aOrient.rotate(rotSpeed); 
      if (FORWARD & (speed < minSpeed) ){
        tempVec.x = aOrient.x;
        tempVec.y = aOrient.y;
        tempVec.mult(minSpeed);
        aVel.x += tempVec.x;
        aVel.y += tempVec.y;
        speed = aVel.mag();
        print(speed);
        print("\n");
      } 
      
      if (FORWARD & (speed >= minSpeed) & speed <= maxSpeed){
        tempVec.x = aOrient.x;
        tempVec.y = aOrient.y;
        tempVec.mult(accel);
        aVel.x += tempVec.x;
        aVel.y += tempVec.y;
        print("Fuck");
        print("\n");
      } 
      else if (FORWARD & (speed > maxSpeed)){
        tempVec = aVel;
        tempVec.normalize();
        tempVec.mult(maxSpeed);
        aVel = tempVec;
      } 
      else if (!FORWARD & (speed > 0)) aVel.mult(friction);
    }
}

class Ball {
    PApplet parent;
    PVector position, velocity, orientation;
    int color, viewColor;
    float radius, m, viewRadius, viewAngle;
    final double PI = 3.1415926536;
  
    Ball(PApplet p, float x, float y, float r_, int[] c) {
      parent = p; // make ball a child of pApplet
      position = new PVector(x, y); // set position to specified x and y
      velocity = PVector.random2D(); // unit vector random direction
      velocity.mult(3); // set velocity magnitude
      orientation = PVector.random2D();
      radius = r_; // set specified radius
      m = radius*.1f; // momentum?
      color = parent.color(c[0],c[1],c[2]);
      viewColor = parent.color(c[0],c[1],c[2],100);
      viewRadius = 2f*radius;
      viewAngle = 60f*(float)PI/180;
    }
    
    // update ball's velocity at given time step
    public void update() {
      position.add(velocity);
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
  
    public void checkCollision(Ball other) {
  
      // Get distances between the balls components
      PVector distanceVect = PVector.sub(other.position, position);
  
      // Calculate magnitude of the vector separating the balls
      float distanceVectMag = distanceVect.mag();
  
      // Minimum distance before they are touching
      float minDistance = radius + other.radius;
  
      if (distanceVectMag < minDistance) {
        float distanceCorrection = (minDistance-distanceVectMag)/2.0f;
        PVector d = distanceVect.copy();
        PVector correctionVector = d.normalize().mult(distanceCorrection);
        other.position.add(correctionVector);
        position.sub(correctionVector);
  
        // get angle of distanceVect
        float theta  = distanceVect.heading();
        // precalculate trig values
        float sine = (float)Math.sin(theta);
        float cosine = (float)Math.cos(theta);
  
        /* bTemp will hold rotated ball positions. You 
         just need to worry about bTemp[1] position*/
        PVector[] bTemp = {
          new PVector(), new PVector()
        };
  
        /* this ball's position is relative to the other
         so you can use the vector between them (bVect) as the 
         reference point in the rotation expressions.
         bTemp[0].position.x and bTemp[0].position.y will initialize
         automatically to 0.0, which is what you want
         since b[1] will rotate around b[0] */
        bTemp[1].x  = cosine * distanceVect.x + sine * distanceVect.y;
        bTemp[1].y  = cosine * distanceVect.y - sine * distanceVect.x;
  
        // rotate Temporary velocities
        PVector[] vTemp = {
          new PVector(), new PVector()
        };
  
        vTemp[0].x  = cosine * velocity.x + sine * velocity.y;
        vTemp[0].y  = cosine * velocity.y - sine * velocity.x;
        vTemp[1].x  = cosine * other.velocity.x + sine * other.velocity.y;
        vTemp[1].y  = cosine * other.velocity.y - sine * other.velocity.x;
  
        /* Now that velocities are rotated, you can use 1D
         conservation of momentum equations to calculate 
         the final velocity along the x-axis. */
        PVector[] vFinal = {  
          new PVector(), new PVector()
        };
  
        // final rotated velocity for b[0]
        vFinal[0].x = ((m - other.m) * vTemp[0].x + 2 * other.m * vTemp[1].x) / (m + other.m);
        vFinal[0].y = vTemp[0].y;
  
        // final rotated velocity for b[0]
        vFinal[1].x = ((other.m - m) * vTemp[1].x + 2 * m * vTemp[0].x) / (m + other.m);
        vFinal[1].y = vTemp[1].y;
  
        // hack to avoid clumping
        bTemp[0].x += vFinal[0].x;
        bTemp[1].x += vFinal[1].x;
  
        /* Rotate ball positions and velocities back
         Reverse signs in trig expressions to rotate 
         in the opposite direction */
        // rotate balls
        PVector[] bFinal = { 
          new PVector(), new PVector()
        };
  
        bFinal[0].x = cosine * bTemp[0].x - sine * bTemp[0].y;
        bFinal[0].y = cosine * bTemp[0].y + sine * bTemp[0].x;
        bFinal[1].x = cosine * bTemp[1].x - sine * bTemp[1].y;
        bFinal[1].y = cosine * bTemp[1].y + sine * bTemp[1].x;
  
        // update balls to screen position
        other.position.x = position.x + bFinal[1].x;
        other.position.y = position.y + bFinal[1].y;
  
        position.add(bFinal[0]);
  
        // update velocities
        velocity.x = cosine * vFinal[0].x - sine * vFinal[0].y;
        velocity.y = cosine * vFinal[0].y + sine * vFinal[0].x;
        other.velocity.x = cosine * vFinal[1].x - sine * vFinal[1].y;
        other.velocity.y = cosine * vFinal[1].y + sine * vFinal[1].x;
      }
    }
    
    public void display() { 
        parent.fill(color);
        parent.ellipse(position.x, position.y, radius*2, radius*2);
        parent.fill(viewColor);
        parent.arc(position.x,position.y,viewRadius*2,viewRadius*2,orientation.heading()-viewAngle/2,orientation.heading()+viewAngle/2);
    }
  }