package moe.caramel.fix248936.mixin;

import moe.caramel.fix248936.util.MacOsUtil;
import moe.caramel.fix248936.util.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static moe.caramel.fix248936.util.ModConfig.ORIGINAL_ICON;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public abstract ResourceManager getResourceManager();

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void loadMinecraftIcon(GameConfig gameConfig, CallbackInfo ci) throws IOException {
        if (!Minecraft.ON_OSX) return;
        final var config = ModConfig.getInstance();
        Resource resource = this.getResourceManager().getResource(ORIGINAL_ICON);
        try { resource = this.getResourceManager().getResource(config.iconLocation.get()); }
        catch (IOException ignored) { config.iconLocation.update(null, ORIGINAL_ICON); }
        MacOsUtil.loadIcon(resource.getInputStream());
    }
}
