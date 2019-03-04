-- Change DELIMITER to $$ 
DELIMITER $$ 


CREATE PROCEDURE addmovie (IN movieyear INT, IN moviename varchar(100), IN director varchar(100),IN starname varchar(100),IN genres_name varchar(32), INOUT inOutParam INT)
  
BEGIN
  declare a int(11);
  declare b int(11);
  declare c int(11);
  
  select @isexist:=count(id) from moviedb.movies where title=moviename and year=movieyear and director=director;
  if @isexist = 0 then
  begin
  
  set @movie_id='';
  select @movie_id:=Max(id) from moviedb.movies where id like 'tt%' ;
  set a=substr(@movie_id,3)+1;
  set @movie_id=CONCAT('tt0', a);
  insert into moviedb.movies values(@movie_id,moviename,movieyear,director);
  
  set @genres_num=0;
  select @genres_num:=count(id) from moviedb.genres where name=genres_name;
  set @star_num=0;
  select @star_num:=count(id) from moviedb.stars where name=starname;
  
  set @genres_id='';
  select @genres_id:=Max(id) from moviedb.genres;
  set b=@genres_id+1;
  
  set @star_id='';
  select @star_id:=Max(id) from moviedb.stars where id like 'nm%';
  set c=substr(@star_id,3)+1;
  set @star_id=CONCAT('nm', c);
  
  if @genres_num = 0 then
  begin
  insert into moviedb.genres values(b,genres_name);
  insert into moviedb.genres_in_movies values(b,@movie_id);
  end;

  else 
  begin
  select @genres_num2:=id from moviedb.genres where name=genres_name;
  insert into moviedb.genres_in_movies values(@genres_num2,@movie_id);
  end;
  end if;
  
  if @star_num = 0 then
  begin
  insert into moviedb.stars (id, name) values(@star_id,starname);
  insert into moviedb.stars_in_movies values(@star_id,@movie_id);
  end;

  else 
  begin
  select @star_num2:=id from moviedb.stars where name=starname;
  insert into moviedb.stars_in_movies values(@star_num2,@movie_id);
  end;
  end if;
  
  insert into moviedb.ratings values(@movie_id,9.0,100);
  
  set inOutParam=1;
  
  end;
  
  
  
  
  
  else 
  begin
  set inOutParam=2;
  end;
  end if;
  

END
$$
-- Change back DELIMITER to ; 
DELIMITER ; 
