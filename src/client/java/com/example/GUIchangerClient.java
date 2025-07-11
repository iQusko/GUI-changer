package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class GUIchangerClient implements ClientModInitializer {
    private static KeyBinding cycleKey;
    private static final int MAX_SCALE = 4;
    private static final int MIN_SCALE = 1;

    @Override
    public void onInitializeClient() {
        cycleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Toggle GUI Scale",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "Simple GUI Changer"
        ));

        // Для игрового мира (когда нет открытого GUI)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (cycleKey.wasPressed()) {
                cycleInterfaceSizes(client);
            }
        });
    }

    // Этот метод вызывает миксин для обработки клавиш в GUI
    public static boolean onKeyPressedInScreen(int keyCode, int scanCode) {
        if (cycleKey != null && cycleKey.matchesKey(keyCode, scanCode)) {
            cycleInterfaceSizes(MinecraftClient.getInstance());
            return true;
        }
        return false;
    }
    public static boolean onMousePressedInScreen(int button) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (cycleKey != null && cycleKey.matchesMouse(button)) {
            if (client.currentScreen == null) {
                return false; // Не обрабатываем нажатия мыши, если нет открытого GUI
            } else {
                cycleInterfaceSizes(MinecraftClient.getInstance());
            }
            return true;
        }
        return false;
    }

    private static void cycleInterfaceSizes(MinecraftClient client) {
        GameOptions options = client.options;
        int current = options.getGuiScale().getValue();
        int next = current + 1;
        if (next > MAX_SCALE) next = MIN_SCALE;
        options.getGuiScale().setValue(next);
        showMessage(client, "GUI scale "+next, next);
    }

    private static void showMessage(MinecraftClient client, String key, Object... args) {
        if (client.player != null) {
            client.player.sendMessage(
                Text.translatable(key, args),
                true
            );
        }
    }
}