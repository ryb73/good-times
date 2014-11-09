1
select team_name, nickname
from team
where league = 'AL' and division = 'West';

2
select team_name, nickname, league, division
from team
order by league, division, team_name;

3
select primary_pos, count(*)
from player
group by primary_pos;

4
select team_name, count(*)
from team
join game on home_team = team_id or away_team = team_id
where num_innings > 9
group by team_name;

5
select count(*)
from game
where
	(away_team = 'PIT' and away_score = 0) or
	(home_team = 'PIT' and home_score = 0);

6
select distinct
	fname,
	lname
from game
join player on player_id = winning_pitcher;

7
select count(*)
from game
join team on
	(team_id = home_team and home_score > away_score) or
	(team_id = away_team and home_score < away_score)
where
	division = 'East' and
	league = 'AL' and
	month(game_date) = 6;

8
select count(*)
from game
where
	(home_score > away_score and away_hits > 15) or
	(home_score < away_score and home_hits > 15);

9
select fname, lname, avg(attendance)
from game
join player on player_id = away_pitcher
group by fname, lname
order by avg(attendance) desc
limit 0,10;