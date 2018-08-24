package fr.reden.voicechat.common.network.audio.auth;

import fr.reden.voicechat.common.VoiceChatMod;
import fr.reden.voicechat.common.network.audio.server.AudioNetworkServerBound;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketClientInfo implements IMessage
{
    private int audioPort;

    public CPacketClientInfo()
    {
    }

    public CPacketClientInfo(int audioPort)
    {
        this.audioPort = audioPort;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.audioPort = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.audioPort);
    }

    public static class ServerHandler implements IMessageHandler<CPacketClientInfo, IMessage>
    {
        @Override
        public IMessage onMessage(CPacketClientInfo message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
            {
                EntityPlayerMP playerMP = ctx.getServerHandler().player;
                if (VoiceChatMod.getProxy().getAudioNetworkServerBound() != null)
                {
                    AudioNetworkServerBound serverBound = VoiceChatMod.getProxy().getAudioNetworkServerBound();
                    serverBound.authenticate(playerMP, message.audioPort);
                }
            });

            return null;
        }
    }
}
