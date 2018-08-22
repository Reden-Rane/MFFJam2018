package fr.reden.voicechat.common.network;

import fr.reden.voicechat.client.audio.AudioManager;
import fr.reden.voicechat.common.NetworkManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.nio.ByteBuffer;

import static fr.reden.voicechat.client.audio.AudioRecorder.SAMPLE_FORMAT;
import static fr.reden.voicechat.client.audio.AudioRecorder.SAMPLE_FREQUENCY;

public class PacketAudioSample implements IMessage, IMessageHandler<PacketAudioSample, IMessage>
{
    private int bufferSize;
    private ByteBuffer audioDataBuffer;

    public PacketAudioSample()
    {
    }

    public PacketAudioSample(int bufferSize, ByteBuffer audioDataBuffer)
    {
        this.bufferSize = bufferSize;
        this.audioDataBuffer = audioDataBuffer;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.bufferSize = buf.readInt();
        this.audioDataBuffer = BufferUtils.createByteBuffer(this.bufferSize);
        buf.readBytes(this.audioDataBuffer);
        this.audioDataBuffer.rewind();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        //TODO le passage par le copiedBuffer n'est sûrement pas nécessaire ici, à modifier
        ByteBuf copy = Unpooled.copiedBuffer(this.audioDataBuffer);
        buf.writeInt(this.bufferSize);
        buf.writeBytes(copy);
    }

    @Override
    public IMessage onMessage(PacketAudioSample message, MessageContext ctx)
    {
        if (ctx.side.isServer())
        {
            //Renvoie simplement l'audio vers les joueurs connectés au serveur
            //TODO Envoyer uniquement aux joueurs sur le bon channel
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> NetworkManager.getInstance().getNetworkWrapper().sendToAll(new PacketAudioSample(message.bufferSize, message.audioDataBuffer)));
        }
        else
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                message.audioDataBuffer.rewind();
                int buf = AL10.alGenBuffers();
                AL10.alBufferData(buf, SAMPLE_FORMAT, message.audioDataBuffer, SAMPLE_FREQUENCY);
                AudioManager.getInstance().testPlaybackSource.pushBufferToQueue(buf, true);
                AudioManager.getInstance().testPlaybackSource.popBufferFromQueue();
            });
        }

        return null;
    }
}
