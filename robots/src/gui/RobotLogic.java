package gui;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class RobotLogic {
    private final Timer m_timer = initTimer();

    private static Timer initTimer()
    {
        return new Timer("events generator", true);
    }
    public Timer getTimer()
    {
        return m_timer;
    }

    private final GameState status;
    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.001;


    public RobotLogic()
    {
        status = new GameState();
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
    }

    public void setTargetPosition(Point p)
    {
        status.m_targetPositionX = p.x;
        status.m_targetPositionY = p.y;
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent()
    {
        double distance = distance(status.m_targetPositionX, status.m_targetPositionY,
                status.m_robotPositionX, status.m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(status.m_robotPositionX, status.m_robotPositionY, status.m_targetPositionX, status.m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > status.m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < status.m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(velocity, angularVelocity, 10);
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = status.m_robotPositionX + velocity / angularVelocity *
                (Math.sin(status.m_robotDirection  + angularVelocity * duration) -
                        Math.sin(status.m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = status.m_robotPositionX + velocity * duration * Math.cos(status.m_robotDirection);
        }
        double newY = status.m_robotPositionY - velocity / angularVelocity *
                (Math.cos(status.m_robotDirection  + angularVelocity * duration) -
                        Math.cos(status.m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = status.m_robotPositionY + velocity * duration * Math.sin(status.m_robotDirection);
        }
        status.m_robotPositionX = newX;
        status.m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(status.m_robotDirection + angularVelocity * duration);
        status.m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }
    public GameState getStatus()
    {
        return status;
    }

}
