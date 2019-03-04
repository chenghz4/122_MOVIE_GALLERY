-- Change DELIMITER to $$ 
DELIMITER $$ 


CREATE PROCEDURE addgenres (IN genres_name varchar(32),IN movie_id varchar(10))
  
BEGIN
  declare b int(11);
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


END
$$
-- Change back DELIMITER to ; 
DELIMITER ; 
