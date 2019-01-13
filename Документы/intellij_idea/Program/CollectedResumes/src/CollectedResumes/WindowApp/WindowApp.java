package CollectedResumes.WindowApp;

import CollectedResumes.DataBase.DataBase;
import CollectedResumes.Parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * Shows the program.
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
    private JFrame mainFrame = new JFrame();

    /**
     * Creates the scroll bar on which located the content.
     */
    private JScrollPane scrollPaneScrollBar = new JScrollPane();

    /**
     * The window size at width.
     */
    private static final int SIZE_WINDOW_WIDTH = 800;

    /**
     * The window size at height.
     */
    private static final int SIZE_WINDOW_HEIGHT = 600;

    /**
     * Closing the window app.
     */
    private boolean closing = false;

    /**
     * Constructor.
     */
    public WindowApp() {
        initializeMainFrame();
        configMainFrame();

        panelInformation();
        panelSearch();

        initialBack();

        closingMainFrame();
    }

    /**
     * Initialize the main frame.
     */
    private void initializeMainFrame() {
        mainFrame.setTitle("Collected resumes"); // ask the name of the window "Collected resumes"
        mainFrame.setLayout(new BorderLayout()); // add to the panel BorderLayout manager
    }

    /**
     * Configuration main frame.
     */
    private void configMainFrame() {
        mainFrame.setPreferredSize(new Dimension(SIZE_WINDOW_WIDTH, SIZE_WINDOW_HEIGHT)); // the size of the window
        mainFrame.setResizable(false); // not able to change of size the window
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // by clicking on the "x" closes the window
        mainFrame.pack(); // the optimal size
        mainFrame.setLocationRelativeTo(null); // the window in the center of the screen
        mainFrame.setVisible(true); // window visibility state
    }

    /**
     * Shows the panel on which located the elements search.
     */
    private void panelSearch() {
        panelSearch = new PanelSearch(this);
        mainFrame.add(panelSearch.getPanelSearch(), BorderLayout.NORTH); // add the panel search on the north main frame
    }

    /**
     * Shows the panel on which of located the main content.
     */
    private void panelContent(WindowApp windowApp, ArrayList<String> listId) {
        panelContent = new PanelContent(dataBase, parser, windowApp, listId); // calling the panel visual content
    }

    /**
     * Add the element on which the panel.
     */
    void showPanelContent(PanelContent panelContent) {
        scrollPaneScrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // set the vertical scroll bar
        this.panelContent = panelContent;
        scrollPaneScrollBar.setViewportView(panelContent.getPanelContent());
        mainFrame.add(scrollPaneScrollBar, BorderLayout.CENTER); // add the panel visual content on the center main frame
    }

    /**
     * Remove all the content panel.
     */
    void removeContent() {
        mainFrame.remove(scrollPaneScrollBar);
        scrollPaneScrollBar.removeAll();
        scrollPaneScrollBar = new JScrollPane();
        mainFrame.add(scrollPaneScrollBar, BorderLayout.CENTER);
        panelInformation.updateLabelNumberFindElement(0);
        panelInformation.updateLabelNumberDisplayedElement(0);
    }

    /**
     * Shows the panel on which located the information about the main values database.
     */
    private void panelInformation() {
        panelInformation = new PanelInformation(); // calling the panel information
        mainFrame.add(panelInformation.getPanelInformation(), BorderLayout.SOUTH); // add the panel information on the south main frame
    }

    /**
     * Start database, parser. Send (database, parser) for background app.
     */
    private void initialBack() {
        dataBase = new DataBase(this);
        dataBase.setPrioritySize(25);
        dataBase.run();

        parser = new Parser(this, dataBase);
        parser.run();

        panelSearch.initialBack(dataBase, parser);
        panelContent(this, dataBase.listIdSortingByColumn("timeUpdate", "DESC"));
    }

    /**
     * Listener by closing.
     */
    private void closingMainFrame() {
        WindowListener listenerByWindowApp = new ListenerByWindowApp();
        mainFrame.addWindowListener(listenerByWindowApp);
    }

    /**
     * Add a listener to the window.
     */
    public class ListenerByWindowApp implements WindowListener {

        @Override
        public void windowOpened(WindowEvent windowEvent) {

        }

        @Override
        public void windowClosing(WindowEvent windowEvent) {
            closingPrepare();

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

    private static final Object monitor = new Object();

    /**
     * Closing the program.
     */
    public void closingSend() {
        synchronized (monitor) {
            if (!panelSearch.isSearching() && !panelContent.isVisual() && !parser.isParser()) {
                monitor.notify();
            }
        }
    }

    /**
     * Waiting while the other thread.
     */
    private void closingPrepare() {
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
        synchronized (monitor) {
            while (panelSearch.isSearching()) {
                monitor.wait();
            }
        }
    }

    /**
     * Waiting while og content the visibility.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareVisibility() throws InterruptedException {
        synchronized (monitor) {
            while (panelContent.isVisual()) {
                monitor.wait();
            }
        }
    }

    /**
     * Waiting while the parser.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareParser() throws InterruptedException {
        synchronized (monitor) {
            while (parser.isParser()) {
                monitor.wait();
            }
        }
    }

    public boolean isClosing() {
        return closing;
    }

    public PanelSearch getPanelSearch() {
        return panelSearch;
    }

    public PanelContent getPanelContent() {
        return panelContent;
    }

    public PanelInformation getPanelInformation() {
        return panelInformation;
    }

}
