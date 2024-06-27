package moe.caramel.chameleon;

import com.google.common.collect.Queues;
import moe.caramel.chameleon.command.ChameleonCommand;
import moe.caramel.chameleon.util.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.Queue;

public final class Main implements ModInitializer {

    /**
     * Toast Message Queue
     */
    public static final Queue<Toast> INIT_TOAST_QUEUE = Queues.newArrayDeque();

    @Override
    public void onInitialize() {
        /* Load Config */
        ModConfig.getInstance();

        /* Register Command */
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
            ChameleonCommand.register(dispatcher);
        });

        /* Watch Reload Resources */
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return ResourceLocation.fromNamespaceAndPath("caramel", "chameleon-dock");
            }

            @Override
            public void onResourceManagerReload(final ResourceManager manager) {
                while (!INIT_TOAST_QUEUE.isEmpty()) {
                    Minecraft.getInstance().getToasts().addToast(INIT_TOAST_QUEUE.poll());
                }
            }
        });
    }
}
