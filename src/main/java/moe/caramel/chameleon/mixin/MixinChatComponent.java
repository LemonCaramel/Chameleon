package moe.caramel.chameleon.mixin;

import moe.caramel.chameleon.util.MacosUtil;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public final class MixinChatComponent {

    @Unique private static final String MIDDLE_DOT = "•";
    @Shadow private boolean newMessageSinceScroll;

    @Inject(
        method = "addMessageToDisplayQueue",
        at = @At(value = "TAIL")
    )
    private void addMessage(final GuiMessage message, final CallbackInfo ci) {
        if (Minecraft.ON_OSX && this.newMessageSinceScroll) {
            MacosUtil.setBadgeLabel(MIDDLE_DOT);
        }
    }

    @Inject(method = "resetChatScroll", at = @At(value = "TAIL"))
    private void resetChatScroll(final CallbackInfo ci) {
        if (Minecraft.ON_OSX) {
            MacosUtil.setBadgeLabel(null);
        }
    }

    @Inject(method = "scrollChat", at = @At(value = "TAIL"))
    private void scrollChat(CallbackInfo ci) {
        if (Minecraft.ON_OSX && !this.newMessageSinceScroll) {
            MacosUtil.setBadgeLabel(null);
        }
    }
}
