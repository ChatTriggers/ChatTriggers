package com.chattriggers.ctjs.mixins.entity;

import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11701
//$$ import net.minecraft.world.entity.EntityType;
//$$ import net.minecraft.world.phys.Vec3;
//#endif

@Mixin(EntityItem.class)
public abstract class EntityItemMixin extends Entity {
    //#if MC<=11202
    public EntityItemMixin(World worldIn) {
        super(worldIn);
    }

    @Shadow
    public abstract ItemStack getEntityItem();

    //#elseif MC>=11701
    //$$ public EntityItemMixin(EntityType<?> arg, Level arg2) {
    //$$     super(arg, arg2);
    //$$ }
    //$$
    //$$ @Shadow
    //$$ public abstract ItemStack getItem();
    //#endif

    //#if MC<=11202
    @Inject(method = "onCollideWithPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityItem;getEntityItem()Lnet/minecraft/item/ItemStack;"), cancellable = true)
    //#elseif MC>=11701
    //$$ @Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;", ordinal = 0), cancellable = true)
    //#endif
    private void chattriggers_pickupItemTrigger(EntityPlayer entityIn, CallbackInfo ci) {
        //#if MC<=11202
        Vector3f position = new Vector3f(
                (float) super.posX,
                (float) super.posY,
                (float) super.posZ
        );
        Vector3f motion = new Vector3f(
                (float) super.motionX,
                (float) super.motionY,
                (float) super.motionZ
        );

        TriggerType.PickupItem.triggerAll(
                new Item(getEntityItem()),
                new PlayerMP(entityIn),
                position,
                motion,
                ci
        );
        //#elseif MC>=11701
        //$$ Vec3 position = super.position();
        //$$ Vec3 motion = super.getDeltaMovement();
        //$$
        //$$ TriggerType.PickupItem.triggerAll(
        //$$     new Item(getItem()),
        //$$     new PlayerMP(entityIn),
        //$$     new Vector3f(
        //$$          (float) position.x,
        //$$          (float) position.y,
        //$$          (float) position.z
        //$$     ),
        //$$     new Vector3f(
        //$$         (float) motion.x,
        //$$         (float) motion.y,
        //$$         (float) motion.z
        //$$     ),
        //$$     ci
        //$$ );
        //#endif
    }
}
