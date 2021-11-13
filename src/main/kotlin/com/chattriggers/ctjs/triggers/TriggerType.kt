package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.module.ModuleManager

enum class TriggerType {
    // client
    Chat,
    ActionBar,
    Tick,
    Step,
    GameUnload,
    GameLoad,
    GuiOpened,
    ScreenshotTaken,
    PickupItem,
    DropItem,
    MessageSent,
    PlayerInteract,
    AttackEntity,
    HitBlock,
    PacketSent,
    PacketReceived,

    // rendering
    RenderWorld,
    BlockHighlight,
    RenderOverlay,
    RenderPlayerList,
    RenderBossHealth,
    RenderDebug,
    RenderCrosshair,
    RenderHotbar,
    RenderExperience,
    RenderArmor,
    RenderHealth,
    RenderFood,
    RenderMountHealth,
    RenderAir,
    RenderEntity,
    RenderTooltip,
    GuiRender,
    PostGuiRender,
    Clicked,
    Scrolled,
    Dragged,
    GuiKey,
    GuiMouseClick,
    GuiMouseRelease,
    GuiMouseDrag,
    ChatComponentClicked,
    ChatComponentHovered,

    // world
    PlayerJoin,
    PlayerLeave,
    SoundPlay,
    NoteBlockPlay,
    NoteBlockChange,
    WorldLoad,
    WorldUnload,
    BlockBreak,
    SpawnParticle,
    EntityDeath,
    EntityDamage,

    // misc
    Command,
    Other;

    fun triggerAll(vararg args: Any?) {
        ModuleManager.trigger(this, args)
    }
}
