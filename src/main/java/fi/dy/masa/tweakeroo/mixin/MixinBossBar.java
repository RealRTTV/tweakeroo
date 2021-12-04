package fi.dy.masa.tweakeroo.mixin;

import fi.dy.masa.tweakeroo.config.Configs;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class MixinBossBar {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void init(MatrixStack matrices, CallbackInfo ci) {

        if (Configs.Disable.DISABLE_BOSS_BAR.getBooleanValue()) {
            ci.cancel();
        }
    }
}
