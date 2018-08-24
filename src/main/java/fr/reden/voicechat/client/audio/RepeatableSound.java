package fr.reden.voicechat.client.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class RepeatableSound implements ISound, ITickableSound
{
    private Sound sound;
    private SoundEventAccessor soundEventAccessor;
    private final ResourceLocation soundLocation;
    private float volume;
    private boolean isDonePlaying = false;

    public RepeatableSound(SoundEvent soundEvent, float volume)
    {
        this.soundLocation = soundEvent.getSoundName();
        this.volume = volume;
    }

    @Override
    public void update()
    {

    }

    public void stop()
    {
        this.isDonePlaying = true;
    }

    @Override
    public ResourceLocation getSoundLocation()
    {
        return this.soundLocation;
    }

    @Override
    public SoundEventAccessor createAccessor(SoundHandler handler)
    {
        this.soundEventAccessor = handler.getAccessor(this.soundLocation);

        if (this.soundEventAccessor == null)
        {
            this.sound = SoundHandler.MISSING_SOUND;
        }
        else
        {
            this.sound = this.soundEventAccessor.cloneEntry();
        }

        return this.soundEventAccessor;
    }

    @Override
    public Sound getSound()
    {
        return this.sound;
    }

    @Override
    public SoundCategory getCategory()
    {
        return SoundCategory.RECORDS;
    }

    @Override
    public boolean canRepeat()
    {
        return true;
    }

    @Override
    public int getRepeatDelay()
    {
        return 0;
    }

    @Override
    public float getVolume()
    {
        return this.volume;
    }

    @Override
    public float getPitch()
    {
        return 1;
    }

    @Override
    public float getXPosF()
    {
        return (float) Minecraft.getMinecraft().player.posX;
    }

    @Override
    public float getYPosF()
    {
        return (float) Minecraft.getMinecraft().player.posY;
    }

    @Override
    public float getZPosF()
    {
        return (float) Minecraft.getMinecraft().player.posZ;
    }

    @Override
    public AttenuationType getAttenuationType()
    {
        return AttenuationType.NONE;
    }

    @Override
    public boolean isDonePlaying()
    {
        return isDonePlaying;
    }
}
