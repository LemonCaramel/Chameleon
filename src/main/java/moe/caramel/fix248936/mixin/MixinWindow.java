package moe.caramel.fix248936.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;

/**
 * for Minecraft ~1.17.1 client
 */
@Mixin(Window.class)
public final class MixinWindow {

    @Inject(method = "setIcon", at = @At(value = "HEAD"), cancellable = true)
    public void setIcon(InputStream inputStream, InputStream inputStream2, CallbackInfo ci) {
        if (Minecraft.ON_OSX) ci.cancel();
    }
}
