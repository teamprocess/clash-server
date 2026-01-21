package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.application.common.actor.Actor;

import java.time.LocalDate;
import java.util.List;

public class FindAllBattleInfoData {

    public record Command(
            Actor actor
    ) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(
            List<BattleInfo> battles
    ) {
        public static Result from(List<BattleInfo> battles) {
            return new Result(battles);
        }
    }

    public record BattleInfo(
            Long id,
            Enemy enemy,
            LocalDate expireDate,
            String result // WON, LOST, WINNING, LOSING, DRAW, PENDING
    ) {
        public static BattleInfo of(Long id, Enemy enemy, LocalDate expireDate, String result) {
            return new BattleInfo(id, enemy, expireDate, result);
        }
    }

    public record Enemy(
            Long id,
            String name,
            String profileImage
    ) {
        public static Enemy of(Long id, String name, String profileImage) {
            return new Enemy(id, name, profileImage);
        }
    }
}