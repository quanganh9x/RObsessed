package com.quanganh9x.rss;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.sdecima.rssreader.*;
import org.sdecima.rssreader.stores.*;


import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MainController extends XMLMainController {

    @FXML private AnchorPane UserManager;
    @FXML private Tab ListRss;
    @FXML private AnchorPane ListRssExpanded;
    @FXML private ToolBar tools;
    @FXML private Label fxCopy;
    private ArrayList<RSSItem> list;
    public int pageOption = 4;
    private boolean allowImage = false;
    private Scene loginScene;

    public void initialize() {
        setPath();
        read();
        start();
        renderList();
    }
    public void fallbackLoginScene(Scene scene) {
        this.loginScene = scene;
    }
    private void setWindow(Number chosen) {
        Stage current = (Stage)fxCopy.getScene().getWindow();
        int choice = chosen.intValue();
        switch (choice) {
            case 0:
                current.setWidth(640);
                current.setHeight(480);
                break;
            case 1:
                current.setWidth(1280);
                current.setHeight(800);
                break;
            case 2:
                current.setWidth(1920);
                current.setHeight(1080);
                break;
        }
    }
    private boolean preAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are thee sure ?");
        alert.setContentText("Changing resolution and pageOptions can lead to some issues with the screen");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) return true;
        return false;
    }
    private void start() {
        Label username = new Label("Login as, " + root.getChild("user").getValue());
        Button logoutBtn = new Button("logout");
        Button refresh = new Button("refresh");
        refresh.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                read();
                renderList();
            }
        });
        logoutBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage current = (Stage)fxCopy.getScene().getWindow();
                current.setScene(loginScene);
            }
        });
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("640x480", "1280x800", "1920x1080"));
        cb.setTooltip(new Tooltip("Select resolution to fit your device. Default is 1280x800 (if only I have a mere-stunning monitor :/)"));
        cb.getSelectionModel().select(1);
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (preAlert()) setWindow(newValue);
            }
        });
        AnchorPane.setTopAnchor(username, 20.0);
        AnchorPane.setBottomAnchor(username, 20.0);
        AnchorPane.setLeftAnchor(username, 40.0);
        username.setId("userCssProps");

        AnchorPane.setTopAnchor(logoutBtn, 20.0);
        AnchorPane.setBottomAnchor(logoutBtn, 20.0);
        AnchorPane.setRightAnchor(logoutBtn, 20.0);

        AnchorPane.setTopAnchor(refresh, 20.0);
        AnchorPane.setBottomAnchor(refresh, 20.0);
        AnchorPane.setRightAnchor(refresh, 100.0);

        fxCopy.setText("Copyright 2018 whocares? Beta v1.0b1");
        fxCopy.setTextAlignment(TextAlignment.CENTER);
        fxCopy.setFont(new Font(20));
        tools.getItems().addAll(cb);
        UserManager.getChildren().addAll(username, logoutBtn, refresh);
    }
    public void renderList() {
        ListRss.setContent(createNodeListRss());
    }
    private Node createNodeListRss() {
        List<Element> sitesName = sitesName();
        final ListView<String> paneListView = new ListView<>();
        for (int i = 0; i < sitesName.size(); i++) {
            paneListView.getItems().add(getChildName(sitesName.get(i), "name"));
        }
        paneListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                renderRss(paneListView.getSelectionModel().getSelectedItem());
            }
        });
        return paneListView;
    }

    public void addRss() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add RSS");
        dialog.setHeaderText("Input url & name. These elements must be unique");
        dialog.setContentText("Havent add checks for names & URLs, so beware of exceptions be thrown - red lines of omg");

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        TextField url = new TextField();
        TextField name = new TextField();
        ButtonType okay = new ButtonType("OK!", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(okay, ButtonType.CANCEL);
        grid.add(new Label("URL: "), 0, 0);
        grid.add(url, 1, 0);
        grid.add(new Label("Name: "), 0, 1);
        grid.add(name, 1, 1);
        Node ok = dialog.getDialogPane().lookupButton(okay);
        ok.setDisable(true);
        url.textProperty().addListener((observable, oldValue, newValue) -> ok.setDisable(newValue.trim().isEmpty()));
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okay) {
                List<Element> sitesName = sitesName();
                for (int i=0; i< sitesName.size(); i++)
                    if (lookup("name", name.getText()) != -1 || lookup("url", url.getText()) != -1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Name or URL has been found. Please use unique values");
                        alert.showAndWait();
                        return null;
                    }
                return new Pair<>(name.getText(), url.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(data -> addRssStep2(data.getKey(), data.getValue()));
    }
    private void addRssStep2(String name, String url) {
        List<Element> dataNames = trace(Arrays.asList("data"));
        Element sites = new Element("sites");
        Element blockName = new Element("name");
        blockName.setText(name);
        Element blockURL = new Element("url");
        blockURL.setText(url);
        sites.addContent(blockName);
        sites.addContent(blockURL);
        dataNames.get(0).addContent(sites);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            FileOutputStream io = new FileOutputStream(path);
            xmlOutput.output(document, io);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        renderList();
    }
    public void editRss() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(":(");
        alert.setContentText("You have to edit the configuration file to remove rss");
        alert.showAndWait();
    }
    private int calculatePages() {
        if (list.size() % pageOption > 0) return list.size() / pageOption + 1;
        return list.size() / pageOption;
    }
    private VBox createPage(int pageIndex) {
        VBox vb = new VBox(10);
        int limit = pageOption;
        int page = pageIndex * limit;
        for (int i = page; i < page + limit; i++) {
            if (i < list.size()) { // simple hax for array index-out-of-bounds
                VBox wrapper = new VBox();
                RSSItem item = list.get(i);
                if (this.allowImage) {
                    String desc = item.getDescription();
                    Label label = new Label(item.getTitle() + "\n\n" + ">> " + item.getPubDate());
                    label.setFont(new Font("serif", 16));
                    Image image = new Image(getBetween(desc, "src=\"", "\""), 180.0, 160.0, true, true);
                    label.setGraphic(new ImageView(image));
                    wrapper.getChildren().addAll(label);
                    wrapper.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            openLinkToRss(item.getLink());
                        }
                    });
                    wrapper.setCursor(Cursor.HAND);
                    vb.getChildren().add(wrapper);
                }
                else {
                    Label label = new Label(">>>" + item.getTitle());
                    label.setFont(new Font("serif", 16));
                    wrapper.getChildren().addAll(label);
                    wrapper.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            openLinkToRss(item.getLink());
                        }
                    });
                    wrapper.setCursor(Cursor.HAND);
                    vb.getChildren().add(wrapper);
                }

            }
        }
        return vb;
    }
    private void openLinkToRss(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void renderRss(String name) {
        renderRssStep2(lookupAndGetURL(name));
    }
    public void renderRssStep2(String link) {
        ArrayListRSSFeedStore feedStore = new ArrayListRSSFeedStore();
        RSSFeedReader.read(link, feedStore);
        ArrayList<RSSItem> list = feedStore.getList();
        this.list = list;
        if (list.size() <= 30) {
            this.pageOption = 4;
            this.allowImage = true;
        } else this.pageOption = 6;
        Pagination pagination = new Pagination(calculatePages(), 0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
        ListRssExpanded.getChildren().clear();
        ListRssExpanded.setTopAnchor(pagination, 10.0);
        ListRssExpanded.setRightAnchor(pagination, 10.0);
        ListRssExpanded.setBottomAnchor(pagination, 0.0);
        ListRssExpanded.setLeftAnchor(pagination, 10.0);
        ListRssExpanded.getChildren().addAll(pagination);
    }
    public void showConf() {
        try {
            Desktop.getDesktop().open(new File(this.path));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void showDest() {
        System.exit(0);
    }
    public void showSource() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/quanganh9x/javarss").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
