package CollectedResumes.WindowApp;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the panel on which located the information about the database.
 */
public class PanelInformation {

    private JPanel panelInformation;

    private JLabel labelPrioritySize;
    private JLabel labelSize;
    private JLabel labelNumberFindsElement;
    private JLabel labelNumberDisplayedElement;

    private JPanel panelCondition;
    private JLabel labelCondition;
    private JProgressBar progressBarCondition; // progress bar the parser

    PanelInformation() {
        createPanelInformation();

        createLabelPrioritySize();
        createLabelSize();
        createLabelNumberFindsElements();
        createLabelNumberDisplayedElement();

        createPanelCondition();
    }

    private void createPanelInformation() {
        panelInformation = new JPanel();
        panelInformation.setLayout(new GridBagLayout());
    }

    private void createLabelPrioritySize() {
        labelPrioritySize = new JLabel();
        panelInformation.add(labelPrioritySize,
                new GridBagConstraints(0, 0, 1, 1, 0, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 20), 0, 0));
    }

    public void updateLabelPrioritySize(final int prioritySize) {
        labelPrioritySize.setText("Priority size database: " + prioritySize);
    }

    private void createLabelSize() {
        labelSize = new JLabel();
        panelInformation.add(labelSize,
                new GridBagConstraints(1, 0, 1, 1, 0, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 20), 0, 0));
    }

    public void updateLabelSize(final int size) {
        labelSize.setText("Size database: " + size);
    }

    private void createLabelNumberFindsElements() {
        labelNumberFindsElement = new JLabel();
        panelInformation.add(labelNumberFindsElement,
                new GridBagConstraints(2, 0, 1, 1, 0, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 20), 0, 0));
    }

    void updateLabelNumberFindElement(final int element) {
        labelNumberFindsElement.setText("Found resumes: " + element);
    }

    private void createLabelNumberDisplayedElement() {
        labelNumberDisplayedElement = new JLabel();
        panelInformation.add(labelNumberDisplayedElement,
                new GridBagConstraints(3, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 1), 0, 0));
    }

    void updateLabelNumberDisplayedElement(final int element) {
        labelNumberDisplayedElement.setText("Displayed resumes: " + element);
    }

    private void createPanelCondition() {
        panelCondition = new JPanel();
        createLabelCondition();
        createProgressBarCondition();
        panelInformation.add(panelCondition,
                new GridBagConstraints(3, 0, 1, 1, 0, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(1, 1, 1, 10), 0, 0));
    }

    private void createLabelCondition() {
        labelCondition = new JLabel();
        labelCondition.setText("Parsing. Please wait...");
        labelCondition.setVisible(false);
        panelCondition.add(labelCondition);
    }

    private void createProgressBarCondition() {
        progressBarCondition = new JProgressBar();
        progressBarCondition.setIndeterminate(true);
        progressBarCondition.setVisible(false);
        panelCondition.add(progressBarCondition);
    }

    public void visibilityPanelCondition(final boolean visibility) {
        labelCondition.setVisible(visibility);
        progressBarCondition.setVisible(visibility);
    }

    JPanel getPanelInformation() {
        return panelInformation;
    }

}
