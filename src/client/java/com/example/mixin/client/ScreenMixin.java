package com.example.mixin.client;

import com.example.GUIchangerClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Screen screen = (Screen) (Object) this;
        if (screen.getFocused() instanceof TextFieldWidget) return;
        if (GUIchangerClient.onKeyPressedInScreen(keyCode, scanCode)) {
            cir.setReturnValue(true);
        }
    }
}