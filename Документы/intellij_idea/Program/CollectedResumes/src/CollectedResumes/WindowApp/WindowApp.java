package CollectedResumes.WindowApp;

import CollectedResumes.DataBase.DataBase;
import CollectedResumes.Parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * Creates a windowed application to display resumes that are in the database.
 */
public class WindowApp {

    private DataBase dataBase;
    private Parser parser;

    private PanelSearch panelSearch;
    private PanelContent panelContent;
    private PanelInformation panelInformation;

    /**
     * Creates the main frame on which is located other frame:
     *     - panelSearch;
     *     - panelContent;
     *     - panelInformation.
     */
    private JFrame mainFrame;

    private JScrollPane scrollPaneScrollBar = new JScrollPane();

    private static final int SIZE_WINDOW_WIDTH = 800;
    private static final int SIZE_WINDOW_HEIGHT = 600;

    private boolean closing;

    /**
     * To control access to an object.
     */
    private static final Object MONITOR = new Object();

    public WindowApp() {
        initMainFrame();

        initPanelInformation();
        initPanelSearch();

        initBack();

        closingWinApp();
    }

    private void initMainFrame() {
        mainFrame = new JFrame();
        configMainFrame();
    }

    private void configMainFrame() {
        mainFrame.setTitle("Collected resumes");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setPreferredSize(new Dimension(SIZE_WINDOW_WIDTH, SIZE_WINDOW_HEIGHT));
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private void initPanelInformation() {
        panelInformation = new PanelInformation();
        mainFrame.add(panelInformation.getPanelInformation(), BorderLayout.SOUTH); // add the panel information on the south main frame
    }

    private void initPanelSearch() {
        panelSearch = new PanelSearch(this);
        mainFrame.add(panelSearch.getPanelSearch(), BorderLayout.NORTH); // add the panel search on the north main frame
    }

    /**
     * Creates a database. Creates a parser.
     */
    private void initBack() {
        dataBase = new DataBase(this);
        dataBase.setPrioritySize(25);
        dataBase.run();

        parser = new Parser(this, dataBase);
        parser.run();

        panelSearch.initBack(dataBase, parser);
        panelContent(dataBase.listIdSortingByColumn("timeUpdate", "DESC"));
    }

    private void panelContent(final ArrayList<String> listId) {
        panelContent = new PanelContent(this, dataBase, parser, listId);
    }

    /**
     * Creates the scroll bar on which located the panelContent.
     */
    void showPanelContent(PanelContent panelContent) {
        scrollPaneScrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.panelContent = panelContent;
        scrollPaneScrollBar.setViewportView(panelContent.getPanelContent());
        mainFrame.add(scrollPaneScrollBar, BorderLayout.CENTER);
    }

    /**
     * What happens when you close the window of the application.
     */
    private void closingWinApp() {
        mainFrame.addWindowListener(new ListenerClosingWinApp());
    }

    private class ListenerClosingWinApp implements WindowListener {

        @Override
        public void windowOpened(WindowEvent windowEvent) {

        }

        @Override
        public void windowClosing(WindowEvent windowEvent) {
            prepareClosing();
            dataBase.close();
        }

        @Override
        public void windowClosed(WindowEvent windowEvent) {

        }


        @Override
        public void windowIconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeiconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowActivated(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeactivated(WindowEvent windowEvent) {

        }
    }

    /**
     * Waiting while the other thread.
     */
    private void prepareClosing() {
        closing = true;

        mainFrame.setTitle("Closing the program. Please wait...");

        try {
            prepareSearching();
            prepareVisibility();
            prepareParser();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waiting while the searching.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareSearching() throws InterruptedException {
        synchronized (MONITOR) {
            while (panelSearch.isSearching()) {
                MONITOR.wait();
            }
        }
    }

    /**
     * Waiting while og content the visibility.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareVisibility() throws InterruptedException {
        synchronized (MONITOR) {
            while (panelContent.isShow()) {
                MONITOR.wait();
            }
        }
    }

    /**
     * Waiting while the parser.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareParser() throws InterruptedException {
        synchronized (MONITOR) {
            while (parser.isParser()) {
                MONITOR.wait();
            }
        }
    }

    /**
     * Closing the program.
     */
    public void closingSend() {
        synchronized (MONITOR) {
            if (!panelSearch.isSearching() && !panelContent.isShow() && !parser.isParser()) {
                MONITOR.notify();
            }
        }
    }

    public boolean isClosing() {
        return closing;
    }

    public PanelInformation getPanelInformation() {
        return panelInformation;
    }

    public PanelSearch getPanelSearch() {
        return panelSearch;
    }

    public PanelContent getPanelContent() {
        return panelContent;
    }

}
