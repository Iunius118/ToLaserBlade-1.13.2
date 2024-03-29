package com.github.iunius118.tolaserblade.network;

import com.github.iunius118.tolaserblade.ToLaserBlade;
import com.github.iunius118.tolaserblade.ToLaserBladeConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Supplier;

public class ServerConfigMessage {
    private static final Marker SVRCFGMSG_MARKER = MarkerManager.getMarker("ServerConfigMessage");

    private final boolean isEnabledBlockingWithLaserBladeInServer;
    private final int laserBladeEfficiencyInServer;

    public ServerConfigMessage(ToLaserBladeConfig.ServerConfig serverConfig) {
        isEnabledBlockingWithLaserBladeInServer = serverConfig.isEnabledBlockingWithLaserBladeInServer;
        laserBladeEfficiencyInServer = serverConfig.laserBladeEfficiencyInServer;
    }

    public static void encode(ServerConfigMessage msg, PacketBuffer buf) {
        buf.writeBoolean(msg.isEnabledBlockingWithLaserBladeInServer);
        buf.writeInt(msg.laserBladeEfficiencyInServer);
    }

    public static ServerConfigMessage decode(PacketBuffer buf) {
        ToLaserBladeConfig.ServerConfig serverConfig = new ToLaserBladeConfig.ServerConfig();
        serverConfig.isEnabledBlockingWithLaserBladeInServer = buf.readBoolean();
        serverConfig.laserBladeEfficiencyInServer = buf.readInt();
        return new ServerConfigMessage(serverConfig);
    }

    public static void handle(ServerConfigMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ToLaserBladeConfig.COMMON.isEnabledBlockingWithLaserBladeInServer = msg.isEnabledBlockingWithLaserBladeInServer ? () -> true : () -> false;
            ToLaserBlade.LOGGER.info(SVRCFGMSG_MARKER, "config.common.enabledBlockingWithLaserBladeInServer: {}", msg.isEnabledBlockingWithLaserBladeInServer);

            ToLaserBladeConfig.COMMON.laserBladeEfficiencyInServer = () -> msg.laserBladeEfficiencyInServer;
            ToLaserBlade.LOGGER.info(SVRCFGMSG_MARKER, "config.common.laserBladeEfficiencyInServer: {}", msg.laserBladeEfficiencyInServer);
        });

        ctx.get().setPacketHandled(true);
    }
}
