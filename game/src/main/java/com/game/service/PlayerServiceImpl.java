package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerDao;
import org.hibernate.engine.transaction.jta.platform.internal.WildFlyStandAloneJtaPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service

public class PlayerServiceImpl implements PlayerService {
    @Autowired
    public void setPlayerDao(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    private PlayerDao playerDao;

    public PlayerServiceImpl() {
    }

    @Override
    public List<Player> getAllPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        List<Player> players = new ArrayList<>();
        playerDao.findAll().forEach((player) -> {
            if (name != null && !player.getName().contains(name)) return;
            if (title != null && !player.getTitle().contains(title)) return;
            if (race != null && player.getRace() != race) return;
            if (profession != null && player.getProfession() != profession) return;
            if (after != null && player.getBirthday().getTime() < after) return;
            if (before != null && player.getBirthday().getTime() > before) return;
            if (banned != null && player.getBanned() != banned) return;
            if (minExperience != null && player.getExperience() < minExperience) return;
            if (maxExperience != null && player.getExperience() > maxExperience) return;
            if (minLevel != null && player.getLevel() < minLevel) return;
            if (maxLevel != null && player.getLevel() > maxLevel) return;
            players.add(player);

        });
        return players;
    }

    @Override
    public List<Player> getPageIndex(List<Player> players, Integer pageNumber, Integer pageSize) {
        final int from = pageNumber * pageSize;
        int to = from + pageSize;
        if (to > players.size()) to = players.size();
        return players.subList(from, to);
    }

    @Override
    public List<Player> sortByOrder(List<Player> playersPage, PlayerOrder order) {
        playersPage.sort((player1, player2) -> {
            switch (order) {
                case ID:
                    return player1.getId().compareTo(player2.getId());
                case NAME:
                    return player1.getName().compareTo(player2.getName());
                case EXPERIENCE:
                    return player1.getExperience().compareTo(player2.getExperience());
                case BIRTHDAY:
                    return player1.getBirthday().compareTo(player2.getBirthday());
                default:
                    return 0;
            }
        });
        return playersPage;
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerDao.findById(id).orElse(null);

    }

    @Override
    public void deletePlayer(Long id) {

        playerDao.deleteById(id);
    }

    @Override
    public Player updatePlayer(Player oldPlayer, Player player, Long id) {
        Calendar minDate = new GregorianCalendar();
        minDate.set(2000,Calendar.JANUARY,1);
        Calendar maxDate = new GregorianCalendar();
        maxDate.set(3000,Calendar.DECEMBER,31);
        if (player.getName() != null) {
            if (player.getName().length() > 12 || player.getName().equals("")) {
                throw new IllegalArgumentException();
            } else oldPlayer.setName(player.getName());
        }
        if (player.getTitle() != null) {
            if (player.getTitle().length() > 30 || player.getTitle().equals("")) {
                throw new IllegalArgumentException();
            } else
                oldPlayer.setTitle(player.getTitle());
        }
        if (player.getRace()!=null){
           Race.valueOf(player.getRace().name());
            if (player.getRace()!=oldPlayer.getRace()){
                oldPlayer.setRace(player.getRace());
            }
        }
        if (player.getProfession()!=null){
            Profession.valueOf(player.getProfession().name());
            if (player.getProfession()!=oldPlayer.getProfession()){
                oldPlayer.setProfession(player.getProfession());
            }
        }
        if (player.getBirthday()!=null){
            if (player.getBirthday().before(minDate.getTime())||player.getBirthday().after(maxDate.getTime())){
                throw new IllegalArgumentException();
            }else oldPlayer.setBirthday(player.getBirthday());
        }
        if (player.getBanned()!=null){
            if (player.getBanned()!=oldPlayer.getBanned()){
                oldPlayer.setBanned(player.getBanned());
            }
        }
        if (player.getExperience()!=null){
            if (player.getExperience()<0||player.getExperience()>10000000){
                throw new IllegalArgumentException();
            }else {
                if (player.getExperience().equals(oldPlayer.getExperience())){

                }else {
                    oldPlayer.setExperience(player.getExperience());
                  int level = (int)(Math.sqrt(2500+200*player.getExperience())-50)/100;
                    oldPlayer.setLevel( level);
                    oldPlayer.setUntilNextLevel( (50 * (level+1) * (level + 2) - player.getExperience()));
                }
            }
        }
        playerDao.save(oldPlayer);
        return oldPlayer;
    }

    @Override
    public Player createNewPlayer(Player player) {
        Calendar minDate = new GregorianCalendar();
        Calendar maxDate = new GregorianCalendar();
        minDate.set(2000,Calendar.JANUARY,1);
        maxDate.set(3000,Calendar.DECEMBER,31);
        if (player.getName().length()>12||player.getName().equals("")){
            return null;
        }
        if (player.getTitle().length()>30||player.getTitle().equals("")){
            return null;
        }
        if (player.getBirthday().getTime()<0||player.getBirthday().before(minDate.getTime())||player.getBirthday().after(maxDate.getTime())){
            return null;
        }
        if (player.getExperience()<0||player.getExperience()>10000000){
            return null;
        }
        if (player.getBanned()==null){
            player.setBanned(false);
        }
        Player newPlayer = new Player();
        newPlayer.setName(player.getName());
        newPlayer.setTitle(player.getTitle());
        newPlayer.setRace(player.getRace());
        newPlayer.setProfession(player.getProfession());
        newPlayer.setBanned(player.getBanned());
        newPlayer.setBirthday(player.getBirthday());
        int level = (int)(Math.sqrt(2500+200*player.getExperience())-50)/100;
        newPlayer.setLevel( level);
        newPlayer.setExperience(player.getExperience());
        newPlayer.setUntilNextLevel( (50 * (level+1) * (level + 2) - player.getExperience()));
        playerDao.save(newPlayer);

        return newPlayer;
    }


}
