package com.chattriggers.ctjs.minecraft.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.EventLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.minecraft.wrappers.objects.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector3d;
import java.util.HashMap;

public class ClientListener {
    private int ticksPassed;
    private HashMap<Integer, Boolean> mouseState;
    private HashMap<Integer, Float[]> draggedState;

    public ClientListener() {
        this.ticksPassed = 0;

        this.mouseState = new HashMap<>();
        for (int i = 0; i < 5; i++)
            this.mouseState.put(i, false);
        draggedState = new HashMap<>();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (World.getWorld() == null) return;

        TriggerType.TICK.triggerAll(this.ticksPassed);
        this.ticksPassed++;
        
        Scoreboard.resetCache();
    }


    private void handleMouseInput() {
        if (!Mouse.isCreated()) return;

        for (int button = 0; button < 5; button++) {
            handleDragged(button);

            // normal clicked
            if (Mouse.isButtonDown(button) == this.mouseState.get(button)) continue;
            TriggerType.CLICKED.triggerAll(Client.getMouseX(), Client.getMouseY(), button, Mouse.isButtonDown(button));
            this.mouseState.put(button, Mouse.isButtonDown(button));

            // add new dragged
            if (Mouse.isButtonDown(button))
                this.draggedState.put(button, new Float[]{Client.getMouseX(), Client.getMouseY()});

            // remove old dragged
            if (Mouse.isButtonDown(button)) continue;
            if (!this.draggedState.containsKey(button)) continue;
            this.draggedState.remove(button);
        }
    }

    private void handleDragged(int button) {
        if (!this.draggedState.containsKey(button))
            return;

        TriggerType.DRAGGED.triggerAll(
                Client.getMouseX() - this.draggedState.get(button)[0],
                Client.getMouseY() - this.draggedState.get(button)[1],
                Client.getMouseX(),
                Client.getMouseY(),
                button
        );

        // update dragged
        this.draggedState.put(button, new Float[]{Client.getMouseX(), Client.getMouseY()});
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        TriggerType.RENDER_WORLD.triggerAll();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        handleOverlayTriggers(event);

        if (EventLib.getType(event) != RenderGameOverlayEvent.ElementType.TEXT)
            return;

        TriggerType.STEP.triggerAll();

        handleMouseInput();
        CTJS.getInstance().getCps().clickCalc();
    }

    private void handleOverlayTriggers(RenderGameOverlayEvent event) {
        RenderGameOverlayEvent.ElementType element = EventLib.getType(event);

        if (element == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
            TriggerType.RENDER_PLAYER_LIST.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            TriggerType.RENDER_CROSSHAIR.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.DEBUG) {
            TriggerType.RENDER_DEBUG.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.BOSSHEALTH) {
            TriggerType.RENDER_BOSS_HEALTH.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.HEALTH) {
            TriggerType.RENDER_HEALTH.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.FOOD) {
            TriggerType.RENDER_FOOD.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
            TriggerType.RENDER_MOUNT_HEALTH.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            TriggerType.RENDER_EXPERIENCE.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.HOTBAR) {
            TriggerType.RENDER_HOTBAR.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.AIR) {
            TriggerType.RENDER_AIR.triggerAll(event);
        } else if (element == RenderGameOverlayEvent.ElementType.TEXT) {
            TriggerType.RENDER_OVERLAY.triggerAll(event);
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (EventLib.getButton(event) == -1) return;

        // add to cps
        if (EventLib.getButton(event) == 0 && EventLib.getButtonState(event)) CTJS.getInstance().getCps().addLeftClicks();
        if (EventLib.getButton(event) == 1 && EventLib.getButtonState(event)) CTJS.getInstance().getCps().addRightClicks();
    }

    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event) {
        TriggerType.GUI_OPENED.triggerAll(event);
    }

    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        Vector3d position = new Vector3d(
                event.target.getBlockPos().getX(),
                event.target.getBlockPos().getY(),
                event.target.getBlockPos().getZ()
        );
        TriggerType.BLOCK_HIGHLIGHT.triggerAll(event, position);
    }

    @SubscribeEvent
    public void onPickupItem(EntityItemPickupEvent event) {
        Vector3d position = new Vector3d(
                event.item.posX,
                event.item.posY,
                event.item.posZ
        );
        Vector3d motion = new Vector3d(
                event.item.motionX,
                event.item.motionY,
                event.item.motionZ
        );
        TriggerType.PICKUP_ITEM.triggerAll(new Item(event.item.getName()), position, motion);
    }
}
