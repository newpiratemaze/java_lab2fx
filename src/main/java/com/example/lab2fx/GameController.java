
package com.example.lab2fx;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;

import java.util.Random;
import javafx.scene.input.MouseButton;

public class GameController {

    @FXML private Pane gamePane;
    @FXML private Circle ball;
    @FXML private Label scoreLabel;

    @FXML private IntegerProperty score = new SimpleIntegerProperty(0);

    private double targetX;
    private double targetY;
    private double speed = 3; // скорость движения в пикселях за кадр
    private Random random = new Random();
    private boolean waveMode = false;
    @FXML
    public void initialize() {
        scoreLabel.textProperty().bind(
                javafx.beans.binding.Bindings.concat("Счет: ", score)
        );

        // Инициализируем стартовую целевую точку
        setNewTarget();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!pause)
                {
                    moveBallTowardsTarget();
                }

            }
        };
        timer.start();

        gamePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                waveMode = !waveMode; // переключаем режим волны / случайный
                if (waveMode) {
                    // Для волны можно зафиксировать стартовое X
                    waveStartX = ball.getCenterX();
                } else {
                    setNewTarget();
                }
            }
        });
    }

    private double waveStartX = 0;

    private void setNewTarget() {
        double radius = ball.getRadius();
        targetX = radius + random.nextDouble() * (gamePane.getWidth() - 2 * radius);
        targetY = radius + random.nextDouble() * (gamePane.getHeight() - 2 * radius);
    }

    private void moveBallTowardsTarget() {
        if (waveMode) {
            // Волнообразное движение по X: двигаем шарик по X с постоянной скоростью
            speed = 2; // можно чуть помедленнее
            ball.setCenterX(ball.getCenterX() + speed);

            // Ограничиваем движение по X границами
            if (ball.getCenterX() >= gamePane.getWidth() - ball.getRadius()) {
                ball.setCenterX(ball.getRadius());
            }

            // Вычисляем Y по формуле y = 300 + 100 * Math.sin(x/50)
            double y = 300 + 100 * Math.sin(ball.getCenterX() / 50);
            // Проверяем, чтобы y оставалось в пределах панели
            y = Math.min(Math.max(y, ball.getRadius()), gamePane.getHeight() - ball.getRadius());

            ball.setCenterY(y);
        } else {
            // Ваш текущий код случайного движения к цели
            double dx = targetX - ball.getCenterX();
            double dy = targetY - ball.getCenterY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < speed) {
                setNewTarget();
                return;
            }

            double dirX = dx / distance;
            double dirY = dy / distance;

            double nextX = ball.getCenterX() + dirX * speed;
            double nextY = ball.getCenterY() + dirY * speed;

            double radius = ball.getRadius();
            double paneWidth = gamePane.getWidth();
            double paneHeight = gamePane.getHeight();

            if (nextX <= radius) {
                nextX = radius;
                targetX = paneWidth - radius;
            } else if (nextX >= paneWidth - radius) {
                nextX = paneWidth - radius;
                targetX = radius;
            }

            if (nextY <= radius) {
                nextY = radius;
                targetY = paneHeight - radius;
            } else if (nextY >= paneHeight - radius) {
                nextY = paneHeight - radius;
                targetY = radius;
            }

            ball.setCenterX(nextX);
            ball.setCenterY(nextY);
        }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        double dx = event.getX() - ball.getCenterX();
        double dy = event.getY() - ball.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= ball.getRadius()) {
            score.set(score.get() + 1);
            moveBallToRandomPosition();
            setNewTarget();
        }
    }

    private void moveBallToRandomPosition() {
        double radius = ball.getRadius();
        double newX = radius + random.nextDouble() * (gamePane.getWidth() - 2 * radius);
        double newY = radius + random.nextDouble() * (gamePane.getHeight() - 2 * radius);

        ball.setCenterX(newX);
        ball.setCenterY(newY);
    }
    protected boolean pause=false;
    @FXML
    protected void setPause()
    {
        pause=!pause;
    }

}

