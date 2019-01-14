package CollectedResumes.WindowApp;

import CollectedResumes.DataBase.DataBase;
import CollectedResumes.Parser.Parser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the panel that contains of content.
 */
public class PanelContent {

    private WindowApp windowApp;
    private DataBase dataBase;
    private Parser parser;

    private JPanel panelContent;

    private JButton buttonNext = new JButton();

    /**
     * Elements resume (id, title resume, name...).
     */
    private List<JPanel> listPanelElements;

    /**
     * The button that hides the hidden elements.
     */
    private List<JButton> listButtonHide;

    private List<String> listId;

    private static final int LOCATION_ID = 0;
    private static final int LOCATION_URL_RESUME = 1;
    private static final int LOCATION_TITLE_RESUME = 2;
    private static final int LOCATION_SALARY = 3;
    private static final int LOCATION_EXPERIENCE_YEAR = 4;
    private static final int LOCATION_EDUCATION = 5;
    private static final int LOCATION_NAME = 6;
    private static final int LOCATION_AGE = 7;
    private static final int LOCATION_CITY = 8;
    private final int LOCATION_OLD_WORK_LOCATION = 9;
    private final int LOCATION_OLD_WORK_WHO = 10;
    private final int LOCATION_OLD_WORK_TIME = 11;
    private final int LOCATION_OLD_WORK_SPECIFICATION = 12;
    private static final int LOCATION_KEY_SKILL = 13;
    private static final int LOCATION_TIME_UPDATE = 14;
    private static final int LOCATION_BUTTON_HIDE = 15;

    private final int NUMBER_ELEMENTS_PAGE = 25;

    private boolean show;

    private int numberElements;

    private int currentPage;

    /**
     * To control access to an object.
     */
    private static final Object MONITOR = new Object();

    private Thread panelContentThread;

    PanelContent(final WindowApp windowApp, final  DataBase dataBase, final Parser parser, final ArrayList<String> listId) {
        this.windowApp = windowApp;
        this.dataBase = dataBase;
        this.parser = parser;
        buttonNext.setText("Next");
        buttonNext.addActionListener(new JButtonNextActionListener());
        updatePanelContent(listId);
    }

    void updatePanelContent(final ArrayList<String> listId) {
        this.listId = listId;
        numberElements = this.listId.size();
        currentPage = 0;
        show();
    }

    private void show() {
        show = true;

        windowApp.getPanelInformation().updateLabelNumberFindElement(numberElements);

        panelContent  = new JPanel(new GridBagLayout());

        windowApp.showPanelContent(this);

        listPanelElements = new ArrayList<>();
        listButtonHide = new ArrayList<>();

        if (panelContentThread != null) {
            panelContentThread.interrupt();
        }

        if (numberElements > 0) {
            createBox(currentPage);
        } else {
            show = false;
            windowApp.getPanelInformation().updateLabelNumberDisplayedElement(0);
        }

        parser.parserSend();
    }

    /**
     * Creates the box of elements.
     *
     * @param page Number the page.
     */
    private void createBox(final int page) {
        panelContentThread = new Thread(() -> {
            for (int element = NUMBER_ELEMENTS_PAGE * (page); element < numberElements; element++) {
                prepareShow();

                if (windowApp.getPanelSearch().isSearching()) {
                    show = false;
                    windowApp.getPanelSearch().wake();
                    break;
                }

                listPanelElements.add(element, new JPanel(new GridBagLayout()));

                visualElements(element, listId.get(element));

                panelContent.add(listPanelElements.get(element),
                        new GridBagConstraints(
                                0, element, 1, 1, 1, ((element == numberElements - 1) ? 1 : 0),
                                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                                new Insets(1, 1, ((element == ((page + 1) * NUMBER_ELEMENTS_PAGE) - 1) ? 1 : 10), 1),
                                0, 0));

                windowApp.getPanelInformation().updateLabelNumberDisplayedElement(element + 1);

                if (element == ((page + 1) * NUMBER_ELEMENTS_PAGE) - 1) {
                    buttonNext.setEnabled(true);
//                    buttonNext.setText("Next");
//                    buttonNext.addActionListener(new JButtonNextActionListener());
                    panelContent.add(buttonNext,
                            new GridBagConstraints(0, element + 1, 1, 1, 1, 1,
                                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                                    new Insets(1, 1, 1, 1), 0, 0));
                    break;
                }
            }
            show = false;
        });
        panelContentThread.setName("PanelContent");
        panelContentThread.setPriority(5);
        panelContentThread.start();
    }

    /**
     * Waiting while the other thread.
     */
    private void prepareShow() {
        try {
            prepareClosing();
            prepareParser();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waiting while the app closing.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareClosing() throws InterruptedException {
        synchronized (MONITOR) {
            while (windowApp.isClosing()) {
                show = false;

                windowApp.closingSend();

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
     * Open thread for the displayed content.
     */
    public void displayedSend() {
        synchronized (MONITOR) {
            if (!windowApp.isClosing() && !windowApp.getPanelSearch().isSearching() && !parser.isParser()) {
                show = true;
                MONITOR.notify();
            }
        }
    }

    /**
     * Creates the elements resumes.
     *
     * @param element Elements resume;
     * @param id by id calculates the value from the database.
     */
    private void visualElements(final int element, final String id) {
        visualBorder(element);
        visualElement(element, id, "id", LOCATION_ID, true);
        visualElement(element, id, "urlResume", LOCATION_URL_RESUME, true);
        visualElement(element, id, "titleResume", LOCATION_TITLE_RESUME, true);
        visualElement(element, id, "salary", LOCATION_SALARY, true);
        visualElement(element, id, "experienceYear", LOCATION_EXPERIENCE_YEAR, true);
        visualElement(element, id, "education", LOCATION_EDUCATION, true);
        visualElement(element, id, "name", LOCATION_NAME, true);
        visualElement(element, id, "age", LOCATION_AGE, true);
        visualElement(element, id, "city", LOCATION_CITY, true);

        visualElement(element, id, "oldWorkLocation", LOCATION_OLD_WORK_LOCATION, false);
        visualElement(element, id, "oldWorkWho", LOCATION_OLD_WORK_WHO, false);
        visualElement(element, id, "oldWorkTime", LOCATION_OLD_WORK_TIME, false);
        visualElement(element, id, "oldWorkSpecification", LOCATION_OLD_WORK_SPECIFICATION, false);

        visualElement(element, id, "keySkill", LOCATION_KEY_SKILL, false);
        visualElement(element, id, "timeUpdate", LOCATION_TIME_UPDATE, true);

        listPanelElements.get(element).add(new JLabel(),
                new GridBagConstraints(1, LOCATION_BUTTON_HIDE, 1, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));

        visualButtonHide(element);
    }

    /**
     * Creates a border elements.
     *
     * @param element Elements resume.
     */
    private void visualBorder(final int element) {
        Border borderContent = BorderFactory.createTitledBorder("Number: " + (element + 1));
        listPanelElements.get(element).setBorder(borderContent);
    }

    /**
     * Creates a field of element.
     *
     * @param element Element resume;
     * @param id by id calculates the value from the database;
     * @param column column (type) desired value;
     * @param location the location of the element in box;
     * @param hide hide element resume.
     */
    private void visualElement(final int element, final String id, final String column, final int location,
                               final boolean hide) {
        JPanel panelElement = new JPanel(new GridBagLayout());

        panelElement.add(new JLabel(column + ": "),
                new GridBagConstraints(0, 0, 1, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 1), 0, 0));

        String value = dataBase.read(column, "id", id);
        JTextArea textAreaElement = new JTextArea(value);

        if (value.equals("null")) {
            textAreaElement.setEnabled(false);
        }

        textAreaElement.setEditable(false);
        textAreaElement.setLineWrap(true);
        textAreaElement.setWrapStyleWord(true);

        panelElement.add(textAreaElement,
                new GridBagConstraints(1, 0, 2, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 1), 0, 0));

        panelElement.add(new JLabel(),
                new GridBagConstraints(2, 0, 0, 0, 1, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));

        listPanelElements.get(element).add(panelElement,
                new GridBagConstraints(0, location, 3, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 1), 0, 0));

        listPanelElements.get(element).getComponent(location).setVisible(hide);
    }

    /**
     *  Creates the button that hides the hidden elements.
     *
     * @param element Elements resume;
     */
    private void visualButtonHide(final int element) {
        listButtonHide.add(element, new JButton("Show"));

        listButtonHide.get(element).addActionListener(new JButtonHideActionListener(element));

        listPanelElements.get(element).add(listButtonHide.get(element),
                new GridBagConstraints(2, LOCATION_BUTTON_HIDE, 1, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 1), 0, 0));
    }

    /**
     * Add a listener to the hide button. Hiding text fields (old work).
     */
    private class JButtonHideActionListener implements ActionListener {

        private int id;

        private JButtonHideActionListener(int id) {
            this.id = id;
        }

        boolean visible = false;

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            visible = !visible;

            listPanelElements.get(id).getComponent(LOCATION_OLD_WORK_LOCATION).setVisible(visible);
            listPanelElements.get(id).getComponent(LOCATION_OLD_WORK_WHO).setVisible(visible);
            listPanelElements.get(id).getComponent(LOCATION_OLD_WORK_TIME).setVisible(visible);
            listPanelElements.get(id).getComponent(LOCATION_OLD_WORK_SPECIFICATION).setVisible(visible);

            if (!visible) {
                listButtonHide.get(id).setText("Show");
            } else {
                listButtonHide.get(id).setText("Hide");
            }
        }

    }

    private class JButtonNextActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            buttonNext.setEnabled(false);
            show = true;
            currentPage++;
            createBox(currentPage);
        }

    }

    JPanel getPanelContent() {
        return panelContent;
    }

    public boolean isShow() {
        return show;
    }

}
