package CollectedResumes.WindowApp;

import javax.swing.*;
import java.awt.*;

/**
 * Shows the panel that contains of elements the information.
 */
public class PanelInformation {

    /**
     * Creates the panel that contains of elements the information:
     *     - search by city;
     *     - search by salary;
     *     - search by work experienceYear;
     *     - search by age;
     *     - search by all (id, titleResume, name).
     */
    private JPanel panelInformation = new JPanel();

    /**
     * Priority size of the database.
     */
    private JLabel labelPrioritySize = new JLabel();

    /**
     * Size of the database.
     */
    private JLabel labelSize = new JLabel();

    /**
     * The number of finds elements.
     */
    private JLabel labelNumberFindsElement= new JLabel();

    /**
     * The number of displayed elements.
     */
    private JLabel labelNumberDisplayedElement= new JLabel();

    /**
     * Condition the program.
     */
    private JLabel labelCondition = new JLabel();

    /**
     * Progress bar the parser.
     */
    private JProgressBar progressBar = new JProgressBar();

    /**
     * Constructor.
     */
    PanelInformation() {
        initializePanelInformation();

        createLabelPrioritySize();
        createLabelSize();
        createLabelNumberFindsElements();
        createLabelNumberDisplayedElement();
        createPanelCondition();
    }

    /**
     * Initialize the panel information.
     */
    private void initializePanelInformation() {
        panelInformation.setLayout(new GridBagLayout()); // add to the panel GridBagLayout manager
    }

    /**
     * Creates a label, a priority database size.
     */
    private void createLabelPrioritySize() {
        panelInformation.add(labelPrioritySize,
                new GridBagConstraints(0, 0, 1, 1, 0, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 20), 0, 0));
    }

    /**
     * Update priority the size of the database.
     *
     * @param prioritySize Sets the priority database size.
     */
    public void updateLabelPrioritySize(final int prioritySize) {
        labelPrioritySize.setText("Set size DB: " + prioritySize);
    }

    /**
     * Creates a label, a database size.
     */
    private void createLabelSize() {
        panelInformation.add(labelSize,
                new GridBagConstraints(1, 0, 1, 1, 0, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 20), 0, 0));

        updateLabelSize(0);
    }

    /**
     * Update the size of the database.
     *
     * @param size The size of the database.
     */
    public void updateLabelSize(final int size) {
        labelSize.setText("Get size DB: " + size);
    }

    /**
     * Creates a label, a number finds of elements.
     */
    private void createLabelNumberFindsElements() {
        panelInformation.add(labelNumberFindsElement,
                new GridBagConstraints(2, 0, 1, 1, 0, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 20), 0, 0));
    }

    /**
     * Update the number finds of element.
     *
     * @param element Number of element.
     */
    void updateLabelNumberFindElement(final int element) {
        labelNumberFindsElement.setText("Found resumes: " + element);
    }

    /**
     * Creates a label, a number displayed of elements.
     */
    private void createLabelNumberDisplayedElement() {
        panelInformation.add(labelNumberDisplayedElement,
                new GridBagConstraints(3, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 1), 0, 0));
    }

    /**
     * Update the number displayed of element.
     *
     * @param element Number of element.
     */
    void updateLabelNumberDisplayedElement(final int element) {
        labelNumberDisplayedElement.setText("Displayed resumes: " + element);
    }

    /**
     * Creates a panel the condition.
     */
    private void createPanelCondition() {
        JPanel panelCondition = new JPanel();

        labelCondition.setVisible(false);
        panelCondition.add(labelCondition);

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        panelCondition.add(progressBar);

        panelInformation.add(panelCondition,
                new GridBagConstraints(3, 0, 1, 1, 0, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 10), 0, 0));
    }

    /**
     * Editor text the panel condition.
     *
     * @param text Text the condition.
     */
    public void editorTextPanelCondition(final String text) {
        labelCondition.setText(text);
    }

    /**
     * Visibility the panel condition.
     *
     * @param visibility Visibility the panel condition.
     */
    public void visibilityPanelCondition(final boolean visibility) {
        labelCondition.setVisible(visibility);
        progressBar.setVisible(visibility);
    }

    JPanel getPanelInformation() {
        return panelInformation;
    }

}
