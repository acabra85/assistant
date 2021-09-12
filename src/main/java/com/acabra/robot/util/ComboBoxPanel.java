package com.acabra.robot.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

public class ComboBoxPanel extends JPanel {
    private final TimeUnit timeUnit;

    private ComboBoxPanel(String prompt) {
        super(new FlowLayout());
        String[] items = {"seconds", "minutes", "hours", "days"};
        TimeUnit[] timeUnits = {TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS};
        JComboBox<String> comboBox = new JComboBox<>(items);
        add(comboBox);
        JOptionPane joptionPane = new JOptionPane(this, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        boolean responseOK = configure(joptionPane, prompt, comboBox).equals(JOptionPane.OK_OPTION);
        this.timeUnit = responseOK ? timeUnits[comboBox.getSelectedIndex()] : timeUnits[0];
    }

    public static TimeUnit getTimeUnit(){
        return new ComboBoxPanel("Choose the time unit ... ").timeUnit;
    }

    private Object configure(JOptionPane jOptionPane, String prompt, JComponent field) {
        JDialog jDialog = promptDialog(prompt, jOptionPane, field);
        Object result = jOptionPane.getValue();
        jDialog.dispatchEvent(new WindowEvent(jDialog, WindowEvent.WINDOW_CLOSING));
        jDialog.dispose();
        return result;
    }

    private JDialog promptDialog(String message, JOptionPane jOptionPane, JComponent pwdField) {
        JDialog dialog = jOptionPane.createDialog(message);
        dialog.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                pwdField.requestFocusInWindow();
            }
        });
        dialog.setVisible(true);
        return dialog;
    }
}