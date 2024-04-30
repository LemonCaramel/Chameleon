package moe.caramel.chameleon.command;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import moe.caramel.chameleon.gui.ChangeDockIconScreen;
import moe.caramel.chameleon.util.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.NoSuchElementException;

public final class ChameleonCommand {

    private static final String ICON_NAME = "icon name";
    private static final SuggestionProvider<FabricClientCommandSource> SUGGEST = (context, builder) -> {
        final Minecraft client = Minecraft.getInstance();
        for (final ResourceLocation resource : ModConfig.GET_ICON_SET.apply(client)) {
            builder.suggest(resource.toString());
        }
        return builder.buildFuture();
    };

    public static void register(final @NotNull CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("chameleon").executes(context -> {
            final Minecraft client = Minecraft.getInstance();
            client.tell(() -> client.setScreen(new ChangeDockIconScreen(null)));
            return 0;
        }).then(
            argument(ICON_NAME, ResourceLocationArgument.id()).suggests(SUGGEST).executes(context -> {
                final Minecraft client = Minecraft.getInstance();
                final FabricClientCommandSource source = context.getSource();
                final ResourceLocation resource = context.getArgument(ICON_NAME, ResourceLocation.class);

                try {
                    ModConfig.changeIcon(client, resource);
                    source.sendFeedback(Component.translatable("caramel.chameleon.change.done", resource));
                    return 0;
                } catch (final NoSuchElementException ignored) {
                    source.sendError(Component.translatable("caramel.chameleon.change.404", resource));
                    return -1;
                } catch (final IOException exception) {
                    source.sendError(Component.translatable("caramel.chameleon.change.exception", resource));
                    exception.printStackTrace();
                    return -1;
                }
            })
        ));
    }
}
