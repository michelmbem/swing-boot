package org.addy.swingboot.ui;

import lombok.extern.slf4j.Slf4j;
import org.addy.simpletable.SimpleTable;
import org.addy.simpletable.SimpleTableModel;
import org.addy.simpletable.column.spec.ColumnSpec;
import org.addy.simpletable.column.spec.ColumnType;
import org.addy.swing.SimpleComboBoxModel;
import org.addy.swingboot.model.Actor;
import org.addy.swingboot.model.Category;
import org.addy.swingboot.model.Film;
import org.addy.swingboot.repository.ActorRepository;
import org.addy.swingboot.repository.CategoryRepository;
import org.addy.swingboot.repository.FilmRepository;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.util.List;

@Slf4j
@Component
public class MainWindow extends JFrame {
    private final CategoryRepository categoryRepository;
    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;

    private JList<Category> categoryList;
    private SimpleTable filmTable;
    private SimpleTable actorTable;
    private JEditorPane descriptionArea;

    private SimpleComboBoxModel<Category> categoryListModel;
    private SimpleTableModel filmTableModel;
    private SimpleTableModel actorTableModel;

    public MainWindow(
            CategoryRepository categoryRepository,
            FilmRepository filmRepository,
            ActorRepository actorRepository) {

        this.categoryRepository = categoryRepository;
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;

        initModels();
        initUI();
        loadData();
    }

    private static JPanel createFramePanel(String title) {
        final int borderWidth = 5;

        var panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(title),
                        BorderFactory.createEmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth)
                )
        ));

        return panel;
    }

    private static String wrapHtml(String text) {
        return String.format("<html><font face='sans-serif' size='4'>%s</font></html>", text);
    }

    private void initModels() {
        categoryListModel = new SimpleComboBoxModel<>();

        filmTableModel = new SimpleTableModel(Film.class, "title", "releaseYear", "language", "length", "rating", "specialFeatures");
        filmTableModel.setEditable(false);

        actorTableModel = new SimpleTableModel(Actor.class, "firstName", "lastName");
        actorTableModel.setEditable(false);
    }

    private void initUI() {
        var contentPane = new JPanel(new BorderLayout());

        var categoryPane = createFramePanel("Categories");
        contentPane.add(categoryPane, BorderLayout.LINE_START);

        categoryList = new JList<>(categoryListModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.addListSelectionListener(this::categorySelected);
        categoryPane.add(new JScrollPane(categoryList), BorderLayout.CENTER);

        var filmPane = createFramePanel("Films");

        filmTable = new SimpleTable(filmTableModel);
        filmTable.setColumnSpecs(
                new ColumnSpec(ColumnType.TEXT, "Title", 200),
                new ColumnSpec(ColumnType.NUMBER, "Year", 75),
                new ColumnSpec(ColumnType.TEXT, "Language", 125),
                new ColumnSpec(ColumnType.NUMBER, "Length", 75),
                new ColumnSpec(ColumnType.TEXT, "Rating", 75),
                new ColumnSpec(ColumnType.TEXT, "Special Features", 300));
        filmTable.getSelectionModel().addListSelectionListener(this::filmSelected);
        filmPane.add(new JScrollPane(filmTable), BorderLayout.CENTER);

        var filmInfoPane = new JPanel(new BorderLayout());

        var descriptionPane = createFramePanel("Description");
        filmInfoPane.add(descriptionPane, BorderLayout.CENTER);

        descriptionArea = new JEditorPane();
        descriptionArea.setEditorKit(new HTMLEditorKit());
        descriptionArea.setEditable(false);
        descriptionPane.add(new JScrollPane(descriptionArea));

        var actorPane = createFramePanel("Actors");
        filmInfoPane.add(actorPane, BorderLayout.LINE_END);

        actorTable = new SimpleTable(actorTableModel);
        actorTable.setColumnSpecs(
                new ColumnSpec(ColumnType.TEXT, "First Name", 200),
                new ColumnSpec(ColumnType.TEXT, "Last Name", 200));
        actorPane.add(new JScrollPane(actorTable), BorderLayout.CENTER);

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
        actorTableModel.setItemSource(List.of());
    }

    private void filmSelected(ListSelectionEvent e) {
        int filmIndex = filmTable.getSelectedRow();

        if (filmIndex < 0) {
            descriptionArea.setText("");
            actorTableModel.setItemSource(List.of());
        }
        else {
            filmIndex = filmTable.convertRowIndexToModel(filmIndex);
            var selectedFilm = (Film) filmTableModel.getRowAt(filmIndex);
            descriptionArea.setText(wrapHtml(selectedFilm.getDescription()));
            actorTableModel.setItemSource(actorRepository.findByFilm(selectedFilm));
        }
    }
}
