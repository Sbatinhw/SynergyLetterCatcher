import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class LetterCatcherGame extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int PLATFORM_WIDTH = 100;
    private static final int PLATFORM_HEIGHT = 20;
    private static final int LETTER_SIZE = 30;
    private static final int GAME_SPEED = 30;
    
    private int platformX = WIDTH / 2 - PLATFORM_WIDTH / 2;
    private int platformY = HEIGHT - 70;
    private char fallingLetter;
    private int fallingLetterX, fallingLetterY;
    private int caughtLetters = 0;
    private int totalLetters = 0;
    private boolean gameRunning = true;
    private Random random = new Random();
    
    public LetterCatcherGame() {
        setTitle("Ловец букв");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Генерация первой падающей буквы
        generateNewFallingLetter();
        
        // Таймер для обновления игры
        Timer timer = new Timer(GAME_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                repaint();
            }
        });
        timer.start();
        
        // Обработка клавиш для движения платформы
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    platformX -= 20;
                    if (platformX < 0) platformX = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    platformX += 20;
                    if (platformX > WIDTH - PLATFORM_WIDTH) platformX = WIDTH - PLATFORM_WIDTH;
                }
            }
        });
        
        setFocusable(true);
        setVisible(true);
    }
    
    private void generateNewFallingLetter() {
        fallingLetter = (char) (random.nextInt(26) + 'A');
        fallingLetterX = random.nextInt(WIDTH - LETTER_SIZE);
        fallingLetterY = 0;
        totalLetters++;
    }
    
    private void updateGame() {
        if (!gameRunning) return;
        
        // Движение падающей буквы
        fallingLetterY += 5;
        
        // Проверка столкновения с платформой
        if (fallingLetterY + LETTER_SIZE >= platformY && 
            fallingLetterY <= platformY + PLATFORM_HEIGHT &&
            fallingLetterX + LETTER_SIZE >= platformX && 
            fallingLetterX <= platformX + PLATFORM_WIDTH) {
            
            caughtLetters++;
            generateNewFallingLetter();
        }
        
        // Если буква упала за пределы экрана
        if (fallingLetterY > HEIGHT) {
            generateNewFallingLetter();
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Очистка экрана
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Рисование платформы
        g.setColor(Color.BLUE);
        g.fillRect(platformX, platformY, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        
        // Рисование падающей буквы
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, LETTER_SIZE));
        g.drawString(String.valueOf(fallingLetter), fallingLetterX, fallingLetterY + LETTER_SIZE);
        
        // Отображение счета
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Поймано: " + caughtLetters + " из " + totalLetters, 20, 60);
        
        // Простое руководство
        g.drawString("Используйте ← и → для движения", 20, HEIGHT - 20);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LetterCatcherGame();
            }
        });
    }
}