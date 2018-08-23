package fr.reden.voicechat.common;

import fr.reden.voicechat.client.GuiTalkieWalkie;
import fr.reden.voicechat.common.item.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler
{
    public static final int TALKIE_WALKIE_GUI_ID = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == TALKIE_WALKIE_GUI_ID)
        {
            ItemStack stack = null;

            if (player.getHeldItemMainhand().getItem() == ItemRegistry.TALKIE_WALKIE)
            {
                stack = player.getHeldItemMainhand();
            }
            else if (player.getHeldItemOffhand().getItem() == ItemRegistry.TALKIE_WALKIE)
            {
                stack = player.getHeldItemOffhand();
            }

            if (stack != null)
            {
                return new GuiTalkieWalkie(player, stack);
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void openGui(EntityPlayer player, int ID)
    {
        openGui(ID, player, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @SideOnly(Side.CLIENT)
    public static void openGui(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        FMLNetworkHandler.openGui(player, VoiceChatMod.getInstance(), ID, world, x, y, z);
    }
}
