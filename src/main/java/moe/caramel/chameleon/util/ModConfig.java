package moe.caramel.chameleon.util;

import static java.util.Map.entry;
//import static net.minecraft.resources.ResourceLocation.withDefaultNamespace;
import static net.minecraft.resources.Identifier.withDefaultNamespace;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import moe.caramel.chameleon.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
//import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.Identifier;
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

/**
 The type Mod config.
 */
public final class ModConfig extends Settings<ModConfig>{
	
	/**
	 The constant ORIGINAL_MAC_ICON.
	 */
	//<editor-fold desc="Icon registry" defaultstate="collapsed">
	public static final Identifier ORIGINAL_MAC_ICON = withDefaultNamespace("icons/minecraft.icns");
	/**
	 The constant ORIGINAL_WIN_ICON.
	 */
	public static final Identifier ORIGINAL_WIN_ICON = withDefaultNamespace("icons/icon_128x128.png");
	/**
	 The constant VANILLA_ICON_SET.
	 */
	public static final Map<Identifier, String[]> VANILLA_ICON_SET = Map.ofEntries(entry(ORIGINAL_MAC_ICON, new String[]{"icons", "minecraft.icns"}), entry(withDefaultNamespace("icons/icon_16x16.png"), new String[]{"icons", "icon_16x16.png"}), entry(withDefaultNamespace("icons/icon_32x32.png"), new String[]{"icons", "icon_32x32.png"}), entry(withDefaultNamespace("icons/icon_48x48.png"), new String[]{"icons", "icon_48x48.png"}), entry(ORIGINAL_WIN_ICON, new String[]{"icons", "icon_128x128.png"}), entry(withDefaultNamespace("icons/icon_256x256.png"), new String[]{"icons", "icon_256x256.png"}), entry(withDefaultNamespace("snapshot/icons/icon_16x16.png"), new String[]{"icons", "snapshot", "icon_16x16.png"}), entry(withDefaultNamespace("snapshot/icons/icon_32x32.png"), new String[]{"icons", "snapshot", "icon_32x32.png"}), entry(withDefaultNamespace("snapshot/icons/icon_48x48.png"), new String[]{"icons", "snapshot", "icon_48x48.png"}), entry(withDefaultNamespace("snapshot/icons/icon_128x128.png"), new String[]{"icons", "snapshot", "icon_128x128.png"}), entry(withDefaultNamespace("snapshot/icons/icon_256x256.png"), new String[]{"icons", "snapshot", "icon_256x256.png"}));
	/**
	 The constant GET_ICON_SET.
	 */
	public static final Function<Minecraft, Set<Identifier>> GET_ICON_SET = (client) -> {
		final Set<Identifier> iconSet = new ObjectOpenHashSet<>();
		client.getResourceManager().listResources("icons", resource -> {
			if (resource != null){
				iconSet.add(resource);
				return true;
			}
			return false;
		});
		iconSet.addAll(VANILLA_ICON_SET.keySet());
		return iconSet;
	};
	private static final Path MOD_CONFIG = new File("./config/caramel.chameleon.properties").toPath();
	private static final int CURRENT_CONFIG_VERSION = 1;
	//</editor-fold>
	//<editor-fold desc="Instance manager" defaultstate="collapsed">
	private static ModConfig instance;
	/**
	 The Config version.
	 */
	//<editor-fold desc="Instance" defaultstate="collapsed">
	public final Settings<ModConfig>.MutableValue<Integer> configVersion;
	//</editor-fold>
	/**
	 The Icon location.
	 */
	public final Settings<ModConfig>.MutableValue<Identifier> iconLocation;
	private ModConfig(){
		this(Settings.loadFromFile(MOD_CONFIG));
	}
	
	private ModConfig(final Properties properties){
		super(properties);
		this.configVersion = this.getMutable("config-version", s -> (s == null) ? 0 : Integer.parseInt(s), ModConfig.CURRENT_CONFIG_VERSION);
		this.iconLocation = this.getMutable("icon-location", Identifier::tryParse, (Main.ON_OSX ? ORIGINAL_MAC_ICON : ORIGINAL_WIN_ICON));
	}
	
	/**
	 Gets instance.
	 
	 @return the instance
	 */
	public static ModConfig getInstance(){
		if (instance == null){
			instance = new ModConfig();
		}
		
		return instance;
	}
	
	/**
	 Change icon.
	 
	 @param client the client
	 @param icon   the icon
	 
	 @throws IOException the io exception
	 */
	//<editor-fold desc="Icon applicator" defaultstate="collapsed">
	public static void changeIcon(final Minecraft client, final Identifier icon) throws IOException{
		final String[] vanillaPath = ModConfig.VANILLA_ICON_SET.get(icon);
		final IoSupplier<InputStream> iconSupplier;
		if (vanillaPath != null){
			iconSupplier = Objects.requireNonNull(client.getVanillaPackResources().getRootResource(vanillaPath));
		} else{
			iconSupplier = ResourceIo.create(client.getResourceManager().getResource(icon).get());
		}
		if (Main.ON_OSX){
			MacosUtil.loadIcon(iconSupplier);
		} else{
			ModConfig.setWindowsIcon(client, iconSupplier);
		}
		ModConfig.getInstance().iconLocation.update(null, icon);
	}
	//</editor-fold>
	
	/**
	 Sets windows icon.
	 
	 @param client the client
	 @param icon   the icon
	 
	 @throws IOException the io exception
	 */
	public static void setWindowsIcon(final Minecraft client, final IoSupplier<InputStream> icon) throws IOException{
		ByteBuffer value = null;
		
		try (final MemoryStack stack = MemoryStack.stackPush(); final NativeImage image = NativeImage.read(icon.get());){
			final GLFWImage.Buffer images = GLFWImage.malloc(1, stack);
			
			value = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
			value.asIntBuffer().put(image.getPixelsABGR());
			images.position(0);
			images.width(image.getWidth());
			images.height(image.getHeight());
			images.pixels(value);
			
			GLFW.glfwSetWindowIcon(client.getWindow().handle(), images.position(0));
		} finally{
			if (value != null){
				MemoryUtil.memFree(value);
			}
		}
	}
	
	@Override
	protected @NotNull ModConfig reload(final RegistryAccess registryAccess, final Properties properties){
		instance = new ModConfig(properties);
		instance.store(MOD_CONFIG);
		return getInstance();
	}
	//</editor-fold>
}
