package io.nghlong3004.system;

import io.nghlong3004.ai.AIDecision;
import io.nghlong3004.ai.AIStrategy;
import io.nghlong3004.model.Bomb;
import io.nghlong3004.model.Bomber;
import io.nghlong3004.model.Explosion;
import io.nghlong3004.model.TileMap;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.nghlong3004.constant.BomberConstant.AI_UPDATE_INTERVAL;

@Slf4j
@Setter
public class BomberAISystem implements UpdateSystem {

    private List<Bomber> allBombers;
    private List<Bomb> bombs;
    private List<Explosion> explosions;
    private TileMap tileMap;

    @Override
    public void update(Object entity) {
        Bomber bomber = (Bomber) entity;

        if (!bomber.isAlive() || bomber.isPlayer()) {
            return;
        }

        updateAI(bomber);
    }

    private void updateAI(Bomber bomber) {
        bomber.setAiTick(bomber.getAiTick() + 1);

        if (bomber.getAiTick() % AI_UPDATE_INTERVAL != 0) {

            return;
        }


        if (tileMap == null || allBombers == null || bombs == null || explosions == null) {
            log.error("AI context not set properly!");
            return;
        }


        AIDecision decision = AIStrategy.makeDecision(bomber, allBombers, bombs, explosions, tileMap);

        log.trace("AI executing decision: {}", decision.getAction());


        executeDecision(bomber, decision);
    }

    private void executeDecision(Bomber bomber, AIDecision decision) {

        bomber.setLeft(false);
        bomber.setRight(false);
        bomber.setUp(false);
        bomber.setDown(false);

        switch (decision.getAction()) {
            case MOVE_TO_TARGET, ESCAPE_DANGER -> {
                int direction = decision.getNextDirection(bomber, tileMap);
                if (direction >= 0) {
                    applyDirection(bomber, direction);
                    log.trace("AI moving direction: {}", direction);
                }
                else {
                    log.trace("AI has no valid direction");
                }
            }
            case PLACE_BOMB -> {
                log.info("AI placing bomb");
                bomber.requestPlaceBomb();

            }
            case PLACE_BOMB_AND_ESCAPE -> {

                log.info("AI placing bomb and escaping");
                bomber.requestPlaceBomb();

                int escapeDirection = decision.getNextDirection(bomber, tileMap);
                if (escapeDirection >= 0) {
                    applyDirection(bomber, escapeDirection);
                    log.debug("AI escape direction: {}", escapeDirection);
                }
                else {
                    log.warn("AI placed bomb but has no escape direction!");
                }
            }
            case IDLE -> {
                log.trace("AI idle");
            }
        }
    }

    private void applyDirection(Bomber bomber, int direction) {
        switch (direction) {
            case 0 -> bomber.setDown(true);
            case 1 -> bomber.setLeft(true);
            case 2 -> bomber.setRight(true);
            case 3 -> bomber.setUp(true);
        }
    }
}
