package fr.reden.voicechat.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemTalkieWalkie extends Item
{
    private boolean used = false;

    public ItemTalkieWalkie()
    {
        this.setMaxStackSize(1);

        this.addPropertyOverride(new ResourceLocation("activated"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            @ParametersAreNonnullByDefault
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return used ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (worldIn.isRemote)
        {
            this.used = !this.used;
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public NBTTagCompound getStackNBTTagCompound(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        NBTTagCompound nbt = getStackNBTTagCompound(stack);
        tooltip.add("Channel: " + ChatFormatting.GREEN + Integer.toString(nbt.getInteger("Channel")));
    }
}
