package org.lobobrowser.fxsite.dynamic;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.transform.*;
import javafx.ext.swing.*;
import javafx.geometry.*;

import javax.swing.*;
import org.lobobrowser.clientlet.*;
import org.lobobrowser.io.*;

/* Get clientlet context and create a new browser frame. */

var URI_FILE:String = "msd-uri.bin";
var X_FILE:String = "msd-x.bin";
var Y_FILE:String = "msd-y.bin";
var scale = 0.4;
var CTX = ClientletAccess.getCurrentClientletContext();
var loboFrame = CTX.createNavigatorFrame();
var frameComponent = loboFrame.getComponent() as JComponent;
frameComponent.setPreferredSize(new java.awt.Dimension(280 / scale, 240 / scale));
var javafxFrame = SwingComponent.wrap(frameComponent);
var managedStore = CTX.getManagedStore("lobobrowser.org");

var uriTextField = SwingTextField {
    columns: 22
    text: "./windowinfo.html";
}

function navigate() {
    var uriText = uriTextField.text;
    managedStore.saveObject(URI_FILE, uriText); 
    var currentURL = new java.net.URL(CTX.getResponse().getResponseURL(), uriText);
	loboFrame.navigate(currentURL);
}

class InternalStage extends CustomNode {
    var anchorX = 0.0;
    var anchorY = 0.0;
    
    postinit {
	    onMousePressed = function(e:MouseEvent):Void {
		    anchorX = translateX;
	 	    anchorY = translateY;	    
	    }    
	    onMouseDragged = function(e:MouseEvent):Void {
		    translateX = anchorX + e.dragX;
		    translateY = anchorY + e.dragY;
        }
    }

	override function create():Node {
        return Group {
            content: [
                Rectangle {
                    x: 0
                    y: 0
                    width: 300
                    height: 300
                    arcWidth: 10
                    arcHeight: 10
                    fill:Color.RED
                },
                Rectangle {
                    blocksMouse: true
                	x: 5
                	y: 15
                	width: 290
                	height: 280
                	arcWidth: 10
                	arcHeight: 10
                	fill:Color.WHITE                
                },
                VBox {
                    translateX: 10
                    translateY: 20
                    content: [
                        HBox {
                        	content: [
                        		uriTextField,
								SwingButton {
									text: "Go"
									action: function() {
									  navigate();
									}
								}                        	
                        	]
                        },
                        Group {
                           content:
		                     javafxFrame
		                   transforms: Scale {
		                     x: scale
		                     y: scale
		                     pivotX: 0
		                     pivotY: 0
		                   }
		                }
	                ]
	            }
            ]
        };
    }
}

var internalStage = InternalStage {
}

/* Retrieve persisted data */

var storedURI = managedStore.retrieveObject(URI_FILE, null);
var storedX = managedStore.retrieveObject(X_FILE, null);
var storedY = managedStore.retrieveObject(Y_FILE, null);
if(storedURI != null) {
	uriTextField.text = storedURI as String;
}
if(storedX != null) {
	internalStage.translateX = java.lang.Double.parseDouble(storedX as String);
}
if(storedY != null) {
	internalStage.translateY = java.lang.Double.parseDouble(storedY as String);
}

navigate();

def persistentX = bind internalStage.translateX on replace oldValue {
	managedStore.saveObject(X_FILE, String.valueOf(persistentX));
}
def persistentY = bind internalStage.translateY on replace oldValue {
	managedStore.saveObject(Y_FILE, String.valueOf(persistentY));
}
 
Stage {
    title: "Draggable Internal Window"
	scene: Scene {
  	  fill: Color.GREEN
	  content: [
	    SwingButton {
	    	translateX: 5
	    	translateY: 5
	    	text: "Clear Store"
	    	action: function() {
	    		managedStore.saveObject(URI_FILE, null);
	    		managedStore.saveObject(X_FILE, null);
	    		managedStore.saveObject(Y_FILE, null);
	    		internalStage.translateX = 50;
	    		internalStage.translateY = 50;
	    		uriTextField.text = "./windowinfo.html";
				navigate();					    		
	    	}
	    },
	    internalStage
	  ]
	}
}