package iunius118.mods.tolaserblade;

import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ToLaserBladeConfig {
	public static class Common {
		public final BooleanValue isEnabledBlockingWithLaserBlade;
		public Supplier<Boolean> isEnabledBlockingWithLaserBladeInServer;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("ToLaserBlade's common settings.").push("common");

			isEnabledBlockingWithLaserBlade = builder
					.comment("Enable blocking with Laser Blade.")
					.translation("tolaserblade.configgui.enableBlockingWithLaserBlade")
					.define("enableBlockingWithLaserBlade", false);

			builder.pop();
		}
	}

	public static class Client {
		public final BooleanValue isEnabledLaserBlade3DModel;

		Client(ForgeConfigSpec.Builder builder) {
			builder.comment("ToLaserBlade's client side settings.").push("client");

			isEnabledLaserBlade3DModel = builder
					.comment("Enable Laser Blade to use 3D Model.")
					.translation("tolaserblade.configgui.enableLaserBlade3DModel")
					.define("enableLaserBlade3DModel", true);

			builder.pop();
		}
	}

	static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();

		COMMON.isEnabledBlockingWithLaserBladeInServer = () -> COMMON.isEnabledBlockingWithLaserBlade.get();
	}

	static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;
	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}
}
