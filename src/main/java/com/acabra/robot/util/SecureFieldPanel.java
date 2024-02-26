package com.acabra.robot.util;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Optional;

public final class SecureFieldPanel extends JPanel {
    public final Optional<String> password;

    public SecureFieldPanel(String prompt) {
        super(new FlowLayout());
        JPasswordField pwdField = new JPasswordField(12);
        add(pwdField);
        JOptionPane joptionPane = new JOptionPane(this, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        boolean responseOK = configure(joptionPane, prompt, pwdField).equals(JOptionPane.OK_OPTION);
        this.password = responseOK
                ? Optional.of(String.valueOf(Objects.requireNonNull(pwdField.getPassword())))
                : Optional.empty();
    }

    private Object configure(JOptionPane jOptionPane, String prompt, JComponent pwdField) {
        JDialog jDialog = promptDialog(prompt, jOptionPane, pwdField);
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
