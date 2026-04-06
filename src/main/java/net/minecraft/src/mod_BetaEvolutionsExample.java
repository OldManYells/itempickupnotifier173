package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class mod_BetaEvolutionsExample extends BaseMod {
    public static String welcomeMessage = "\247eExample mod ready. Press G to show your position.";

    public static boolean showFacing = true;

    private static final KeyBinding POSITION_KEY = new KeyBinding("Example Mod Position", Keyboard.KEY_G);

    private boolean announcedForCurrentWorld = false;

    public mod_BetaEvolutionsExample() {
        ModLoader.RegisterKey(this, POSITION_KEY, false);
        ModLoader.SetInGameHook(this, true, false);
    }

    @Override
    public String Version() {
        return "1.0.0-template";
    }

    @Override
    public void KeyboardEvent(KeyBinding keyBinding) {
        Minecraft minecraft = ModLoader.getMinecraftInstance();

        int x = MathHelper.floor_double(minecraft.thePlayer.posX);
        int y = MathHelper.floor_double(minecraft.thePlayer.posY);
        int z = MathHelper.floor_double(minecraft.thePlayer.posZ);
        String message = "\247bPosition:\247f " + x + ", " + y + ", " + z;

        if (showFacing) {
            message += " \2477(" + getFacingName(minecraft.thePlayer.rotationYaw) + ")";
        }

        minecraft.ingameGUI.addChatMessage(message);
    }

    @Override
    public boolean OnTickInGame(Minecraft minecraft) {
        // Reset the announcement flag when leaving the world or going to the main menu
        if (minecraft.theWorld == null || minecraft.thePlayer == null) {
            announcedForCurrentWorld = false;
            return true;
        }

        // Don't announce again
        if (announcedForCurrentWorld) {
            return true;
        }

        // Announce when joining a world
        minecraft.ingameGUI.addChatMessage(welcomeMessage);
        announcedForCurrentWorld = true;
        return true;
    }

    // Get the direction based on yaw
    private static String getFacingName(float yaw) {
        int direction = MathHelper.floor_double((double) (yaw * 4.0F / 360.0F) + 0.5D) & 3;
        switch (direction) {
            case 0:
                return "South";
            case 1:
                return "West";
            case 2:
                return "North";
            default:
                return "East";
        }
    }
}
