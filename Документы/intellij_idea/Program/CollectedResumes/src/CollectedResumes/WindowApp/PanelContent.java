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
 * Shows the panel that contains of content.
 */
public class PanelContent {

    private DataBase dataBase;
    private Parser parser;
    private WindowApp windowApp;

    /**
     * Creates panel on which is located the main (full) content.
     */
    private JPanel panelContent;

    private JButton buttonNext;

    /**
     * Elements resume (id, title resume, name...).
     */
    private List<JPanel> listPanelElements = new ArrayList<>();

    /**
     * The button that hides the hidden elements.
     */
    private List<JButton> listButtonHide = new ArrayList<>();

    /**
     * List id for show.
     */
    private List<String> listId;

    /**
     * Displayed content the runs.
     */
    private boolean visual = false;

    /**
     * The number of elements on the page.
     */
    private final int NUMBER_ELEMENTS_PAGE = 25;

    /**
     * Location of the id element.
     */
    private static final int LOCATION_ID = 0;

    /**
     * Location of the URL resume element.
     */
    private static final int LOCATION_URL_RESUME = 1;

    /**
     * Location of the title resume element.
     */
    private static final int LOCATION_TITLE_RESUME = 2;

    /**
     * Location of the salary element.
     */
    private static final int LOCATION_SALARY = 3;

    /**
     * Location of the experience in years element.
     */
    private static final int LOCATION_EXPERIENCE_YEAR = 4;

    /**
     * Location of the education element.
     */
    private static final int LOCATION_EDUCATION = 5;

    /**
     * Location of the name element.
     */
    private static final int LOCATION_NAME = 6;

    /**
     * Location of the age element.
     */
    private static final int LOCATION_AGE = 7;

    /**
     * Location of the city element.
     */
    private static final int LOCATION_CITY = 8;

    /**
     * Location of the old work location element.
     */
    private final int LOCATION_OLD_WORK_LOCATION = 9;

    /**
     * Location of the old work who element.
     */
    private final int LOCATION_OLD_WORK_WHO = 10;

    /**
     * Location of the old work time element.
     */
    private final int LOCATION_OLD_WORK_TIME = 11;

    /**
     * Location of the old work specification element.
     */
    private final int LOCATION_OLD_WORK_SPECIFICATION = 12;

    /**
     * Location of the key skill element.
     */
    private static final int LOCATION_KEY_SKILL = 13;

    /**
     * Location of the update time element.
     */
    private static final int LOCATION_TIME_UPDATE = 14;

    /**
     * Location of the button hide element.
     */
    private static final int LOCATION_BUTTON_HIDE = 15;

    /**
     * The number of elements.
     */
    private int numberElements;

    private int numberPage = 0;

    private int currentPage;

    /**
     * Constructor.
     *
     * @param listId List id for show.
     */
    PanelContent(DataBase dataBase, Parser parser, final WindowApp windowApp, final ArrayList<String> listId) {
        this.dataBase = dataBase;
        this.parser = parser;
        this.windowApp = windowApp;
        createPanelContent(listId);
    }

    void createPanelContent(ArrayList<String> listId) {
        currentPage = 0;
        this.listId = listId;
        numberElements = this.listId.size();
        show();
    }

    /**
     * To shows the content.
     */
    private void show() {
        visual = true;

        windowApp.getPanelInformation().updateLabelNumberFindElement(numberElements);

        panelContent  = new JPanel(new GridBagLayout());
        windowApp.showPanelContent(this);
        if (numberElements > 0) {
            numberPage = numberPageCalculate();

            createBox(currentPage);
        }

//        visual = false;

        parser.parserSend();
    }

    /**
     * Creates the box of elements.
     *
     * @param page Number the page.
     */
    private void createBox(final int page) {
        Thread panelContentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int element = NUMBER_ELEMENTS_PAGE * (page); element < numberElements; element++) {
                    displayedPrepare();

                    if (windowApp.getPanelSearch().isSearching()) {
                        visual = false;
                        windowApp.getPanelSearch().searchSend();
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
                        buttonNext = new JButton();
                        buttonNext.setEnabled(true);
                        buttonNext.setText("Next");
                        buttonNext.addActionListener(new JButtonNextActionListener());
                        panelContent.add(buttonNext,
                                new GridBagConstraints(0, element + 1, 1, 1, 1, 1,
                                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                                        new Insets(1, 1, 1, 1), 0, 0));
                        break;
                    }
                }
                visual = false;
            }
        });
        panelContentThread.setName("PanelContent");
        panelContentThread.setPriority(5);
        panelContentThread.start();
    }

    private final Object monitor = new Object();

    /**
     * Open thread for the displayed content.
     */
    public void displayedSend() {
        synchronized (monitor) {
            if (!windowApp.isClosing() && !windowApp.getPanelSearch().isSearching() && !parser.isParser()) {
                visual = true;

                monitor.notify();
            }
        }
    }

    /**
     * Waiting while the other thread.
     */
    private void displayedPrepare() {
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
        synchronized (monitor) {
            while (windowApp.isClosing()) {
                visual = false;

                windowApp.closingSend();

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
            visual = true;
            createBox(++currentPage);
//            visual = false;
        }

    }

    /**
     * Calculates the number of pages.
     *
     * @return The number of page.
     */
    private int numberPageCalculate() {
        int numberPage;
        if (numberElements % NUMBER_ELEMENTS_PAGE == 0) {
            numberPage = numberElements / NUMBER_ELEMENTS_PAGE;
        } else {
            numberPage = Math.floorDiv(numberElements, NUMBER_ELEMENTS_PAGE) + 1;
        }
        return numberPage;
    }

    JPanel getPanelContent() {
        return panelContent;
    }

    public boolean isVisual() {
        return visual;
    }

}
