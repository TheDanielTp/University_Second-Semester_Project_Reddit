module org.project.reddit {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;
    requires jdk.jfr;


    opens org.project.reddit to javafx.fxml;
    exports org.project.reddit;
    exports org.project.reddit.controllers;
    exports org.project.reddit.classes;
    opens org.project.reddit.controllers to javafx.fxml;
}