DROP TABLE if exists player;

CREATE TABLE player (
	player_id	char(8)		not null,
	lname		varchar(20)	not null,
	fname		varchar(20)	not null,
	bats		char(1)		not null,
	throws		char(1)		not null,
	primary_pos	char(2)		not null,
	PRIMARY KEY(player_id)
);

LOAD DATA LOCAL INFILE "players.txt"
INTO TABLE player
FIELDS OPTIONALLY ENCLOSED BY "\"" TERMINATED BY ",";