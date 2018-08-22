package fr.reden.voicechat.common.item;

import fr.reden.voicechat.common.VoiceChatMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = VoiceChatMod.MOD_ID)
public class ItemRegistry
{
    private static final List<Item> itemList = new ArrayList<>();

    public static final Item TALKIE_WALKIE;

    static
    {
        itemList.add(TALKIE_WALKIE = formatItem(new ItemTalkieWalkie(), "talkie_walkie", CreativeTabs.MISC));
    }

    public static Item formatItem(Item item, String name, CreativeTabs creativeTabs)
    {
        item.setRegistryName(new ResourceLocation(VoiceChatMod.MOD_ID, name)).setUnlocalizedName(name);
        if (creativeTabs != null)
        {
            item.setCreativeTab(creativeTabs);
        }
        return item;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        VoiceChatMod.logger.info("Registering items...");
        Item[] items = new Item[itemList.size()];
        itemList.toArray(items);
        event.getRegistry().registerAll(items);
        VoiceChatMod.logger.info("Registered items.");
    }

    public static void registerItemModels()
    {
        for (Item item : itemList)
        {
            String resourceName = item.getUnlocalizedName().substring(5).replace('.', ':');
            ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation(VoiceChatMod.MOD_ID, resourceName), "inventory");
            ModelLoader.setCustomModelResourceLocation(TALKIE_WALKIE, 0, model);
        }
    }

}