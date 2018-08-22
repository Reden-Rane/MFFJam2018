package fr.reden.voicechat.common.capability;

import fr.reden.voicechat.common.CommonProxy;
import fr.reden.voicechat.common.network.NetworkManager;
import fr.reden.voicechat.common.network.PacketCustomFrequenciesCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CustomFrequenciesCapability implements ICapabilityProvider, INBTSerializable<NBTTagCompound>
{
    private final EntityPlayer player;
    private List<CustomFrequency> customFrequencyList = new ArrayList<>();

    public CustomFrequenciesCapability(EntityPlayer player)
    {
        this.player = player;
    }

    public void sync()
    {
        PacketCustomFrequenciesCapability packet = new PacketCustomFrequenciesCapability(new ArrayList<>(this.getCustomFrequencyList()));
        if (!this.player.world.isRemote)
        {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            NetworkManager.getInstance().getNetworkWrapper().sendTo(packet, playerMP);
        }
        else
        {
            NetworkManager.getInstance().getNetworkWrapper().sendToServer(packet);
        }
    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(CustomFrequenciesCapability.class, new CustomFrequenciesCapabilityStorage(), new CustomFrequenciesCapabilityFactory());
    }

    @Override
    public boolean hasCapability(@Nonnull Capability capability, EnumFacing facing)
    {
        return CommonProxy.SAVED_FREQUENCIES_CAPABILITY != null && capability == CommonProxy.SAVED_FREQUENCIES_CAPABILITY;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
    {
        return CommonProxy.SAVED_FREQUENCIES_CAPABILITY != null && capability == CommonProxy.SAVED_FREQUENCIES_CAPABILITY ? (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {


        NBTTagCompound compound = new NBTTagCompound();
        for (int i = 0; i < customFrequencyList.size(); i++)
        {
            CustomFrequency customFrequency = customFrequencyList.get(i);
            NBTTagCompound customFrequencyNBT = new NBTTagCompound();
            customFrequencyNBT.setString("Name", customFrequency.name);
            customFrequencyNBT.setInteger("Frequency", customFrequency.frequency);
            compound.setTag(String.valueOf(i), customFrequencyNBT);
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound)
    {
        this.customFrequencyList.clear();

        for (int i = 0; i < compound.getKeySet().size(); i++)
        {
            NBTTagCompound customFrequencyNBT = compound.getCompoundTag(String.valueOf(i));
            String name = customFrequencyNBT.getString("Name");
            int frequency = customFrequencyNBT.getInteger("Frequency");
            this.customFrequencyList.add(new CustomFrequency(name, frequency));
        }
    }

    public void addCustomFrequency(CustomFrequency frequency)
    {
        this.customFrequencyList.add(frequency);
    }

    public void removeCustomFrequency(CustomFrequency frequency)
    {
        this.customFrequencyList.remove(frequency);
    }

    public void setCustomFrequencyList(List<CustomFrequency> customFrequencyList)
    {
        if (customFrequencyList != null)
        {
            this.customFrequencyList = customFrequencyList;
        }
    }

    public List<CustomFrequency> getCustomFrequencyList()
    {
        return this.customFrequencyList;
    }
}