package io.nghlong3004.boom_battle_swing.view.game;

import io.nghlong3004.boom_battle_swing.constant.AudioConstant;
import io.nghlong3004.boom_battle_swing.model.GameState;
import io.nghlong3004.boom_battle_swing.util.ObjectContainer;

import java.util.function.Consumer;

import static io.nghlong3004.boom_battle_swing.constant.AudioConstant.BYE_BYE;

public final class GameAudioStateObserver implements Consumer<GameState> {
    @Override
    public void accept(GameState state) {
        switch (state) {
            case MENU -> ObjectContainer.getAudioPlayer().playSong(AudioConstant.MENU);
            case PLAYING -> ObjectContainer.getAudioPlayer().playSong(AudioConstant.GAME);
            case QUIT -> ObjectContainer.getAudioPlayer().playSong(BYE_BYE);
        }
    }
}
