DROP TABLE person if exists;
CREATE TABLE person (age CHAR, name CHAR);

DROP TABLE Tweet if exists;
CREATE TABLE Tweet (id int,accountimgURL VARCHAR,accountName VARCHAR,screenName VARCHAR,
                    tweetContents VARCHAR,tweetimgURL VARCHAR,fav int,rt int,tweetCount int
                    ,followersCount int,friendsCount int,tweetID VARCHAR,tweetTime VARCHAR,
                    favIcon VARCHAR,rtIcon VARCHAR);