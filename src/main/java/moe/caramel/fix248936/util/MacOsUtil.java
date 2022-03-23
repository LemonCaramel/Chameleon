package moe.caramel.fix248936.util;

import ca.weblite.objc.Client;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public final class MacOsUtil {

    private MacOsUtil() { throw new UnsupportedOperationException(); }

    /**
     * Loads the icon from macOS
     * @param stream icon's InputStream
     * @throws IOException if an I/O error occurs
     */
    public static void loadIcon(final @NotNull InputStream stream) throws IOException {
        final String icon = Base64.getEncoder().encodeToString(stream.readAllBytes());
        final Client client = Client.getInstance();
        final Object NSData = client.sendProxy("NSData", "alloc").send("initWithBase64Encoding:", icon);
        final Object NSImage = client.sendProxy("NSImage", "alloc").send("initWithData:", NSData);
        client.sendProxy("NSApplication", "sharedApplication").send("setApplicationIconImage:", NSImage);
    }
}
