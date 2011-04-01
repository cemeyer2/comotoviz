/*
 * RoundBorderRectangle.fx
 */
package org.lobobrowser.fxsite.dynamic;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
 
Stage {
    title: "Easy Round Borders!"
	scene: Scene {
	content: [
    	Rectangle {
      	  x: 45   
      	  y: 35
     	  width: 200  
     	  height: 200
     	  arcWidth: 15   
     	  arcHeight: 15
          fill: LinearGradient {
	          startX: 0.0 
              startY: 0.0 
              endX: 0.0 
              endY: 1.0
              proportional: true
              stops: [ 
                     Stop {offset: 0.0 color: Color.WHITE},
                     Stop {offset: 1.0 color: Color.BLACK} 
              ] 
          }
        }
    ]
    }
}