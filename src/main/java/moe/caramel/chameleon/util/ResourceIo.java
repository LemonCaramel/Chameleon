package moe.caramel.chameleon.util;

import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStream;

/**
 The interface Resource io.
 */
public interface ResourceIo extends IoSupplier<Resource>{
	
	/**
	 Create io supplier.
	 
	 @param resource the resource
	 
	 @return the io supplier
	 */
	static IoSupplier<InputStream> create(final Resource resource){
		return resource::open;
	}
}
