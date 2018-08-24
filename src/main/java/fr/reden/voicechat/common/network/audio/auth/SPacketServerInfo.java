package fr.reden.voicechat.common.network.audio.auth;

import fr.reden.voicechat.client.ClientProxy;
import fr.reden.voicechat.common.network.audio.client.IAudioNetworkClientBound;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static fr.reden.voicechat.common.VoiceChatMod.getProxy;

public class SPacketServerInfo implements IMessage
{
    private int serverAudioPort;

    public SPacketServerInfo()
    {
    }

    public SPacketServerInfo(int serverAudioPort)
    {
        this.serverAudioPort = serverAudioPort;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.serverAudioPort = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.serverAudioPort);
    }

    public static class ClientHandler implements IMessageHandler<SPacketServerInfo, IMessage>
    {

        @Override
        public IMessage onMessage(SPacketServerInfo message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                IAudioNetworkClientBound clientBound = ((ClientProxy) getProxy()).getAudioNetworkClientBound();
                clientBound.markAsAuthenticated();
                clientBound.storeAudioServerPort(message.serverAudioPort);
            });

            return null;
        }
    }

}
