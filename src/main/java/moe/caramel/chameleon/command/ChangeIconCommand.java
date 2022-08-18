package moe.caramel.chameleon.command;

import com.mojang.blaze3d.platform.MacosUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import moe.caramel.chameleon.util.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.NoSuchElementException;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class ChangeIconCommand {

    private static final String ICON_NAME = "icon name";
    private static final SuggestionProvider<FabricClientCommandSource> SUGGEST = (context, builder) -> {
        final var client = Minecraft.getInstance();
        for (final var resource : ModConfig.GET_ICON_SET.apply(client)) {
            builder.suggest(resource.toString());
        }
        return builder.buildFuture();
    };

    public static void register(@NotNull final CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("changeicon").then(
            argument(ICON_NAME, ResourceLocationArgument.id()).suggests(SUGGEST).executes(context -> {
                final var client = Minecraft.getInstance();
                final var source = context.getSource();
                final var resource = context.getArgument(ICON_NAME, ResourceLocation.class);

                try {
                    ModConfig.changeIcon(client, resource);
                    source.sendFeedback(Component.translatable("caramel.chameleon.change.done", resource));
                    return 0;
                } catch (NoSuchElementException ignored) {
                    source.sendError(Component.translatable("caramel.chameleon.change.404", resource));
                    return -1;
                } catch (IOException exception) {
                    source.sendError(Component.translatable("caramel.chameleon.change.exception", resource));
                    exception.printStackTrace();
                    return -1;
                }
            })
        ));
    }
}
