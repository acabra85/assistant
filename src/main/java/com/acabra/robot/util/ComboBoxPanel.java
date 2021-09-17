package com.acabra.robot.util;

import com.acabra.robot.bot.ExecutionType;
import com.acabra.robot.bot.OnFinishAction;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ComboBoxPanel<T> extends JPanel {
    private final T response;

    private ComboBoxPanel(String prompt, String[] options, T[] items, T defValue) {
        super(new FlowLayout());
        JComboBox<String> comboBox = new JComboBox<>(options);
        add(comboBox);
        JOptionPane joptionPane = new JOptionPane(this, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        boolean responseOK = configure(joptionPane, prompt, comboBox).equals(JOptionPane.OK_OPTION);
        this.response = responseOK ? items[comboBox.getSelectedIndex()] : defValue;
    }

    public static TimeUnit getTimeUnit(String title, TimeUnit defValue){
        TimeUnit[] items = {TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS};
        String[] options = buildOptionsFromItems(items);
        return new ComboBoxPanel<>(title, options, items, defValue).response;
    }

    public static OnFinishAction getFinishAction(String title, OnFinishAction defValue) {
        OnFinishAction[] items = OnFinishAction.values();
        String[] options = buildOptionsFromItems(items);
        return new ComboBoxPanel<>(title, options, items, defValue).response;
    }

    private static String[] buildOptionsFromItems(final Object[] items) {
        final String[] opts = new String[items.length];
        IntStream.range(0, items.length).forEach(i -> opts[i] = items[i].toString().toLowerCase(Locale.ROOT));
        return opts;
    }

    public static long getRunningTime(TimeUnit timeUnit, int defaultValue) {
        String title = String.format("How much time in %s ...", timeUnit.toString().toLowerCase(Locale.ROOT));
        Integer[] items = buildIntegersToRange(2000);
        String[] options = buildOptionsFromItems(items);
        return new ComboBoxPanel<>(title, options, items, defaultValue).response;

    }

    private static Integer[] buildIntegersToRange(int range) {
        Integer[] items = new Integer[range];
        IntStream.range(0, items.length).forEach(i -> items[i] = i + 1);
        return items;
    }

    public static ExecutionType getExecutionType(String title, ExecutionType defValue) {
        ExecutionType[] items = ExecutionType.values();
        String[] options = buildOptionsFromItems(items);
        return new ComboBoxPanel<>(title, options, items, defValue).response;
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