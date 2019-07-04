import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.AWTException; 
import java.awt.Robot; 
import java.awt.event.KeyEvent; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ALTERPONG extends PApplet {

//libraries



//-------------------------------------------------------------------------------------------------------------------------//
/*
*ALTER/PONG V0.3
*DONE BY EDDY IKHLEF USING PROCESS
*rules: 
* player(left) q to move up, w to move down
* player(right) up to move up, down to move down
* r to stop and go back to title screen (saved scores)
*ALTERNATIVE PONG, each player can't move after send the ball until the other player touch it
*Adding with unstopable platform, and ball who take speed, it make the pong harder than ever !
*/
//-------------------------------------------------------------------------------------------------------------------------//
//STARTING VARIABLES
//version
String version = "V0.3";
//player 1 & 2 position, only y + score
int p1, p2, s1, s2;
//ball position bx by / ball moves dx dy / ball size bsize
int bx, by, dx, dy, bsize;
//ball buffer ordiLuck
int buffer, bufferBase, ordiLuck;
//canHit the ball / start the game / mode of game / startGame after pressing ENTER
boolean canHit, start, startGame;
//mode 1 = 1P, 2 = 2P / launchTimer pseudoTimer
int mode, launchTimer;
//create computer opponent
Robot robot;
//-------------------------------------------------------------------------------------------------------------------------//
//FUNCTION USED IN THE PROGRAM
//-------------------------------------------------------------------------------------------------------------------------//
//CHECKCOLISSION() = collision with players and border
public void checkColission() {
  //collision with players
  if ((bx > 16 && bx <= 26-dx) && (by > p1-8 && by < p1+100 && canHit == true)) {
    canHit = false;
    dx = bufferBase;
  }
  if ((bx > 1280-26 && bx <= 1280-18+dx) && (by > p2-8 && by < p2+100 && canHit == true)) {
    canHit = false;
    dx = -bufferBase;
  }
  
  //collision with border
  if (bx < bsize && canHit == true)  { canHit = false; dx = bufferBase; buffer = 0; ballMove(); s2++;}  
  else if (bx > 1280-bsize && canHit == true)  { canHit = false; dx = -bufferBase; buffer = 0; ballMove(); s1++;}  
  if (by < bsize || by > 719-bsize)  { dy = -dy; buffer++; }
}

//BALLMOVE() - Move the ball and bounce on edge, check when it is in the "goal zone" and change speed
public void ballMove() {
  //update can hit
  if (bx >= 36 && bx <= 1280-36) { canHit = true; }
  //update position
  if (dx < 0 ) { bx += dx - buffer; }
  else { bx += dx + buffer; }
   if (dy < 0 ) { by += dy - buffer; }
  else { by += dy + buffer; }
  //DEBUG BORDER
  if (by > 720) { by = 718; ballMove();}
  //draw
  fill(255);
  ellipse(bx, by, bsize, bsize);
}

//PLAYERMOVES()
public void playerMoves() {
  fill(50, 50, 50);
  //update player 1
  if (dx < 0) {
    fill(255);
    if (key == 'q') { if (p1 > 0) { p1 -= bufferBase*2; } }
    if (key == 'w') { if (p1 < 720-100) { p1 += bufferBase*2; } }
  }
  rect(16, p1, 10, 100);
  //update player 2
  if (mode == 2) { //ONLY P2 VERSION
    fill(50, 50, 50);
    if (dx > 0) {
      fill(255);
      if (keyCode == UP) { if (p2 > 0) { p2 -= bufferBase*2; } }
      if (keyCode == DOWN) { if (p2 < 720-100) { p2 += bufferBase*2; } }
    }
  }
  else if (mode == 1 && dx > 0) { //ONLY P1 VERSION
    fill(255);
    //not cheated move
    if (PApplet.parseInt(random(0,25)) >= ordiLuck) {
      if (p2 > by) {
        robot.keyPress(KeyEvent.VK_UP);
      }
      if (p2 <= by) {
        robot.keyPress(KeyEvent.VK_DOWN);
      }
    }
    if (keyCode == UP) { if (p2 > 0) { p2 -= bufferBase*2; } }
    if (keyCode == DOWN) { if (p2 < 720-100) { p2 += bufferBase*2; } }
    }
  else if (mode == 1) { fill(50, 50, 50); }
  rect(1280-26, p2, 10, 100);
}

//DRAWSCORE() Draw the gui and the scores
public void drawScore() {
  textSize(100);
  fill(50,50,50);
  textAlign(CENTER, CENTER);
  text("ALTER/PONG", 628, 30); 
  textSize(255);
  text(s1, 320, 360);
  text(s2, 960, 360);
  fill(255);
  //TODO - DRAW HIGHSCORE
}

//CLEAN() - Clean the screen function
public void clean() {
 background(0); 
 //draw the separator line
 stroke(255,255,255);
 line(640, 0, 640, 1280);
}

//LAUNCH() - Start the game scene
public void launch() {
  //transition
  launchTimer = 1;
  //set transition color to black
  color(0);
}
//-------------------------------------------------------------------------------------------------------------------------//
//START TITLE SCREEN
//-------------------------------------------------------------------------------------------------------------------------//
//initialize setup
public void setup() {
  //INITIALIZE VARIABLES
  p1 = 360;
  p2 = 360;
  s1 = 0;
  s2 = 0;
  bx = 640;
  by = 360;
  dx = 0;
  dy = 0;
  bsize = 16;
  buffer = 1;
  bufferBase = 4;
  canHit = true;
  start = false;
  mode = -1;
  ordiLuck = PApplet.parseInt(random(1, 5));
  launchTimer = 0;
  startGame = false;
  //create robot
  try { robot = new Robot(); } 
  catch (Exception e) { e.printStackTrace(); exit(); }
  //create window with a size of 1280*720
  
  //draw text
  textSize(100);
  strokeWeight(1);
  background(255);
  fill(0);
  line(640, 0, 640, 1280);
  fill(50,50,50);
  textAlign(CENTER, CENTER);
  text("ALTER/PONG", 628, 30); 
  textSize(100);
  text("P1 V ORDI", 320, 360);
  text("P1 V P2", 960, 360);
  textSize(50);
  text("<press LEFT ARROW>", 320, 480);
  text("<press RIGHT ARROW>", 960, 480);
  fill(0);
  textSize(25);
  text("<"+version+" - 2019 - Eddy Ikhlef>\nm-o-k-a.github.io", 628, 680); 
}

//-------------------------------------------------------------------------------------------------------------------------//
//MAIN LOOP: DRAW THE GAME
//-------------------------------------------------------------------------------------------------------------------------//
public void draw() {
  if (start == true && mode >= 0) {
    strokeWeight(1);
    clean();
    drawScore();
    checkColission();
    if (startGame == false) {
      textSize(25);
      text("<press ENTER to start>", 628, 680);
      if (keyCode == ENTER) { startGame = true; }
    }
    else {
      ballMove();
      playerMoves();
    }
    if (key == 'r') { color(0); clear(); setup(); }
 }
 else {
   //title screen select
   if (keyCode == RIGHT && launchTimer == 0) { 
     mode = 2;
     color(0);
     launch();
   }
   if (keyCode == LEFT && launchTimer == 0) { 
     mode = 1;
     color(0);
     launch();
   }
 }
 
 //TRANSITION DRAW
 if (launchTimer > 0) {
   strokeWeight(18);
   line(0, (launchTimer*16)-16, 1280, (launchTimer*16)-16); 
   launchTimer++;
   if (launchTimer >= 46) {
     //chose direction for ball
     while (dx == 0 || dy == 0) {
      dx = PApplet.parseInt(random(-bufferBase, bufferBase));
       dy = PApplet.parseInt(random(-bufferBase, bufferBase));
     }
    //draw ball and players 1 and 2
    ellipse(bx, by, bsize, bsize);
    rect(16, p1, 10, 100);
    rect(1280-26, p2, 10, 100);
    //start game
    launchTimer = 0;
    start = true;
}
 }
}
  public void settings() {  size(1280, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ALTERPONG" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
