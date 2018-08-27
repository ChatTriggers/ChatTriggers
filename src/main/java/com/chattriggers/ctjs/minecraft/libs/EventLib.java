//package com.chattriggers.ctjs.minecraft.libs;
//
//import net.minecraftforge.client.event.ClientChatReceivedEvent;
//import net.minecraftforge.client.event.MouseEvent;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.client.event.sound.PlaySoundEvent;
//import net.minecraftforge.fml.client.event.ConfigChangedEvent;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
////#if MC<=10809
//import net.minecraft.util.IChatComponent;
////#else
////$$ import net.minecraft.util.text.ITextComponent;
////#endif
//
//public class EventLib {
//    /**
//     * Gets the button clicked in the MouseEvent that is passed in
//     *
//     * @param event a MouseEvent
//     * @return the button clicked (0 for left click, 1 for middle click, 2 for right click, etc.)
//     */
//    public static int getButton(MouseEvent event) {
//        //#if MC<=10809
//        return event.button;
//        //#else
//        //$$ return event.getButton();
//        //#endif
//    }
//
//    /**
//     * Gets the state of the button passed in
//     *
//     * @param event a MouseEvent
//     * @return the state of the button true for pressed, false for unpressed
//     */
//    public static Boolean getButtonState(MouseEvent event) {
//        //#if MC<=10809
//        return event.buttonstate;
//        //#else
//        //$$ return event.isButtonstate();
//        //#endif
//    }
//
//    /**
//     * Gets the type of the overlay event i.e. air, armor, crosshair, chat, bosshealth, etc.
//     *
//     * @param event the render overlay event you want information on
//     * @return the type of the event
//     */
//    public static RenderGameOverlayEvent.ElementType getType(RenderGameOverlayEvent event) {
//        //#if MC<=10809
//        return event.type;
//        //#else
//        //$$ return event.getType();
//        //#endif
//    }
//
//    /**
//     * Gets the type of the chat event that was received
//     *
//     * @param event a chat event
//     * @return the type of the event, 0 for standard chat message, 1 for system message displayed as standard text
//     */
//    public static int getType(ClientChatReceivedEvent event) {
//        //#if MC<=10809
//        return event.type;
//        //#else
//        //$$ return event.getType().getId();
//        //#endif
//    }
//
//    /**
//     * Gets the message from a chat event
//     *
//     * @param event a chat event
//     * @return the message from the event
//     */
//    //#if MC<=10809
//    public static IChatComponent getMessage(ClientChatReceivedEvent event) {
//        return event.message;
//    }
//    //#else
//    //$$ public static ITextComponent getMessage(ClientChatReceivedEvent event) {
//    //$$    return event.getMessage();
//    //$$ }
//    //#endif
//
//    /**
//     * Gets the name of a sound that was played
//     *
//     * @param event a sound event
//     * @return the name of the sound that was played
//     */
//    public static String getName(PlaySoundEvent event) {
//        //#if MC<=10809
//        return event.name;
//        //#else
//        //$$ return event.getName();
//        //#endif
//    }
//
//    /**
//     * Gets the Mod ID from a config changed event
//     *
//     * @param event a config changed event
//     * @return the mod id
//     */
//    public static String getModId(ConfigChangedEvent.OnConfigChangedEvent event) {
//        //#if MC<=10809
//        return event.modID;
//        //#else
//        //$$ return event.getModID();
//        //#endif
//    }
//
//    /**
//     * Cancel an event. Automatically used with <code>cancel(event)</code>.
//     *
//     * @param event the event to cancel
//     * @throws IllegalArgumentException if event can be cancelled "normally"
//     */
//    public static void cancel(Object event) throws IllegalArgumentException {
//        if (event instanceof CallbackInfoReturnable) {
//            CallbackInfoReturnable cbir = (CallbackInfoReturnable) event;
//            if (!cbir.isCancellable()) return;
//            cbir.setReturnValue(null);
//        } else if (event instanceof CallbackInfo) {
//            CallbackInfo cbi = (CallbackInfo) event;
//            if (!cbi.isCancellable()) return;
//            cbi.cancel();
//        } else if (event instanceof PlaySoundEvent) {
//            //#if MC<=10809
//            ((PlaySoundEvent) event).result = null;
//            //#else
//            //$$ ((PlaySoundEvent) event).setResultSound(null);
//            //#endif
//        } else {
//            throw new IllegalArgumentException();
//        }
//    }
//}
