package moe.caramel.chameleon.mixin;

import com.mojang.blaze3d.platform.MacosUtil;
import com.mojang.blaze3d.platform.Window;
import moe.caramel.chameleon.Main;
import moe.caramel.chameleon.util.ModConfig;
import moe.caramel.chameleon.util.ResourceIo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.main.GameConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.InputStream;
import java.util.Optional;
import java.io.IOException;

import static moe.caramel.chameleon.util.ModConfig.*;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public abstract ResourceManager getResourceManager();
    @Shadow @Final private Window window;

    @Redirect(
        method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/MacosUtil;loadIcon(Lnet/minecraft/server/packs/resources/IoSupplier;)V"
        )
    )
    public void loadCancelMac(IoSupplier<InputStream> ioSupplier) throws IOException {
        ioSupplier.get().close(); // No
    }

    @Redirect(
        method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/Window;setIcon(Lnet/minecraft/server/packs/resources/IoSupplier;Lnet/minecraft/server/packs/resources/IoSupplier;)V"
        )
    )
    public void loadCancelWin(Window instance, IoSupplier<InputStream> smallSupplier, IoSupplier<InputStream> bigSupplier) throws IOException {
        smallSupplier.get().close(); // Nono
        bigSupplier.get().close(); // Nonono
    }

    // Run after all resources are loaded
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void loadMinecraftIcon(GameConfig gameConfig, CallbackInfo ci) throws IOException {
        final ModConfig config = ModConfig.getInstance();

        final ResourceLocation location = config.iconLocation.get();
        Optional<Resource> resource = this.getResourceManager().getResource(location);
        if (resource.isEmpty()) {
            Main.INIT_TOAST_QUEUE.add(new SystemToast(
                SystemToast.SystemToastIds.PACK_LOAD_FAILURE,
                Component.translatable("caramel.chameleon.resetToast.title"),
                Component.translatable("caramel.chameleon.resetToast.desc")
            ));
            final ResourceLocation icon = (Minecraft.ON_OSX ? ORIGINAL_MAC_ICON : ORIGINAL_WIN_ICON);
            config.iconLocation.update(null, icon);
            resource = this.getResourceManager().getResource(icon);
        }
        if (resource.isPresent()) { // um..
            final IoSupplier<InputStream> iconSupplier = ResourceIo.create(resource.get());
            if (Minecraft.ON_OSX) {
                MacosUtil.loadIcon(iconSupplier);
            } else {
                this.window.setIcon(iconSupplier, iconSupplier);
            }
        }
    }
}
