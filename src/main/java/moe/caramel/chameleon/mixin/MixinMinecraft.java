package moe.caramel.chameleon.mixin;

import com.mojang.blaze3d.platform.MacosUtil;
import moe.caramel.chameleon.Main;
import moe.caramel.chameleon.util.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.main.GameConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.InputStream;
import java.util.Optional;
import java.io.IOException;

import static moe.caramel.chameleon.util.ModConfig.ORIGINAL_ICON;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public abstract ResourceManager getResourceManager();

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/MacosUtil;loadIcon(Ljava/io/InputStream;)V"
        )
    )
    public void dockIconLoadCancel(InputStream stream) throws IOException {
        stream.close(); // No
    }

    // Run after all resources are loaded
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void loadMinecraftIcon(GameConfig gameConfig, CallbackInfo ci) throws IOException {
        if (!Minecraft.ON_OSX) return;
        final var config = ModConfig.getInstance();

        final ResourceLocation location = config.iconLocation.get();
        Optional<Resource> resource = this.getResourceManager().getResource(location);
        if (resource.isEmpty()) {
            Main.INIT_TOAST_QUEUE.add(new SystemToast(
                SystemToast.SystemToastIds.PACK_LOAD_FAILURE,
                Component.translatable("caramel.chameleon.resetToast.title"),
                Component.translatable("caramel.chameleon.resetToast.desc")
            ));
            config.iconLocation.update(null, ORIGINAL_ICON);
            resource = this.getResourceManager().getResource(ORIGINAL_ICON);
        }
        if (resource.isPresent()) { // um..
            MacosUtil.loadIcon(resource.get().open());
        }
    }
}
