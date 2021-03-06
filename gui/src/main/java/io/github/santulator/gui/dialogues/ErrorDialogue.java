/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.santulator.gui.dialogues;

import io.github.santulator.gui.i18n.I18nManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

import static io.github.santulator.gui.i18n.I18nKey.ERROR_DETAILS;
import static io.github.santulator.gui.view.CssTool.applyCss;

public class ErrorDialogue {
    private final Alert alert;

    public ErrorDialogue(final I18nManager i18nManager, final String title, final Throwable e, final String... messages) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText(messages));
        applyCss(alert);

        TextArea textArea = new TextArea(exceptionText(e));
        VBox expContent = new VBox();
        DialogPane dialoguePane = alert.getDialogPane();

        expContent.getChildren().setAll(new Label(i18nManager.text(ERROR_DETAILS)), textArea);
        dialoguePane.setExpandableContent(expContent);
        dialoguePane.expandedProperty().addListener(p -> Platform.runLater(this::resizeAlert));
        dialoguePane.setId("errorDialogue");
    }

    private String headerText(final String... messages) {
        return String.join("\n\n", messages);
    }

    private void resizeAlert() {
        alert.getDialogPane().requestLayout();
        
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        stage.sizeToScene();
    }

    private String exceptionText(final Throwable e) {
        StringWriter writer = new StringWriter();

        e.printStackTrace(new PrintWriter(writer));

        return writer.toString();
    }

    public void showError() {
        alert.showAndWait();
    }
}
