package moe.caramel.chameleon.util;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.Settings;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

public final class ModConfig extends Settings<ModConfig> {

    private static final Path MOD_CONFIG = new File("./config/caramel.chameleon.properties").toPath();
    private static final int CURRENT_CONFIG_VERSION = 1;
    public static final ResourceLocation ORIGINAL_MAC_ICON = new ResourceLocation("icons/minecraft.icns");
    public static final ResourceLocation ORIGINAL_WIN_ICON = new ResourceLocation("icons/icon_128x128.png");
    public static final Map<ResourceLocation, String[]> VANILLA_ICON_SET = new Object2ObjectOpenHashMap<>();
    static {
        VANILLA_ICON_SET.put(ORIGINAL_MAC_ICON, new String[]{ "icons", "minecraft.icns" });
        VANILLA_ICON_SET.put(new ResourceLocation("icons/icon_16x16.png"), new String[]{ "icons", "icon_16x16.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("icons/icon_32x32.png"), new String[]{ "icons", "icon_32x32.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("icons/icon_48x48.png"), new String[]{ "icons", "icon_48x48.png" });
        VANILLA_ICON_SET.put(ORIGINAL_WIN_ICON, new String[]{ "icons", "icon_128x128.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("icons/icon_256x256.png"), new String[]{ "icons", "icon_256x256.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("snapshot/icons/icon_16x16.png"), new String[]{ "icons", "snapshot", "icon_16x16.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("snapshot/icons/icon_32x32.png"), new String[]{ "icons", "snapshot", "icon_32x32.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("snapshot/icons/icon_48x48.png"), new String[]{ "icons", "snapshot", "icon_48x48.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("snapshot/icons/icon_128x128.png"), new String[]{ "icons", "snapshot", "icon_128x128.png" });
        VANILLA_ICON_SET.put(new ResourceLocation("snapshot/icons/icon_256x256.png"), new String[]{ "icons", "snapshot", "icon_256x256.png" });
    }
    public static final Function<Minecraft, Set<ResourceLocation>> GET_ICON_SET = client -> {
        final Set<ResourceLocation> iconSet = new ObjectOpenHashSet<>();
        client.getResourceManager().listResources("icons", resource -> {
            if (resource != null) {
                iconSet.add(resource);
                return true;
            }
            return false;
        });
        iconSet.addAll(VANILLA_ICON_SET.keySet());
        return iconSet;
    };

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
    public final Settings<ModConfig>.MutableValue<Integer> configVersion;
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
        this.configVersion = this.getMutable(
            "config-version", s -> {
                if (s == null) return 0;
                return Integer.parseInt(s);
            }, ModConfig.CURRENT_CONFIG_VERSION
        );
        this.iconLocation = this.getMutable(
            "icon-location", ResourceLocation::tryParse,
            (Minecraft.ON_OSX ? ORIGINAL_MAC_ICON : ORIGINAL_WIN_ICON)
        );
    }
    /* ======================================== */

    @Override
    protected @NotNull ModConfig reload(RegistryAccess registryAccess, Properties properties) {
        instance = new ModConfig(properties);
        instance.store(MOD_CONFIG);
        return getInstance();
    }

    /* ======================================== */
    /**
     * Apply and save icon setting.
     *
     * @param client Minecraft object
     * @param icon Icon Resource location
     * @throws IOException InputStream open failed
     */
    public static void changeIcon(Minecraft client, ResourceLocation icon) throws IOException {
        final String[] vanillaPath = ModConfig.VANILLA_ICON_SET.get(icon);
        final IoSupplier<InputStream> iconSupplier;
        if (vanillaPath != null) {
            iconSupplier = Objects.requireNonNull(client.getVanillaPackResources().getRootResource(vanillaPath));
        } else {
            iconSupplier = ResourceIo.create(client.getResourceManager().getResource(icon).get());
        }
        if (Minecraft.ON_OSX) {
            MacosUtil.loadIcon(iconSupplier);
        } else {
            ModConfig.setWindowsIcon(client, iconSupplier);
        }
        ModConfig.getInstance().iconLocation.update(null, icon);
    }

    /**
     * Change the icon in Windows OS.
     *
     * @param client Minecraft object
     * @param icon Icon Resource location
     * @throws IOException InputStream open failed
     */
    public static void setWindowsIcon(final Minecraft client, final IoSupplier<InputStream> icon) throws IOException {
        ByteBuffer value = null;

        try (
            final MemoryStack stack = MemoryStack.stackPush();
            final NativeImage image = NativeImage.read(icon.get());
        ) {
            final GLFWImage.Buffer images = GLFWImage.malloc(1, stack);

            value = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            value.asIntBuffer().put(image.getPixelsRGBA());
            images.position(0);
            images.width(image.getWidth());
            images.height(image.getHeight());
            images.pixels(value);

            GLFW.glfwSetWindowIcon(client.getWindow().getWindow(), images.position(0));
        } finally {
            if (value != null) {
                MemoryUtil.memFree(value);
            }
        }
    }
    /* ======================================== */
}
