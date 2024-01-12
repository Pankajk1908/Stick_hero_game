module com.example.stickhero2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;
    requires javafx.media;


    opens com.example.stickhero2 to javafx.fxml;
    exports com.example.stickhero2;
}