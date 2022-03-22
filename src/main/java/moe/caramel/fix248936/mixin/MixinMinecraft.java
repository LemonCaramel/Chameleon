package moe.caramel.fix248936.mixin;

import moe.caramel.fix248936.MacOsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public abstract ClientPackSource getClientPackSource();

    @Inject(
        method = "<init>",
        at = @At(
            value = "TAIL",
            target = "Lnet/minecraft/client/Minecraft;setWindowActive(Z)V"
        )
    )
    public void loadMinecraftIcon(GameConfig gameConfig, CallbackInfo ci) throws IOException {
        if (!Minecraft.ON_OSX) return;
        MacOsUtil.loadIcon(this.getClientPackSource().getVanillaPack().getResource(PackType.CLIENT_RESOURCES, new ResourceLocation("icons/minecraft.icns")));
    }
}
