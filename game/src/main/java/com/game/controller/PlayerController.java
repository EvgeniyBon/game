package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerController {
 List<Player>getAllPlayers(String name, String title, Race race, Profession profession,Long after,Long before,Boolean banned,Integer minExperience,Integer maxExperience,Integer minLevel,Integer maxLevel,PlayerOrder order,Integer pageNumber,Integer pageSize);
}
