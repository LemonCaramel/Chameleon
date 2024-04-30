package moe.caramel.chameleon.util;

import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import java.io.InputStream;

public interface ResourceIo extends IoSupplier<Resource> {

    static IoSupplier<InputStream> create(final Resource resource) {
        return resource::open;
    }
}
