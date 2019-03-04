DELIMITER $$ 


CREATE PROCEDURE addxmlmovie (IN movie_id varchar(10), IN movieyear INT, IN moviename varchar(100), IN director varchar(100),IN genres_name varchar(32))
  
BEGIN
  declare a int(11);
  declare b int(11);
  declare c int(11);
  
  select @isexist:=count(id) from moviedb.movies where title=moviename and year=movieyear and director=director;
  if @isexist = 0 then
  begin
  insert into moviedb.movies values(movie_id,moviename,movieyear,director);
  set @genres_num=0;
  select @genres_num:=count(id) from moviedb.genres where name=genres_name;
  set @genres_id='';
  select @genres_id:=Max(id) from moviedb.genres;
  set b=@genres_id+1;
  
  if @genres_num = 0 then
  begin
  insert into moviedb.genres values(b,genres_name);
  insert into moviedb.genres_in_movies values(b,movie_id);
  end;

  else 
  begin
  select @genres_num2:=id from moviedb.genres where name=genres_name;
  insert into moviedb.genres_in_movies values(@genres_num2,movie_id);
  end;
  end if;
  insert into moviedb.ratings values(movie_id,9.0,100);
  
  end;
  end if;
  

END
$$
-- Change back DELIMITER to ; 
DELIMITER ; 
