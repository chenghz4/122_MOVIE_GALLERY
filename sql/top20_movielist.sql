
select distinct a.id, a.title, a.year, a.director, GROUP_CONCAT(distinct a.genre_name) as genre_name, a.rating, 
GROUP_CONCAT(distinct s.name order by s.id) as star_name,  GROUP_CONCAT(distinct s.id) as star_id
from
(select distinct m.id, m.title, m.year, m.director, GROUP_CONCAT(distinct g.name) as genre_name, r.rating
from movies as m, ratings as r, genres as g, genres_in_movies as y
where m.id=y.movieId and y.genreId=g.id and r.movieId=m.id 
group by m.id
order by r.rating desc
limit 20
) as a, 

stars as s, stars_in_movies as x
where a.id=x.movieId and x.starId=s.id 
group by a.id
order by a.rating desc 
limit 20






