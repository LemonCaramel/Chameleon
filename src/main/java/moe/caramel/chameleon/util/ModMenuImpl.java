package moe.caramel.chameleon.util;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import moe.caramel.chameleon.gui.ChangeDockIconScreen;
import net.minecraft.client.gui.screens.Screen;

/**
 The type Mod menu.
 */
public final class ModMenuImpl implements ModMenuApi{
	
	@Override
	public ConfigScreenFactory<? extends Screen> getModConfigScreenFactory(){
		return ChangeDockIconScreen::new;
	}
}
