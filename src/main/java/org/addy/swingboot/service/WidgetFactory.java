package org.addy.swingboot.service;

import lombok.extern.slf4j.Slf4j;
import org.addy.swing.JPictureBox;
import org.addy.swing.SizeMode;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

@Service
@Slf4j
public class WidgetFactory {
    public static final Border MARGIN = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    public JPanel createFramePanel(String title) {
        var panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                MARGIN,
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(title),
                        MARGIN
                )
        ));

        return panel;
    }

    public JDialog createImageDialog(Window owner, String title, Image image) {
        var pictureBox = new JPictureBox(image, SizeMode.AUTO);
        pictureBox.setBorder(MARGIN);

        var dialog = new JDialog(owner);
        dialog.setContentPane(new JScrollPane(pictureBox));
        dialog.setTitle(title);
        dialog.setSize(520, 800);
        dialog.setLocationRelativeTo(owner);
        dialog.setModal(true);

        return dialog;
    }

    @SuppressWarnings("unchecked")
    public <T> ListCellRenderer<T> createListCellRenderer() {
        return (ListCellRenderer<T>) new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                           boolean isSelected, boolean cellHasFocus) {
                var component = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                component.setBorder(BorderFactory.createCompoundBorder(
                        component.getBorder(),
                        BorderFactory.createEmptyBorder(1, 2, 1, 2)
                ));

                return component;
            }
        };
    }
}
