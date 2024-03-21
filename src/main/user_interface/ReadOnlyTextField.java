package main.user_interface;

import javax.swing.*;
import java.awt.Color;
import java.math.BigDecimal;

class ReadOnlyTextField extends JTextField {
    public ReadOnlyTextField(String text) {
        super(text);
        setEditable(false);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        setBackground(editable ? Color.WHITE : Color.LIGHT_GRAY);
    }
}

class ReadOnlyFormattedTextField extends JFormattedTextField {
    public ReadOnlyFormattedTextField(String text) {
        super(text);
        setEditable(false);
    }

    public ReadOnlyFormattedTextField(BigDecimal text) {
        super(text);
        setEditable(false);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        setBackground(editable ? Color.WHITE : Color.LIGHT_GRAY);
    }
}
