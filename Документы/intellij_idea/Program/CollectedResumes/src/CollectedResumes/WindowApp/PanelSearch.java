package CollectedResumes.WindowApp;

import CollectedResumes.DataBase.DataBase;
import CollectedResumes.Parser.Parser;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows the panel that contains of elements the search.
 */
public class PanelSearch {

    private DataBase dataBase;
    private Parser parser;
    private WindowApp windowApp;

    /**
     * Creates the panel that contains of elements the search:
     *     - search by city;
     *     - search by salary;
     *     - search by work experienceYear;
     *     - search by age;
     *     - search by all (id, titleResume, name).
     */
    private JPanel panelSearch;

    /**
     * Creates a text field (search by city) that accepts user input.
     */
    private JTextField textFieldSearchCity = new JTextField();

    /**
     * Creates a text field (search by salary) that accepts user input.
     */
    private JTextField textFieldSearchSalary = new JTextField();

    /**
     * Creates a combo box (search by experience in years) that accept user choice.
     */
    private JComboBox<String> comboBoxSearchExperienceYear;

    /**
     * Creates a text field (search by age) that accepts user input.
     */
    private JTextField textFieldSearchAge = new JTextField();

    /**
     * Creates a text field (search by all (id, titleResume, name)) that accepts user input.
     */
    private JTextField textFieldSearchAll = new JTextField();

    /**
     * Sortable column.
     */
    private JComboBox<String> comboBoxSortColumn;

    /**
     * The type of sorting.
     */
    private JComboBox<String> comboBoxSortType;

    /**
     * Search at the value.
     */
    private JButton buttonSearch = new JButton();

    /**
     * Location of text field (search by city).
     */
    private static final int LOCATION_SEARCH_CITY = 0;

    /**
     * Location of text field (search by salary).
     */
    private static final int LOCATION_SEARCH_SALARY = 1;

    /**
     * Location of text field (search by work experienceYear).
     */
    private static final int LOCATION_SEARCH_EXPERIENCE_YEAR = 2;

    /**
     * Location of text field (search by age).
     */
    private static final int LOCATION_SEARCH_AGE = 3;

    /**
     * Location of text field (search by all).
     */
    private static final int LOCATION_SEARCH_ALL = 4;

    /**
     * Location of button search.
     */
    private static final int LOCATION_BUTTON_SEARCH = 5;

    /**
     * Searching the runs.
     */
    private boolean searching = false;

    /**
     * Constructor.
     */
    PanelSearch(final WindowApp windowApp) {
        this.windowApp = windowApp;

        initializePanelSearch();

        createTextFieldSearch();
        createComboBoxSortColumn();
        createComboBoxSortType();
        createButtonSearch();
    }

    /**
     * Initialize the panel search.
     */
    private void initializePanelSearch() {
        panelSearch = new JPanel(); // initialize
        panelSearch.setLayout(new GridBagLayout()); // add to the panel GridBagLayout manager
    }

    /**
     * Creates of text field for search.
     */
    private void createTextFieldSearch() {
        createTextFieldSearch("City", textFieldSearchCity, LOCATION_SEARCH_CITY, false);
        createTextFieldSearch("Salary", textFieldSearchSalary, LOCATION_SEARCH_SALARY, true);
        createComboBoxSearch();
        createTextFieldSearch("Age", textFieldSearchAge, LOCATION_SEARCH_AGE, true);
        createTextFieldSearch("All", textFieldSearchAll, LOCATION_SEARCH_ALL, false);
    }

    /**
     * Creates the panel that contains of text field (search by column).
     *
     * @param column Name the type (column);
     * @param textFieldSearchColumn a text field used to search;
     * @param location panel location;
     * @param digit filter for input (if this is true enter digits).
     */
    private void createTextFieldSearch(final String column, JTextField textFieldSearchColumn, final int location, final boolean digit) {
        JPanel panelSearchByColumn = new JPanel(); // initialize
        textFieldSearchColumn.setColumns(6); // size of text field
        textFieldSearchColumn.setText(column);
        textFieldSearchColumn.addFocusListener(new JTextFieldSearchHintFocusListener(textFieldSearchColumn, column));

        if (digit) {
            PlainDocument doc = (PlainDocument) textFieldSearchColumn.getDocument();
            doc.setDocumentFilter(new DigitFilter(column));
        }

        panelSearchByColumn.add(textFieldSearchColumn); // add textField in panelSearchByColumn
        panelSearch.add(panelSearchByColumn, new GridBagConstraints(location, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0)); // visual of text field search
    }

    private class DigitFilter extends DocumentFilter {

        private final String DIGITS = "\\d+";
        private final String EMPTY = "";
        private String column;

        private DigitFilter(final String column) {
            this.column = column;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches(DIGITS)) {
                super.insertString(fb, offset, string, attr);
            } else if (string.matches(column)) {
                super.insertString(fb, offset, string, attr);
            } else if (string.matches(EMPTY)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            if (string.matches(DIGITS)) {
                super.replace(fb, offset, length, string, attrs);
            } else if (string.matches(column)) {
                super.replace(fb, offset, length, string, attrs);
            } else if (string.matches(EMPTY)) {
                super.replace(fb, offset, length, string, attrs);
            }
        }
    }

    private class JTextFieldSearchHintFocusListener implements FocusListener {

        private JTextField textFieldSearchColumn;
        private String column;

        private JTextFieldSearchHintFocusListener(final JTextField textFieldSearchColumn, final String column) {
            this.textFieldSearchColumn = textFieldSearchColumn;
            this.column = column;
        }

        @Override
        public void focusGained(FocusEvent focusEvent) {
            if (textFieldSearchColumn.getText().contains(column)) {
                textFieldSearchColumn.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            if (textFieldSearchColumn.getText().isEmpty()) {
                textFieldSearchColumn.setText(column);
            }
        }
    }

    /**
     * Creates a combo box, a contains (search by experience in years).
     *
     */
    private void createComboBoxSearch() {
        String[] columnElement = {
                "Стаж",
                "без опыта",
                "до 1 года",
                "1-3 года",
                "3-5 лет",
                "более 5 лет",
        };

        comboBoxSearchExperienceYear = new JComboBox<>(columnElement);

        comboBoxSearchExperienceYear.setSelectedItem("стаж");

        panelSearch.add(comboBoxSearchExperienceYear, new GridBagConstraints(LOCATION_SEARCH_EXPERIENCE_YEAR, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }

    /**
     * Creates a combo box, a sort the column.
     */
    private void createComboBoxSortColumn() {
        String[] columnElement = {
                "salary",
                "name",
                "age",
                "timeUpdate"
        };

        comboBoxSortColumn = new JComboBox<>(columnElement);

        comboBoxSortColumn.setSelectedItem("timeUpdate");

        panelSearch.add(comboBoxSortColumn, new GridBagConstraints(5, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }

    /**
     * Creates a combo box, a the type sort.
     */
    private void createComboBoxSortType() {
        String[] itemSort = {
                "ASC",
                "DESC"
        };

        comboBoxSortType = new JComboBox<>(itemSort);

        comboBoxSortType.setSelectedItem("DESC");

        panelSearch.add(comboBoxSortType, new GridBagConstraints(6, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }

    /**
     * Creates the button search that parses text field search.
     */
    private void createButtonSearch() {
        panelSearch.add(buttonSearch, new GridBagConstraints(7, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0)); // visual

        buttonSearch.setText("Search"); // set name
    }

    /**
     * Initialize for searching in database and turn thread.
     *
     * @param dataBase Get value from database, after searching.
     * @param parser For turn thread (if parser run, panelSearch wait).
     */
    void initialBack(DataBase dataBase, Parser parser) {
        this.dataBase = dataBase;
        this.parser = parser;

        listenerButtonSearch();
    }

    /**
     * Listener by button search.
     */
    private void listenerButtonSearch() {
        ActionListener listenerByButtonSearch = new ListenerByButtonSearch(dataBase);
        buttonSearch.addActionListener(listenerByButtonSearch); // add listener
    }

    /**
     * Add a listener to the search button.
     */
    public class ListenerByButtonSearch implements ActionListener {

        private DataBase dataBase;

        public ListenerByButtonSearch(DataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            searchPrepare();

            String sortColumn = (String) comboBoxSortColumn.getSelectedItem();
            String sortType = (String) comboBoxSortType.getSelectedItem();

            String searchCity = textFieldSearchCity.getText().trim().toLowerCase();
            searchCity = fieldSearchHint(searchCity, "city");

            String searchSalary = textFieldSearchSalary.getText().trim().toLowerCase();
            searchSalary = fieldSearchHint(searchSalary, "salary");

            String searchExperienceYear = (String) comboBoxSearchExperienceYear.getSelectedItem();

            String searchAge = textFieldSearchAge.getText().trim().toLowerCase();
            searchAge = fieldSearchHint(searchAge, "age");

            String searchAll = textFieldSearchAll.getText().trim().toLowerCase();
            searchAll = fieldSearchHint(searchAll, "all");

            ArrayList<String> listIdSorted = dataBase.listIdSortingByColumn(sortColumn, sortType);

            List<String> listIdContainsValues = dataBase.listIdContainsValues(
                    searchCity, searchSalary, searchExperienceYear, searchAge, searchAll);

            listIdSorted.retainAll(listIdContainsValues);

            contentSearch(listIdSorted);
        }

        /**
         * Makes search empty if the search word is a hint.
         *
         * @param searchValue The search word;
         * @param column hint for search word.
         *
         * @return String for searching.
         */
        private String fieldSearchHint(final String searchValue, final String column) {
            if (searchValue.equals(column)) {
                return "";
            }
            return searchValue;
        }

    }

    /**
     * Waiting while the other thread.
     */
    private void searchPrepare() {
        searching = true;

        try {
            prepareClosing();
            prepareVisibility();
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
                searching = false;

                windowApp.closingSend();

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
            while (windowApp.getPanelContent().isVisual()) {
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
     * Create new panel content.
     *
     * @param listIdSorted List id with founded resumes.
     */
    private void contentSearch(ArrayList<String> listIdSorted) {
        searching = false;
//        windowApp.panelContent(windowApp, listIdSorted);
        windowApp.getPanelContent().createPanelContent(listIdSorted);
    }

    private final Object monitor = new Object();

    /**
     * Open thread for the searching.
     */
    public void searchSend() {
        synchronized (monitor) {
            if (!windowApp.isClosing() && !windowApp.getPanelContent().isVisual() && !parser.isParser()) {
                searching = true;

                monitor.notify();
            }
        }
    }

    JPanel getPanelSearch() {
        return panelSearch;
    }

    public boolean isSearching() {
        return searching;
    }

}
