package com.github.iunius118.tolaserblade.network;

import com.github.iunius118.tolaserblade.ToLaserBlade;
import com.github.iunius118.tolaserblade.ToLaserBladeConfig;
import com.github.iunius118.tolaserblade.client.ClientEventHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Supplier;

public class ServerConfigMessage {
    private static final Marker SVRCFGMSG_MARKER = MarkerManager.getMarker("ServerConfigMessage");

    private final boolean isEnabledBlockingWithLaserBladeInServer;

    public ServerConfigMessage(boolean enableBlockingWithLaserBlade) {
        isEnabledBlockingWithLaserBladeInServer = enableBlockingWithLaserBlade;
    }

    public static void encode(ServerConfigMessage msg, PacketBuffer buf) {
        buf.writeBoolean(msg.isEnabledBlockingWithLaserBladeInServer);
    }

    public static ServerConfigMessage decode(PacketBuffer buf) {
        return new ServerConfigMessage(buf.readBoolean());
    }

    public static void handle(ServerConfigMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ToLaserBladeConfig.COMMON.isEnabledBlockingWithLaserBladeInServer = msg.isEnabledBlockingWithLaserBladeInServer ? () -> true : () -> false;
            ToLaserBlade.LOGGER.info(SVRCFGMSG_MARKER, "config.common.enabledBlockingWithLaserBladeInServer: {}", msg.isEnabledBlockingWithLaserBladeInServer);

            ClientEventHandler.checkUpdate();
        });

        ctx.get().setPacketHandled(true);
    }
}
