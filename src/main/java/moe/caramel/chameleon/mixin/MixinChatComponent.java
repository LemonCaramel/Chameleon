package moe.caramel.chameleon.mixin;

import moe.caramel.chameleon.util.MacosUtil;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
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
        method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",
        at = @At(value = "TAIL")
    )
    private void addMessage(Component message, MessageSignature signature, int addedTime, GuiMessageTag tag, boolean system, CallbackInfo ci) {
        if (Minecraft.ON_OSX && this.newMessageSinceScroll) {
            MacosUtil.setBadgeLabel(MIDDLE_DOT);
        }
    }

    @Inject(method = "resetChatScroll", at = @At(value = "TAIL"))
    private void resetChatScroll(CallbackInfo ci) {
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
