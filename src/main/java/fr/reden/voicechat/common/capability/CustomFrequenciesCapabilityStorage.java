package fr.reden.voicechat.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class CustomFrequenciesCapabilityStorage implements Capability.IStorage<CustomFrequenciesCapability>
{

    @Override
    public NBTBase writeNBT(Capability<CustomFrequenciesCapability> capability, CustomFrequenciesCapability instance, EnumFacing side)
    {
        return null;
    }

    @Override
    public void readNBT(Capability<CustomFrequenciesCapability> capability, CustomFrequenciesCapability instance, EnumFacing side, NBTBase nbt)
    {

    }

}