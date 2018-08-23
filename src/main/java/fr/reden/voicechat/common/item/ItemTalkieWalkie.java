package fr.reden.voicechat.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import fr.reden.voicechat.client.audio.SoundRegistry;
import fr.reden.voicechat.common.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemTalkieWalkie extends Item
{

    public ItemTalkieWalkie()
    {
        this.setMaxStackSize(1);

        this.addPropertyOverride(new ResourceLocation("activated"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            @ParametersAreNonnullByDefault
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return isActivated(stack) ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack heldItem = player.getHeldItem(hand);

        if (player.isSneaking())
        {
            GuiHandler.openGui(player, GuiHandler.TALKIE_WALKIE_GUI_ID);
            return new ActionResult<>(EnumActionResult.FAIL, heldItem);
        }
        else
        {
            setActivated(heldItem, !isActivated(heldItem));
            return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
        }
    }

    public NBTTagCompound getStackNBTTagCompound(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound();
    }

    public boolean isActivated(ItemStack stack)
    {
        return getStackNBTTagCompound(stack).getBoolean("Activated");
    }

    public void setActivated(ItemStack stack, boolean activated)
    {
        getStackNBTTagCompound(stack).setBoolean("Activated", activated);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            if (activated)
            {
                Minecraft.getMinecraft().player.playSound(SoundRegistry.TALKIE_TRANSMISSION_NOTIFY_SOUND, 1, 1);
            }
            else
            {
                Minecraft.getMinecraft().player.playSound(SoundRegistry.TALKIE_TRANSMISSION_NOTIFY_SOUND, 1, 0.8f);
            }
        }
    }

    public int getFrequency(ItemStack stack)
    {
        return getStackNBTTagCompound(stack).getInteger("Frequency");
    }

    public void setFrequency(ItemStack stack, int frequency)
    {
        getStackNBTTagCompound(stack).setInteger("Frequency", frequency);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            Minecraft.getMinecraft().player.playSound(SoundRegistry.TALKIE_SWITCH_SOUND, 1, 1);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add("Frequency: " + ChatFormatting.GREEN + Integer.toString(getFrequency(stack)) + "Hz");
        tooltip.add(isActivated(stack) ? ChatFormatting.GREEN + "Activated" : ChatFormatting.RED + "Deactivated");
    }
}
