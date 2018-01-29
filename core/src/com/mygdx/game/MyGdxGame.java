package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.Random;

import sun.security.provider.SHA;


public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture gameover;
    //ShapeRenderer shapeRenderer;


    //creating an array of texture
   Texture rocket;
   Circle rocketCircle;
    //flap state flips between 0 and 1 and keeps track of which one
    //we want to display at each time

    //for our gravity we need to know 2 things about this bird
    //only the vertical position changes
    //how fast the bird is moving
    float velocityx = 0;
    float velocityy = 0;

    //keeps track of the state of the game
    int gameState = 0;


    //our meteors + galaxies
    Texture meteors;
    Texture galaxy;
    float gap = 400;
    float maxstaroffset;
    float starvelocity = (10);
    int numberofstars = 10;
    float[] stars = new float[numberofstars];
    float[] starsoffset = new float[numberofstars];
    float distancebetweenstars;
    //we are going to need 20 rectangles in total
    Rectangle[] galaxyrects;
    Rectangle[] meteorsrects;


    //now we are dealing with scoreing
    int score = 0;
    int scoringstar = 0;


    //getting a font
    BitmapFont font;

    @Override
    public String toString() {
        return "MyGdxGame{" +
                "batch=" + batch +
                ", background=" + background +
                //", shapeRenderer=" + shapeRenderer +
                ", rocket=" + rocket +
                ", rocketCircle=" + rocketCircle +
                ", velocityx=" + velocityx +
                ", velocityy=" + velocityy +
                ", gameState=" + gameState +
                ", meteors=" + meteors +
                ", galaxy=" + galaxy +
                ", gap=" + gap +
                ", maxstaroffset=" + maxstaroffset +
                ", starvelocity=" + starvelocity +
                ", numberofstars=" + numberofstars +
                ", stars=" + Arrays.toString(stars) +
                ", starsoffset=" + Arrays.toString(starsoffset) +
                ", distancebetweenstars=" + distancebetweenstars +
                ", galaxyrects=" + Arrays.toString(galaxyrects) +
                ", meteorsrects=" + Arrays.toString(meteorsrects) +
                ", randomGenerator=" + randomGenerator +
                ", speedx=" + speedx +
                ", speedy=" + speedy +
                ", renderx=" + renderx +
                ", rendery=" + rendery +
                '}';
    }

    //randonmly generating tube locations
    Random randomGenerator;

    float speedx;
    float speedy;




    //For accelerometer
    private float renderx;
    private float rendery;


    @Override
    public void create() {
        //setting the new font
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(25);


        //runs when the app is run
        batch = new SpriteBatch();
        //bg.png so now our background refers to this texture
        //all 80 backgrounds;
        background = new Texture("1.png");

        gameover = new Texture("gameover.jpg");


        //specifying the number of items in our array which is birds
        rocket = new Texture("rocket1.png");


        //creating our shape from our shape method
        //shapeRenderer = new ShapeRenderer();
        //creating our cicle from our circle method
        rocketCircle = new Circle();


        //for accelerometer;
        renderx = 200;
        rendery = 250;
        //set y equal to hight of the screen and bird
        //both birds have the same heigh so it doesnt matter whut u use
        //rocketx = Gdx.graphics.getWidth() / 2 - rocket.getWidth() / 2;
        //rockety = Gdx.graphics.getHeight() / 2 - rocket.getHeight() / 2;



        galaxy = new Texture("galaxy.gif");

        maxstaroffset = Gdx.graphics.getHeight() - gap / 2 - 100;


        //random generator
        //for each star wee initially wanna set their x coord and their offset
        //used to randomize orientation of meteors on our screen
        randomGenerator = new Random();
        distancebetweenstars = Gdx.graphics.getWidth() / 3f;


        //creating rec for our meteors and galaxies
        galaxyrects = new Rectangle[numberofstars];
        startgame();
    }



    public void  startgame(){

        for(int i =0; i< numberofstars;i++){
            //mmultiply by scale factor
            starsoffset[i] = (randomGenerator.nextFloat()-0.5f)* (Gdx.graphics.getHeight()-gap-200);
            //this creats a ranom number between 0 and 1 since we
            //are working with the center of te screen


            stars[i] = Gdx.graphics.getHeight()/1.3f - galaxy.getHeight()/1.8f + i*distancebetweenstars;


            galaxyrects[i] = new Rectangle();

        }

    }


    @Override
    public void render() {

        //our background is always seen begin here so it doesnt block tubes
        batch.begin();
        //we want this background to be full screen so we get it to begin
        //at the top right to the bottom left
        //want the width to be the same as the width of the screen
        //we want the height to be the same as the width of the screen


        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        //getting the rocket to move with sensors
        renderx += Gdx.input.getAccelerometerY();
        rendery -= Gdx.input.getAccelerometerX();

        if(renderx<0)
            renderx=0;
        if(renderx>Gdx.graphics.getWidth()-200)
            renderx = Gdx.graphics.getWidth() - 500;

        if(rendery<0)
            rendery = 0;

        else if(rendery>Gdx.graphics.getHeight()-200)
            rendery = Gdx.graphics.getHeight() - 500;





        if (gameState == 1) {
            if(stars[scoringstar] < Gdx.graphics.getWidth()/2) {
                //add one to the score if that happens
                score++;
                Gdx.app.log("Score",String.valueOf(score));

                //cause scoringstar is gonna go to one
                //2 3,4 ...19 but we dont ac have a 20th block
                if(scoringstar < numberofstars-1){
                    scoringstar++;
                }
            }



            if (Gdx.input.isTouched()) {
                //when the screen is touched
                //galaxies start flying
               starvelocity +=2;
               gameState =1;



            }

            for(int i = 0; i<numberofstars;i++){
                if(stars[i]< - galaxy.getWidth()){
                    stars[i]+=(numberofstars*distancebetweenstars);
                }

                else{
                    stars[i]= stars[i]-starvelocity;
                    //if a star is on the left hand of the screen
                    //then we get a score

                }


                batch.draw(galaxy,stars[i],Gdx.graphics.getWidth()/1.9f+galaxy.getWidth()+starsoffset[i]);



                //creating the rectangle is very similar to drawing the tube

                galaxyrects[i] = new Rectangle(stars[i],Gdx.graphics.getWidth()/1.9f+galaxy.getWidth()+starsoffset[i],galaxy.getHeight(),galaxy.getWidth());
            }


            if(rocket.getHeight()>0){
               //

            }

            else{
                gameState = 2 ;

                batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
            }


        }//only do this check if the gae state is zero
        else if (gameState == 0){
                if(Gdx.input.isTouched()){
                //when the screen is touched
                gameState = 1;

            }

        }else if (gameState == 2){
            batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
            if(Gdx.input.isTouched()){
                //when the screen is touched
                gameState = 1;
                startgame();

            }
        }





        //batch.draw(rocket, Gdx.graphics.getWidth() / 2 - rocket.getWidth() , rockety);
        batch.draw(rocket,renderx*1.5f,rendery*2,200,200);

        //drawing the score on the bottom left of the screen
        //font.draw(batch,String.valueOf(score),100,);



        batch.end();


        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //shapeRenderer.setColor(new Color(0f, 0f, 1f, 0.5f));
        //Gdx.gl.glEnable(GL20.GL_BLEND);
        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        rocketCircle.set (renderx*1.5f+100, rendery*2+100,30);
        //shapeRenderer.circle(rocketCircle.x,rocketCircle.y,rocketCircle.radius);


        //shapeRenderer.circle(rocketCircle.x,rocketCircle.y,rocketCircle.radius);


        //each time we loop through we want to render a rect
        for(int i =0; i<numberofstars;i++){
            //shapeRenderer.rect(stars[i],Gdx.graphics.getWidth()/1.9f+galaxy.getWidth()+starsoffset[i],galaxy.getHeight(),galaxy.getWidth());

            if(Intersector.overlaps(rocketCircle,galaxyrects[i])){


                starvelocity-= starvelocity;
                gameState = 2;

            }



        }

        //shapeRenderer.end();


    }
}

