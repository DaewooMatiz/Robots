package gui;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;



public class PosWindow extends JInternalFrame
{
    private Timer timer;
    private RobotLogic robot;
    private TextArea field;
    private GameState status;

    public PosWindow(RobotLogic robot)
    {
        super("Координаты робота", true, true, true, true);
        this.robot = robot;
        timer = robot.getTimer();
        status = robot.getStatus();
        field = new TextArea("");
        field.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(field, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                updatePosition();

            }
        }, 0, 50);

    }

    private void updatePosition()
    {
        StringBuilder content = new StringBuilder();
        status = robot.getStatus();
        content.append("X: ").append(" ");
        content.append(status.m_robotPositionX).append(" ");
        content.append("Y: ").append(" ");
        content.append(status.m_robotPositionY).append("\n");
        field.setText(content.toString());
        field.invalidate();
    }

}