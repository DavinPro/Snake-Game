package com.github.davinpro.viewmodel;

import com.github.davinpro.App;
import com.github.davinpro.SoundManager;
import com.github.davinpro.SoundManager.Sound;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class Highscores implements Initializable {

  @FXML
  TableView<String[]> scoresTable;

  @FXML
  TableColumn<String[], String> nameColumn;

  @FXML
  TableColumn<String[], String> scoreColumn;

  @FXML
  TableColumn<String[], String> timeColumn;

  ObservableList<String[]> entries = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue()[0]));
    scoreColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue()[1]));
    timeColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue()[2]));

    try {
      String path = new File("./src/main/java/com/github/davinpro").getCanonicalPath();
      BufferedReader buffReader = new BufferedReader(new FileReader(path + "/Scores.txt"));

      String curLine;
      while ((curLine = buffReader.readLine()) != null) {
        entries.add(curLine.split(":"));
      }

      buffReader.close();
    }
    catch(IOException ex) {
      ex.printStackTrace();
    }

    scoresTable.setItems(entries);

    scoreColumn.setComparator((o1, o2) -> Integer.valueOf(o2).compareTo(Integer.valueOf(o1)));
    scoresTable.getSortOrder().add(scoreColumn);
  }

  @FXML
  public void switchToMainMenu() throws IOException {
    SoundManager.play(Sound.BUTTON_PRESS);
    App.setRoot("MainMenu");
  }
}
