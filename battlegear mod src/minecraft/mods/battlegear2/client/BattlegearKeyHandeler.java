package mods.battlegear2.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import mods.battlegear2.Battlegear;
import mods.battlegear2.gui.BattlegearGUIHandeler;
import mods.battlegear2.inventory.InventoryPlayerBattle;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;

public class BattlegearKeyHandeler extends KeyBindingRegistry.KeyHandler {

    public static KeyBinding battleInv = new KeyBinding("Battle Inventory", Keyboard.KEY_I);
    public static KeyBinding drawWeapons = new KeyBinding("Draw Weapons", Keyboard.KEY_R);

    //TODO: I will replace this with some sort of command in the future, this is primarily for testing.
    //public static KeyBinding openSigilEditor = new KeyBinding("Open Sigil Editor", Keyboard.KEY_P);

    private static int previousNormal = 0;
    private static int previousBattlemode = InventoryPlayerBattle.OFFSET;

    public BattlegearKeyHandeler() {
        //super(new KeyBinding[]{battleInv, drawWeapons, openSigilEditor}, new boolean[]{false, false, false});
        super(new KeyBinding[]{battleInv, drawWeapons}, new boolean[]{false, false});
    }

    @Override
    public String getLabel() {
        return "Battlegear2";
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb,
                        boolean tickEnd, boolean isRepeat) {


        Minecraft mc = FMLClientHandler.instance().getClient();

        //null checks to prevent any crash outside the world (and to make sure we have no screen open)
        if (mc != null && mc.thePlayer != null && mc.theWorld != null && mc.currentScreen == null) {

            EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;



            if (kb.keyCode == battleInv.keyCode) {

                //send packet to open container on server
                PacketDispatcher.sendPacketToServer(BattlegearGUIPacket.generatePacket(BattlegearGUIHandeler.equipID));
                //Also open on client
                System.out.println(player);
                System.out.println(Battlegear.INSTANCE);
                System.out.println(mc.theWorld);

                player.openGui(
                        Battlegear.INSTANCE, BattlegearGUIHandeler.equipID, mc.theWorld,
                        (int) player.posX, (int) player.posY, (int) player.posZ);


            } else
            if (kb.keyCode == drawWeapons.keyCode && tickEnd) {

                InventoryPlayer playerInventory = player.inventory;
                if (player.isBattlemode()) {
                    //i'd use int bounds check (0-8) for the item, just in case
                    previousBattlemode = playerInventory.currentItem;
                    playerInventory.currentItem = previousNormal;

                } else {
                    //i'd use int bounds check (0-8) for the item, just in case
                    previousNormal = playerInventory.currentItem;
                    playerInventory.currentItem = previousBattlemode;

                }
                mc.playerController.updateController();
            } /*else if (kb.keyCode == openSigilEditor.keyCode) {
                //send packet to open container on server
                PacketDispatcher.sendPacketToServer(BattlegearGUIPacket.generatePacket(BattlegearGUIHandeler.sigilEditor));
                player.openGui(
                        BattleGear.instance, BattlegearGUIHandeler.sigilEditor, mc.theWorld,
                        (int) player.posX, (int) player.posY, (int) player.posZ);
            }*/
        }
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

}