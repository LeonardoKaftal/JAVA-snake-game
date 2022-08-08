import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePannel extends JPanel implements ActionListener {

    //game variable
    Timer timer;
    Random random;
    Apple apple;
    final int SCREEN_WIDTH = 600;
    final int SCREEN_HEIGHT = 600;
    final int UNIT_SIZE = 25;
    final int GAME_UNIT = SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE;

    boolean isRunning = false;
    final int DELAY = 75;

    // snake variable
    int appleEaten = 0;
    char direction = 'R';
    int snakeNumberOfParts = 6;
    final SnakePart[] snakeBodyParts = new SnakePart[GAME_UNIT];



    GamePannel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        this.setBackground(Color.BLACK);
        startGame();
    }

    public void startGame() {
        generateInitialSnakePart();
        generateApple();
        isRunning = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(apple.x,apple.y,UNIT_SIZE,UNIT_SIZE);

       for (int i = 0; i < snakeNumberOfParts; i++) {
            if (i == 0) {
                g.setColor(new Color(45,180,0));
                g.fillRect(snakeBodyParts[i].x,snakeBodyParts[i].y,UNIT_SIZE,UNIT_SIZE);
            } else {
                g.setColor(Color.green);
                g.fillRect(snakeBodyParts[i].x,snakeBodyParts[i].y,UNIT_SIZE,UNIT_SIZE);
            }
       }
       g.setColor(Color.white);
       g.drawString("Score: " + appleEaten,SCREEN_WIDTH / 2,10);
    }

    public void generateInitialSnakePart() {
        for (int i = snakeNumberOfParts - 1; i > -1; i--) {
            snakeBodyParts[i] = new SnakePart(UNIT_SIZE * (snakeNumberOfParts - i),SCREEN_WIDTH / 2);
        }
    }

    public void generateNewSnakePart() {
        snakeNumberOfParts++;

        switch (direction) {
            case 'U' -> snakeBodyParts[snakeNumberOfParts - 1] = new SnakePart(snakeBodyParts[snakeNumberOfParts - 2].x,snakeBodyParts[snakeNumberOfParts - 2].y + UNIT_SIZE);
            case 'D' -> snakeBodyParts[snakeNumberOfParts - 1] = new SnakePart(snakeBodyParts[snakeNumberOfParts - 2].x,snakeBodyParts[snakeNumberOfParts - 2].y - UNIT_SIZE);
            case 'R' -> snakeBodyParts[snakeNumberOfParts - 1] = new SnakePart(snakeBodyParts[snakeNumberOfParts - 2].x - UNIT_SIZE,snakeBodyParts[snakeNumberOfParts - 2].y);
            case 'L' -> snakeBodyParts[snakeNumberOfParts - 1] = new SnakePart(snakeBodyParts[snakeNumberOfParts - 2].x + UNIT_SIZE,snakeBodyParts[snakeNumberOfParts - 2].y);

        }
    }

    public void generateApple() {
        apple = new Apple(
                // apple x coordinate
                (int)(random.nextInt(SCREEN_WIDTH) / UNIT_SIZE) * UNIT_SIZE,
                // apple y coordinate
                (int)(random.nextInt(SCREEN_HEIGHT) / UNIT_SIZE) * UNIT_SIZE);
    }

    public void move() {
        for (int i = snakeNumberOfParts - 1; i > 0; i--) {
            snakeBodyParts[i].x = snakeBodyParts[i - 1].x;
            snakeBodyParts[i].y = snakeBodyParts[i - 1].y;
        }

        switch (direction) {
            case 'U' -> snakeBodyParts[0].y -= UNIT_SIZE;
            case 'D' -> snakeBodyParts[0].y += UNIT_SIZE;
            case 'R' -> snakeBodyParts[0].x += UNIT_SIZE;
            case 'L' -> snakeBodyParts[0].x -= UNIT_SIZE;
        }
    }


    public void checkAppleCollision() {
        for (int i = 0; i < snakeNumberOfParts - 1; i++) {
            if (snakeBodyParts[i].x == apple.x && snakeBodyParts[i].y == apple.y) {
                generateNewSnakePart();
                generateApple();
                appleEaten++;
                break;
            }
        }
    }

    public void checkSnakeCollision() {
        if (snakeBodyParts[0].x < 0 || snakeBodyParts[0].x > SCREEN_WIDTH || snakeBodyParts[0].y > SCREEN_HEIGHT || snakeBodyParts[0].y < 0) {
            isRunning = false;
        }
        for (int i = 1; i < snakeNumberOfParts; i++) {
            if (snakeBodyParts[i].x == snakeBodyParts[0].x && snakeBodyParts[0].y == snakeBodyParts[i].y) {
                isRunning = false;
                break;
            }
        }
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkAppleCollision();
            checkSnakeCollision();
        }
        repaint();
    }

    public class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
            }
        }
    }
}
