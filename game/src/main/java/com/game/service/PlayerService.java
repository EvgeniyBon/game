package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface PlayerService {
    List<Player>getAllPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel);
    List<Player>getPageIndex(List<Player> players,Integer pageNumber,Integer pageSize);

    List<Player> sortByOrder(List<Player> playersPage, PlayerOrder order);

    Player getPlayerById(Long id);

    void deletePlayer(Long id);

    Player updatePlayer(Player oldPlayer, Player player, Long id);

    Player createNewPlayer(Player player);
}
