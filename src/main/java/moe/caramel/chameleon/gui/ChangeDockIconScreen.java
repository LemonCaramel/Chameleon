package moe.caramel.chameleon.gui;

import moe.caramel.chameleon.util.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public final class ChangeDockIconScreen extends Screen {

    private static final int COLOR_WHITE = 0xFFFFFFFF;

    private final Screen lastScreen;
    private IconSelectionList iconSelectionList;

    public ChangeDockIconScreen(final Screen parent) {
        super(Component.translatable("caramel.chameleon.modmenu.title"));
        this.lastScreen = parent;
    }

    @Override
    protected void init() {
        this.iconSelectionList = new IconSelectionList(this.minecraft);
        this.addWidget(this.iconSelectionList);
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            final IconSelectionList.Entry entry = this.iconSelectionList.getSelected();
            if (entry != null && !ModConfig.getInstance().iconLocation.get().equals(entry.icon)) {
                try {
                    ModConfig.changeIcon(this.minecraft, entry.icon);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                    this.minecraft.getToastManager().addToast(new SystemToast(
                        SystemToast.SystemToastId.PACK_LOAD_FAILURE,
                        Component.translatable("caramel.chameleon.change.exception.title"),
                        Component.translatable("caramel.chameleon.change.exception")
                    ));
                }
            }
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width / 2 - 100, this.height - 30, 200, 20).build());
    }

    @Override
    public void render(final GuiGraphics graphics, final int mouseX, final int mouseY, final float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.iconSelectionList.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.getTitle(), this.width / 2, 12, COLOR_WHITE);
    }


    /**
     * Icon Selection list
     */
    private final class IconSelectionList extends ObjectSelectionList<IconSelectionList.Entry> {

        public IconSelectionList(final Minecraft client) {
            super(client, ChangeDockIconScreen.this.width, ChangeDockIconScreen.this.height - 70, 32, 20);

            for (final ResourceLocation resource : ModConfig.GET_ICON_SET.apply(client)) {
                if (!Minecraft.ON_OSX && resource.getPath().endsWith(".icns")) {
                    continue;
                }

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
        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        @Override
        public boolean isFocused() {
            return ChangeDockIconScreen.this.getFocused() == this;
        }


        /**
         * Icon Selection list Entry
         */
        public class Entry extends ObjectSelectionList.Entry<Entry> {

            private final ResourceLocation icon;

            public Entry(final ResourceLocation icon) {
                this.icon = icon;
            }

            @Override
            public void render(
                final GuiGraphics graphics, final int index,
                final int y, final int x, final int rowWidth, final int rowHeight,
                final int mouseX, final int mouseY, final boolean hover, final float delta
            ) {
                final String iconLocation = this.icon.toString();
                graphics.drawString(
                    ChangeDockIconScreen.this.font, iconLocation,
                    IconSelectionList.this.width / 2 - ChangeDockIconScreen.this.font.width(iconLocation) / 2,
                    y + 4, COLOR_WHITE, false
                );
            }

            @Override
            public boolean mouseClicked(final double xPos, final double yPos, final int action) {
                if (action != 0) {
                    return false;
                } else {
                    IconSelectionList.this.setSelected(this);
                    return true;
                }
            }

            @Override
            public @NotNull Component getNarration() {
                return Component.literal(this.icon.toString());
            }
        }
    }
}
