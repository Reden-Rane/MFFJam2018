package fr.reden.voicechat.common.network.audio.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SideOnly(Side.CLIENT)
public class AudioNetworkServerInfo
{
    private final int serverAudioPort;

    public AudioNetworkServerInfo(int serverAudioPort)
    {
        this.serverAudioPort = serverAudioPort;
    }

    public InetAddress getAddress() throws IllegalStateException, UnknownHostException
    {
        ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();

        if (serverData == null)
        {
            return InetAddress.getByName("localhost");
        }

        return InetAddress.getByName(ServerAddress.fromString(serverData.serverIP).getIP());
    }

    public int getServerAudioPort()
    {
        return serverAudioPort;
    }
}
