package moe.caramel.chameleon.command;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import moe.caramel.chameleon.gui.ChangeDockIconScreen;
import moe.caramel.chameleon.util.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
//import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.NoSuchElementException;

/*
 * The type Chameleon command.
 */
public final class ChameleonCommand {

    private static final String ICON_NAME = "icon name";
    private static final SuggestionProvider<FabricClientCommandSource> SUGGEST = (context, builder) -> {
        final Minecraft client = Minecraft.getInstance();
	    for (final Identifier resource : ModConfig.GET_ICON_SET.apply(client)){
            builder.suggest(resource.toString());
        }
        return builder.buildFuture();
    };
	
	/**
	 Register.
	 
	 @param dispatcher the dispatcher
	 */
	public static void register(final @NotNull CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(literal("chameleon").executes(context -> {
            final Minecraft client = Minecraft.getInstance();
	        client.wrapRunnable(() -> client.setScreenAndShow(new ChangeDockIconScreen(null)));
	        return 0;
        }).then(argument(ICON_NAME, IdentifierArgument.id()).suggests(SUGGEST).executes(context -> {
                final Minecraft client = Minecraft.getInstance();
                final FabricClientCommandSource source = context.getSource();
	        final Identifier resource = context.getArgument(ICON_NAME, Identifier.class);

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