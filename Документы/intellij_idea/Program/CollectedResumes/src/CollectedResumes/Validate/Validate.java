package CollectedResumes.Validate;

import CollectedResumes.DataBase.DataBase;

/**
 * Parameter validation.
 */
public class Validate {

    private DataBase dataBase;

    public Validate(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * Check for null.
     *
     * @param value Check the value.
     */
    public void validateNull(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null.");
        }
    }

    /**
     * Check for null.
     *
     * @param value Check the value.
     */
    public void validateNull(final Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null.");
        }
    }

    /**
     * Check for null.
     *
     * @param value Check the value.
     */
    public void validateNull(final Long value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null.");
        }
    }

    /**
     * Check for empty.
     *
     * @param value Check the value.
     */
    public void validateEmpty(final String value) {
        if (value.equals("") || value.length() == 0) {
            throw new IllegalArgumentException("Value must not be empty.");
        }
    }

    /**
     * Check for priority size.
     *
     * @param size Size of database;
     * @param prioritySize priority size of database.
     */
    public void validatePrioritySize(final int size, final int prioritySize) {
        if (size >= prioritySize) {
            throw new IllegalArgumentException("Database size must not be greater than database priority size.");
        }
    }

    /**
     * Check for present ID.
     *
     * @param id ID resume.
     */
    public void validatePresentId(final int id) {
        if (dataBase.isEqual(id)) {
            throw new IllegalArgumentException("This ID is already in database.");
        }
    }

    /**
     * Check for present of value in the database at the column.
     *
     * @param searchByColumn Search by type (column);
     * @param value search by value.
     */
    public void validateSearch(final String searchByColumn, final String value) {
        if (!dataBase.isEqual(searchByColumn, value)) {
            throw new IllegalArgumentException("The value is not found in the database.");
        }
    }

    /**
     * Check for a validated the column in the database.
     *
     * @param column The column in the database.
     */
    public void validateColumn(final String column) {
        if (!(column.equals("id")
                || column.equals("urlResume")
                || column.equals("titleResume")
                || column.equals("salary")
                || column.equals("experienceYear")
                || column.equals("education")
                || column.equals("name")
                || column.equals("age")
                || column.equals("city")
                || column.equals("oldWorkLocation")
                || column.equals("oldWorkWho")
                || column.equals("oldWorkTime")
                || column.equals("oldWorkSpecification")
                || column.equals("keySkill")
                || column.equals("timeUpdate"))) {
            throw new IllegalArgumentException("The column must be equal to:\n" +
                    "- id\n" +
                    "- urlResume\n" +
                    "- titleResume\n" +
                    "- salary\n" +
                    "- experienceYear\n" +
                    "- education\n" +
                    "- name\n" +
                    "- age\n" +
                    "- city\n" +
                    "- oldWorkLocation\n" +
                    "- oldWorkWho\n" +
                    "- oldWorkTime\n" +
                    "- oldWorkSpecification\n" +
                    "- keySkill\n" +
                    "- timeUpdate");
        }
    }

    /**
     * Check for a validate the sort.
     *
     * @param sort The sort in the database.
     */
    public void validateSort(final String sort) {
        if (!(sort.equals("ASC") || sort.equals("DESC"))) {
            throw new IllegalArgumentException("Sort must be equal:\n" +
                    "- ASC\n" +
                    "- DESC");
        }
    }

    /**
     * Check for a empty the database.
     */
    public void validateSizeDatabase() {
        if (dataBase.getSize() == 0) {
            throw new IllegalArgumentException("Database must not be empty.");
        }
    }

    /**
     * Check for empty.
     *
     * @param value Check the value.
     *
     * @return True if the value is empty.
     */
    public boolean isValidateEmpty(final String value) {
        return value.equals("") || value.length() == 0;
    }

    public void validateWriteSalary() {

    }

}
