package net.minecraft.src;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class PickupUI extends GuiIngame {
    private static final long DISPLAY_TIME_MS = 4000L;
    private static final int MAX_ENTRIES = 6;

    private static final LinkedHashMap entries = new LinkedHashMap();
    private static final RenderItem itemRenderer = new RenderItem();

    private final Minecraft client;
    private final GuiIngame parent;

    public PickupUI(Minecraft minecraft, GuiIngame parent) {
        super(minecraft);
        this.client = minecraft;
        this.parent = parent;
    }

    public static void clearEntries() {
        entries.clear();
    }

    public static void addPickup(ItemStack stack, int amount) {
        if (stack == null) {
            return;
        }

        if (amount <= 0) {
            amount = 1;
        }

        String key = stack.itemID + ":" + getDamage(stack);

        PickupEntry entry = (PickupEntry) entries.remove(key);
        if (entry == null) {
            entry = new PickupEntry();
            entry.name = getDisplayName(stack);
            entry.count = amount;
        } else {
            entry.name = getDisplayName(stack);
            entry.count += amount;
        }

        ItemStack icon = stack.copy();
        icon.stackSize = 1;
        entry.iconStack = icon;

        entry.lastUpdated = System.currentTimeMillis();
        entries.put(key, entry);

        while (entries.size() > MAX_ENTRIES) {
            Object firstKey = entries.keySet().iterator().next();
            entries.remove(firstKey);
        }
    }

    @Override
    public void addChatMessage(String message) {
        if (parent != null) {
            parent.addChatMessage(message);
        } else {
            super.addChatMessage(message);
        }
    }

    @Override
    public void updateTick() {
        if (parent != null) {
            parent.updateTick();
        } else {
            super.updateTick();
        }
    }

    @Override
    public void renderGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY) {
        if (parent != null) {
            parent.renderGameOverlay(partialTicks, flag, mouseX, mouseY);
        } else {
            super.renderGameOverlay(partialTicks, flag, mouseX, mouseY);
        }

        if (client == null || client.theWorld == null || client.thePlayer == null) {
            return;
        }

        ScaledResolution sr =
                new ScaledResolution(client.gameSettings, client.displayWidth, client.displayHeight);

        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();
        long now = System.currentTimeMillis();

        ArrayList ordered = new ArrayList(entries.entrySet());
        int y = screenHeight - 6;

        for (int i = ordered.size() - 1; i >= 0; i--) {
            Map.Entry mapEntry = (Map.Entry) ordered.get(i);
            PickupEntry entry = (PickupEntry) mapEntry.getValue();

            if (now - entry.lastUpdated > DISPLAY_TIME_MS) {
                entries.remove(mapEntry.getKey());
                continue;
            }

            String text = entry.count + "x " + entry.name;
            int textWidth = client.fontRenderer.getStringWidth(text);

            int iconSize = 16;
            int gap = 2;

            int iconX = screenWidth - 6 - iconSize;
            int iconY = y - 14;

            int textX = iconX - gap - textWidth;
            int textY = y - 10;

            int left = textX - 4;
            int top = y - 14;
            int right = iconX + iconSize + 4;
            int bottom = y + 2;

            drawRect(left, top, right, bottom, 0x90000000);
            client.fontRenderer.drawStringWithShadow(text, textX, textY, 0xFFFFFF);

            if (entry.iconStack != null) {
                GL11.glPushMatrix();
                RenderHelper.enableStandardItemLighting();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                this.zLevel = 100.0F;

                itemRenderer.renderItemIntoGUI(
                        client.fontRenderer,
                        client.renderEngine,
                        entry.iconStack,
                        iconX,
                        iconY
                );

                this.zLevel = 0.0F;

                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glPopMatrix();
            }

            y -= 18;
        }
    }

    private static int getDamage(ItemStack stack) {
        try {
            return stack.getItemDamage();
        } catch (Throwable t) {
            return 0;
        }
    }

    private static String getDisplayName(ItemStack stack) {
        String raw = stack.getItemName();
        if (raw == null) {
            return "Unknown";
        }

        String translated = StringTranslate.getInstance().translateKey(raw);
        if (translated != null && translated.length() > 0 && !translated.equals(raw)) {
            return translated;
        }

        if (raw.startsWith("item.")) {
            raw = raw.substring(5);
        } else if (raw.startsWith("tile.")) {
            raw = raw.substring(5);
        }

        StringBuffer pretty = new StringBuffer();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);

            if (i == 0) {
                pretty.append(Character.toUpperCase(c));
            } else if (Character.isUpperCase(c)) {
                pretty.append(' ');
                pretty.append(c);
            } else {
                pretty.append(c);
            }
        }

        return pretty.toString();
    }

    private static class PickupEntry {
        public String name;
        public int count;
        public long lastUpdated;
        public ItemStack iconStack;
    }
}