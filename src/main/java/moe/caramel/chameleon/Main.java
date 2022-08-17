package moe.caramel.chameleon;

import moe.caramel.chameleon.command.ChangeIconCommand;
import moe.caramel.chameleon.util.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public final class Main implements ModInitializer {

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
