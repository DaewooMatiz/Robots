package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

import javax.swing.*;

import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ResourceBundle resources;
    private boolean isReadyToClose = false;

    public MainApplicationFrame(ResourceBundle resources) {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        this.resources = resources;
        localizeStandartButtons();
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(resources);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exitApplication();
            }
        });
    }
    protected void localizeStandartButtons() {
        UIManager.put("InternalFrame.iconButtonToolTip", resources.getString("universalMinimize"));
        UIManager.put("InternalFrame.maxButtonToolTip", resources.getString("universalMaximize"));
        UIManager.put("InternalFrame.closeButtonToolTip", resources.getString("universalClose"));
        UIManager.put("InternalFrameTitlePane.restoreButtonText", resources.getString("universalMaximize"));
        UIManager.put("InternalFrameTitlePane.moveButtonText", resources.getString("universalMove"));
        UIManager.put("InternalFrameTitlePane.sizeButtonText", resources.getString("universalSize"));
        UIManager.put("InternalFrameTitlePane.minimizeButtonText", resources.getString("universalMinimize"));
        UIManager.put("InternalFrameTitlePane.maximizeButtonText", resources.getString("universalMaximize"));
        UIManager.put("InternalFrameTitlePane.closeButtonText", resources.getString("universalClose"));
        UIManager.put("OptionPane.yesButtonText", resources.getString("universalYes"));
        UIManager.put("OptionPane.noButtonText", resources.getString("universalNo"));

    }
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), resources);
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(resources.getString("debugLogMessage"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }


    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu(resources.getString("menuShowMode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                resources.getString("menuShowModeManagement"));

        {
            JMenuItem systemLookAndFeel = new JMenuItem(resources.getString("menuShowModeManagementSystemScheme"), KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(resources.getString("menuShowModeManagementJFrameScheme"), KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu(resources.getString("menuTests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                resources.getString("menuTestsCommands"));

        {
            JMenuItem addLogMessageItem = new JMenuItem(resources.getString("menuTestsCommandsLogMessage"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug(resources.getString("testString"));
            });
            testMenu.add(addLogMessageItem);
        }
        JMenu exitMenu = new JMenu(resources.getString("menuExit"));
        exitMenu.setMnemonic(KeyEvent.VK_V);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                resources.getString("menuExitApplication"));
        {
            JMenuItem exitItem = new JMenuItem(resources.getString("menuExitApplicationDo"), KeyEvent.VK_S);
            exitItem.addActionListener((event) -> {
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            });
            exitMenu.add(exitItem);
        }


        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);

        return menuBar;
    }

    public void exitApplication() {
        if (!isReadyToClose) {
            int answer = JOptionPane.showConfirmDialog(null,
                    resources.getString("exitDialogText"), resources.getString("exitDialogTitle"), JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {

                JInternalFrame[] windowsList = this.desktopPane.getAllFrames();
                for (int i = 0; i < windowsList.length; i++) {
                    try {
                        windowsList[i].setClosed(true);
                    } catch (PropertyVetoException e) {
                        break;
                    }
                }
                if (this.desktopPane.getAllFrames().length == 0) {
                    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    isReadyToClose = true;
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }
            }
        }
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
