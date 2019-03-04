DELIMITER $$ 
CREATE PROCEDURE addstar ( IN starname varchar(100),IN birthyear INT)
  
BEGIN
  declare c int(11);
  
  set @star_id='';
  select @star_id:=Max(id) from moviedb.stars;
  set c=substr(@star_id,3)+1;
  set @star_id=CONCAT('xx', c);
  insert into moviedb.stars values(@star_id,starname,birthyear);
 

END
$$
-- Change back DELIMITER to ; 
DELIMITER ; 
