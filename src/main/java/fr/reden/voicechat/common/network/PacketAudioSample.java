package fr.reden.voicechat.common.network;

import fr.reden.voicechat.client.audio.AudioManager;
import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.item.ItemTalkieWalkie;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static fr.reden.voicechat.client.audio.AudioRecorder.SAMPLE_FORMAT;
import static fr.reden.voicechat.client.audio.AudioRecorder.SAMPLE_FREQUENCY;

public class PacketAudioSample implements IMessage
{
    private UUID senderUUID;
    private Set<Integer> frequencies;
    private int bufferSize;
    private ByteBuffer audioDataBuffer;

    //TODO Solve crash The received string length is longer than maximum allowed (22 > 20)

    public PacketAudioSample()
    {
    }

    public PacketAudioSample(UUID senderUUID, Set<Integer> frequencies, int bufferSize, ByteBuffer audioDataBuffer)
    {
        this.senderUUID = senderUUID;
        this.frequencies = frequencies;
        this.bufferSize = bufferSize;
        this.audioDataBuffer = audioDataBuffer;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.senderUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));

        this.frequencies = new TreeSet<>();
        int n = buf.readInt();

        for (int i = 0; i < n; i++)
        {
            this.frequencies.add(buf.readInt());
        }

        this.bufferSize = buf.readInt();
        this.audioDataBuffer = BufferUtils.createByteBuffer(this.bufferSize);
        buf.readBytes(this.audioDataBuffer);
        this.audioDataBuffer.rewind();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.senderUUID.toString());

        buf.writeInt(this.frequencies.size());

        for (int frequency : this.frequencies)
        {
            buf.writeInt(frequency);
        }

        buf.writeInt(this.bufferSize);

        //TODO le passage par le copiedBuffer n'est sûrement pas nécessaire ici, à modifier
        ByteBuf copy = Unpooled.copiedBuffer(this.audioDataBuffer);
        buf.writeBytes(copy);
    }

    public static class ServerHandler implements IMessageHandler<PacketAudioSample, IMessage>
    {
        @Override
        public IMessage onMessage(PacketAudioSample message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
            {
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

                //On envoie le paquet sur les fréquences communes utilisées par l'émetteur et les récepteurs
                playerList.getPlayers().stream().filter(playerMP -> !playerMP.getUniqueID().equals(message.senderUUID)).forEach(playerMP ->
                {
                    Set<Integer> commonFrequencies = new TreeSet<>();
                    for (ItemStack stack : playerMP.inventory.mainInventory)
                    {
                        if (!stack.isEmpty() && stack.getItem() == ItemRegistry.TALKIE_WALKIE)
                        {
                            boolean activated = ((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).isActivated(stack);
                            int frequency = ((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).getFrequency(stack);

                            if (activated && message.frequencies.contains(frequency))
                            {
                                commonFrequencies.add(frequency);
                            }
                        }
                    }

                    if (commonFrequencies.size() > 0)
                    {
                        NetworkManager.getInstance().getNetworkWrapper().sendTo(new PacketAudioSample(message.senderUUID, commonFrequencies, message.bufferSize, message.audioDataBuffer), playerMP);
                    }
                });

            });

            return null;
        }
    }

    public static class ClientHandler implements IMessageHandler<PacketAudioSample, IMessage>
    {

        @Override
        public IMessage onMessage(PacketAudioSample message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                message.audioDataBuffer.rewind();
                int buf = AL10.alGenBuffers();
                AL10.alBufferData(buf, SAMPLE_FORMAT, message.audioDataBuffer, SAMPLE_FREQUENCY);
                //TODO Changer la source en fonction de la fréquence
                AudioManager.getInstance().testPlaybackSource.pushBufferToQueue(buf, true);
                AudioManager.getInstance().testPlaybackSource.popBufferFromQueue();
            });
            return null;
        }
    }
}
