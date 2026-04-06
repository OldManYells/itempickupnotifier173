package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class mod_ItemPickUp extends BaseMod {
    public static String welcomeMessage = "\247eItem PickUp mod ready.";
    public static boolean showFacing = true;

    private boolean announcedForCurrentWorld = false;

    public mod_ItemPickUp() {
        ModLoader.SetInGameHook(this, true, false);
    }

    @Override
    public String Version() {
        return "1.0.0";
    }

    @Override
    public void OnItemPickup(EntityPlayer player, ItemStack item) {
        Minecraft mc = ModLoader.getMinecraftInstance();

        if (mc == null || mc.thePlayer == null || player != mc.thePlayer) {
            return;
        }

        if (!(mc.ingameGUI instanceof PickupUI)) {
            mc.ingameGUI = new PickupUI(mc);
        }

        int amount = item.stackSize;
        if (amount <= 0) {
            amount = 1;
        }

        PickupUI.addPickup(item, amount);
    }

    @Override
    public boolean OnTickInGame(Minecraft minecraft) {
        if (minecraft.theWorld == null || minecraft.thePlayer == null) {
            announcedForCurrentWorld = false;
            return true;
        }


        if (!(minecraft.ingameGUI instanceof PickupUI)) {
            minecraft.ingameGUI = new PickupUI(minecraft);
        }

        if (!announcedForCurrentWorld) {
            minecraft.ingameGUI.addChatMessage(welcomeMessage);
            announcedForCurrentWorld = true;
        }

        return true;
    }
}