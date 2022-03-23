package moe.caramel.fix248936.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;

/**
 * for Minecraft ~1.17.1 client // fix GLFW error 65548
 */
@Mixin(Window.class)
public final class MixinWindow {

    @Inject(method = "setIcon", at = @At(value = "HEAD"), cancellable = true)
    public void setIcon(InputStream x16, InputStream x32, CallbackInfo ci) {
        if (Minecraft.ON_OSX) ci.cancel();
    }
}
