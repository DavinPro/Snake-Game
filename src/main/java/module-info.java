module com.github.davinpro {
    requires javafx.controls;
    requires javafx.fxml;
  requires javafx.media;

  opens com.github.davinpro to javafx.fxml;
    opens com.github.davinpro.viewmodel to javafx.fxml;
    exports com.github.davinpro;
}
