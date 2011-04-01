/*!
 * # This title takes precedence over Stage title.
 * title: New Lobo Home Page - Under Construction.
 * # This directive causes the content to be centered
 * # based on its preferred size
 * use-preferred-size: true
 */ 
package org.lobobrowser.fxsite.dynamic; 

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.effect.*;
 
Stage {
    title: "Home Page Scene"
	scene: Scene {
	    fill: Color.TRANSPARENT
		content: [
			Rectangle {
     		  width: 750  
     		  height: 540
     		  arcWidth: 40   
     		  arcHeight: 40
              fill: LinearGradient { 
                  startX: 0.0 
                  startY: 0.0 
                  endX: 0.0 
                  endY: 1.0
                  proportional: true
                  stops: [ 
                     Stop {offset: 0.0 color: Color.WHITE},
                     Stop {offset: 1.0 color: Color.GREEN} 
                  ]
                  
              }                 	
           },
           HBox {
             spacing: 15
             translateY: 60
             translateX: 20
             effect:  Reflection {fraction: 0.9  topOpacity: 0.5 topOffset: 2.5}
             content: [             
               ImageView {
                   image: Image {url: "http://fx.lobobrowser.org/images/underconstruction.gif"}
               },
               VBox {
                 content: [
                  Text {
                    y: 100
                    font: Font { size: 50 }
               	    content: "This page is"
                  },
                  Text {
                    y: 120
                    font: Font { size: 50 }
               	    content: "under construction."                  
                  }   
                 ]
               }
             ]
           }
		]	
	}
}
