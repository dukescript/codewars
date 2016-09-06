package com.dukescript.starwars;

import com.dukescript.api.canvas.GraphicsContext2D;
import com.dukescript.api.canvas.Style;
import com.dukescript.starwars.js.CanvasExtras;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.Models;
import net.java.html.json.Property;
import net.java.html.sound.AudioClip;

@Model(
        className = "Loader", properties = {
            @Property(name = "file", type = String.class),
            @Property(name = "idx", type = int.class),
            @Property(name = "vader", type = boolean.class),
            @Property(name = "obi", type = boolean.class)
        }, targetId = ""
)
final class DataModel {

    static Loader LOADER;
    private static final GraphicsContext2D g = GraphicsContext2D.getOrCreate("stitch");

    /**
     * Called when the page is ready.
     */
    static void onPageLoad() throws Exception {
        LOADER = new Loader();
        Models.toRaw(LOADER);
        CanvasExtras.registerAudioBinding();
        LOADER.applyBindings();

    }

    @Function
    public static void start(Loader loader) {
        try {
            read(g, "JavaCup.dst");
        } catch (IOException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Function
    static void loadFile(Loader model) {
        try {
            read(g, model.getFile());
        } catch (IOException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void read(GraphicsContext2D g2d, String fileName) throws IOException {
        g2d.setTransform(1, 0, 0, 1, 0, 0);

        g2d.clearRect(0, 0, 1000, 1000);
        CanvasExtras.shadowColor("#ff0000", 3);
        g.setFillStyle(new Style.Color("#ff0000"));
        final BrwsrCtx ctx = BrwsrCtx.findDefault(DataModel.class);
        File file = new File(fileName);
        FileInputStream fs = new FileInputStream(file);
        DSTModel design = DSTModel.readDesign(fs);
        LOADER.setVader(true);

        int min = Math.min(g.getWidth(), g.getHeight());
        double scale = Math.min((min / design.getMaxDist()), 1.0) - .2;
        g2d.setTransform(scale, 0, 0, scale, design.getWidth() - design.getxMax(), design.getHeight() - design.getyMax());
        g2d.setLineWidth(1);
        g2d.setStrokeStyle(new Style.Color("rgba(255,255,255,100)"));
        int x = 0, y = 0;
        int colors = design.getColors();
        DSTModel.Stitch[] stitches = design.getStitches();
        final StitchAnimation stitchAnimation = new StitchAnimation(stitches, g2d);
        stitch(stitchAnimation, ctx);
    }

    protected static void stitch(final StitchAnimation stitchAnimation, final BrwsrCtx ctx) {
        timer = new Timer();
        final AudioClip clip = AudioClip.create("./css/lightsabre.mp3");

        final Runnable stitcher = new Runnable() {
            @Override
            public void run() {
                if (!stitchAnimation.stitch(g)) {
                    timer.cancel();
                    clip.pause();
                    if (stitchAnimation.isFinished()) {
                        LOADER.setVader(false);
                        LOADER.setObi(false);

                        System.out.println("finished");
                        return;
                    }
                    LOADER.setVader(false);
                    LOADER.setObi(true);
                    CanvasExtras.shadowColor("#0000ff", 3);
                    stitch(stitchAnimation, ctx);
                }
            }
        };
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ctx.execute(stitcher);
            }
        }, 100, 5);
        clip.play();

    }
    private static Timer timer;

}
