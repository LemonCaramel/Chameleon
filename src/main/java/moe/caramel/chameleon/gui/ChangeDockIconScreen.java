package moe.caramel.chameleon.gui;

import moe.caramel.chameleon.Main;
import moe.caramel.chameleon.util.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 The type Change dock icon screen.
 */
public final class ChangeDockIconScreen extends Screen{
	
	private static final int COLOR_WHITE = 0xFFFFFFFF;
	
	private final Screen lastScreen;
	private IconSelectionList iconSelectionList;
	
	/**
	 Instantiates a new Change dock icon screen.
	 
	 @param parent the parent
	 */
	public ChangeDockIconScreen(final Screen parent){
		super(Component.translatable("caramel.chameleon.modmenu.title"));
		this.lastScreen = parent;
	}
	
	/**
	 Render.
	 
	 @param graphics the graphics
	 @param mouseX   the mouse x
	 @param mouseY   the mouse y
	 @param delta    the delta
	 */
	@Override
	public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float delta){
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		this.iconSelectionList.extractRenderState(graphics, mouseX, mouseY, delta);
		graphics.centeredText(this.font, this.getTitle(), this.width / 2, 12, COLOR_WHITE);
	}
	
	@Override
	protected void init(){
		this.iconSelectionList = new IconSelectionList(this.minecraft);
		this.addWidget(this.iconSelectionList);
		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
			final IconSelectionList.Entry entry = this.iconSelectionList.getSelected();
			if (entry != null && !ModConfig.getInstance().iconLocation.get().equals(entry.icon)){
				try{
					ModConfig.changeIcon(this.minecraft, entry.icon);
				} catch (final IOException exception){
					exception.printStackTrace();
					this.minecraft.gui.toastManager().addToast(new SystemToast(SystemToast.SystemToastId.PACK_LOAD_FAILURE, Component.translatable("caramel.chameleon.change.exception.title"), Component.translatable("caramel.chameleon.change.exception")));
				}
			}
			this.minecraft.setScreenAndShow(this.lastScreen);
		}).bounds(this.width / 2 - 100, this.height - 30, 200, 20).build());
	}
	
	/**
	 Icon Selection list
	 */
	private final class IconSelectionList extends ObjectSelectionList<IconSelectionList.Entry>{
		
		/**
		 Instantiates a new Icon selection list.
		 
		 @param client the client
		 */
		public IconSelectionList(final Minecraft client){
			super(client, ChangeDockIconScreen.this.width, ChangeDockIconScreen.this.height - 70, 32, 20);
			
			for (final Identifier resource : ModConfig.GET_ICON_SET.apply(client)){
				if (!Main.ON_OSX && resource.getPath().endsWith(".icns")){
					continue;
				}
				
				final Entry entry = new Entry(resource);
				this.addEntry(entry);
				if (ModConfig.getInstance().iconLocation.get().equals(resource)){
					this.setSelected(entry);
				}
			}
			
			if (this.getSelected() != null){
				this.centerScrollOn(this.getSelected());
			}
		}
		
		@Override
		public int getRowWidth(){
			return super.getRowWidth() + 50;
		}
		
		@Override
		public boolean isFocused(){
			return ChangeDockIconScreen.this.getFocused() == this;
		}
		
		
		/**
		 Icon Selection list Entry
		 */
		public class Entry extends ObjectSelectionList.Entry<Entry>{
			
			private final Identifier icon;
			
			/**
			 Instantiates a new Entry.
			 
			 @param icon the icon
			 */
			public Entry(final Identifier icon){
				this.icon = icon;
			}
			
			/**
			 Render content.
			 
			 @param graphics the graphics
			 @param mouseX   the mouse x
			 @param mouseY   the mouse y
			 @param hover    the hover
			 @param delta    the delta
			 */
			@Override
			public void extractContent(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hover, float delta){
				final String iconLocation = this.icon.toString();
				graphics.text(ChangeDockIconScreen.this.font, iconLocation, IconSelectionList.this.width / 2 - ChangeDockIconScreen.this.font.width(iconLocation) / 2, this.getY() + 6, COLOR_WHITE, false);
			}
			
			@Override
			public @NotNull Component getNarration(){
				return Component.literal(this.icon.toString());
			}
			
			@Override
			public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick){
				if (event.input() != 0){
					return false;
				} else{
					IconSelectionList.this.setSelected(this);
					return true;
				}
			}
		}
	}
}
