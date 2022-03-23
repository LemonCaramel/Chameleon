package moe.caramel.fix248936;

import moe.caramel.fix248936.command.ChangeIconCommand;
import moe.caramel.fix248936.util.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

public final class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        /* Load Config */
        ModConfig.getInstance();

        /* Register Command */
        final var dispatcher = ClientCommandManager.DISPATCHER;
        ChangeIconCommand.register(dispatcher);
    }
}
