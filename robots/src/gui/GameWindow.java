package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    private RobotLogic m_robot;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_robot = new RobotLogic();
        m_visualizer = new GameVisualizer(m_robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
    public RobotLogic shareRobot()
    {
        return m_robot;
    }
}
