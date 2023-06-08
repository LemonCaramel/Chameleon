package moe.caramel.chameleon.util;

import ca.weblite.objc.Client;
import ca.weblite.objc.Proxy;
import org.jetbrains.annotations.Nullable;

/**
 * macOS Utilities.
 */
public final class MacosUtil extends com.mojang.blaze3d.platform.MacosUtil {

    /**
     * Sets the badge label for the dock.
     *
     * @param label badge label
     */
    public static void setBadgeLabel(@Nullable String label) {
        final Client client = Client.getInstance();
        final Proxy dockTile = client.sendProxy("NSApplication", "sharedApplication").sendProxy("dockTile");
        dockTile.send("setBadgeLabel:", label);
    }
}
