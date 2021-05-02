package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.util.List;

@RestController
public class PlayerControllerImpl implements PlayerController {
    public PlayerControllerImpl() {
    }

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/rest/players", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Player> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize
    ) {
        List<Player> players = playerService.getAllPlayers(
                name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        List<Player> playersPage = playerService.sortByOrder(players, order);
        return playerService.getPageIndex(playersPage, pageNumber, pageSize);
    }

    @RequestMapping(value = "/rest/players/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Integer getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {
        return playerService.getAllPlayers(
                name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel
        ).size();
    }

    @RequestMapping(value = "rest/players/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Player> getPlayerById(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player = playerService.getPlayerById(id);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<HttpStatus> deletePlayer(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player = playerService.getPlayerById(id);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/players/{id}", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Player> updatePlayer(
            @PathVariable(value = "id") Long id,
            @RequestBody Player player) {
        ResponseEntity<Player>pl = getPlayerById(id);
        Player oldPlayer = pl.getBody();
        if (oldPlayer==null){
            return pl;
        }

        try {
            return new ResponseEntity<>(playerService.updatePlayer(oldPlayer,player,id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "rest/players",method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Player>createPlayer(@RequestBody Player player){
        if (player==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getName()==null||player.getTitle()==null||player.getRace()==null||player.getProfession()==null||
        player.getBirthday()==null||player.getExperience()==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player createdPlayer = playerService.createNewPlayer(player);
        if (createdPlayer==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdPlayer,HttpStatus.OK);
    }

}
