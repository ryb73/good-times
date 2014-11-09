DROP TABLE if exists game;

CREATE TABLE game (
	game_id			char(12)	not null,
	game_date		date		not null,
	away_team		char(3)		not null,
	home_team		char(3)		not null,
	away_pitcher	char(8)		not null,
	home_pitcher	char(8)		not null,
	attendance		int			not null,
	num_innings		int			not null,
	away_score		int			not null,
	home_score		int			not null,
	away_hits		int			not null,
	home_hits		int			not null,
	away_errors		int			not null,
	home_errors		int			not null,
	winning_pitcher	char(8)		not null,
	losing_pitcher	char(8)		not null,
	save_pitcher	char(8),
	PRIMARY KEY(game_id),
	FOREIGN KEY(away_team) REFERENCES TEAM(team_id),
	FOREIGN KEY(home_team) REFERENCES TEAM(team_id),
	FOREIGN KEY(away_pitcher) REFERENCES PLAYER(player_id),
	FOREIGN KEY(home_pitcher) REFERENCES PLAYER(player_id),
	FOREIGN KEY(winning_pitcher) REFERENCES PLAYER(player_id),
	FOREIGN KEY(losing_pitcher) REFERENCES PLAYER(player_id),
	FOREIGN KEY(save_pitcher) REFERENCES PLAYER(player_id)
);

LOAD DATA LOCAL INFILE "games.txt"
INTO TABLE game
FIELDS OPTIONALLY ENCLOSED BY "\"" TERMINATED BY ",";