package CollectedResumes.DataBase;

import CollectedResumes.Validate.Validate;
import CollectedResumes.WindowApp.PanelInformation;
import CollectedResumes.WindowApp.WindowApp;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Work with the database. Create, connect, write, read...
 *
 *     - The ID of the resumes (id),
 *     - the URL of the resume (urlResume),
 *     - title resume (titleResume),
 *     - the salary of the worker (salary),
 *     - the experience in years of the worker (experienceYear),
 *     - the education of the worker (education),
 *     - the name of the worker (name),
 *     - the age of the worker (age),
 *     - the city of the worker (city),
 *     - the location of the old work (oldWorkLocation),
 *     - what worked on the last job (oldWorkWho),
 *     - the time work of the old work (oldWorkTime),
 *     - the specification of the old work (oldWorkSpecification),
 *     - the key skill of the worker (keySkill),
 *     - update time resume (timeUpdate).
 */
public class DataBase {

    private WindowApp windowApp;
    private Validate validate;

    /**
     * Running the database.
     */
    private boolean run = false;

    /**
     * The priority size of the database.
     * Default size: 5.
     */
    private int prioritySize = 5;

    /**
     * Current database size.
     */
    private int size = 0;

    /**
     * To connect to the database.
     */
    private Connection connection;

    /**
     * Query the database.
     */
    private Statement statement;

    /**
     * Get result.
     */
    private ResultSet resultSet;

    public DataBase(WindowApp windowApp) {
        this.windowApp = windowApp;
        validate = new Validate(this);
    }

    /**
     * Database startup.
     */
    public void run() {
        try {
            connection();
            create();
            size = size();
            run = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The database connection.
     */
    private void connection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:RESUME.s3db");
    }

    /**
     * Create database (if not exists).
     */
    private void create() throws SQLException {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS 'resume'" +
                "(" +
                "'id' INTEGER NOT NULL," +
                "'urlResume' TEXT NOT NULL," +
                "'titleResume' TEXT NOT NULL," +
                "'salary' INTEGER NOT NULL," +
                "'experienceYear' TEXT NOT NULL," +
                "'education' TEXT NOT NULL," +
                "'name' TEXT NOT NULL," +
                "'age' INTEGER NOT NULL," +
                "'city' TEXT NOT NULL," +
                "'oldWorkLocation' TEXT NOT NULL," +
                "'oldWorkWho' TEXT NOT NULL," +
                "'oldWorkTime' TEXT NOT NULL," +
                "'oldWorkSpecification' TEXT NOT NULL," +
                "'keySkill' TEXT NOT NULL," +
                "'timeUpdate' INTEGER NOT NULL" +
                ");");
    }

    /**
     * Calculates the size of the database.
     *
     * @return - database size
     */
    private int size() {
        int size = 0;
        try {
            resultSet = statement.executeQuery("SELECT id FROM 'resume';");
            while (resultSet.next()) {
                size++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        windowApp.getPanelInformation().updateLabelSize(size);
        return size;
    }

    /**
     * The write to the database.
     *
     * @param id The ID of the resumes;
     * @param urlResume the URL of the resume;
     * @param titleResume title resume;
     * @param salary the salary of the worker;
     * @param experienceYear the experience in years of the worker;
     * @param education the education of the worker;
     * @param name the name of the worker;
     * @param age the age of the worker;
     * @param city the city of the worker;
     * @param oldWorkLocation the location of the old work;
     * @param oldWorkWho what worked on the last job;
     * @param oldWorkTime the time work of the old work;
     * @param oldWorkSpecification the specification of the old work;
     * @param keySkill the key skill of the worker;
     * @param timeUpdate update time resume.
     */
    public void write(final int id, final String urlResume, final String titleResume, final int salary,
                             final String experienceYear, final String education, final String name, final int age,
                             final String city, final String oldWorkLocation, final String oldWorkWho,
                             final String oldWorkTime, final String oldWorkSpecification, final String keySkill,
                             final long timeUpdate) {
        validate.validatePrioritySize(size, prioritySize);

        validate.validateNull(id);
        validate.validateNull(urlResume);
        validate.validateNull(titleResume);
        validate.validateNull(salary);
        validate.validateNull(experienceYear);
        validate.validateNull(education);
        validate.validateNull(name);
        validate.validateNull(age);
        validate.validateNull(city);
        validate.validateNull(oldWorkLocation);
        validate.validateNull(oldWorkWho);
        validate.validateNull(oldWorkTime);
        validate.validateNull(oldWorkSpecification);
        validate.validateNull(keySkill);
        validate.validateNull(timeUpdate);

        validate.validateEmpty(urlResume);
        validate.validateEmpty(titleResume);
        validate.validateEmpty(experienceYear);
        validate.validateEmpty(education);
        validate.validateEmpty(name);
        validate.validateEmpty(city);
        validate.validateEmpty(oldWorkLocation);
        validate.validateEmpty(oldWorkWho);
        validate.validateEmpty(oldWorkTime);
        validate.validateEmpty(oldWorkSpecification);
        validate.validateEmpty(keySkill);

        validate.validatePresentId(id);

        try {
            statement.execute("INSERT INTO 'resume'" +
                    "(" +
                    "'id'," +
                    "'urlResume'," +
                    "'titleResume'," +
                    "'salary'," +
                    "'experienceYear'," +
                    "'education'," +
                    "'name'," +
                    "'age'," +
                    "'city'," +
                    "'oldWorkLocation'," +
                    "'oldWorkWho'," +
                    "'oldWorkTime'," +
                    "'oldWorkSpecification'," +
                    "'keySkill'," +
                    "'timeUpdate'" +
                    ")" +
                    "VALUES" +
                    "(" +
                    "'" + id + "'" + "," +
                    "'" + urlResume + "'" + "," +
                    "'" + titleResume + "'" + "," +
                    "'" + salary + "'" + "," +
                    "'" + experienceYear + "'" + "," +
                    "'" + education + "'" + "," +
                    "'" + name + "'" + "," +
                    "'" + age + "'" + "," +
                    "'" + city + "'" + "," +
                    "'" + oldWorkLocation + "'" + "," +
                    "'" + oldWorkWho + "'" + "," +
                    "'" + oldWorkTime + "'" + "," +
                    "'" + oldWorkSpecification + "'" + "," +
                    "'" + keySkill + "'" + "," +
                    "'" + timeUpdate + "'" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        size++;
        windowApp.getPanelInformation().updateLabelSize(size);
    }

    /**
     * Returns the value from the database.
     * Search column for value of searchByColumn.
     *
     * @param column The type (column) of the desired values;
     * @param searchByColumn search by type (column);
     * @param value search by value.
     *
     * @return The item found in the row, the column
     */
    public String read(final String column, final String searchByColumn, final String value) {
        validate.validateSizeDatabase();

        validate.validateNull(column);
        validate.validateNull(searchByColumn);
        validate.validateNull(value);

        validate.validateEmpty(column);
        validate.validateEmpty(searchByColumn);
        validate.validateEmpty(value);

        validate.validateColumn(column);
        validate.validateColumn(searchByColumn);

        validate.validateSearch(searchByColumn, value);

        String item = "";
        try {
            resultSet = statement.executeQuery("SELECT " + column + " FROM 'resume' " +
                    "WHERE " + searchByColumn + "=" + "'" + value + "'" +
                    ";");

            item = resultSet.getString(column);

            switch (column) {
                case "salary":
                    if (item.equals("0")) {
                        item = "договорная";
                    }
                    break;
                case "age":
                    if (item.equals("0")) {
                        item = "null";
                    }
                    break;
                case "timeUpdate":
                    DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                    item = dateFormat.format(Long.valueOf(item) * 1000L);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * Removes the data at given value from the database.
     *
     * @param column The type (column) of the desired values;
     * @param value search by value.
     */
    private void reamove(String column, String value) {
        validate.validateSizeDatabase();

        validate.validateNull(column);
        validate.validateNull(value);

        validate.validateEmpty(column);
        validate.validateEmpty(value);

        validate.validateColumn(column);

        validate.validateSearch(column, value);

        try {
            statement.execute("DELETE FROM 'resume' " +
                    "WHERE " + column + "=" + "'" + value + "'" +
                    ";");
            statement.execute("VACUUM;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        size--;
        windowApp.getPanelInformation().updateLabelSize(size);
    }

    /**
     * Sorts for the column and returns the list id with sorted values.
     *
     * @param sortColumn Sortable column;
     * @param sortType the type of sorting.
     *
     * @return List id of the sorted values.
     */
    public ArrayList<String> listIdSortingByColumn(final String sortColumn, final String sortType) {
        validate.validateNull(sortColumn);
        validate.validateNull(sortType);

        validate.validateEmpty(sortColumn);
        validate.validateEmpty(sortType);

        validate.validateColumn(sortColumn);

        validate.validateSort(sortType);

        ArrayList<String> listSorting = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id," + sortColumn + " FROM 'resume' " +
                    "ORDER BY " + sortColumn + " " + sortType + ";");
            while (resultSet.next()) {
                listSorting.add(resultSet.getString("id"));
//                if (listSorting.size() == 50) { // TODO
//                    break;
//                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listSorting;
    }

    /**
     * Looking for a duplicate the ID in database.
     *
     * @param value Search by value.
     *
     * @return The object present in database.
     */
    public boolean isEqual(final int value) {
        validate.validateNull(value);

        boolean present = false;
        try {
            resultSet = statement.executeQuery("SELECT id FROM 'resume';");
            while (resultSet.next()) {
                if (resultSet.getInt("id") == value) {
                    present = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return present;
    }

    /**
     * Looking for a duplicate object in database.
     *
     * @param column The type (column) of the desired values;
     * @param value search by value.
     *
     * @return The object present in database.
     */
    public boolean isEqual(final String column, final String value) {
        validate.validateNull(column);
        validate.validateNull(value);

        validate.validateEmpty(column);
        validate.validateEmpty(value);

        validate.validateColumn(column);

        boolean present = false;
        try {
            resultSet = statement.executeQuery("SELECT " + column + " FROM 'resume';");
            while (resultSet.next()) {
                if (resultSet.getString(column).equals(value)) {
                    present = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return present;
    }

    /**
     * Returns the ID of the list that contains the values.
     *
     * @param searchCity Search by city;
     * @param searchSalary search by salary;
     * @param searchExperienceYear search by experience in years;
     * @param searchAge search by age;
     * @param searchAll search by all parameters;
     *
     * @return List id.
     */
    public ArrayList<String> listIdContainsValues(final String searchCity, final String searchSalary,
                                                         final String searchExperienceYear, final String searchAge,
                                                         final String searchAll) {
        ArrayList<String> listIdContainsValues;

        listIdContainsValues = initializeListIdContainsValue(null, "city", searchCity);
        listIdContainsValues = initializeListIdContainsValue(listIdContainsValues, "salary", searchSalary);
        listIdContainsValues = initializeListIdContainsValue(listIdContainsValues, "experienceYear", searchExperienceYear);
        listIdContainsValues = initializeListIdContainsValue(listIdContainsValues, "age", searchAge);
        listIdContainsValues = initializeListIdContainsValue(listIdContainsValues, "all", searchAll);

        if (listIdContainsValues == null) {
            listIdContainsValues = new ArrayList<>();
        }

        return listIdContainsValues;
    }

    /**
     * Initialize the ID of the list that contains the values
     *
     * @param listIdContainsValues Contains the values.
     * @param column the type (column) of the desired values;
     * @param value search by value.
     *
     * @return List id that contains the values.
     */
    private ArrayList<String> initializeListIdContainsValue(ArrayList<String> listIdContainsValues,
                                                                   final String column, final String value) {
        if (!value.equals("")) {
            if (listIdContainsValues == null) {
                listIdContainsValues = new ArrayList<>();
                if (column.equals("all")) {
                    listIdContainsValues.addAll(listIdContainsValueByAll(column, value));
                } else {
                    listIdContainsValues.addAll(listIdContainsValueByColumn(column, value));
                }
            } else {
                if (column.equals("all")) {
                    listIdContainsValues.retainAll(listIdContainsValueByAll(column, value));
                } else {
                    listIdContainsValues.retainAll(listIdContainsValueByColumn(column, value));
                }
            }
        }
        return listIdContainsValues;
    }

    /**
     * Returns the ID of the list that contains the value by column.
     *
     * @param column The type (column) of the desired values;
     * @param value desired the item.
     *
     * @return List id.
     */
    private ArrayList<String> listIdContainsValueByColumn(final String column, final String value) {
        validate.validateNull(column);
        validate.validateNull(value);

        validate.validateEmpty(column);
        validate.validateEmpty(value);

        validate.validateColumn(column);

        ArrayList<String> listIdContainsValue = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id," + column + " FROM 'resume';");

            String resultColumn = "";
            String resultId = "";

            switch (column) {
                case "experienceYear":
                    switch (value) {
                        case "Стаж":
                            while (resultSet.next()) {
                                listIdContainsValue.add(resultSet.getString("id"));
                            }
                            break;
                        case "без опыта":
                            while (resultSet.next()) {
                                if (resultSet.getString(column).equals("без опыта")) {
                                    listIdContainsValue.add(resultSet.getString("id"));
                                }
                            }
                            break;
                        case "до 1 года":
                            while (resultSet.next()) {
                                if (resultSet.getString(column).equals("до 1 года")) {
                                    listIdContainsValue.add(resultSet.getString("id"));
                                }
                            }
                            break;
                        case "1-3 года":
                            while (resultSet.next()) {
                                if (resultSet.getString(column).equals("1-3 года")) {
                                    listIdContainsValue.add(resultSet.getString("id"));
                                }
                            }
                            break;
                        case "3-5 лет":
                            while (resultSet.next()) {
                                if (resultSet.getString(column).equals("3-5 лет")) {
                                    listIdContainsValue.add(resultSet.getString("id"));
                                }
                            }
                            break;
                        case "более 5 лет":
                            while (resultSet.next()) {
                                if (resultSet.getString(column).equals("более 5 лет")) {
                                    listIdContainsValue.add(resultSet.getString("id"));
                                }
                            }
                            break;
                    }
                    break;
                case "salary":
                    while (resultSet.next()) {
                        resultColumn = resultSet.getString(column);
                        resultId = resultSet.getString("id");
                        if (resultColumn.equals("договорная") || resultColumn.equals("Salary")) {
                            listIdContainsValue.add(resultId);
                        } else if (resultSet.getInt(column) <= Integer.valueOf(value)) {
                            listIdContainsValue.add(resultSet.getString("id"));
                        }
                    }
                    break;
                case "age":
                    while (resultSet.next()) {
                        if (resultSet.getString(column).equals("null")) {
                            listIdContainsValue.add(resultSet.getString("id"));
                        } else {
                            if (resultSet.getInt(column) <= Integer.valueOf(value)) {
                                listIdContainsValue.add(resultSet.getString("id"));
                            }
                        }
                    }
                    break;
                default:
                    while (resultSet.next()) {
                        if (resultSet.getString(column).toLowerCase().contains(value.toLowerCase())) {
                            listIdContainsValue.add(resultSet.getString("id"));
                        }
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listIdContainsValue;
    }

    /**
     * Returns the ID of the list that contains the value by all (id, titleResume, name).
     *
     * @param column The type (column) of the desired values;
     * @param value desired the item.
     *
     * @return List id.
     */
    private ArrayList<String> listIdContainsValueByAll(final String column, final String value) {
        validate.validateNull(column);
        validate.validateNull(value);

        validate.validateEmpty(column);
        validate.validateEmpty(value);

        ArrayList<String> listIdContainsValueByAll = new ArrayList<>();
        listIdContainsValueByAll.addAll(listIdContainsValueByColumn("id", value));
        listIdContainsValueByAll.addAll(listIdContainsValueByColumn("titleResume", value));
        listIdContainsValueByAll.addAll(listIdContainsValueByColumn("name", value));
        return listIdContainsValueByAll;
    }

    /**
     * Close the database.
     */
    public void close() {
        try {
            resultSet.close();
            statement.close();
            connection.close();
            run = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isRun() {
        return run;
    }

    public int getSize() {
        return size;
    }

    public int getPrioritySize() {
        return prioritySize;
    }

    public void setPrioritySize(final int prioritySize) {
        this.prioritySize = prioritySize;
        windowApp.getPanelInformation().updateLabelPrioritySize(prioritySize);
    }

}
