import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Timer;
import java.util.TimerTask;


public class ShapedWindowDemo extends JFrame{
    public ShapedWindowDemo() {
        super("ShapedWindow");
        setLayout(new GridBagLayout());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new Rectangle.Double(0,0,getWidth(),getHeight()));
            }
        });

        setUndecorated(true);
        setSize(1920,1080);
//        new change
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static float opacity = 0.5f;

    public static void main(String args[]){

    }

    public static Runnable dimming() {
        /*// Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        final boolean isTranslucencySupported =
            gd.isWindowTranslucencySupported(TRANSLUCENT);

        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT)) {
            System.err.println("Shaped windows are not supported");
            System.exit(0);
        }

        //If translucent windows aren't supported,
        //create an opaque window.
        if (!isTranslucencySupported) {
            System.out.println(
                "Translucency is not supported, creating an opaque window");
        }*/
        ShapedWindowDemo sw = new ShapedWindowDemo();
        sw.setOpacity(opacity);
        sw.getContentPane().setBackground(Color.BLACK);
        sw.setVisible(true);
        // Create the GUI on the event-dispatching thread
        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShapedWindowDemo sw = new ShapedWindowDemo();

                // Set the window to 70% translucency, if supported.
                // if (isTranslucencySupported) {
                //     sw.setOpacity(0.5f);
                // }
                sw.setOpacity(0.5f);

                sw.getContentPane().setBackground(Color.BLACK);

                // Display the window.
                sw.setVisible(true);
            }
        });*/

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run(){
                try {
                    BrainFlow_BandPowers.logging();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (BrainFlow_BandPowers.band_powers[1] > 585){
                    incrementOpacity(opacity);
                } else if (BrainFlow_BandPowers.band_powers[1] < 584){
                    decrementOpacity(opacity);
                }
                sw.setOpacity(opacity);
            }
        } , 300, 300);
        return null;
    }

    public static void incrementOpacity(float old_opacity){
        if (opacity <= 0.9){
            opacity = old_opacity += .1;
        }
    }

    public static void decrementOpacity(float old_opacity){
        if (opacity >= 0.1){
            opacity = old_opacity -= .1;
        }
    }
}