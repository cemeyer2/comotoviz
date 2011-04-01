package mediatest;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.media.*;
import javafx.scene.layout.*;
import javafx.ext.swing.*;

var mymedia = Media {
           source: "http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_2956241001_big-buck-bunny-640x360.flv"
         }

var player =       MediaPlayer {
         media: mymedia
         autoPlay: true
       }

Stage {
  scene: Scene {
    content: VBox {
      content: [
             SwingButton {  
                 text: "Play"  
                 action: function() {  
                     player.play()  
                 }  
             },  
			 MediaView {  
                 mediaPlayer:player  
             }
      ]
    }
  }
}