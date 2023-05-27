package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private GameWindow gameWindow = new GameWindow();
    private LogWindow logWindow = createLogWindow();
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        




        loadWindowsSizeLocation();
        addWindow(gameWindow);
        addWindow(logWindow);
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exitApplication();
            }
        });
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        
        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }
        JMenu exitMenu = new JMenu("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_V);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Выход из приложения");
        {
            JMenuItem exitItem = new JMenuItem("Выйти", KeyEvent.VK_S);
            exitItem.addActionListener((event) -> {
                exitApplication();
            });
            exitMenu.add(exitItem);
        }


        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);

        return menuBar;
    }
    {UIManager.put("OptionPane.yesButtonText","Да");
        UIManager.put("OptionPane.noButtonText","Нет");}
    public void exitApplication() {
        int answer = JOptionPane.showConfirmDialog(null,
                "Вы уверены? Весь несохраненный прогресс будет утерян.", "Выход из приложения", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            saveWindowsSizeLocation();
            System.exit(0);
        }
    }
    private void saveWindowsSizeLocation(){
        Dimension gwsize = gameWindow.getSize();
        Dimension lwsize = logWindow.getSize();
        Point gwloc = gameWindow.getLocation();
        Point lwloc = logWindow.getLocation();
        int gwicn = (gameWindow.isIcon()) ? 1 : 0;
        int lwicn = (logWindow.isIcon()) ? 1 : 0;
        logWindow.isIcon();
        String settings = gwsize.height + "\n" + gwsize.width + "\n" + lwsize.height + "\n" + lwsize.width + "\n" +
                gwloc.x + "\n" + gwloc.y + "\n" + lwloc.x + "\n" + lwloc.y + "\n" + gwicn + "\n" + lwicn;
        File file = new File(System.getenv("HOMEPATH")+"\\windows.txt" );
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(settings);
            writer.flush();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void loadWindowsSizeLocation(){
        File file = new File(System.getenv("HOMEPATH")+"\\windows.txt" );
        Dimension gwsize = new Dimension();
        Dimension lwsize = new Dimension();
        Point gwloc = new Point();
        Point lwloc = new Point();
        boolean gwicn = false;
        boolean lwicn = false;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            gwsize.height = Integer.parseInt(reader.readLine());
            gwsize.width = Integer.parseInt(reader.readLine());
            lwsize.height = Integer.parseInt(reader.readLine());
            lwsize.width = Integer.parseInt(reader.readLine());
            gwloc.x = Integer.parseInt(reader.readLine());
            gwloc.y = Integer.parseInt(reader.readLine());
            lwloc.x = Integer.parseInt(reader.readLine());
            lwloc.y = Integer.parseInt(reader.readLine());
            gwicn = (Integer.parseInt(reader.readLine()) == 1);
            lwicn = (Integer.parseInt(reader.readLine()) == 1);
            reader.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
            gwsize.height = 400;
            gwsize.width = 400;
            lwsize.height = 400;
            lwsize.width = 400;
            gwloc.x = 0;
            gwloc.y = 0;
            lwloc.x = 10;
            lwloc.y = 10;
        }
        gameWindow.setSize(gwsize);
        logWindow.setSize(lwsize);
        gameWindow.setLocation(gwloc);
        logWindow.setLocation(lwloc);
        try {
            gameWindow.setIcon(gwicn);
            logWindow.setIcon(lwicn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
