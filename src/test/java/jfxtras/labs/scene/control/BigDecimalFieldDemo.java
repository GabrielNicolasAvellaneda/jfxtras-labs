/**
 * BigDecimalFieldDemo.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 *
 * @author Thomas Bolz
 */
public class BigDecimalFieldDemo extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX BigDecimalField Demo");
        ObjectProperty<DateFormat> dateFormatProperty = new SimpleObjectProperty<DateFormat>(DateFormat.getDateInstance());
        ObjectProperty<NumberFormat> numberFormatProperty = new SimpleObjectProperty<NumberFormat>(NumberFormat.getNumberInstance());
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        final BigDecimalField defaultSpinner = new BigDecimalField();
        final BigDecimalField decimalFormat = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.05"), new DecimalFormat("#,##0.00"));
        final BigDecimalField percent = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getPercentInstance());
        final BigDecimalField localizedCurrency = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getCurrencyInstance(Locale.UK));
        final BigDecimalField promptText = new BigDecimalField();
        promptText.setNumber(null);
        promptText.setPromptText("Enter something");
        Label label1;
        root.addRow(1, new Label("default"), defaultSpinner, label1 = LabelBuilder.create().build());
//        label1.textProperty().bind(Bindings.convert(defaultSpinner.numberProperty()));
        root.addRow(2, new Label("custom decimal format"), decimalFormat);
        root.addRow(3, new Label("percent"), percent);
        root.addRow(4, new Label("localized currency"), localizedCurrency);
        final BigDecimalField disabledField = new BigDecimalField();
        disabledField.setDisable(true);
        root.addRow(5, new Label("disabled field"), disabledField);
        root.addRow(6, new Label("regular TextField"), new TextField("1.000,1234"));
        root.addRow(7, new Label("with promptText"), promptText);
        CalendarTextField calendarTextField = new CalendarTextField();
        root.addRow(8, new Label("CalendarTextField"), calendarTextField);
        ComboBox<Locale> cmbLocales = new ComboBox<>(FXCollections.observableArrayList(Locale.GERMANY, Locale.UK, Locale.FRANCE));
        cmbLocales.setOnAction(event->{
            dateFormatProperty.set(DateFormat.getDateInstance(DateFormat.MEDIUM, cmbLocales.getValue()));
            numberFormatProperty.set(NumberFormat.getNumberInstance(cmbLocales.getValue()));
        });
        root.addRow(9, new Label("Locale"), cmbLocales);

        root.addRow(10, new Label("Field with boundaries (0,100%)"), 
        		BigDecimalFieldBuilder.create()
            	.number(new BigDecimal("0.1"))
            	.minValue(BigDecimal.ZERO)
            	.maxValue(BigDecimal.ONE)
            	.stepwidth(new BigDecimal("0.01"))
            	.format(DecimalFormat.getPercentInstance())
            	.build()
        	);

        promptText.numberProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observableValue, BigDecimal o, BigDecimal o1) {
                System.out.println(o1);
            }
        });

        Button button = new Button("Reset fields");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                defaultSpinner.setNumber(new BigDecimal(Math.random() * 1000));
                decimalFormat.setNumber(new BigDecimal(Math.random() * 1000));
                percent.setNumber(new BigDecimal(Math.random()));
                localizedCurrency.setNumber(new BigDecimal(Math.random() * 1000));
                disabledField.setNumber(new BigDecimal(Math.random() * 1000));
                promptText.setNumber(null);
                calendarTextField.setCalendar(Calendar.getInstance());
            }
        });
        root.addRow(11, new Label(), button);
        BigDecimalLabel bigDecimalLabel = new BigDecimalLabel();
        bigDecimalLabel.numberProperty().bind(defaultSpinner.numberProperty());
        bigDecimalLabel.formatProperty().bind(numberFormatProperty);
        root.addRow(12, new Label("BigDecimalLabel"), bigDecimalLabel);

        CalendarLabel calendarLabel = new CalendarLabel();
        calendarLabel.valueProperty().bind(calendarTextField.calendarProperty());
        calendarLabel.formatProperty().bind(dateFormatProperty);
        root.addRow(13, new Label("CalendarLabel"), calendarLabel);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
