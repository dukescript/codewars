/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.starwars;

import com.dukescript.api.canvas.GraphicsContext2D;
import com.dukescript.starwars.DSTModel.Stitch;

//C:\Users\Eppleton\Documents\NetBeansProjects\jstitch\client\GOLDFISH.DST
/**
 *
 * @author antonepple
 */
public class StitchAnimation {

    private int i = 0, x = 0, y = 0, color = 0;

    private final GraphicsContext2D g2d;
    private final Stitch[] stitches;

    private boolean finished = false;

    /**
     * Get the value of finished
     *
     * @return the value of finished
     */
    public boolean isFinished() {
        return finished;
    }

    public StitchAnimation(Stitch[] stitches, GraphicsContext2D g2d) {
        this.stitches = stitches;
        this.g2d = g2d;
    }

    public boolean stitch(GraphicsContext2D g2d) {
        if (i >= stitches.length) {
            return false;
        }
        Stitch stitch = stitches[i];
        x += stitch.getIncX();
        y -= stitch.getIncY();
//        System.out.format("%d (%d,%d)\n", i, x, y);
        if (stitch.isColorChange() || stitch.isJump()) {
            g2d.beginPath();
            g2d.moveTo(x, y);
            if (stitch.isColorChange()) {
                color++;
                i++;
                if (stitches.length == i) {
                    finished = true;
                }
                System.out.println("stitches "+stitches.length+" i "+i);
                return false;
            }
        } else {
            g2d.lineTo(x, y);
            g2d.stroke();
        }
        i++;
        return true;
    }

}
