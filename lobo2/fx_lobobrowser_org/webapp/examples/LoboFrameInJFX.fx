import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.ext.swing.*;
import javax.swing.*;
import java.awt.Component;
import java.awt.FlowLayout;
import org.lobobrowser.clientlet.*;

/* Get clientlet context and create a new browser frame. */

var CTX = ClientletAccess.getCurrentClientletContext();
var loboFrame = CTX.createNavigatorFrame();
var frameComponent = loboFrame.getComponent() as JComponent;
var javafxFrame = SwingComponent.wrap(frameComponent);

Stage {
   title: "Lobo Frame in JavaFX Script"
   scene: Scene {
     fill: Color.YELLOW 
     content: [
       VBox {
         content: [
           HBox { 
             var textField = SwingTextField {
               columns: 60
    	       text: "http://google.com"
             };
             var button = SwingButton {
               text: "GO"
               action: function() {
                 loboFrame.navigate(textField.text);
               }
             };
             content: [
  	           textField,	
               button
             ]
           },
           javafxFrame
         ]
       }
     ]
   }
}