package org.addy.swingboot.ui;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.simpletable.SimpleTable;
import org.addy.simpletable.SimpleTableModel;
import org.addy.simpletable.column.spec.CellFormat;
import org.addy.simpletable.column.spec.ColumnSpec;
import org.addy.simpletable.column.spec.ColumnType;
import org.addy.swing.JPictureBox;
import org.addy.swing.SimpleComboBoxModel;
import org.addy.swing.SizeMode;
import org.addy.swingboot.model.Actor;
import org.addy.swingboot.model.Category;
import org.addy.swingboot.model.Film;
import org.addy.swingboot.repository.ActorRepository;
import org.addy.swingboot.repository.CategoryRepository;
import org.addy.swingboot.repository.FilmRepository;
import org.addy.swingboot.service.HtmlWrapper;
import org.addy.swingboot.service.ImageLoader;
import org.addy.swingboot.service.WidgetFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MainWindow extends JFrame {
    public static final Dimension SIDE_WIDGET_SIZE = new Dimension(200, 0);

    private final CategoryRepository categoryRepository;
    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final WidgetFactory widgetFactory;
    private final ImageLoader imageLoader;
    private final HtmlWrapper htmlWrapper;

    private JList<Category> categoryList;
    private SimpleTable filmTable;
    private JPictureBox posterBox;
    private JEditorPane descriptionArea;
    private JList<Actor> actorList;

    private SimpleComboBoxModel<Category> categoryListModel;
    private SimpleTableModel filmTableModel;
    private SimpleComboBoxModel<Actor> actorListModel;

    @PostConstruct
    private void initialize() {
        initModels();
        initUI();
        loadData();
    }

    private void initModels() {
        categoryListModel = new SimpleComboBoxModel<>();

        filmTableModel = new SimpleTableModel(Film.class, "title", "releaseYear", "language", "length", "replacementCost", "rating", "specialFeatures");
        filmTableModel.setEditable(false);

        actorListModel = new SimpleComboBoxModel<>();
    }

    private void initUI() {
        var contentPane = new JPanel(new BorderLayout());

        var categoryPane = widgetFactory.createFramePanel("Categories");
        contentPane.add(categoryPane, BorderLayout.LINE_START);

        categoryList = new JList<>(categoryListModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setCellRenderer(widgetFactory.createListCellRenderer());
        categoryList.addListSelectionListener(this::categorySelected);
        categoryPane.add(new JScrollPane(categoryList), BorderLayout.CENTER);

        var filmPane = widgetFactory.createFramePanel("Films");

        filmTable = new SimpleTable(filmTableModel);
        filmTable.setColumnSpecs(
                new ColumnSpec(ColumnType.TEXT, "Title", 200),
                new ColumnSpec(ColumnType.NUMBER, "Year", 60, "###0"),
                new ColumnSpec(ColumnType.TEXT, "Language", 80),
                new ColumnSpec(ColumnType.NUMBER, "Length", 60, CellFormat.DEFAULT, CellFormat.LINE_END, "##0 'min'"),
                new ColumnSpec(ColumnType.NUMBER, "Replacement", 80, CellFormat.DEFAULT, CellFormat.LINE_END, "##0.00 '$'"),
                new ColumnSpec(ColumnType.TEXT, "Rating", 60),
                new ColumnSpec(ColumnType.TEXT, "Special features", 300));
        filmTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        filmTable.getSelectionModel().addListSelectionListener(this::filmSelected);
        filmPane.add(new JScrollPane(filmTable), BorderLayout.CENTER);

        var filmInfoPane = new JPanel(new BorderLayout());

        var posterPane = widgetFactory.createFramePanel("Poster");
        filmInfoPane.add(posterPane, BorderLayout.LINE_START);

        posterBox = new JPictureBox(null, SizeMode.CONTAIN);
        posterBox.setPreferredSize(SIDE_WIDGET_SIZE);
        posterBox.setToolTipText("Double-click the image to display in full size");
        posterBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                posterClicked(e);
            }
        });
        posterPane.add(posterBox, BorderLayout.CENTER);

        var descriptionPane = widgetFactory.createFramePanel("Description");
        filmInfoPane.add(descriptionPane, BorderLayout.CENTER);

        descriptionArea = new JEditorPane();
        descriptionArea.setEditorKit(new HTMLEditorKit());
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        descriptionPane.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        var actorPane = widgetFactory.createFramePanel("Actors");
        filmInfoPane.add(actorPane, BorderLayout.LINE_END);

        actorList = new JList<>(actorListModel);
        actorList.setPreferredSize(SIDE_WIDGET_SIZE);
        actorList.setCellRenderer(widgetFactory.createListCellRenderer());
        actorPane.add(new JScrollPane(actorList), BorderLayout.CENTER);

        var detailPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filmPane, filmInfoPane);
        detailPane.setDividerLocation(450);
        detailPane.setBorder(null);
        contentPane.add(detailPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setSize(1000, 700);
        setTitle("Spring-Boot and Swing Demo");
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("spring-boot-logo.png")).getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void loadData() {
        java.util.List<Category> allCategories = categoryRepository.findAll();
        categoryListModel.setItems(allCategories);
        if (!allCategories.isEmpty()) categoryList.setSelectedIndex(0);
    }

    private void categorySelected(ListSelectionEvent e) {
        Category selectedCategory = categoryList.getSelectedValue();
        List<Film> categoryFilms = filmRepository.findByCategory(selectedCategory);
        filmTableModel.setItemSource(categoryFilms);
        filmSelected(null);
    }

    private void filmSelected(ListSelectionEvent e) {
        int filmIndex = filmTable.getSelectedRow();

        if (filmIndex < 0) {
            posterBox.setImage(null);
            descriptionArea.setText("");
            actorListModel.setItems(List.of());
        } else {
            filmIndex = filmTable.convertRowIndexToModel(filmIndex);
            var selectedFilm = (Film) filmTableModel.getRowAt(filmIndex);
            posterBox.setImage(imageLoader.loadImage(selectedFilm.getPoster()));
            posterBox.getParent().doLayout();
            descriptionArea.setText(htmlWrapper.wrap(selectedFilm.getDescription()));
            actorListModel.setItems(actorRepository.findByFilm(selectedFilm));
        }
    }

    private void posterClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() != 2 || posterBox.getImage() == null)
            return;

        int filmIndex = filmTable.convertRowIndexToModel(filmTable.getSelectedRow());
        var selectedFilm = (Film) filmTableModel.getRowAt(filmIndex);
        String dialogTitle = String.format("%s - Movie Poster", selectedFilm.getTitle());

        var dialog = widgetFactory.createImageDialog(this, dialogTitle, posterBox.getImage());
        dialog.setVisible(true);
    }
}
