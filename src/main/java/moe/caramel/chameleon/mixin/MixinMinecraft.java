package moe.caramel.chameleon.mixin;

import com.mojang.blaze3d.platform.MacosUtil;
import moe.caramel.chameleon.util.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Optional;
import java.io.IOException;

import static moe.caramel.chameleon.util.ModConfig.ORIGINAL_ICON;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public abstract ResourceManager getResourceManager();

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/MacosUtil;loadIcon(Ljava/io/InputStream;)V"
        )
    )
    public void loadMinecraftIcon(GameConfig gameConfig, CallbackInfo ci) throws IOException {
        if (!Minecraft.ON_OSX) return;
        final var config = ModConfig.getInstance();

        Optional<Resource> resource = this.getResourceManager().getResource(config.iconLocation.get());
        if (resource.isEmpty()) {
            config.iconLocation.update(null, ORIGINAL_ICON);
            resource = this.getResourceManager().getResource(ORIGINAL_ICON);
        }
        if (resource.isPresent()) {
            MacosUtil.loadIcon(resource.get().open());
        }
    }
}
