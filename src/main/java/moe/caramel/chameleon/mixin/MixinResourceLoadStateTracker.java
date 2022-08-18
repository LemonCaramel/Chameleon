package moe.caramel.chameleon.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ResourceLoadStateTracker;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static moe.caramel.chameleon.Main.INIT_TOAST_QUEUE;

@Mixin(ResourceLoadStateTracker.class)
public final class MixinResourceLoadStateTracker {

    @Shadow @Nullable private ResourceLoadStateTracker.ReloadState reloadState;

    /**
     * Don't show toast before the font is loaded.
     */
    @Inject(method = "finishReload", at = @At(value = "TAIL"))
    public void finishReload(CallbackInfo ci) {
        if (this.reloadState == null) return;

        final ResourceLoadStateTracker.ReloadReason reason = this.reloadState.reloadReason;
        if (reason != ResourceLoadStateTracker.ReloadReason.INITIAL) return;

        final Minecraft client = Minecraft.getInstance();
        if (client == null) return; // ???

        while (!INIT_TOAST_QUEUE.isEmpty()) {
            client.getToasts().addToast(INIT_TOAST_QUEUE.poll());
        }
    }
}
