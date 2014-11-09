DROP TABLE if exists team;

CREATE TABLE team (
	team_id		char(3)		not null,
	team_name	varchar(20)	not null,
	nickname	varchar(15) not null,
	league		char(2)		not null,
	division	varchar(7)	not null,
	PRIMARY KEY(team_id),
	UNIQUE(nickname)
);

LOAD DATA LOCAL INFILE "teams.txt"
INTO TABLE team
FIELDS OPTIONALLY ENCLOSED BY "\"" TERMINATED BY ",";