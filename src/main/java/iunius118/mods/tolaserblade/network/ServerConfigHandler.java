package iunius118.mods.tolaserblade.network;

import iunius118.mods.tolaserblade.ToLaserBlade;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ServerConfigHandler {
	public static final String VERSION = "1.0";
	public static final SimpleChannel channel = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(ToLaserBlade.MOD_ID, "config"))
			.clientAcceptedVersions(v -> true)
			.serverAcceptedVersions(v -> true)
			.networkProtocolVersion(() -> VERSION)
			.simpleChannel();

	public static void init() {
		channel.registerMessage(0, ServerConfigMessage.class, ServerConfigMessage::encode, ServerConfigMessage::decode, ServerConfigMessage::handle);
	}
}
