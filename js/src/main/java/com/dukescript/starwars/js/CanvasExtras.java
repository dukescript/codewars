/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.starwars.js;

import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

/**
 *
 * @author antonepple
 */
public class CanvasExtras {

    @JavaScriptBody(args = { "color", "blur" }, body = "var canvas = document.getElementById('stitch');\n"
            + "        var context = canvas.getContext('2d');\n"
            + "        context.shadowColor = color; \n"
            + "        context.shadowOffsetX = 0; \n"
            + "        context.shadowOffsetY = 0; \n"
            + "        context.shadowBlur = blur;")
    public static native void shadowColor(String color, int blur);

    @JavaScriptBody(args = {  }, body = "ko.bindingHandlers.audio = {\n" +
"    init: function (element, valueAccessor) {\n" +
"        var config = ko.unwrap(valueAccessor());\n" +
"        var file = config.sound;\n" +
"        var observable = config.value;\n" +
"        observable.subscribe(function () {\n" +
"            var audio = new Audio(file);\n" +
"            audio.play();\n" +
"        });\n" +
"    }\n" +
"};")
    public static native void registerAudioBinding();
    
}
