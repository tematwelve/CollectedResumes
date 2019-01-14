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
 * Creates the panel that contains of elements the search:
 *     - search by city;
 *     - search by salary;
 *     - search by work experienceYear;
 *     - search by age;
 *     - search by all (id, titleResume, name).
 */
public class PanelSearch {

    private WindowApp windowApp;
    private DataBase dataBase;
    private Parser parser;

    private JPanel panelSearch;

    private JTextField textFieldSearchCity;
    private JTextField textFieldSearchSalary;
    private JComboBox<String> comboBoxSearchExperienceYear;
    private JTextField textFieldSearchAge;
    private JTextField textFieldSearchAll;

    private JComboBox<String> comboBoxSortColumn;
    private JComboBox<String> comboBoxSortType;

    private JButton buttonSearch;

    private static final int LOCATION_SEARCH_CITY = 0;
    private static final int LOCATION_SEARCH_SALARY = 1;
    private static final int LOCATION_SEARCH_EXPERIENCE_YEAR = 2;
    private static final int LOCATION_SEARCH_AGE = 3;
    private static final int LOCATION_SEARCH_ALL = 4;
    private static final int LOCATION_SORT_COLUMN = 5;
    private static final int LOCATION_SORT_TYPE = 6;
    private static final int LOCATION_BUTTON_SEARCH = 7;

    private boolean searching;

    /**
     * To control access to an object.
     */
    private static final Object MONITOR = new Object();

    PanelSearch(final WindowApp windowApp) {
        this.windowApp = windowApp;

        initPanelSearch();

        createTextFieldSearch();
        createComboBoxSortColumn();
        createComboBoxSortType();
        createButtonSearch();
    }

    private void initPanelSearch() {
        panelSearch = new JPanel();
        panelSearch.setLayout(new GridBagLayout());
    }

    private void createTextFieldSearch() {
        textFieldSearchCity = new JTextField();
        createTextFieldSearch("City", textFieldSearchCity, LOCATION_SEARCH_CITY, false);
        textFieldSearchSalary = new JTextField();
        createTextFieldSearch("Salary", textFieldSearchSalary, LOCATION_SEARCH_SALARY, true);
        createComboBoxSearch();
        textFieldSearchAge = new JTextField();
        createTextFieldSearch("Age", textFieldSearchAge, LOCATION_SEARCH_AGE, true);
        textFieldSearchAll = new JTextField();
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
        textFieldSearchColumn.setColumns(6);
        textFieldSearchColumn.setText(column);
        textFieldSearchColumn.addFocusListener(new JTextFieldSearchHintFocusListener(textFieldSearchColumn, column));

        if (digit) {
            PlainDocument doc = (PlainDocument) textFieldSearchColumn.getDocument();
            doc.setDocumentFilter(new DigitFilter(column));
        }

        JPanel panelSearchByColumn = new JPanel();
        panelSearchByColumn.add(textFieldSearchColumn);
        panelSearch.add(panelSearchByColumn, new GridBagConstraints(location, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
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

    private void createComboBoxSearch() {
        String[] columnElement = {
                "стаж",
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

    private void createComboBoxSortColumn() {
        String[] columnElement = {
                "salary",
                "name",
                "age",
                "timeUpdate"
        };

        comboBoxSortColumn = new JComboBox<>(columnElement);
        comboBoxSortColumn.setSelectedItem("timeUpdate");

        panelSearch.add(comboBoxSortColumn, new GridBagConstraints(LOCATION_SORT_COLUMN, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }

    private void createComboBoxSortType() {
        String[] itemSort = {
                "ASC",
                "DESC"
        };

        comboBoxSortType = new JComboBox<>(itemSort);
        comboBoxSortType.setSelectedItem("DESC");

        panelSearch.add(comboBoxSortType, new GridBagConstraints(LOCATION_SORT_TYPE, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }

    private void createButtonSearch() {
        buttonSearch = new JButton();
        buttonSearch.setText("Search");
        panelSearch.add(buttonSearch, new GridBagConstraints(LOCATION_BUTTON_SEARCH, 0, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
    }

    void initBack(final DataBase dataBase, final Parser parser) {
        this.dataBase = dataBase;
        this.parser = parser;
        listenerButtonSearch();
    }

    private void listenerButtonSearch() {
        buttonSearch.addActionListener(new ListenerButtonSearch(dataBase));
    }

    private class ListenerButtonSearch implements ActionListener {

        private DataBase dataBase;

        ListenerButtonSearch(DataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            prepareSearch();

            String sortColumn = (String) comboBoxSortColumn.getSelectedItem();
            String sortType = (String) comboBoxSortType.getSelectedItem();

            String searchCity = textFieldSearchCity.getText().trim().toLowerCase();
            searchCity = fieldSearchHint(searchCity, "city");

            String searchSalary = textFieldSearchSalary.getText().trim().toLowerCase();
            searchSalary = fieldSearchHint(searchSalary, "salary");

            String searchExperienceYear = (String) comboBoxSearchExperienceYear.getSelectedItem();
            if (searchExperienceYear != null) {
                searchExperienceYear = searchExperienceYear.equals("стаж") ? "" : searchExperienceYear;
            }

            String searchAge = textFieldSearchAge.getText().trim().toLowerCase();
            searchAge = fieldSearchHint(searchAge, "age");

            String searchAll = textFieldSearchAll.getText().trim().toLowerCase();
            searchAll = fieldSearchHint(searchAll, "all");

            ArrayList<String> listIdSorted = dataBase.listIdSortingByColumn(sortColumn, sortType);

            if (!(searchCity + searchSalary + searchExperienceYear + searchAge + searchAll).isEmpty()) {
                List<String> listIdContainsValues = dataBase.listIdContainsValues(
                        searchCity, searchSalary, searchExperienceYear, searchAge, searchAll);

                listIdSorted.retainAll(listIdContainsValues);
            }

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

        /**
         * Create new panel content.
         *
         * @param listIdSorted List id with founded resumes.
         */
        private void contentSearch(final ArrayList<String> listIdSorted) {
            searching = false;
            windowApp.getPanelContent().updatePanelContent(listIdSorted);
        }

    }

    /**
     * Waiting while the other thread.
     */
    private void prepareSearch() {
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
        synchronized (MONITOR) {
            while (windowApp.isClosing()) {
                searching = false;
                windowApp.closingSend();
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
            while (windowApp.getPanelContent().isShow()) {
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
     * Open thread for the searching.
     */
    public void wake() {
        synchronized (MONITOR) {
            if (!windowApp.isClosing() && !windowApp.getPanelContent().isShow() && !parser.isParser()) {
                searching = true;
                MONITOR.notify();
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
