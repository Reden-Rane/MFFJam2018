package fr.reden.voicechat.common.network;

import fr.reden.voicechat.common.CommonProxy;
import fr.reden.voicechat.common.capability.CustomFrequency;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PacketCustomFrequenciesCapability implements IMessage
{

    public List<CustomFrequency> frequencyList;

    public PacketCustomFrequenciesCapability()
    {
    }

    public PacketCustomFrequenciesCapability(List<CustomFrequency> frequencyList)
    {
        this.frequencyList = frequencyList;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.frequencyList = new ArrayList<>();

        int size = buf.readInt();
        for (int i = 0; i < size; i++)
        {
            String name = ByteBufUtils.readUTF8String(buf);
            int frequency = buf.readInt();
            this.frequencyList.add(new CustomFrequency(name, frequency));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.frequencyList.size());
        this.frequencyList.forEach((frequency) ->
        {
            ByteBufUtils.writeUTF8String(buf, frequency.name);
            buf.writeInt(frequency.frequency);
        });
    }

    public static class ServerHandler implements IMessageHandler<PacketCustomFrequenciesCapability, IMessage>
    {

        @Override
        public IMessage onMessage(PacketCustomFrequenciesCapability message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new PacketFrequencyTask(ctx.getServerHandler().player, message));
            return null;
        }
    }

    public static class ClientHandler implements IMessageHandler<PacketCustomFrequenciesCapability, IMessage>
    {

        @Override
        public IMessage onMessage(PacketCustomFrequenciesCapability message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(new PacketFrequencyTask(null, message));
            return null;
        }
    }

    public static class PacketFrequencyTask implements Runnable
    {
        private EntityPlayer player;
        private PacketCustomFrequenciesCapability message;

        public PacketFrequencyTask(EntityPlayer player, PacketCustomFrequenciesCapability message)
        {
            this.player = player;
            this.message = message;
        }

        @Override
        public void run()
        {
            EntityPlayer player = this.player == null ? getPlayer() : this.player;
            player.getCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null).setCustomFrequencyList(message.frequencyList);
        }

        @SideOnly(Side.CLIENT)
        private EntityPlayer getPlayer()
        {
            return Minecraft.getMinecraft().player;
        }

    }
}
