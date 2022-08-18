package moe.caramel.chameleon.gui;

import com.mojang.blaze3d.platform.MacosUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import moe.caramel.chameleon.util.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.io.IOException;

public final class ChangeDockIconScreen extends Screen {

    private static final int COLOR_WHITE = 0xFFFFFF;

    private final Screen lastScreen;
    private IconSelectionList iconSelectionList;

    public ChangeDockIconScreen(Screen parent) {
        super(Component.translatable("caramel.chameleon.modmenu.title"));
        this.lastScreen = parent;
    }

    @Override
    protected void init() {
        this.iconSelectionList = new IconSelectionList(this.minecraft);
        this.addWidget(this.iconSelectionList);
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 30, 200, 20, CommonComponents.GUI_DONE, (button) -> {
            final IconSelectionList.Entry entry = this.iconSelectionList.getSelected();
            if (entry != null && !ModConfig.getInstance().iconLocation.get().equals(entry.icon)) {
                try {
                    ModConfig.changeIcon(this.minecraft, entry.icon);
                } catch (IOException exception) {
                    exception.printStackTrace();
                    this.minecraft.getToasts().addToast(new SystemToast(
                        SystemToast.SystemToastIds.PACK_LOAD_FAILURE,
                        Component.translatable("caramel.chameleon.change.exception.title"),
                        Component.translatable("caramel.chameleon.change.exception")
                    ));
                }
            }
            this.minecraft.setScreen(this.lastScreen);
        }));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        this.iconSelectionList.render(poseStack, mouseX, mouseY, delta);
        drawCenteredString(poseStack, this.font, this.getTitle(), this.width / 2, 12, COLOR_WHITE);
        super.render(poseStack, mouseX, mouseY, delta);
    }


    /**
     * Icon Selection list
     */
    private final class IconSelectionList extends ObjectSelectionList<IconSelectionList.Entry> {

        public IconSelectionList(Minecraft client) {
            super(
                client, ChangeDockIconScreen.this.width, ChangeDockIconScreen.this.height,
                32, ChangeDockIconScreen.this.height - 40, 20
            );

            for (final var resource : ModConfig.GET_ICON_SET.apply(client)) {
                final Entry entry = new Entry(resource);
                this.addEntry(entry);
                if (ModConfig.getInstance().iconLocation.get().equals(resource)) {
                    this.setSelected(entry);
                }
            }

            if (this.getSelected() != null) {
                this.centerScrollOn(this.getSelected());
            }
        }

        @Override
        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + 20;
        }

        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        @Override
        protected boolean isFocused() {
            return ChangeDockIconScreen.this.getFocused() == this;
        }


        /**
         * Icon Selection list Entry
         */
        public class Entry extends ObjectSelectionList.Entry<Entry> {

            private final ResourceLocation icon;

            public Entry(ResourceLocation icon) {
                this.icon = icon;
            }

            @Override
            public void render(
                PoseStack poseStack, int currentCount, int j, int k, int l, int m,
                int mouseX, int mouseY, boolean hover, float delta
            ) {
                final String iconLocation = this.icon.toString();
                ChangeDockIconScreen.this.font.drawShadow(
                    poseStack, iconLocation,
                    IconSelectionList.this.width / 2 - ChangeDockIconScreen.this.font.width(iconLocation) / 2,
                    j + 3.5f, COLOR_WHITE, true
                );
            }

            @Override
            public boolean mouseClicked(double xPos, double yPos, int action) {
                if (action != 0) return false;
                IconSelectionList.this.setSelected(this);
                return true;
            }

            @Override
            public Component getNarration() {
                return Component.literal(this.icon.toString());
            }
        }
    }
}
