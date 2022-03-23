package moe.caramel.fix248936.util;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.Settings;

import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

public final class ModConfig extends Settings<ModConfig> {

    private static final Path MOD_CONFIG = new File("./config/caramel.fix_mc-248936.properties").toPath();
    public static final ResourceLocation ORIGINAL_ICON = new ResourceLocation("icons/minecraft.icns");

    /* ======================================== */
    private static ModConfig instance;

    /**
     * Get Mod config instance.
     * @return Mod config instance
     */
    public static ModConfig getInstance() {
        if (instance == null) instance = new ModConfig();
        return instance;
    }
    /* ======================================== */


    /* ======================================== */
    public final Settings<ModConfig>.MutableValue<ResourceLocation> iconLocation;

    /**
     * Mod config constructor
     */
    private ModConfig() {
        this(Settings.loadFromFile(MOD_CONFIG));
    }

    /**
     * Mod config constructor
     */
    private ModConfig(Properties properties) {
        super(properties);
        this.iconLocation = this.getMutable("icon-location", ResourceLocation::tryParse, ORIGINAL_ICON);

    }
    /* ======================================== */

    @Override
    protected ModConfig reload(RegistryAccess registryAccess, Properties properties) {
        instance = new ModConfig(properties);
        instance.store(MOD_CONFIG);
        return getInstance();
    }
}
