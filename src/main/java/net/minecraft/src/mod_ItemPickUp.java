package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class mod_ItemPickUp extends BaseMod {
    private boolean installedForCurrentWorld = false;

    public mod_ItemPickUp() {
        ModLoader.SetInGameHook(this, true, false);
    }

    @Override
    public String Version() {
        return "1.0.1";
    }

    @Override
    public void OnItemPickup(EntityPlayer player, ItemStack item) {
        Minecraft mc = ModLoader.getMinecraftInstance();

        if (mc == null || mc.thePlayer == null || player != mc.thePlayer) {
            return;
        }

        int amount = item.stackSize;
        if (amount <= 0) {
            amount = 1;
        }

        PickupUI.addPickup(item, amount);
    }

    @Override
    public boolean OnTickInGame(Minecraft minecraft) {
        if (minecraft == null || minecraft.theWorld == null || minecraft.thePlayer == null) {
            installedForCurrentWorld = false;
            PickupUI.clearEntries();
            return true;
        }

        if (!installedForCurrentWorld) {
            minecraft.ingameGUI = new PickupUI(minecraft, minecraft.ingameGUI);
            installedForCurrentWorld = true;
        }

        return true;
    }
}