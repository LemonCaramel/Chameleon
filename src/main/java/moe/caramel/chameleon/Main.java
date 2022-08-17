package moe.caramel.chameleon;

import com.google.common.collect.Queues;
import moe.caramel.chameleon.command.ChangeIconCommand;
import moe.caramel.chameleon.util.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.gui.components.toasts.Toast;
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
            ChangeIconCommand.register(dispatcher);
        });
    }
}
