package moe.caramel.chameleon;

import com.google.common.collect.Queues;
import moe.caramel.chameleon.command.ChameleonCommand;
import moe.caramel.chameleon.util.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener; // TODO this is deprecated
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Util;

import java.util.Queue;

/**
 The type Main.
 */
public final class Main implements ModInitializer{
	
	/**
	 Toast Message Queue
	 */
	public static final Queue<Toast> INIT_TOAST_QUEUE = Queues.newArrayDeque();
	
	/**
	 {@code true} if Minecraft is running on macOS.
	 */
	public static final boolean ON_OSX = Util.getPlatform() == Util.OS.OSX;
	
	@Override
	public void onInitialize(){
		/* Load Config */
		ModConfig.getInstance();
		
		/* Register Command */
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
			ChameleonCommand.register(dispatcher);
		});
		
		/* Watch Reload Resources */
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener(){
			@Override
			public Identifier getFabricId(){
				return Identifier.fromNamespaceAndPath("caramel", "chameleon-dock");
			}
			
			@Override
			public void onResourceManagerReload(final ResourceManager manager){
				while (!INIT_TOAST_QUEUE.isEmpty()){
					//Minecraft.getInstance().getToastManager().addToast(INIT_TOAST_QUEUE.poll());
					Minecraft.getInstance().gui.toastManager().addToast(INIT_TOAST_QUEUE.poll());
				}
			}
		});
	}
}
